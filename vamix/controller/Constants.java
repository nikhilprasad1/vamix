package vamix.controller;

import java.io.File;

/**
 * Repository class that contains all constants required for VAMIX operations
 * This includes current user directory, hidden temporary files directory, etc.
 * Also includes constants for video playback control speeds i.e. rewind speed, etc.
 * @author Nikhil Prasad and Guyver (Yeu-Shin) Fu
 */
public class Constants {
	
	public static final String LOG_DIR=System.getenv("HOME")+File.separator+".vamix";
	public static final String LOG_PATH=LOG_DIR+File.separator+"log";
	public static final String CURRENT_DIR =System.getProperty("user.dir")+File.separator;
	public static final int SKIP_RATE=1000;
	public static final int REPLACE_PROCESS_NUMBER=5;
	public static final int OVERLAY_PROCESS_NUMBER=7;
	public static final long SKIP_RATE_THREAD_SLEEP=20;
	public static final String VIDEO_AUDIO_TYPE="(video)|Media|Audio|MPEG|ISO Media";
	
}
