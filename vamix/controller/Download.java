package vamix.controller;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
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
 * This class handles the download functionality for VAMIX.
 * It does the downloading in the background and also displays progress through its own GUI
 * @author Nikhil Prasad and Guyver (Yeu-Shin) Fu
 **/
public class Download {

	private DownloadWorker worker;
	private String _url;
	private String urlEnd = "";	//variable for file name
	
	public Download(String url) {
		_url = url;
	}
	
	/*
	 * Method that takes the entered URL, does basic error checking and if all is well, downloads from the URL
	 */
	public void downloadFunction() {
		int overrideChoice = -1;	//initialize value of override -1 as file doesn't exist
		String url = _url;	//variable for url
		if (url == null) {
			JOptionPane.showMessageDialog(null, "You have not entered a URL. Please input a valid URL."); //when url is null
			
		} else if (url.equals("")) {
			//error message of empty links
			JOptionPane.showMessageDialog(null, "You have entered a empty URL. Please input a valid URL.");
			
		}else{
			urlEnd = url.split(File.separator)[url.split(File.separator).length-1];
			//create object for choice of options
			Object[] option= {"Override","Resume partial download"};
			//check if the file exist locally
			if (Helper.fileExist(Constants.CURRENT_DIR+urlEnd)){
				//note 0 is override i.e. first option chosen and 1 is resume
				overrideChoice=JOptionPane.showOptionDialog(null, "File " +urlEnd +" already exists. Do you wish to override or resume partial download?",
						"Override?",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,option,option[0]);
			}	
			
			//check if file to be downloaded is open source
			Object[] options={"It is open source","No, it is not open source"};
			if (0==(JOptionPane.showOptionDialog(null, "Is the file you are trying to download open source?",
					"Open Source?",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,options[0]))){

				if(overrideChoice == 0) { //when override signal delete existing file
					File file = new File(Constants.CURRENT_DIR + urlEnd);
					file.delete();
				}
				
				//create the progress GUI
				JFrame downloadFrame = new JFrame("Downloading");
				Container pane=downloadFrame.getContentPane();
				pane.setLayout(new GridLayout(2,0));
				JButton cancelButton =new JButton("Cancel Download");
				JProgressBar dlProgressBar=new JProgressBar();
				downloadFrame.setSize(300, 100); //set size of frame
				
				//if cancel is pressed, cancel the download
				cancelButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						worker.cancel(true);
					}
				});
				
				//add window listener to close button so it cancel as well
				downloadFrame.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e){
						worker.cancel(true);
					}
				});
				
				downloadFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				downloadFrame.add(cancelButton,pane); //add cancel button to new frame
				downloadFrame.add(dlProgressBar,pane); //add progress bar to new frame
				downloadFrame.setVisible(true); //set visiblity of frame on
				downloadFrame.setResizable(false); //set frame so it cant be resize
				
				//initialize download
				worker=new DownloadWorker(downloadFrame,dlProgressBar,url);
				worker.execute();
			}
		}

	}

	/*
	 * Custom made swing worker for downloading use
	 */
	public class DownloadWorker extends SwingWorker<Void,Integer>{
		int errorCode=0;//setup error code
		private JFrame _DownloadFrame;
		private Process process;
		private JProgressBar _dlProgressBar;
		private String _url;

		//construct download worker object with the frame and progress bar
		DownloadWorker(JFrame DownloadFrame, JProgressBar dlProgressBar,String url){
			_url=url;
			_DownloadFrame=DownloadFrame;
			_dlProgressBar=dlProgressBar;
		}


		@Override
		protected Void doInBackground() throws Exception {
			String cmd ="wget";
			ProcessBuilder builder;
			String downloadOption="-c";
			//setup builder for the process
			builder=new ProcessBuilder(cmd,downloadOption,_url); 
			builder.redirectErrorStream(true);

			try{
				process = builder.start();
				//setup reader to read input
				InputStream stdout = process.getInputStream();
				BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
				String line;
				while((line=stdoutBuffered.readLine())!=null){
					if (isCancelled()){ //when cancel button click force exit of process
						errorCode=-1;
						process.destroy();
					}else {
						Matcher m =Pattern.compile("(\\d+)%").matcher(line);
						if(m.find()){
							publish(Integer.parseInt(m.group(1)));
						}
					}
				}

			}catch(Exception er){
				//System.out.println(er.getMessage());
			}
			return null;
		}

		@Override
		protected void done() {
			//when it have finish downloading
			try {
				errorCode=process.waitFor();
				get();
			} catch (InterruptedException e) {
			} catch (ExecutionException e) {
			} catch (CancellationException e){
				errorCode=-1;
			}
			switch(errorCode){
			case -1://cancel button
				//Helper.logFunction("d");
				JOptionPane.showMessageDialog(_DownloadFrame, "Download has been cancelled. Note partially downloaded to: \n" +Constants.CURRENT_DIR+urlEnd);
				break;
			case 0://nothing wrong so write to log
				//Helper.logFunction("d");
				JOptionPane.showMessageDialog(_DownloadFrame, "Download has finished. Note downloaded to: \n" +Constants.CURRENT_DIR+urlEnd);
				break;
			case 3://error message of File IO
				JOptionPane.showMessageDialog(_DownloadFrame, "File IO error. Make sure the directory is safe to write to. Or not denied by your anti-virus.");
				break;
			case 4://error message of Network
				JOptionPane.showMessageDialog(_DownloadFrame, "Network error. Make sure you are connected to the internet correctly. Or link is invalid");
				break;
			case 8://error message of Sever issue
				JOptionPane.showMessageDialog(_DownloadFrame, "Server issued error. Server could be down. Please try again later.");
				break;
			default://error message of generic
				JOptionPane.showMessageDialog(_DownloadFrame, "An error has occured. Please try again. The error code is: "+errorCode);
				break;
			}
			this._DownloadFrame.dispose();
			if (errorCode==0){
				Helper.loadAndPreview(Constants.CURRENT_DIR+urlEnd, "00:00:00", "00:00:30");
			}
		}

		@Override
		protected void process(List<Integer> chunks) {
			if (!isCancelled()){
				//publish to progress bar
				for(int pro : chunks){
					_dlProgressBar.setValue(pro);
				}
			}
		}
	}
}
