package vamix.util;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class contains methods for getting simple file names and file directories (excluding file name)
 * @author Nikhil Prasad
 */
public class FileNameOperations {

	/**
	 * This method splits a full file path and returns the simple file name
	 * @param
	 * input: String - file path
	 * @return String - simple file name
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
	 * This method splits a full file path and returns the location of the file without the simple name
	 * @param
	 * input: String - file whole path
	 * @return String -file path (without file name)
	 */
	public static String pathGetter(String _inputAddr){
		String path="";
		Matcher m=Pattern.compile("(.*"+File.separator+")(\\S+).*$").matcher(_inputAddr);
		if(m.find()){
			path=m.group(1); //get file path
		}
		return path;
	}

}
