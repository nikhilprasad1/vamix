package vamix.controller;

import java.io.File;

public class Constants {
	//setup finals
	public static final String LOG_DIR=System.getenv("HOME")+File.separator+".vamix";
	public static final String LOG_PATH=LOG_DIR+File.separator+"log";
	public static final String CURRENT_DIR =System.getProperty("user.dir")+File.separator;
	public static final int SKIP_RATE=1000;
	public static final long SKIP_RATE_THREAD_SLEEP=20;
	//public static List<String> FILE_TYPES=Arrays.asList("Video/audio file","avi","mov","mp4","mp3","wav");
	
	//Leave for now
//	cmd = "avconv -i " + Constants.LOG_DIR + fileSep + "sample.jpg"
//			+ " -vf \"drawtext=fontfile='" + fileSep + "usr" + fileSep + "share" + fileSep + "fonts" + fileSep 
//			+ "truetype" + fileSep + "ubuntu-font-family" + fileSep + "Ubuntu-L.ttf':text='" + titleText.getText()
//			+ "':x=" + titleXPos.getText() + ":y=" + titleYPos.getText() + ":fontsize=16:fontcolor=black\" "
//			+ Constants.LOG_DIR + fileSep + "sample.jpg";
	
//	String cmd = "avconv -i " + videoFileAdd + " -ss " + startTitle.getText() + " -vsync 1 -t 0.01 "
//			+ Constants.LOG_DIR + fileSep + "sample.jpg";
	
	//now display this image to user as a preview in a pop-up window
//	BufferedImage image = ImageIO.read(new File(Constants.LOG_DIR + fileSep + "sample.jpg"));
//	JLabel picLabel = new JLabel(new ImageIcon(image));
//	JOptionPane.showMessageDialog(null, picLabel, "Preview of Text", JOptionPane.PLAIN_MESSAGE, null);
}
