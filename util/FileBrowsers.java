package util;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import vamix.controller.Constants;

/**
 * Utility class that contains methods for choosing files for input and for choosing file destinations for saving
 * @author Nikhil Prasad
 *
 */
public class FileBrowsers {

	/**
	 * Method that opens a file browser for choosing a subtitles file
	 * @param input: none
	 * 		  output: String full path name of chosen subtitle file
	 */
	public static String subtitleFileBrowser(){
		String tempName = "";
		
		//setup chooser
		JFileChooser chooser = new JFileChooser(Constants.CURRENT_DIR);
	    FileNameExtensionFilter filter = new FileNameExtensionFilter("Subtitle files","srt");
	    //set .srt file filter
	    chooser.setFileFilter(filter);
	    
	    //get save path
	    int choice =chooser.showOpenDialog(null);
		if (!(choice==JFileChooser.CANCEL_OPTION)){
			File file =chooser.getSelectedFile();
			tempName=file.getAbsolutePath();//get path address
		}

		return tempName;
	}
	
	/**
	 * Method to choose where to save a file, only allows the specified file type
	 * @param input:  String file_type_message: File filter name to show on dialog
	 * 				  String filterType: file extension (e.g. .mp3)
	 * 		  output: String full path name of file
	 */
	public static String saveFileBrowser(String file_type_message, String filterType){
		String tempName = "";
		
		//setup chooser
		JFileChooser chooser = new JFileChooser(Constants.CURRENT_DIR);
	    FileNameExtensionFilter filter = new FileNameExtensionFilter(file_type_message,filterType);
	    //set specified filter
	    chooser.setFileFilter(filter);
	    
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
}
