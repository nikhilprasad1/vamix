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
import java.util.Date;
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


	/*
	 * Function to write to a file
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

	/*
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

	/*
	 * Function to check if a file exist given its directory
	 */
	public static boolean fileExist(String pathname) {
		File f = new File(pathname);
		return f.exists();
	}

	/*
	 * Function to return timestamp in the correct format
	 */
	public static String timeStamper(){
		Date date =new Date();
		SimpleDateFormat temp =new SimpleDateFormat("dd-MMMM-yyyy HH:mm");
		return temp.format(date);
	}

	/*
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
	
	/*
	 * Function which change time in milsec to the xx:xx:xx/xx:xx:xx format
	 * @param input: double currentTime (current time of file) in sec
	 * 				 double totalTime (total time of file) in sec
	 * 		  output: String formatted time as string xx:xx:xx/xx:xx:xx 
	 */
	public static String timeOfVideo(double currenttime,double totalTime){
		//return the concatenation of time
		return formatTime((int)currenttime)+"/"+formatTime((int)totalTime);
	}
	
	/*
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
	
	/*
	 * Function to choose where to save file only allow mp3
	 * @param input: none
	 * 		  output: String full path name of file
	 */
	public static String saveFileChooser(){
		String tempName = null;
		//setup chooser
		JFileChooser chooser = new JFileChooser(Constants.CURRENT_DIR);
	    FileNameExtensionFilter filter = new FileNameExtensionFilter("Mp3 File","mp3");
	    chooser.setFileFilter(filter); //set mp3 filter
	    //get save path
	    int choice =chooser.showSaveDialog(null);
		if (!(choice==JFileChooser.CANCEL_OPTION)){
			File file =chooser.getSelectedFile();
			tempName=file.getAbsolutePath();//get path address
			//check if it is mp3 if not add
			if (!tempName.substring(tempName.length()-4, tempName.length()).equals(".mp3")){
				tempName=tempName+".mp3"; //add the .png to end of file
			}
		}

		return tempName;
	}
	
	/*
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

	/*
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
	
}
