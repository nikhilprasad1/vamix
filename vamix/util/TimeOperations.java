package vamix.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

/**
 * This class contains methods for converting time and checking whether or not input time strings are valid
 * @author Nikhil Prasad
 */
public class TimeOperations {

	/**
	 * Method which takes as input the current time of play back and total length of media and 
	 * generates a string representative in the format hh:mm:ss/ hh:mm:ss
	 * @param input: double currentTime (current time of file) in seconds
	 * 				 double totalTime (total time of file) in seconds
	 * @return Time formatted as hh:mm:ss/ hh:mm:ss 
	 */
	public static String timeOfVideo(double currenttime,double totalTime){
		//return the concatenation of time
		return TimeOperations.formatTime((int)currenttime)+"/"+TimeOperations.formatTime((int)totalTime);
	}

	/**
	 * Function which change time given (in seconds) to the hh:mm:ss format
	 * @param input: time in seconds
	 * @return String formatted time as string hh:mm:ss
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
	 * Function which change time in hh:mm:ss format to seconds
	 * @param input:String formatted time as string hh:mm:ss
	 * @return int time in seconds
	 */
	public static int timeInSec(String formattime){
		int time=0;
		//split string then convert
		String[] durationBits = formattime.split(":",-1);
		time=(int)Integer.parseInt(durationBits[0])*60*60+Integer.parseInt(durationBits[1])*60+Integer.parseInt(durationBits[2]);
		return time;
	}

	/**
	 * Method that checks the input time string is in the correct format, hh:mm:ss
	 * @param
	 * input: String time is time to check and String timeType is which time eg start time
	 * @return true if time is in the valid format
	 */
	public static boolean timeFormatChecker(String time, String timeType){
		boolean valid=false;
		if(time.equals("")){
			//error message of empty starttime
			JOptionPane.showMessageDialog(null, "You have not entered a "+timeType);
		}else{
			Matcher m =Pattern.compile("^\\d{2}:\\d{2}:\\d{2}$").matcher(time);
			if (m.find()){
				valid=true;
			}else{
				//error message of empty file name
				JOptionPane.showMessageDialog(null, "You have entered an invalid format for "+timeType +". Please input in the format hh:mm:ss.");
			}
		}
		return valid;
	}

	/**
	 * Method which checks that the start time entered comes before the end time
	 * @param
	 * input: String timeStart is the start time to check 
	 * 		  String start timeEnd String 
	 * 		  timeType is which time eg from file
	 * @return true if times entered are valid
	 */
	public static boolean timeValidChecker(String timeStart,String timeEnd, String timeType){
		boolean valid=false;
		if (timeStart.equals(timeEnd)){
			JOptionPane.showMessageDialog(null, "You have entered the same start time and end time for "+timeType+". Please enter a different start or end time.");
		}else if (timeInSec(timeStart)>=timeInSec(timeEnd)){
			JOptionPane.showMessageDialog(null, "You have entered a greater start time than end time for "+timeType+". Please enter a different start or end time.");
		}else{
			valid=true;//if didnt fail checks set to valid
		}
		return valid;
	}

}
