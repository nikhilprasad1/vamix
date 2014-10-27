package vamix.util;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class contains methods for generating file names that will NOT be duplicates
 * as well as creating the vamix hidden directory
 * @author Nikhil Prasad
 */
public class FileGeneratorOperations {

	/**
	 * This method generates a file name while making sure that it is not the 
	 * same name as a file that already exists. It takes as input a string that is
	 * the partial name of the file.
	 * @param
	 * input: String inputAddr - file whole path
	 * 		  String extraName - special partial name of file
	 * @return String - new file name whole path with new non repeated file name
	 */
	public static String fileNameGen(String inputAddr,String extraName){
		String path="";//
		String fileType="";//
		String nameGen=""; //
		int i=1; //addition to the name
		boolean run =true;//for while loop logic set to true automatically
		//split to path with fileName and file type
		Matcher m=Pattern.compile("(.*)(\\p{Punct}.*)$").matcher(inputAddr);
		if(m.find()){
			path=m.group(1); //get file path with name
			fileType=m.group(2); //get type
		}
		while(run){
			nameGen=path+"_"+extraName+"_"+i+fileType;
			run=FileChecker.fileExist(nameGen);
			i=i+1;//increse value by 1
		}
		return nameGen;
	}

	/**
	 * This method creates the vamix hidden folder if it does not already exist on the system.
	 * The hidden folder is required to hold the saved state file as well as temporary
	 * files created during editing processes.
	 */
	public static void genTempFolder(){
		if (!(FileChecker.fileExist(Constants.HIDDEN_DIR))){
			//when directory doesnt exist create directory
			File dir =new File(Constants.HIDDEN_DIR);
			dir.mkdir();
		}
	}

}
