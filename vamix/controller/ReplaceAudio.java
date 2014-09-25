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


public class ReplaceAudio {
	ReplaceAudioWorker raWork;//class variable for worker so cancel button can work
	private String _inFile;
	private String _inAudio;
	private	String _startTime;
	private String _endtime;
	private	String _startTime2;
	private String _endtime2;
	//constructor for replace audio pass infile and out file
	ReplaceAudio(String inFile,String inAudio,String startTime,String endtime,String startTime2,String endtime2){
		_inFile=inFile;//current directory input to be replace file
		_inAudio=inAudio;//current directory input audio file
		_startTime=startTime;//start time for replace audio
		_endtime=endtime;//end time for replace audio
		_startTime2=startTime2;//start time for replace audio
		_endtime2=endtime2;//end time for replace audio
	}

	/*
	 * Function to perform replace audio of a file and basic error handle
	 */
	public void replaceAudioFunction(){
		//set validness of filename to false as initialisation and other general initialisation
		boolean valid=false; //if inputs are valid
		boolean isAudio=false;//if file is audio
		String inFileName=_inFile; //input to replace filename full path
		String audioFile=_inAudio; //input audio filename full path
		
		//get output file
		if(Helper.validInFile(inFileName,Constants.VIDEO_AUDIO_TYPE)){
			valid=false; //set corretness of outfile to false
			if (audioFile==null){
				JOptionPane.showMessageDialog(null, "You have not entered an audio file name. Please input a valid file name.");
			}else if(audioFile.equals("")){
				//error message of empty file name
				JOptionPane.showMessageDialog(null, "You have entered a empty file name. Please input a valid file name.");
			}else if (_inFile.equals(_inAudio)){
				//error message need to not be the same name as input file
				JOptionPane.showMessageDialog(null, "You have entered a replace file name that is same with input audio file. Please input a valid audio file name that isnt the same.");
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
					e.printStackTrace();
				}
				if (isAudio==true){
					valid=true;//audio file valid
				}else{
					//not an audio file
					JOptionPane.showMessageDialog(null, "You have not enter a valid audio file for replace.");
				}
			}
		}
		
		if (valid){
			//send startTime of audio for overlay to get check
			valid=Helper.timeValidTypeChecker(_startTime,"start time for the audio file used for overlay");
		}
		
		if (valid){
			//send endtime of audio for overlay to get check
			valid=Helper.timeValidTypeChecker(_endtime,"end time for the audio file used for overlay");
		}
		if (valid){
			//send startTime of audio for overlay to get check
			valid=Helper.timeValidTypeChecker(_startTime2,"start time for the audio file to be overlay");
		}
		
		if (valid){
			//send endtime of audio for overlay to get check
			valid=Helper.timeValidTypeChecker(_endtime2,"end time for the audio file to be overlay");
		}
		
		if (valid){ //check if start time and end time make logic sense after the format is valid
			valid=false;
			valid=Helper.timeValidChecker(_startTime, _endtime, "audio file for replace");
			valid=Helper.timeValidChecker(_startTime2, _endtime2, "file to be replace");
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
			replaceAudioFrame.setVisible(true); //set visiblity of frame on
			replaceAudioFrame.setResizable(false); //set frame so it cant be resize
			//create swing worker obejct and run it
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
		//constructor to allow the input from user to be use in extractworker
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
			Helper.genTempFolder();//generate temp folder if doesnt exist
			outputName=Helper.fileNameGen(_inFileName, "replace"); //generate output filename
			//get the audio file name and get the
			String audio=Helper.fileNameGetter(_audioFileName);
			String input=Helper.fileNameGetter(_inFileName);
			//get the required replace audio section
			//calculate duration from input
			int duration=Helper.timeInSec(_endtime2)-Helper.timeInSec(_startTime2);
			//get part for after replace
			if ((Helper.timeInSec(_endtime)-Helper.timeInSec(_startTime))<(Helper.timeInSec(_endtime2)-Helper.timeInSec(_startTime2))){
				duration=Helper.timeInSec(_endtime)-Helper.timeInSec(_startTime);
			}
			String[] cmdsArray=("avconv -i "+_audioFileName+" -vn -c:a libmp3lame -ss "+_startTime+" -t "+Helper.formatTime(duration)+" -y "+Constants.LOG_DIR+File.separator+audio+"1.mp3").split(" ");
			List<String> cmds=Arrays.asList(cmdsArray);
			ProcessBuilder builder;
			builder=new ProcessBuilder(cmds); 
			builder.redirectErrorStream(true);
			// workout the length of the extracted file tho work out progress bar
			int totalLength=(int)(vamix.view.Main.vid.getLength()/1000.0);
			try{
				process = builder.start();
				InputStream stdout = process.getInputStream();
				BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
				while((line=stdoutBuffered.readLine())!=null){
					if (isCancelled()){
						process.destroy();//force quit extract
					}else {
						//check time use this as indication for progress
						Matcher m =Pattern.compile("time=(\\d+)").matcher(line);
						if(m.find()){
							//weird problem sometimes avconv gives int 100000000 so dont read it
							if (!(m.group(1).equals("10000000000"))){
								publish((int)(Integer.parseInt(m.group(1))*100/duration));
							}
						}
					}
				}
				
				duration=Helper.timeInSec(_startTime2);
				//get part before replacae
				cmdsArray=("avconv -i "+_inFileName+" -vn -c:a libmp3lame -ss 00:00:00 -t "+_startTime2+" -y "+Constants.LOG_DIR+File.separator+input+"1.mp3").split(" ");
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
						process.destroy();//force quit extract
					}else {
						//check time use this as indication for progress
						Matcher m =Pattern.compile("time=(\\d+)").matcher(line);
						if(m.find()){
							//weird problem sometimes avconv gives int 100000000 so dont read it
							if (!(m.group(1).equals("10000000000"))){
								publish((int)(Integer.parseInt(m.group(1))*100/duration));
							}
						}
					}
				}
				
				//get part for after replace
				if ((Helper.timeInSec(_endtime)-Helper.timeInSec(_startTime))<(Helper.timeInSec(_endtime2)-Helper.timeInSec(_startTime2))){
					duration=Helper.timeInSec(_startTime2)+Helper.timeInSec(_endtime)-Helper.timeInSec(_startTime);
				}else{
					duration=Helper.timeInSec(_endtime2);
				}

				cmdsArray=("avconv -i "+_inFileName+" -vn -c:a libmp3lame -ss "+Helper.formatTime(duration)+" -t "+Helper.formatTime(totalLength)+" -y "+Constants.LOG_DIR+File.separator+input+"2.mp3").split(" ");
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
						process.destroy();//force quit extract
					}else {
						//check time use this as indication for progress
						Matcher m =Pattern.compile("time=(\\d+)").matcher(line);
						if(m.find()){
							//weird problem sometimes avconv gives int 100000000 so dont read it
							if (!(m.group(1).equals("10000000000"))){
								publish((int)(Integer.parseInt(m.group(1))*100/duration));
							}
						}
					}
				}

				//merge the audios
				cmdsArray=("avconv -i concat:"+Constants.LOG_DIR+File.separator+input+"1.mp3"+"|"+Constants.LOG_DIR+File.separator+audio+"1.mp3"+"|"+Constants.LOG_DIR+File.separator+input+"2.mp3"+" -c copy -y "+Constants.LOG_DIR+File.separator+input+"3.mp3").split(" ");
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
						process.destroy();//force quit extract
					}else {
						//check time use this as indication for progress
						Matcher m =Pattern.compile("time=(\\d+)").matcher(line);
						if(m.find()){
							//weird problem sometimes avconv gives int 100000000 so dont read it
							if (!(m.group(1).equals("10000000000"))){
								publish((int)(Integer.parseInt(m.group(1))*100/totalLength));
							}
						}
					}
				}
				
				//replace video's audio with transformed audio
				cmdsArray=("avconv -i "+_inFileName+" -i "+Constants.LOG_DIR+File.separator+input+"3.mp3 -map 0:v -map 1:a -c:v copy -c:a libmp3lame "+outputName).split(" ");
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
						process.destroy();//force quit extract
					}else {
						//check time use this as indication for progress
						Matcher m =Pattern.compile("time=(\\d+)").matcher(line);
						if(m.find()){
							//weird problem sometimes avconv gives int 100000000 so dont read it
							if (!(m.group(1).equals("10000000000"))){
								publish((int)(Integer.parseInt(m.group(1))*100/totalLength));
							}
						}
					}
				}
					

			}catch(Exception er){
				//exWork.cancel(true);//cancel the extract work when error encounterd
			}

			return null;
		}

		@Override
		protected void done() {
			//when it have finish Extracting
			int errorCode=0;
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
				JOptionPane.showMessageDialog(_replaceAudioFrame, "Replace audio has finished. \nNote output is saved to "+outputName+".");
				break;
			case -1://extract cancelled
				JOptionPane.showMessageDialog(_replaceAudioFrame, "Replace audio  has been cancelled.");
				break;
			default://error message of generic
				JOptionPane.showMessageDialog(_replaceAudioFrame, "An error have occured. Please try again. The error code is: "+errorCode);
				break;
			}
			this._replaceAudioFrame.dispose();
			//ask user if they want to load or preview the video
			if (errorCode==0){ //when finish correctly
				Helper.loadAndPreview(outputName,_startTime2,_endtime2);
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
