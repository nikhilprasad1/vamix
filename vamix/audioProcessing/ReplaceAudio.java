package vamix.audioProcessing;

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

import vamix.util.Constants;
import vamix.util.FileChecker;
import vamix.util.FileGeneratorOperations;
import vamix.util.FileNameOperations;
import vamix.util.TimeOperations;
import vamix.videoProcessing.VideoOperations;

/**
 * This class replaces the audio on an input video with the audio of an input audio file
 * @author Nikhil Prasad
 */
public class ReplaceAudio {
	
	ReplaceAudioWorker raWork;//class variable for worker so cancel button can work
	private String _inFile;
	private String _inAudio;
	private	String _startTime;
	private String _endtime;
	private	String _startTime2;
	private String _endtime2;
	
	public ReplaceAudio(String inFile,String inAudio,String startTime,String endtime,String startTime2,String endtime2){
		_inFile=inFile;//current directory input to be replace file
		_inAudio=inAudio;//current directory input audio file
		_startTime=startTime;//start time for replace audio
		_endtime=endtime;//end time for replace audio
		_startTime2=startTime2;//start time for replace audio
		_endtime2=endtime2;//end time for replace audio
	}

	/**
	 * Function to perform the replacement audio operation as well as basic error handling
	 */
	public void replaceAudioFunction(){
		//set validness of filename to false as initialization and other general initialization
		boolean valid=false; //if inputs are valid
		boolean isAudio=false;//if file is audio
		String inFileName=_inFile; //input to replace filename full path
		String audioFile=_inAudio; //input audio filename full path
		
		//get output file
		if(FileChecker.validInFile(inFileName,Constants.VIDEO_AUDIO_TYPE)){
			valid=false; //set correctness of output file to false
			if (audioFile==null){
				JOptionPane.showMessageDialog(null, "You have not entered an audio file name. Please input a valid file name.");
			}else if(audioFile.equals("")){
				//error message of empty file name
				JOptionPane.showMessageDialog(null, "You have entered an empty file name. Please input a valid file name.");
			}else if (_inFile.equals(_inAudio)){
				//error message need to not be the same name as input file
				JOptionPane.showMessageDialog(null, "The file to use as replacement has the same name as the input audio file. Please input a valid audio file name that is not the same.");
			}else {
				String bash =File.separator+"bin"+File.separator+"bash";
				String cmd ="echo $(file "+audioFile+")";
				ProcessBuilder builder=new ProcessBuilder(bash,"-c",cmd); 
				builder.redirectErrorStream(true);

				try{
					Process process = builder.start();
					InputStream stdout = process.getInputStream();
					BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
					String line;
					while((line=stdoutBuffered.readLine())!=null){
						Matcher macth =Pattern.compile("(Audio)",Pattern.CASE_INSENSITIVE).matcher(line);
						if(macth.find()){
							isAudio=true;
						}
					}
				}catch(Exception e){
					//e.printStackTrace();
				}
				if (isAudio==true){
					valid=true;//audio file valid
				}else{
					//not an audio file
					JOptionPane.showMessageDialog(null, "You have not entered a valid audio file for replacement.");
				}
			}
		}
		
		if (valid){
			//send startTime of audio for replacement to get check
			valid=TimeOperations.timeFormatChecker(_startTime,"start time for the audio file to be used as replacement");
		}
		
		if (valid){
			//send endtime of audio for replacement to get check
			valid=TimeOperations.timeFormatChecker(_endtime,"end time for the audio file to be used as replacement");
		}
		if (valid){
			//send startTime of audio for replacement to get check
			valid=TimeOperations.timeFormatChecker(_startTime2,"start time for the audio file to be used as replacement");
		}
		
		if (valid){
			//send endtime of audio for replacement to get check
			valid=TimeOperations.timeFormatChecker(_endtime2,"end time for the audio file to be used as replacement");
		}
		
		if (valid){ //check if start time and end time make logic sense after the format is valid
			valid=false;
			valid=TimeOperations.timeValidChecker(_startTime, _endtime, "audio file for replacing");
			valid=TimeOperations.timeValidChecker(_startTime2, _endtime2, "file to be replaced");
		}
		
		if(valid){
			//create the gui of replace audio which consist of cancel and progress bar
			JFrame replaceAudioFrame=new JFrame("Replace audio");
			Container pane=replaceAudioFrame.getContentPane();
			pane.setLayout(new GridLayout(2,0));
			JButton cancelButton =new JButton("Cancel Replace Audio");
			JProgressBar dlProgressBar=new JProgressBar();
			replaceAudioFrame.setSize(300, 100); //set size of frame
			cancelButton.addActionListener(new ActionListener() {
				//setup cancel button listener
				@Override
				public void actionPerformed(ActionEvent e) {
					raWork.cancel(true);
				}
			});
			//add window listener to close button (cross hair) so it cancel as well
			replaceAudioFrame.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e){
					raWork.cancel(true);
				}
			});
			replaceAudioFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			replaceAudioFrame.add(cancelButton,pane); //add cancel button to new frame
			replaceAudioFrame.add(dlProgressBar,pane); //add progress bar to new frame
			replaceAudioFrame.setVisible(true); //set visibility of frame on
			replaceAudioFrame.setResizable(false); //set frame so it can't be resize
			//create swing worker object and run it
			raWork=new ReplaceAudioWorker(inFileName,audioFile,replaceAudioFrame,dlProgressBar);
			raWork.execute();
		}
	}

	class ReplaceAudioWorker extends SwingWorker<Void,Integer>{

		private Process process;
		private String _inFileName;
		private String _audioFileName;
		private JFrame _replaceAudioFrame;
		private JProgressBar _dlProgressBar;
		private String outputName="";
		private int i=1;
		
		//constructor to allow the input from user to be use in replaceworker
		ReplaceAudioWorker(String inFileName,String audioFileName,JFrame replaceAudioFrame,JProgressBar dlProgressBar){
			_inFileName=inFileName;
			_audioFileName=audioFileName;
			_replaceAudioFrame=replaceAudioFrame;
			_dlProgressBar=dlProgressBar;
			_dlProgressBar.setStringPainted(true);
			_dlProgressBar.setString(i+"/"+Constants.REPLACE_PROCESS_NUMBER);
		}


		@Override
		protected Void doInBackground() throws Exception {
			//make sure the correct process Builder is setup as it is weird	
			String line;
			FileGeneratorOperations.genTempFolder();//generate temp folder if doesn't exist
			outputName=FileGeneratorOperations.fileNameGen(_inFileName, "replace"); //generate output filename
			//get the audio file name and get the
			String audio=FileNameOperations.fileNameGetter(_audioFileName);
			String input=FileNameOperations.fileNameGetter(_inFileName);
			//get the required replace audio section
			//calculate duration from input
			//if specify outside file
			if(TimeOperations.timeInSec(_endtime2)>(int)(vamix.userInterface.Main.vid.getLength()/1000)){
				_endtime2=TimeOperations.formatTime((int)(vamix.userInterface.Main.vid.getLength()/1000));
			}
			int duration=TimeOperations.timeInSec(_endtime2)-TimeOperations.timeInSec(_startTime2);
			//get part for before replace
			if ((TimeOperations.timeInSec(_endtime)-TimeOperations.timeInSec(_startTime))<(TimeOperations.timeInSec(_endtime2)-TimeOperations.timeInSec(_startTime2))){
				duration=TimeOperations.timeInSec(_endtime)-TimeOperations.timeInSec(_startTime);
			}
			String[] cmdsArray=("avconv -i "+_audioFileName+" -vn -c:a libmp3lame -ss "+_startTime+" -t "+TimeOperations.formatTime(duration)+" -y "+Constants.HIDDEN_DIR+File.separator+audio+"1.mp3").split(" ");
			List<String> cmds=Arrays.asList(cmdsArray);
			ProcessBuilder builder;
			builder=new ProcessBuilder(cmds); 
			builder.redirectErrorStream(true);
			// calculate progress repeatedly and report it
			int totalLength=(int)(vamix.userInterface.Main.vid.getLength()/1000.0);
			try{
				process = builder.start();
				InputStream stdout = process.getInputStream();
				BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
				while((line=stdoutBuffered.readLine())!=null){
					if (isCancelled()){
						process.destroy();//force quit replace
					}else {
						//check time use this as indication for progress
						Matcher m =Pattern.compile("time=(\\d+)").matcher(line);
						if(m.find()){
							//weird problem sometimes avconv gives int 100000000 so don't read it
							if (!(m.group(1).equals("10000000000"))){
								publish((int)(Integer.parseInt(m.group(1))*100/(1+duration)));
							}
						}
					}
				}
				
				duration=TimeOperations.timeInSec(_startTime2);
				//get part before section to be replaced
				cmdsArray=("avconv -i "+_inFileName+" -vn -c:a libmp3lame -ss 00:00:00 -t "+_startTime2+" -y "+Constants.HIDDEN_DIR+File.separator+input+"1.mp3").split(" ");
				cmds=Arrays.asList(cmdsArray);
				builder=new ProcessBuilder(cmds);
				builder.redirectErrorStream(true);
				process = builder.start();
				stdout = process.getInputStream();
				stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
				i=i+1; //increase counter of process by 1
				publish(0);//reset bar
				while((line=stdoutBuffered.readLine())!=null){
					if (isCancelled()){
						process.destroy();//force quit replace
					}else {
						//check time use this as indication for progress
						Matcher m =Pattern.compile("time=(\\d+)").matcher(line);
						if(m.find()){
							//weird problem sometimes avconv gives int 100000000 so don't read it
							if (!(m.group(1).equals("10000000000"))){
								publish((int)(Integer.parseInt(m.group(1))*100/(1+duration)));
							}
						}
					}
				}
				
				//get part for after section to be replaced
				if ((TimeOperations.timeInSec(_endtime)-TimeOperations.timeInSec(_startTime))<(TimeOperations.timeInSec(_endtime2)-TimeOperations.timeInSec(_startTime2))){
					duration=TimeOperations.timeInSec(_startTime2)+TimeOperations.timeInSec(_endtime)-TimeOperations.timeInSec(_startTime);
				}else{
					duration=TimeOperations.timeInSec(_endtime2);
				}

				cmdsArray=("avconv -i "+_inFileName+" -vn -c:a libmp3lame -ss "+TimeOperations.formatTime(duration)+" -t "+TimeOperations.formatTime(totalLength)+" -y "+Constants.HIDDEN_DIR+File.separator+input+"2.mp3").split(" ");
				cmds=Arrays.asList(cmdsArray);
				builder=new ProcessBuilder(cmds);
				builder.redirectErrorStream(true);
				process = builder.start();
				stdout = process.getInputStream();
				stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
				i=i+1; //increase counter of process by 1
				publish(0);//reset bar
				while((line=stdoutBuffered.readLine())!=null){
					if (isCancelled()){
						process.destroy();//force quit replace
					}else {
						//check time use this as indication for progress
						Matcher m =Pattern.compile("time=(\\d+)").matcher(line);
						if(m.find()){
							//weird problem sometimes avconv gives int 100000000 so don't read it
							if (!(m.group(1).equals("10000000000"))){
								publish((int)(Integer.parseInt(m.group(1))*100/(1+duration)));
							}
						}
					}
				}

				//merge the temporary audio files
				cmdsArray=("avconv -i concat:"+Constants.HIDDEN_DIR+File.separator+input+"1.mp3"+"|"+Constants.HIDDEN_DIR+File.separator+audio+"1.mp3"+"|"+Constants.HIDDEN_DIR+File.separator+input+"2.mp3"+" -c copy -y "+Constants.HIDDEN_DIR+File.separator+input+"3.mp3").split(" ");
				cmds=Arrays.asList(cmdsArray);
				builder=new ProcessBuilder(cmds);
				builder.redirectErrorStream(true);
				process = builder.start();
				stdout = process.getInputStream();
				stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
				i=i+1; //increase counter of process by 1
				publish(0);//reset bar
				while((line=stdoutBuffered.readLine())!=null){
					if (isCancelled()){
						process.destroy();//force quit replace
					}else {
						//check time use this as indication for progress
						Matcher m =Pattern.compile("time=(\\d+)").matcher(line);
						if(m.find()){
							//weird problem sometimes avconv gives int 100000000 so don't read it
							if (!(m.group(1).equals("10000000000"))){
								publish((int)(Integer.parseInt(m.group(1))*100/(1+totalLength)));
							}
						}
					}
				}
				
				//replace video's audio with transformed audio
				cmdsArray=("avconv -i "+_inFileName+" -i "+Constants.HIDDEN_DIR+File.separator+input+"3.mp3 -map 0:v -map 1:a -c:v copy -c:a libmp3lame "+outputName).split(" ");
				cmds=Arrays.asList(cmdsArray);
				builder=new ProcessBuilder(cmds);
				builder.redirectErrorStream(true);
				process = builder.start();
				stdout = process.getInputStream();
				stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
				i=i+1; //increase counter of process by 1
				publish(0);//reset bar
				while((line=stdoutBuffered.readLine())!=null){
					if (isCancelled()){
						process.destroy();//force quit replace
					}else {
						//check time use this as indication for progress
						Matcher m =Pattern.compile("time=(\\d+)").matcher(line);
						if(m.find()){
							//weird problem sometimes avconv gives int 100000000 so don't read it
							if (!(m.group(1).equals("10000000000"))){
								publish((int)(Integer.parseInt(m.group(1))*100/(1+totalLength)));
							}
						}
					}
				}
					

			}catch(Exception er){
				//exWork.cancel(true);//cancel the extract work when error encountered
			}

			return null;
		}

		@Override
		protected void done() {
			//when it have finish replacing
			int errorCode=0;
			try {
				errorCode=process.waitFor();
				get();
				publish(100);//set complete in case doinbackground isn't quick enough
			} catch (InterruptedException e) {
			} catch (ExecutionException e) {
			} catch (CancellationException e){
				errorCode=-1; //when cancel error code is -1 
			}

			switch(errorCode){
			case 0://nothing wrong so write to log
				JOptionPane.showMessageDialog(_replaceAudioFrame, "Replacement has finished. Note output is saved to:\n" 
						+outputName+".");
				break;
			case -1://replacing cancelled
				JOptionPane.showMessageDialog(_replaceAudioFrame, "Replacement has been cancelled.");
				break;
			default://error message of generic
				JOptionPane.showMessageDialog(_replaceAudioFrame, "An error has occurred. Please try again. The error code is: "+errorCode);
				break;
			}
			this._replaceAudioFrame.dispose();
			//ask user if they want to load or preview the video
			if (errorCode==0){ //when finish correctly
				VideoOperations.loadAndPreview(outputName,_startTime2,_endtime2);
			}
		}
		
		@Override
		protected void process(List<Integer> chunks) {
			if (!isCancelled()){
				//publish to progress bar
				for(int pro : chunks){
					_dlProgressBar.setValue(pro);
					_dlProgressBar.setString(i+"/"+Constants.REPLACE_PROCESS_NUMBER);
				}
			}
		}
	}

}
