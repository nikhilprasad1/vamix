package vamix.controller;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

/**
 * Class that takes care of the trim video command on an input video
 * Does all processing in the background and displays progress to the user
 * @author Nikhil Prasad
 */
public class TrimVideo {
	
	//variables needed for command
	private String _startTrim, _endTrim, _inputAddr,_outputFile;
	
	//GUI objects to show progress of trim process
	JFrame trimFrame;
	Container pane;
	JButton cancelBtn;
	JProgressBar progressBar;
	
	//worker object to process in background
	private TrimWorker trimWorker = new TrimWorker();
	
	public TrimVideo(String startTrim, String endTrim, String inputAddr) {
		_startTrim = startTrim;
		_endTrim = endTrim;
		_inputAddr = inputAddr;
	}
	
	class TrimWorker extends SwingWorker<Void, Integer> {

		private Process process;
		private ProcessBuilder builder;
		private BufferedReader stdoutBuffered;
		
		@Override
		protected Void doInBackground() throws Exception {
			//generate folder to hold temporary files (if it doesn't exist already)
			Helper.genTempFolder();
			//now get length of video being edited, helps to calculate progress
			int totalLength = (int)(vamix.view.Main.vid.getLength()/1000);
			String cmd = buildTrimCommand();
			builder = new ProcessBuilder("/bin/bash","-c",cmd);
			builder.redirectErrorStream(true);
			try {
				//run the bash command as a process
				process = builder.start();
				InputStream stdout = process.getInputStream();
				stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
				String line;
				while((line = stdoutBuffered.readLine()) != null){
					if (isCancelled()){
						process.destroy();//force process to terminate
					}else {
						System.out.println(line);
						//check time in output, use this as indication for progress
						Matcher m =Pattern.compile("time=(\\d+)").matcher(line);
						if(m.find()) {
							//weird problem sometimes avconv gives int 100000000 so dont read it
							if (!(m.group(1).equals("10000000000"))) {
								publish((int)(Integer.parseInt(m.group(1))*100/totalLength));
							}
						}
					}
				}
				stdoutBuffered.close();
			} catch(Exception e) {
				//e.printStackTrace();
			}			
			return null;
		}
		
		@Override
		protected void done() {
			//when it has finished the process
			int errorCode=0;
			try {
				errorCode=process.waitFor();
				get();
				//set the progress bar to full in case the last process' updating was too slow
				publish(100);				
			} catch (InterruptedException e) {
			} catch (ExecutionException e) {
			} catch (CancellationException e){
				errorCode=-1; //when cancel error code is -1 
			}
			switch(errorCode){
			//everything went well
			case 0:	
				JOptionPane.showMessageDialog(trimFrame, "The trim operation has finished successfully.");
				break;
			//user cancelled trimming
			case -1:
				JOptionPane.showMessageDialog(trimFrame, "Trim operation has been cancelled. Note there may be partial output at \n" + _outputFile);
				break;
			//any other error code means something went wrong
			default:
				JOptionPane.showMessageDialog(trimFrame, "An error has occurred. Please try again. The error code is: "+errorCode);
				break;
			}
			trimFrame.dispose();
			//ask user if they want to load or preview the video
			if (errorCode==0){ //when finish correctly
				String duration = String.valueOf(Helper.formatTime(Helper.timeInSec(_endTrim) - Helper.timeInSec(_startTrim)));
				Helper.loadAndPreview(_outputFile, "00:00:00", duration);
			}
		}
		
		@Override
		protected void process(List<Integer> chunks) {
			if (!isCancelled()){
				//update progress value and process number
				for(int chunk : chunks){
					progressBar.setValue(chunk);
					progressBar.setString(String.valueOf(chunk) + "%");
				}
			}
		}
		
		protected String buildTrimCommand() {
			String cmd = "";
			//generate the output name for the trimmed file
			_outputFile = Helper.fileNameGen(_inputAddr, "trimmed");
			//get the duration of the trimmed video
			String duration = String.valueOf(Helper.formatTime(Helper.timeInSec(_endTrim) - Helper.timeInSec(_startTrim)));
			//build the command
			cmd = "avconv -i " + _inputAddr + " -ss " + _startTrim + " -t " + duration 
					+ " -codec copy -strict experimental " + _outputFile;
			return cmd;
		}
		
	}
	
	/*
	 * Method that executes the TrimWorker which in turn performs the command to trim the input video
	 */
	public void trimVideoAsync() {
		showProgressGUI();
		trimWorker.execute();
	}
	
	/*
	 * Method that shows the GUI to show trimming progress to the user
	 */
	public void showProgressGUI() {
		//create the GUI objects required
		trimFrame = new JFrame("Performing Trim Operation");
		pane = trimFrame.getContentPane();
		pane.setLayout(new GridLayout(2,0));
		cancelBtn = new JButton("Cancel Trimming");
		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		trimFrame.setSize(300, 100);
		
		//set function of cancel button to cancel the background task of the trim worker
		cancelBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				trimWorker.cancel(true);
			}
		});
		//if user closes the progress window, assume they want to cancel the trimming
		trimFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				trimWorker.cancel(true);
			}
		});
		//make sure gui objects are disposed on closing of the window
		trimFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		//add the gui objects to the frame
		trimFrame.add(progressBar, pane);
		trimFrame.add(cancelBtn, pane);
		trimFrame.setVisible(true);
		trimFrame.setResizable(false);
	}
}
