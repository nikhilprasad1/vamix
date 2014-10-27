package vamix.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

/**
 * Utility class that contains methods for checking that input files are valid
 * @author Nikhil Prasad
 */
public class FileChecker {

	/**
	 * Method to check whether an input file is valid or not. To be valid it must be either an audio file OR a video file.
	 * @param input: String file name
	 * @return true if file is valid
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
			JOptionPane.showMessageDialog(null, "You have entered an empty file name. Please input a valid file name.");
		}else{
			//check if the file exist locally
			if (FileChecker.fileExist(path+inFileName)){
				String bash =File.separator+"bin"+File.separator+"bash";
				String cmd ="echo $(file \""+path+inFileName+"\")";
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
					//e.printStackTrace();
				}
				//check if video audio using bash commands
				if (isVideoAudio){
					isValid=true;
				}else{
					//file is not audio/mpeg type
					JOptionPane.showMessageDialog(null, "You have entered a non-video/audio file, please enter a valid file.");
				}
			}else{
				//file does not exist so give error
				JOptionPane.showMessageDialog(null, "You have entered a non-existing file. Please input a valid file type.");
			}
		}
		return isValid;
	}

	/**
	 * Function that determines if the given input file is a valid video file
	 * Only meant to accept .mp4, .avi and .flv
	 * @param filePath : full path of the file to be checked
	 * 		  pattern  : the pattern to check against the file name
	 * @return true if file is valid
	 */
	public static boolean validVideoFile(String filePath,String pattern){
		String inFileName="";
		String path="";
		boolean isValid=false;
		boolean isVideo=false;
		//now get the path of file and just file name
		Matcher m=Pattern.compile("(.*"+File.separator+")(.*)$").matcher(filePath);
		if(m.find()){
			path = m.group(1); //get path
			inFileName=m.group(2); //get file name
		}
		
		if(filePath.equals("")){
			//error message of empty file name
			JOptionPane.showMessageDialog(null, "You have entered an empty file name. Please input a valid file name.");
		}else{
			//check if the file exist locally
			if (FileChecker.fileExist(path+inFileName)){
				String bash =File.separator+"bin"+File.separator+"bash";
				String cmd ="echo $(file \""+path+inFileName+"\")";
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
					//e.printStackTrace();
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
	 * Function to check if a file exist given its directory
	 * @param pathname : full path of file
	 * @return true if file exists
	 */
	public static boolean fileExist(String pathname) {
		File f = new File(pathname);
		return f.exists();
	}

	
}
