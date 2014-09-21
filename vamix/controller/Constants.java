package vamix.controller;

import java.io.File;

public class Constants {
	//setup finals
	public static final String LOG_DIR=System.getenv("HOME")+File.separator+".vamix";
	public static final String LOG_PATH=LOG_DIR+File.separator+"log";
	public static final String CURRENT_DIR =System.getProperty("user.dir")+File.separator;
	//public static List<String> FILE_TYPES=Arrays.asList("Video/audio file","avi","mov","mp4","mp3","wav");
	
	//Leave for now
//	cmd = "avconv -ss " + startTitle.getText() + " -i " + videoFileAdd + " -t " + "00:00:10" + " -vcodec libx264 -acodec copy "
//			+ "-vf \"drawtext=fontfile='" + fileSep + "usr" + fileSep + "share" + fileSep + "fonts" + fileSep 
//			+ "truetype" + fileSep + "ubuntu-font-family" + fileSep + "Ubuntu-L.ttf':text='" + titleText.getText()
//			+ "':x=" + titleXPos.getText() + ":y=" + titleYPos.getText() + ":fontsize=16:fontcolor=black\" "
//			+ Constants.LOG_DIR + fileSep + "sample.avi";
}
