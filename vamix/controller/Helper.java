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

import uk.co.caprica.vlcj.player.MediaPlayer;

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

}
