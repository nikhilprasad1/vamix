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
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JRootPane;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileNameExtensionFilter;


public class Extract {
	ExtractWorker exWork;//class variable for worker so cancel button can work
	private String _saveDir;
	//constructor for extract pass in rootPane for message box to attached to
	Extract(String saveDir){
		_saveDir=saveDir;
	}

	/*
	 * Function to perform extract from a file and basic error handle
	 */
	public void extractFunction(){
		//set validness of filename to false as initialisation and other general initialisation
		boolean valid=false; //if inputs are valid
		boolean isAudio=false; //if file type is audio
		String inFileName=""; //input filename
		String outFileName=""; //output file name
		String startTime=""; //start time of extract
		String durationTime=""; //duration of extract
		int overrideChoice=-1; //for override button choice
		String path=""; //variable for path
		
		//get input file
		while(!valid){
			//setup file chooser
			JFileChooser chooser = new JFileChooser(Constants.CURRENT_DIR);
		    FileNameExtensionFilter filter = new FileNameExtensionFilter("Video/audio file","avi","mov","mp4"
					,"mp3","wav","wmv");
		    chooser.setFileFilter(filter); //set mp3 filter
		    //get save path
		    chooser.showOpenDialog(null);
			File file =chooser.getSelectedFile();
			try{
				inFileName=file.getAbsolutePath();//get path address
			}catch(NullPointerException e){
				return; //when user cancel
			}
			
			//now get the path of file and just file name
			Matcher m=Pattern.compile("(.*"+File.separator+")(.*)$").matcher(inFileName);
			if(m.find()){
				path = m.group(1); //get path
				inFileName=m.group(2); //get file name
			}
			
			if(inFileName.equals("")){
				//error message of empty file name
				JOptionPane.showMessageDialog(null, "You have entered a empty file name. Please input a valid file name.");
			}else{
				//check if the file exist locally
				if (Helper.fileExist(path+inFileName)){
					String bash =File.separator+"bin"+File.separator+"bash";
					String cmd ="echo $(file "+path+inFileName+")";
					ProcessBuilder builder=new ProcessBuilder(bash,"-c",cmd); 
					builder.redirectErrorStream(true);

					try{
						Process process = builder.start();
						InputStream stdout = process.getInputStream();
						BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
						String line;
						while((line=stdoutBuffered.readLine())!=null){
							Matcher macth =Pattern.compile("(MPEG)").matcher(line);
							if(macth.find()){
								isAudio=true;
							}
						}
					}catch(Exception e){
						e.printStackTrace();
					}
					//check if audio using bash commands
					if (isAudio){
						valid=true;
					}else{
						//file is not audio/mpeg type
						JOptionPane.showMessageDialog(null, "You have entered a non-audio file please enter a valid file.");
					}
				}else{
					//file does not exist so give error
					JOptionPane.showMessageDialog(null, "You have entered a non-existing file. Please input a valid file type.");
				}
			}
		}

		//get output file
		valid=false;
		while(!valid){
			outFileName =(String)JOptionPane.showInputDialog(null,"Name of output file of extract.");
			if (outFileName==null){
				return; //if cancel
			}else if(outFileName.equals("")){
				//error message of empty file name
				JOptionPane.showMessageDialog(null, "You have entered a empty file name. Please input a valid file name.");
			}else{
				Matcher m =Pattern.compile(".mp3$").matcher(outFileName);
				if (inFileName.equals(outFileName)){
					//error message need to not be the same name as input file
					JOptionPane.showMessageDialog(null, "You have entered a output file name that is same with input file. Please input a valid file name that isnt the same.");
				}else if (m.find()){
					if (Helper.fileExist(path+outFileName)){
						//file exist ask if override
						Object[] option= {"Override","New output file name"};
						//check if the file exist locally
						//note 0 is override ie first option chosen and 1 is new name
						overrideChoice=JOptionPane.showOptionDialog(null, "File " +outFileName +" already exist. Do you wish to override or input new file name?",
								"Override?",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,option,option[0]);
						if(overrideChoice==0){
							valid=true;
						}
					}else{
						valid=true;
					}
				}else{
					//error message need to end with mp3
					JOptionPane.showMessageDialog(null, "You have entered a file name that doesnt end with mp3. Please input a valid file name that ends with mp3.");
				}

			}
		}

		//get start time and duration time
		valid=false;
		while(!valid){
			startTime =(String)JOptionPane.showInputDialog(null,"Start time of extract must be in format of hh:mm:ss");
			if (startTime==null){
				return; //if cancel
			}else if(startTime.equals("")){
				//error message of empty starttime
				JOptionPane.showMessageDialog(null, "You have entered a empty start time. Please input a start time.");
			}else{
				Matcher m =Pattern.compile("^\\d{2}:\\d{2}:\\d{2}$").matcher(startTime);
				if (m.find()){
					valid=true;
				}else{
					//error message of empty file name
					JOptionPane.showMessageDialog(null, "You have entered a invalid format. Please input a valid format for start time.");
				}
			}
		}

		valid=false;
		while(!valid){
			durationTime =(String)JOptionPane.showInputDialog(null,"duration of extract must be in format of hh:mm:ss");
			if (durationTime==null){
				return; //if cancel
			}else if(durationTime.equals("")){
				//error message of empty duration Time
				JOptionPane.showMessageDialog(null, "You have entered a empty start time. Please input a non-empty duration.");
			}else{
				//set up matcher that reads the correct format of starThh:mm:ssEnd
				Matcher m =Pattern.compile("^\\d{2}:\\d{2}:\\d{2}$").matcher(durationTime);
				if (m.find()){
					valid=true;
				}else{
					//error message of empty file name
					JOptionPane.showMessageDialog(null, "You have entered a invalid format. Please input a valid format for duration time.");
				}
			}
		}
		//delete to be override file
		if(overrideChoice==0){
			File file=new File(path+outFileName);
			file.delete();
		}
		//create the gui of extract which consist of cancel and progress bar
		JFrame extractFrame=new JFrame("Extracting");
		Container pane=extractFrame.getContentPane();
		pane.setLayout(new GridLayout(2,0));
		JButton cancelButton =new JButton("Cancel Extract");
		JProgressBar dlProgressBar=new JProgressBar();
		extractFrame.setSize(300, 100); //set size of frame
		cancelButton.addActionListener(new ActionListener() {
			//setup cancel button listener
			@Override
			public void actionPerformed(ActionEvent e) {
				exWork.cancel(true);
			}
		});
		//add window listener to close button (cross hair) so it cancel as well
		extractFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e){
				exWork.cancel(true);
			}
		});
		extractFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		extractFrame.add(cancelButton,pane); //add cancel button to new frame
		extractFrame.add(dlProgressBar,pane); //add progress bar to new frame
		extractFrame.setVisible(true); //set visiblity of frame on
		extractFrame.setResizable(false); //set frame so it cant be resize
		//create swing worker obejct and run it
		exWork=new ExtractWorker(path+inFileName,path+outFileName,startTime,durationTime,extractFrame,dlProgressBar);
		exWork.execute();
	}

	class ExtractWorker extends SwingWorker<Void,Integer>{

		private Process process;
		private String _inFileName;
		private String _outFileName;
		private String _startTime;
		private String _duration;
		private JFrame _extractFrame;
		private JProgressBar _dlProgressBar;
		
		//constructor to allow the input from user to be use in extractworker
		ExtractWorker(String inFileName,String outFileName,String startTime,String duration,JFrame extractFrame,JProgressBar dlProgressBar){
			_inFileName=inFileName;
			_outFileName=outFileName;
			_startTime=startTime;
			_duration=duration;
			_extractFrame=extractFrame;
			_dlProgressBar=dlProgressBar;
		}


		@Override
		protected Void doInBackground() throws Exception {
			//make sure the correct process Builder is setup as it is weird
			//the bash command avconv -i $inputFile -vn -c:a libmp3lame -ss $startTime -t $duration $outputFile
			
			List<String> cmds=new ArrayList<String>();//jumbo all cmd into a list
			cmds.add("avconv");//use avconv
			cmds.add("-i");//set avconv to i
			cmds.add(_inFileName);//add file name
			cmds.add("-vn");//more avconv functions
			cmds.add("-c:a");//option of avconv of copying audio only
			cmds.add("libmp3lame"); //format of output of audio extraction
			cmds.add("-ss");//set option of setting starttime
			cmds.add(_startTime);//the start time
			cmds.add("-t");//option of duration of extration
			cmds.add(_duration); //the duration
			cmds.add(_outFileName); //the output file name
			ProcessBuilder builder;

			builder=new ProcessBuilder(cmds); 
			builder.redirectErrorStream(true);
			// workout the length of the extracted file tho work out progress bar
			String[] durationBits = _duration.split(":",-1);
			int totalLength=(int)Integer.parseInt(durationBits[0])*60*60+Integer.parseInt(durationBits[1])*60+Integer.parseInt(durationBits[2]);
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
								publish((int)Integer.parseInt(m.group(1))*100/totalLength);
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
				JOptionPane.showMessageDialog(null, "Extract has finished. Note output is saved to "+_outFileName+".");
				break;
			case -1://extract cancelled
				JOptionPane.showMessageDialog(null, "Extract has been cancelled. Note output is saved to "+_outFileName+".");
				break;
			default://error message of generic
				JOptionPane.showMessageDialog(null, "An error have occured. Please try again. The error code is: "+errorCode);
				break;
			}
			this._extractFrame.dispose();
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
