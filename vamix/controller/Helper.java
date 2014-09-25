package vamix.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Class to provide function which help check file existence
 **/
public class Helper {
	/**
	 * Function to read from a file
	 */
	public static void fileRead() {
		try{
			BufferedReader tempRead =new BufferedReader(new FileReader(Constants.LOG_PATH));
			String line=null;
			String output="";
			while((line =tempRead.readLine())!=null){
				output=output+line+System.lineSeparator();
			}
			tempRead.close();//close the reader
			//setup new window with scroll for log
			JFrame logFrame =new JFrame("Log");
			JTextArea logDisplay=new JTextArea(output);
			logDisplay.setEditable(false);
			//JOptionPane.showMessageDialog(this, logDisplay);
			JScrollPane scroll = new JScrollPane(logDisplay,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
			logFrame.add(scroll);
			logFrame.setSize(400, 400);
			logFrame.setResizable(false);
			logFrame.setVisible(true);

		}catch(IOException e){
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Function to write to a file
	 */
	public static void fileWrite(String message) {
		try{
			PrintWriter out =new PrintWriter(new BufferedWriter(new FileWriter(Constants.LOG_PATH,true)));
			out.println(message);
			out.close();
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Function to check if a file exist given its directory
	 */
	public static boolean fileExist(String pathname) {
		File f = new File(pathname);
		return f.exists();
	}

	/**
	 * Function to return timestamp in the correct format
	 */
	public static String timeStamper(){
		Date date =new Date();
		SimpleDateFormat temp =new SimpleDateFormat("dd-MMMM-yyyy HH:mm");
		return temp.format(date);
	}

	/**
	 * Function to get line number from a file
	 */
	public static int lineNumberOfFile(String fileDir){
		int lines =0;
		try{
			BufferedReader tempReader =new BufferedReader(new FileReader(fileDir));
			while (tempReader.readLine()!=null) {lines++;}	
			tempReader.close(); //close the reader
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
		return lines;
	}
	
	/**
	 * Function which change time in milsec to the xx:xx:xx/xx:xx:xx format
	 * @param input: double currentTime (current time of file) in sec
	 * 				 double totalTime (total time of file) in sec
	 * 		  output: String formatted time as string xx:xx:xx/xx:xx:xx 
	 */
	public static String timeOfVideo(double currenttime,double totalTime){
		//return the concatenation of time
		return formatTime((int)currenttime)+"/"+formatTime((int)totalTime);
	}
	
	/**
	 * Function which change time in sec to the xx:xx:xxformat
	 * @param input: int time
	 * 		  output: String formatted time as string xx:xx:xx
	 */
	public static String formatTime(int time){
		String formatTime="";
		//convert to hr 
		if (time/3600>=1){
			if (time/3600>=10){
				formatTime=time/3600+":";
			}else{
				formatTime="0"+time/3600+":";
			}			
			time=time-(time/3600)*3600;
		}else{
			formatTime="00:";
		}
		//convert to minute
		if (time/60>=1){
			if (time/60>=10){
				formatTime=formatTime+time/60+":";
			}else{
				formatTime=formatTime+"0"+time/60+":";
			}
			time=time-(time/60)*60;
		}else{
			formatTime=formatTime+"00:";
		}
		if (time<10){//work out second add addition zero
			formatTime=formatTime+"0"+time;
		}else{
			formatTime=formatTime+time;
		}
		return formatTime;
	}
	
	/**
	 * Function which change time in  xx:xx:xx format to sec
	 * @param input:String formatted time as string xx:xx:xx 
	 * 		  output: int time
	 */
	public static int timeInSec(String formattime){
		int time=0;
		//split string then convert
		String[] durationBits = formattime.split(":",-1);
		time=(int)Integer.parseInt(durationBits[0])*60*60+Integer.parseInt(durationBits[1])*60+Integer.parseInt(durationBits[2]);
		return time;
	}
	
	/*
	 * Function to get the length of the video in the format hh:mm:ss
	 * Assumes length of video is not greater than 99hrs, 59mins and 59secs
	 */
//	public static String getVideoLength(MediaPlayer player) {
//		//string to return
//		String length = "";
//		String hours = "00", minutes = "00", seconds = "00";
//		long songLength = player.getLength();
//		
//		//divide song length by no. of milliseconds in an hour to get the no. of hours
//		hours = String.valueOf((songLength % 3600000));
//		//get the remaining time to sort into minutes and then seconds
//		songLength = songLength - (Long.parseLong(hours)*3600000);
//		//divide remaining length by no. of ms in an minute to get no. of minutes
//		minutes = String.valueOf((songLength % 60000));
//		//get remaining time and convert to seconds
//		songLength = songLength - (Long.parseLong(minutes)*60000);
//		seconds = String.valueOf((int)(songLength/1000));
//		
//		//now check to see if either of the three components are only one digit long
//		if (hours.length() == 1) {
//			hours = "0" + hours;
//		}
//		if (minutes.length() == 1) {
//			minutes = "0" + minutes;
//		}
//		if (seconds.length() == 1) {
//			seconds = "0" + seconds;
//		}
//		length = hours + ":" + minutes + ":" + seconds;
//		return length;
//	}
	
	/**
	 * Function to choose where to save file only allow specified file type
	 * @param input: String file_type_message: a message about type ie mp3 file, audio etc
	 * 				String filterType: the actual type like .mp3
	 * 		  output: String full path name of file
	 */
	public static String saveFileChooser(String file_type_message,String filterType){
		String tempName = null;
		//setup chooser
		JFileChooser chooser = new JFileChooser(Constants.CURRENT_DIR);
	    FileNameExtensionFilter filter = new FileNameExtensionFilter(file_type_message,filterType);
	    chooser.setFileFilter(filter); //set mp3 filter
	    //get save path
	    int choice =chooser.showSaveDialog(null);
		if (!(choice==JFileChooser.CANCEL_OPTION)){
			File file =chooser.getSelectedFile();
			tempName=file.getAbsolutePath();//get path address
			//check if it is mp3 if not add
			if (!tempName.substring(tempName.length()-4, tempName.length()).equals("."+filterType)){
				tempName=tempName+"."+filterType; //add the filter type to end of file
			}
		}

		return tempName;
	}
	
	/**
	 * Function to check whether file is valid
	 * @param input: String file name
	 * 		  output: boolean if file is valid
	 */
	public static boolean validInFile(String fileName,String pattern){
		String inFileName="";
		String path="";
		boolean isValid=false;
		boolean isVideoAudio=false;
		//now get the path of file and just file name
		Matcher m=Pattern.compile("(.*"+File.separator+")(.*)$").matcher(fileName);
		if(m.find()){
			path = m.group(1); //get path
			inFileName=m.group(2); //get file name
		}
		
		if(fileName.equals("")){
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
						Matcher macth =Pattern.compile(pattern,Pattern.CASE_INSENSITIVE).matcher(line);
						if(macth.find()){
							isVideoAudio=true;
						}
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				//check if video audio using bash commands
				if (isVideoAudio){
					isValid=true;
				}else{
					//file is not audio/mpeg type
					JOptionPane.showMessageDialog(null, "You have entered a non-video/audio file please enter a valid file.");
				}
			}else{
				//file does not exist so give error
				JOptionPane.showMessageDialog(null, "You have entered a non-existing file. Please input a valid file type.");
			}
		}
		return isValid;
	}

	/**
	 * Function to choose audio file for replace
	 * @param input: none
	 * 		  output: String full path name of file
	 */
	public static String audioFileChooser(){
		String tempName = null;
		//setup chooser
		JFileChooser chooser = new JFileChooser(Constants.CURRENT_DIR);
	    FileNameExtensionFilter filter = new FileNameExtensionFilter("Mp3 File","mp3");
	    chooser.setFileFilter(filter); //set mp3 filter
	    //get save path
	    int choice =chooser.showOpenDialog(null);
		if (!(choice==JFileChooser.CANCEL_OPTION)){
			File file =chooser.getSelectedFile();
			tempName=file.getAbsolutePath();//get path address
		}

		return tempName;
	}
	
	/**
	 * Function to check validness of time input need to be informat of dd:dd:dd
	 * @param
	 * input: String time is time to check and String timeType is which time eg start time
	 * output boolean validness of time ie true if format is ok
	 */
	public static boolean timeValidTypeChecker(String time, String timeType){
		boolean valid=false;
		if(time.equals("")){
			//error message of empty starttime
			JOptionPane.showMessageDialog(null, "You have entered a empty "+timeType+". Please input a start time.");
		}else{
			Matcher m =Pattern.compile("^\\d{2}:\\d{2}:\\d{2}$").matcher(time);
			if (m.find()){
				valid=true;
			}else{
				//error message of empty file name
				JOptionPane.showMessageDialog(null, "You have entered a invalid format for "+timeType +". Please input a valid format for start time.");
			}
		}
		return valid;
	}
	
	/**
	 * Function to check validness of time start input relative to the end time
	 * @param
	 * input: String timeStart is the start time to check 
	 * 		  String start timeEnd String 
	 * 		  timeType is which time eg from file
	 * output boolean validness of time ie true if format is ok
	 */
	public static boolean timeValidChecker(String timeStart,String timeEnd, String timeType){
		boolean valid=false;
		if (timeStart.equals(timeEnd)){
			JOptionPane.showMessageDialog(null, "You have entered the same start time and end time for "+timeType+". Please enter a different end time.");
		}else if (timeInSec(timeStart)>=timeInSec(timeEnd)){
			JOptionPane.showMessageDialog(null, "You have entered a greater start time than end time for "+timeType+". Please enter a different start or end time.");
		}else{
			valid=true;//if didnt fail checks set to valid
		}
		return valid;
	}
	
	/**
	 * This function is to get the file name of a file full paht
	 * @param
	 * input: String - file path
	 * output: String -file name
	 */
	public static String fileNameGetter(String _inputAddr){
		String inFileName="";
		Matcher m=Pattern.compile("(.*"+File.separator+")(\\S+).*$").matcher(_inputAddr);
		if(m.find()){
			inFileName=m.group(2); //get file name
		}
		return inFileName;
	}
	
	/**
	 * This function is to get the file path of a file full path
	 * @param
	 * input: String - file whole path
	 * output: String -file path (without file name)
	 */
	public static String pathGetter(String _inputAddr){
		String path="";
		Matcher m=Pattern.compile("(.*"+File.separator+")(\\S+).*$").matcher(_inputAddr);
		if(m.find()){
			path=m.group(1); //get file path
		}
		return path;
	}
	
	/**
	 * This function is to generate fileName so overwritten wont occur
	 * @param
	 * input: String - file whole path
	 * 		  String extraName -extra message want to add after file but before new number
	 * output: String -new file name whole path with new non repeated file name
	 */
	public static String fileNameGen(String _inputAddr,String extraName){
		String path="";//
		String fileType="";//
		String nameGen=""; //
		int i=1; //addition to the name
		boolean run =true;//for while loop logic set to true automatically
		//split to path with fileName and file type
		Matcher m=Pattern.compile("(.*)(\\p{Punct}.*)$").matcher(_inputAddr);
		if(m.find()){
			path=m.group(1); //get file path with name
			fileType=m.group(2); //get type
		}
		while(run){
			nameGen=path+"_"+extraName+"_"+i+fileType;
			run=fileExist(nameGen);
			i=i+1;//increse value by 1
		}
		return nameGen;
	}
	
	/**
	 * Function that determines if the given input file is a valid video file
	 * Only meant to accept .mp4, .avi and .flv
	 */
	public static boolean validVideoFile(String fileName,String pattern){
		String inFileName="";
		String path="";
		boolean isValid=false;
		boolean isVideo=false;
		//now get the path of file and just file name
		Matcher m=Pattern.compile("(.*"+File.separator+")(.*)$").matcher(fileName);
		if(m.find()){
			path = m.group(1); //get path
			inFileName=m.group(2); //get file name
		}
		
		if(fileName.equals("")){
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
						Matcher match =Pattern.compile(pattern,Pattern.CASE_INSENSITIVE).matcher(line);
						if(match.find()){
							isVideo=true;
						}
					}
				}catch(Exception e){
					e.printStackTrace();
				}
				//check if video audio using bash commands
				if (isVideo){
					isValid=true;
				}else{
					//file is not audio/mpeg type
					JOptionPane.showMessageDialog(null, "You have entered a non-video file, please enter a valid file.");
				}
			}else{
				//file does not exist so give error
				JOptionPane.showMessageDialog(null, "You have entered a non-existing file. Please input a valid file type.");
			}
		}
		return isValid;
	}

	/**
	 * This function is to .vamix folder for temporary files
	 * @param
	 * input: void
	 * output: void 
	 */
	public static void genTempFolder(){
		if (!(fileExist(Constants.LOG_DIR))){
			//when directory doesnt exist create directory
			File dir =new File(Constants.LOG_DIR);
			dir.mkdir();
		}
	}
	
	/**
	 * This function is to load and/or preview product
	 * @param
	 * input: String outputName: the output file full path
	 * 		  String startTime: the start time for transform
	 * 		  String endTime: the end time for transform
	 * output: void 
	 */
	public static void loadAndPreview(String outputName,String startTime,String endTime){
		//initialise booleans
		boolean load=false;
		boolean preview=false;
		if (Helper.validVideoFile(outputName, "(audio)|MPEG|video")){
			String[] cmdsArray=("avplay -i "+outputName).split(" ");
			if (startTime!=null&&endTime!=null){ //only do this when start and endtime not null
				int previewTimeS=timeInSec(startTime);
				if (previewTimeS>5){ //set preview start time to 5 sec before if its longer than 5
					previewTimeS=previewTimeS-5;
				}
				int previewTimeE=timeInSec(endTime);
				if (previewTimeE>5){ //set preview end time to 5 sec before if its longer than 5
					previewTimeE=previewTimeE-5;
				}
				cmdsArray=("avplay -i "+outputName+" -ss "+formatTime(previewTimeS)+" -t "+formatTime(previewTimeE)).split(" ");
			}
			
			
			//create option for user to choose
			Object[] option= {"Load","Play preview","Both", "None"};
			int overrideChoice=JOptionPane.showOptionDialog(null, "Do you wish to play and/or load " +outputName+".",
					"Load and/or Play preview?",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,option,option[0]);
			if(overrideChoice==0){
				load=true;
			}else if(overrideChoice==1){
				//play the preview using avplay
				preview=true;
			}else if (overrideChoice==2){
				load=true;
				preview=true;
			}
			
			if (load){
				//prepare (load) vid and set the new address
				VamixController.vidAddSetter(outputName);
				vamix.view.Main.vid.prepareMedia(outputName);
			}
			if(preview){
				List<String> cmds=Arrays.asList(cmdsArray);
				ProcessBuilder builder;
				builder=new ProcessBuilder(cmds); 
				builder.redirectErrorStream(true);
				//run process to preview the video
				try{
					Process process = builder.start();
					//InputStream stdout = process.getInputStream();
					//BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
				}catch(Exception e){
				}
			}
		}
	}
	

}
