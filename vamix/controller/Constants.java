package vamix.controller;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class Constants {
	//setup finals
	public static final String LOG_DIR=System.getenv("HOME")+File.separator+".vamix";
	public static final String LOG_PATH=LOG_DIR+File.separator+"log";
	public static final String CURRENT_DIR =System.getProperty("user.dir")+File.separator;
	//public static List<String> FILE_TYPES=Arrays.asList("Video/audio file","avi","mov","mp4","mp3","wav");
}
