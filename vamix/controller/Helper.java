package vamix.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/*
 * Class to provide function which help check file existence
 */
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
	
	public static String timeOfVideo(double currenttime,double totalTime){
		
		return formatTime((int)currenttime)+"/"+formatTime((int)totalTime);
	}
	
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
	
}
