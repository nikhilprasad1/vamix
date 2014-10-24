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
import java.util.ArrayList;
import java.util.Arrays;
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

public class ExtractAudio {
	StripAudioWorker saWork;//class variable for worker so cancel button can work
	private String _curDir;
	private String _saveDir;
	//constructor for extract pass infile and out file
	ExtractAudio(String curDir,String saveDir){
		_curDir=curDir;//current directory input file
		_saveDir=saveDir;//output file directory
	}

	/*
	 * Function to perform strip from a file and basic error handle
	 */
	public void stripFunction(){
		//set validness of filename to false as initialisation and other general initialisation
		boolean valid=false; //if inputs are valid
		String inFileName=_curDir; //input filename
		String outFileName=_saveDir; //output file name
		int overrideChoice=-1; //for override button choice
		String path=""; //variable for path

		//get output file
		if(Helper.validInFile(inFileName,Constants.VIDEO_AUDIO_TYPE)){
			valid=false; //set corretness of outfile to false
			if (outFileName==null){
				JOptionPane.showMessageDialog(null, "You have not entered a destination to save to. Please input a valid file name.");
			}else if(outFileName.equals("")){
				//error message of empty file name
				JOptionPane.showMessageDialog(null, "You have entered an empty file name. Please input a valid file name.");
			}else{
				Matcher m =Pattern.compile(".mp3$").matcher(outFileName);
				if (_curDir.equals(_saveDir)){
					//error message need to not be the same name as input file
					JOptionPane.showMessageDialog(null, "Your input file and output file cannot be the same. Please input a valid file name that is not the same.");
				}else if (m.find()){
					if (Helper.fileExist(outFileName)){
						//file exist ask if override
						Object[] option= {"Override","New output file name"};
						//check if the file exist locally
						//note 0 is override ie first option chosen and 1 is new name
						overrideChoice=JOptionPane.showOptionDialog(null, "File " +outFileName +" already exist. Do you wish to override or input new file name?",
								"Override?",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,option,option[0]);
						if(overrideChoice==0){
							valid=true;//out file valid
						}
					}else{
						valid=true;//out file valid
					}
				}else{
					//error message need to end with mp3
					JOptionPane.showMessageDialog(null, "You have entered a file name that does not end with .mp3 \nPlease input a valid file name that ends with .mp3");
				}

			}
		}
		
		if(valid){
			//delete to be override file
			if(overrideChoice==0){
				File file=new File(outFileName);
				file.delete();
			}
			//create the gui of extract which consist of cancel and progress bar
			JFrame stripAudioFrame=new JFrame("Strip audio");
			Container pane=stripAudioFrame.getContentPane();
			pane.setLayout(new GridLayout(2,0));
			JButton cancelButton =new JButton("Cancel Strip Audio");
			JProgressBar dlProgressBar=new JProgressBar();
			stripAudioFrame.setSize(300, 100); //set size of frame
			cancelButton.addActionListener(new ActionListener() {
				//setup cancel button listener
				@Override
				public void actionPerformed(ActionEvent e) {
					saWork.cancel(true);
				}
			});
			//add window listener to close button (cross hair) so it cancel as well
			stripAudioFrame.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e){
					saWork.cancel(true);
				}
			});
			stripAudioFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			stripAudioFrame.add(cancelButton,pane); //add cancel button to new frame
			stripAudioFrame.add(dlProgressBar,pane); //add progress bar to new frame
			stripAudioFrame.setVisible(true); //set visiblity of frame on
			stripAudioFrame.setResizable(false); //set frame so it cant be resize
			//create swing worker obejct and run it
			saWork=new StripAudioWorker(path+inFileName,outFileName,stripAudioFrame,dlProgressBar);
			saWork.execute();
		}
	}

	class StripAudioWorker extends SwingWorker<Void,Integer>{

		private Process process;
		private String _inFileName;
		private String _outFileName;
		private JFrame _stripAudioFrame;
		private JProgressBar _dlProgressBar;
		private String stripVideo="";
		private int errorCode=0;
		boolean containAudio=false;
		boolean containVideo=false;
		//constructor to allow the input from user to be use in extractworker
		StripAudioWorker(String inFileName,String outFileName,JFrame stripAudioFrame,JProgressBar dlProgressBar){
			_inFileName=inFileName;
			_outFileName=outFileName;
			_stripAudioFrame=stripAudioFrame;
			_dlProgressBar=dlProgressBar;
		}


		@Override
		protected Void doInBackground() throws Exception {
			//make sure the correct process Builder is setup as it is weird
			//the bash command avconv -i $inputFile -vn -c:a libmp3lame -ss $startTime -t $duration $outputFile
			String path="";
			ProcessBuilder builder;
			String[] checkCmd=("avconv -i " +_inFileName).split(" ");
			List<String> cmds=Arrays.asList(checkCmd);
			builder=new ProcessBuilder(cmds); 
			builder.redirectErrorStream(true);
			// workout the length of the extracted file tho work out progress bar
			int totalLength=(int)(vamix.view.Main.vid.getLength()/1000.0);
			//Audio check if file have audio
			
			try{
				process = builder.start();
				InputStream stdout = process.getInputStream();
				BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
				String line;
				while((line=stdoutBuffered.readLine())!=null){
					if (isCancelled()){
						process.destroy();//force quit extract
					}else {
						//check time use this as indication for progress
						Matcher mAudio =Pattern.compile("(Audio)",Pattern.CASE_INSENSITIVE).matcher(line);
						Matcher mVideo =Pattern.compile("(Video)",Pattern.CASE_INSENSITIVE).matcher(line);
						if (mAudio.find()){
							containAudio=true; 
						}else if (mVideo.find()){
							containVideo=true;
						}
					}
				}
				
				if (containAudio){
					cmds=new ArrayList<String>();//jumbo all cmd into a list
					cmds.add("avconv");//use avconv
					cmds.add("-i");//set avconv to i
					cmds.add(_inFileName);//add file name
					cmds.add("-vn");//more avconv functions
					cmds.add("-c:a");//option of avconv of copying audio only
					cmds.add("libmp3lame"); //format of output of audio extraction
					cmds.add(_outFileName); //the output file name
					if (containVideo){
						//for stripping off the audio to a video
						cmds.add("-an");
						cmds.add("-c:v");
						cmds.add("copy");
						//generate output name for striped video need to get original first
						Matcher vName=Pattern.compile("(.*)(\\p{Punct}.*)$").matcher(_outFileName);
						if(vName.find()){
							path=vName.group(1); //get file path with name
						}
						stripVideo=Helper.fileNameGen(path+".mp4","no_audio");
						cmds.add(stripVideo);
					}
					//setup process cmd for striping audio
					builder=new ProcessBuilder(cmds); 
					builder.redirectErrorStream(true);
					process = builder.start();
					stdout = process.getInputStream();
					stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
					while((line=stdoutBuffered.readLine())!=null){
						if (isCancelled()){
							process.destroy();//force quit extract
						}else {
							//check time use this as indication for progress
							Matcher m =Pattern.compile("time=(\\d+)").matcher(line);
							Matcher mError =Pattern.compile("(Output file #0 does not contain any stream)",Pattern.CASE_INSENSITIVE).matcher(line);
							if(m.find()){
								//weird problem sometimes avconv gives int 100000000 so dont read it
								if (!(m.group(1).equals("10000000000"))){
									publish((int)(Integer.parseInt(m.group(1))*100/totalLength));
								}
							}else if (mError.find()){
								errorCode=143; //custom error code
								process.destroy();//force quit extract
								break;
							}
						}
					}
				}else{
					errorCode=143;
				}


			}catch(Exception er){
				//exWork.cancel(true);//cancel the extract work when error encounterd
			}
			return null;
		}

		@Override
		protected void done() {
			//when it have finish Extracting
			try {
				errorCode=process.waitFor();
				get();
				publish(100);//set complete incase doinbackground isnt quick enough
			} catch (InterruptedException e) {
			} catch (ExecutionException e) {
			} catch (CancellationException e){
				errorCode=-1; //when cancel error code is -1 
			}
			
			switch(errorCode){
			case 0://nothing wrong so write to log
				if (containVideo){
					JOptionPane.showMessageDialog(_stripAudioFrame, "Strip audio has finished. Note output is saved to "+_outFileName+" and \n"+stripVideo+".");
				}else{
					JOptionPane.showMessageDialog(_stripAudioFrame, "Strip audio has finished. Note output is saved to "+_outFileName+".");
				}
				break;
			case -1://extract cancelled
				if (containVideo){
					JOptionPane.showMessageDialog(_stripAudioFrame, "Strip audio has been cancelled. Note output is saved to "+_outFileName+" and \n"+stripVideo+".");
				}else{
					JOptionPane.showMessageDialog(_stripAudioFrame, "Strip audio has been cancelled. Note output is saved to "+_outFileName+".");
				}
				break;
			case 143://when no audio or video stream
				JOptionPane.showMessageDialog(_stripAudioFrame, "The input file doesn't have an audio/video stream. Please use a file with valid streams.");
				break;
			case 1://when no audio or video stream
				JOptionPane.showMessageDialog(_stripAudioFrame, "The input file doesn't have an audio/video stream. Please use a file with valid streams.");
				break;
			default://error message of generic
				JOptionPane.showMessageDialog(_stripAudioFrame, "An error has occurred. Please try again. The error code is: "+errorCode);
				break;
			}
			this._stripAudioFrame.dispose();
			if (errorCode==0){
				Helper.loadAndPreview(_outFileName, null, null);
				if (containVideo){
					Helper.loadAndPreview(stripVideo, null, null);
				}
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
