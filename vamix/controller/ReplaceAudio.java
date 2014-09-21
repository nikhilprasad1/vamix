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
	//constructor for replace audio pass infile and out file
	ReplaceAudio(String inFile,String inAudio,String startTime,String endtime){
		_inFile=inFile;//current directory input to be replace file
		_inAudio=inAudio;//current directory input audio file
		_startTime=startTime;//start time for replace audio
		_endtime=endtime;//end time for replace audio
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
		if(Helper.validInFile(inFileName,"(MPEG)|Video")){
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
			valid=false;
			if(_startTime.equals("")){
				//error message of empty starttime
				JOptionPane.showMessageDialog(null, "You have entered a empty start time. Please input a start time.");
			}else{
				Matcher m =Pattern.compile("^\\d{2}:\\d{2}:\\d{2}$").matcher(_startTime);
				if (m.find()){
					valid=true;
				}else{
					//error message of empty file name
					JOptionPane.showMessageDialog(null, "You have entered a invalid format. Please input a valid format for start time.");
				}
			}
		}
		
		if (valid){
			valid=false;
			if(_endtime.equals("")){
				//error message of empty duration Time
				JOptionPane.showMessageDialog(null, "You have entered a empty start time. Please input a non-empty duration.");
			}else{
				//set up matcher that reads the correct format of starThh:mm:ssEnd
				Matcher m =Pattern.compile("^\\d{2}:\\d{2}:\\d{2}$").matcher(_endtime);
				if (m.find()){
					valid=true;
				}else{
					//error message of empty file name
					JOptionPane.showMessageDialog(null, "You have entered a invalid format. Please input a valid format for duration time.");
				}
			}
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
		private String infileType="";
		//constructor to allow the input from user to be use in extractworker
		ReplaceAudioWorker(String inFileName,String audioFileName,JFrame replaceAudioFrame,JProgressBar dlProgressBar){
			_inFileName=inFileName;
			_audioFileName=audioFileName;
			_replaceAudioFrame=replaceAudioFrame;
			_dlProgressBar=dlProgressBar;
		}


		@Override
		protected Void doInBackground() throws Exception {
			//make sure the correct process Builder is setup as it is weird
			List<String> cmds=new ArrayList<String>();//jumbo all cmd into a list
			cmds.add("avconv");//use avconv
			cmds.add("-i");//set avconv to i input
			cmds.add(_inFileName);//add file name
			cmds.add("-i");//set inout
			cmds.add(_audioFileName);
			cmds.add("-map"); //map video of input stream 1 to ouput video
			cmds.add("0:v"); 
			cmds.add("-map"); //map audio of input stream 2 to output audio
			cmds.add("1:a");
			cmds.add("-c:v");//option of avconv of copying video
			cmds.add("copy");//just copy video
			cmds.add("-c:a");//option of avconv of copying audio
			cmds.add("libmp3lame"); //format of output of audio extraction
			cmds.add("-ss");//set start time of file
			cmds.add(_startTime); //set time of extraction
			cmds.add("-t");//set duration time of file
			cmds.add(_endtime); //set time of extraction duration
			infileType=_inFileName.substring(_inFileName.length()-4, _inFileName.length());
			cmds.add(_inFileName.substring(0, _inFileName.length()-4)+"_trackReplace"+infileType); //the output file name

			ProcessBuilder builder;

			builder=new ProcessBuilder(cmds); 
			builder.redirectErrorStream(true);
			// workout the length of the extracted file tho work out progress bar
			int totalLength=(int)(vamix.view.Main.vid.getLength()/1000.0);
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
				JOptionPane.showMessageDialog(null, "Replace audio has finished. Note output is saved to "+_inFileName.substring(0, _inFileName.length()-4)+"_trackReplace"+infileType+".");
				break;
			case -1://extract cancelled
				JOptionPane.showMessageDialog(null, "Replace audio  has been cancelled. Note output is saved to "+_inFileName.substring(0, _inFileName.length()-4)+"_trackReplace"+infileType+".");
				break;
			default://error message of generic
				JOptionPane.showMessageDialog(null, "An error have occured. Please try again. The error code is: "+errorCode);
				break;
			}
			this._replaceAudioFrame.dispose();
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
