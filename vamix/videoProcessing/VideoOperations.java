package vamix.videoProcessing;

import java.util.Arrays;
import java.util.List;

import javax.swing.JOptionPane;

import vamix.util.FileChecker;
import vamix.util.TimeOperations;

/**
 * This class contains methods for preparing media, checking time of media and
 * getting the frame number at a certain time.
 * @author Nikhil Prasad
 **/
public class VideoOperations {
	
	/**
	 * Function that checks if the time given is within the length of the video
	 * @param
	 * input: time - time given in format hh:mm:ss
	 * @return true if the time entered is valid
	 */
	public static boolean timeLessThanVideo(String time) {
		boolean valid = true;
		int videoLength = (int)(vamix.userInterface.Main.vid.getLength()/1000);
		int timeGiven = TimeOperations.timeInSec(time);
		if (timeGiven > videoLength) {
			valid = false;
			JOptionPane.showMessageDialog(null, "You have entered a time greater than the length of the video. Please enter a valid time");
		}
		return valid;
	}
	
	/**
	 * This method is for loading and/or previewing media
	 * @param
	 * input: String outputName: the output file full path
	 * 		  String startTime: the start time for transform
	 * 		  String endTime: the end time for transform
	 * @return void
	 */
	@SuppressWarnings("unused")
	public static void loadAndPreview(String outputName,String startTime,String endTime){
		//initialize boolean variables
		boolean load=false;
		boolean preview=false;
		if (FileChecker.validVideoFile(outputName, "(audio)|MPEG|video")){
			String[] cmdsArray=("avplay -i "+outputName).split(" ");
			if (startTime!=null&&endTime!=null){ //only do this when start and endtime not null
				int previewTimeS=TimeOperations.timeInSec(startTime);
				if (previewTimeS>5){ //set preview start time to 5 sec before if its longer than 5
					previewTimeS=previewTimeS-5;
				}
				int previewTimeE=TimeOperations.timeInSec(endTime);
				if (previewTimeE>5){ //set preview end time to 5 sec before if its longer than 5
					previewTimeE=previewTimeE-5;
				}
				cmdsArray=("avplay -i "+outputName+" -ss "+TimeOperations.formatTime(previewTimeS)+" -t "+TimeOperations.formatTime(previewTimeE)).split(" ");
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
				vamix.userInterface.Main.vid.prepareMedia(outputName);
				try {
					Thread.sleep((long) 50.0);
				} catch (InterruptedException e) {
				}
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
	
	/**
	 * This method takes as input a time in the format hh:mm:ss and converts it to a specific frame number given an input video
	 * @param String time - time in format hh:mm:ss
	 * @return int - frame number at specified time
	 */
	public static int getFrameNumber(String time) { 
		int frameNumber = 0;
		//get the current input video's fps
		int frameRate = (int)vamix.userInterface.Main.vid.getFps();
		//split input string into hrs, mins and secs
		String[] splitTime = time.split(":");
		//convert and add to get total seconds
		int seconds = Integer.parseInt(splitTime[0])*3600 + Integer.parseInt(splitTime[1])*60 + Integer.parseInt(splitTime[2]);
		//multiply by fps to get frame number at time specified
		frameNumber = seconds * frameRate;
		return frameNumber;
	}
}
