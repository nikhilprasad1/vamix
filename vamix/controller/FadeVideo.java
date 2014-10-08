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
 * Class that takes care of the fade in/out commands on an input video
 * Does all processing in the background and displays progress to user
 * @author Nikhil Prasad
 */
public class FadeVideo {

	//variables needed for command
	private String _startFadeIn, _endFadeIn, _startFadeOut, _endFadeOut, _inputAddr, _outputFile;
	//tells this class which command(s) to use
	public enum FadeType {IN, OUT, BOTH};
	private FadeType _fadeType;
	
	//GUI objects to show progress of fade processes
	JFrame fadeFrame;
	Container pane;
	JButton cancelBtn;
	JProgressBar progressBar;
	
	private FadeWorker fadeWorker = new FadeWorker();
	
	public FadeVideo(String startFadeIn, String endFadeIn, String startFadeOut, String endFadeOut, String inputAddr) {
		_startFadeIn = startFadeIn;
		_endFadeIn = endFadeIn;
		_startFadeOut = startFadeOut;
		_endFadeOut = endFadeOut;
		_inputAddr = inputAddr;
	}
	
	/*
	 * Sub-class of SwingWorker that will do the fading processes in the background and report progress
	 */
	class FadeWorker extends SwingWorker<Void, Integer> {
		
		private Process process;
		private ProcessBuilder builder;
		private BufferedReader stdoutBuffered;
		
		@Override
		protected Void doInBackground() throws Exception {
			//generate folder to hold temporary files (if it doesn't exist already)
			Helper.genTempFolder();
			//now get length of video being edited, helps to calculate progress
			int totalLength = (int)(vamix.view.Main.vid.getLength()/1000);
			String cmd = buildFadeCommand();
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
				JOptionPane.showMessageDialog(fadeFrame, "The fade operation has finished successfully.");
				break;
			//user cancelled fading
			case -1:
				JOptionPane.showMessageDialog(fadeFrame, "Fade operation has been cancelled. Note there may be partial output at \n" + _outputFile);
				break;
			//any other error code means something went wrong
			default:
				JOptionPane.showMessageDialog(fadeFrame, "An error has occurred. Please try again. The error code is: "+errorCode);
				break;
			}
			fadeFrame.dispose();
			//ask user if they want to load or preview the video
			if (errorCode==0){ //when finish correctly
				if ((_fadeType == FadeType.IN) || (_fadeType == FadeType.BOTH)) {
					String previewDuration = Helper.formatTime(Helper.timeInSec(_endFadeIn) - Helper.timeInSec(_startFadeIn) + 20);
					Helper.loadAndPreview(_outputFile, _startFadeIn, previewDuration);
				} else {
					String previewDuration = Helper.formatTime(Helper.timeInSec(_endFadeOut) - Helper.timeInSec(_startFadeOut) + 20);
					Helper.loadAndPreview(_outputFile, _startFadeOut, previewDuration);
				}
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
		
		protected String buildFadeCommand() {
			String cmd = "";
			//if the user wants to only fade in a section of the video
			if (_fadeType == FadeType.IN) {
				//get the auto generated output file name using "fade_in" as the tag
				_outputFile = Helper.fileNameGen(_inputAddr, "fade_in");
				//get the start frame number
				String startFrame = String.valueOf((Helper.getFrameNumber(_startFadeIn)));
				//get the number of frames that fade in is to occur over (the duration)
				String durationFrames = String.valueOf(Helper.getFrameNumber(_endFadeIn) - Helper.getFrameNumber(_startFadeIn));
				cmd = "avconv -i " + _inputAddr + " -vf \"fade=in:" + startFrame + ":" + durationFrames + "\" -strict experimental " + _outputFile;
			//else if the user only wants to fade out a section of the video
			} else if (_fadeType == FadeType.OUT) {
				//get the auto generated output file name using "fade_out" as the tag
				_outputFile = Helper.fileNameGen(_inputAddr, "fade_out");
				//get the start frame number
				String startFrame = String.valueOf((Helper.getFrameNumber(_startFadeOut)));
				//get the number of frames that fade in is to occur over (the duration)
				String durationFrames = String.valueOf(Helper.getFrameNumber(_endFadeOut) - Helper.getFrameNumber(_startFadeOut));
				cmd = "avconv -i " + _inputAddr + " -vf \"fade=out:" + startFrame + ":" + durationFrames + "\" -strict experimental " + _outputFile;
			//otherwise they want both; fade in and fade out
			} else {
				//get the auto generated output file name using "fade_in_out" as the tag
				_outputFile = Helper.fileNameGen(_inputAddr, "fade_in_out");
				//get the start frame number for fade in and fade out
				String startFadeIn = String.valueOf((Helper.getFrameNumber(_startFadeIn)));
				String startFadeOut = String.valueOf(Helper.getFrameNumber(_startFadeOut));
				//get the number of frames that fade in and fade out are to occur over (the duration)
				String durationFadeIn = String.valueOf(Helper.getFrameNumber(_endFadeIn) - Helper.getFrameNumber(_startFadeIn));
				String durationFadeOut = String.valueOf(Helper.getFrameNumber(_endFadeOut) - Helper.getFrameNumber(_startFadeOut));
				cmd = "avconv -i " + _inputAddr + " -vf \"fade=in:" + startFadeIn + ":" + durationFadeIn + ", fade=out:" + startFadeOut + ":" + durationFadeOut
						+ "\" -strict experimental " + _outputFile;
			}
			return cmd;
		}
		
	}
	
	/*
	 * Method that executes the FadeWorker which in turn performs the command to fade in/out the input video
	 */
	public void fadeVideoAsync(FadeType fadeType) {
		_fadeType = fadeType;
		showProgressGUI();
		fadeWorker.execute();
	}
	
	/*
	 * Method that shows the GUI to show fading progress to the user
	 */
	public void showProgressGUI() {
		//create the GUI objects required
		fadeFrame = new JFrame("Performing Fade Operation");
		pane = fadeFrame.getContentPane();
		pane.setLayout(new GridLayout(2,0));
		cancelBtn = new JButton("Cancel Fading");
		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		fadeFrame.setSize(300, 100);
		
		//set function of cancel button to cancel the background task of the fade worker
		cancelBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fadeWorker.cancel(true);
			}
		});
		//if user closes the progress window, assume they want to cancel the fading
		fadeFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				fadeWorker.cancel(true);
			}
		});
		//make sure gui objects are disposed on closing of the window
		fadeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		//add the gui objects to the frame
		fadeFrame.add(progressBar, pane);
		fadeFrame.add(cancelBtn, pane);
		fadeFrame.setVisible(true);
		fadeFrame.setResizable(false);
	}
}
