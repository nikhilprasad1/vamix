package vamix.controller;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

/**
 * Class that takes care of merging the opening scene and closing scene with the video being edited
 * Does it in the background so to not freeze the GUI
 **/
public class TextEdit {
	
	private String _titleText, _titleFont, _titleSize, _titleColor, _startTitle, _endTitle, _titleXPos, _titleYPos;
	private String _creditsText, _creditsFont, _creditsSize, _creditsColor, _startCredits, _endCredits, _creditsXPos, _creditsYPos;
	private String _titleOrCredits, _inputAddr, _outputAddr;
	
	private String fileSep = File.separator;
	
	//renderType tells RenderWorker if both or only one (of opening/closing scenes) need to be rendered
	public enum RenderType {OPENING, CLOSING, BOTH};
	private RenderType _renderType = null; 
	
	//int to display to user which tells them which process of rendering Vamix is currently at
	private int processNumber = 1, totalProcesses;
	
	//workers which are executed depending on if a preview is required or full text-on-video render
	private PreviewWorker previewWorker = new PreviewWorker();
	private RenderWorker renderWorker = new RenderWorker();
	
	public TextEdit(String titleText, String titleFont, String titleSize, String titleColor, String startTitle, String endTitle, String titleXPos, String titleYPos,
			String creditsText, String creditsFont, String creditsSize, String creditsColor, String startCredits, String endCredits, String creditsXPos, String creditsYPos,
			String inputAddr, String outputAddr, String titleOrCredits) {
		_titleText = titleText;
		_titleFont = titleFont;
		_titleSize = titleSize;
		_titleColor = titleColor;
		_startTitle = startTitle;
		_endTitle = endTitle;
		_titleXPos = titleXPos;
		_titleYPos = titleYPos;
		_creditsText = creditsText;
		_creditsFont = creditsFont;
		_creditsSize = creditsSize;
		_creditsColor = creditsColor;
		_startCredits = startCredits;
		_endCredits = endCredits;
		_creditsXPos = creditsXPos;
		_creditsYPos = creditsYPos;
		_titleOrCredits = titleOrCredits;
		_inputAddr = inputAddr;
		_outputAddr = outputAddr;
	}
	
	/*
	 * Sub-class of SwingWorker which has the sole purpose of creating a snapshot of the input video
	 * at the time specified by the user (start of scene) and printing on it the text the user has given.
	 * It then displays the preview in a dialog box via the ED thread (called in done())
	 */
	class PreviewWorker extends SwingWorker<Void, Integer> {
		
		JLabel picLabel;
		
		@Override
		protected Void doInBackground() {
			String bash = fileSep + "bin"+ fileSep + "bash";
			String [] cmds = new String[2];
			if (_titleOrCredits.equals("title")) {
				cmds = buildPreviewCommands(_titleText, _titleFont, _titleSize, _titleColor, _startTitle, _titleXPos, _titleYPos);
			} else {
				cmds = buildPreviewCommands(_creditsText, _creditsFont, _creditsSize, _creditsColor, _startCredits, _creditsXPos, _creditsYPos);
			}
			ProcessBuilder builder = new ProcessBuilder(bash,"-c",cmds[0]);
			builder.redirectErrorStream(true);
			try {
				Process process = builder.start();
				InputStream stdout = process.getInputStream();
				BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
				String line;
				while((line = stdoutBuffered.readLine()) != null){
					System.out.println(line);
				}
				builder = new ProcessBuilder(bash, "-c", cmds[1]);
				builder.redirectErrorStream(true);
				process = builder.start();
				stdout = process.getInputStream();
				stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
				while((line=stdoutBuffered.readLine())!=null){
					System.out.println(line);
				}
				
				BufferedImage image = ImageIO.read(new File(Constants.LOG_DIR + fileSep + _titleOrCredits + ".jpg"));
				picLabel = new JLabel(new ImageIcon(image));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void done() {
			JOptionPane.showMessageDialog(null, picLabel, "Preview of Text", JOptionPane.PLAIN_MESSAGE, null);
		}
		
		/*
		 * Method that builds the two bash commands required to create a preview for the user
		 */
		protected String[] buildPreviewCommands(String text, String font, String size, String color, String start, String xPos, String yPos) {
			String [] cmds = new String[2];
			
			String cmd1 = "avconv -i " + _inputAddr + " -ss " + start + " -vsync 1 -t 0.01 "
			+ Constants.LOG_DIR + fileSep + _titleOrCredits + ".jpg";
			
			String cmd2 = "avconv -i " + Constants.LOG_DIR + fileSep + _titleOrCredits + ".jpg"
				+ " -vf \"drawtext=fontfile='" + fileSep + "usr" + fileSep + "share" + fileSep + "fonts" + fileSep 
				+ "truetype" + fileSep + "freefont" + fileSep + font + ".ttf':text='" + text
				+ "':x=" + xPos + ":y=" + yPos + ":fontsize=" + size +":fontcolor=" + color + "\" "
				+ Constants.LOG_DIR + fileSep + _titleOrCredits + ".jpg";
			
			cmds[0] = cmd1;
			cmds[1] = cmd2;
			
			return cmds;
		}
	}
	
	/*
	 * Method that executes the PreviewWorker which in turn forms and carries out the bash commands
	 * required to display a preview of the scene the user has created.
	 */
	public void showScenePreviewAsync() {previewWorker.execute();}
	
	/*
	 * Sub-class of SwingWorker which creates the rendered output video with the text printed on the
	 * opening scene and/or closing scene 
	 */
	class RenderWorker extends SwingWorker<Void, Integer> {
		
		@Override
		protected Void doInBackground() {
			String bash = fileSep + "bin"+ fileSep + "bash";
			List<String> cmds = buildRenderCommandList();
			for (String cmd : cmds) {
				System.out.println(cmd);
				ProcessBuilder builder = new ProcessBuilder("/bin/bash","-c",cmd);
				builder.redirectErrorStream(true);
				try {
					Process process = builder.start();
					InputStream stdout = process.getInputStream();
					BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
					String line;
					while((line = stdoutBuffered.readLine()) != null){
						System.out.println(line);
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
			return null;
		}
		
		protected List<String> buildRenderCommandList() {
			List<String> cmds = new ArrayList<String>();
			//first get the filename of the input file (not the full address)
			String inFileName = "";
			//now get length of video being edited
			int totalLength = (int)(vamix.view.Main.vid.getLength()/1000);	
			Matcher m=Pattern.compile("(.*"+File.separator+")(\\S+).*$").matcher(_inputAddr);
			if(m.find()){
				inFileName=m.group(2); //get file name
			}
			//if user only wants to add a title OR a credits scene
			if (_renderType == RenderType.OPENING) {
				int endLength = Helper.timeInSec(_endTitle);
				//get duration left (if any) after finish time specified by user
				String timeAtEnd = Helper.formatTime(totalLength - endLength);
				//create the bash command strings that will split the input video into 3 parts depending on start time and
				//finish time specified by user
				String cmd = "avconv -i " + _inputAddr + " -ss 00:00:00 -t " + _startTitle + " -vcodec libx264 -acodec aac "
						+ "-bsf:v h264_mp4toannexb -f mpegts -strict experimental -y " +_inputAddr + "1.ts";
				cmds.add(cmd);
				int duration = Helper.timeInSec(_endTitle)-Helper.timeInSec(_startTitle);
				cmd = "avconv -i " + _inputAddr +" -ss " + _startTitle +" -t " + Helper.formatTime(duration) + " -vcodec libx264 -acodec aac "
						+ "-bsf:v h264_mp4toannexb -f mpegts -strict experimental -y " +_inputAddr + "2.ts";
				cmds.add(cmd);
				cmd = "avconv -i " + _inputAddr +" -ss " + _endTitle +" -t " + timeAtEnd + " -vcodec libx264 -acodec aac "
						+ "-bsf:v h264_mp4toannexb -f mpegts -strict experimental -y " +_inputAddr + "3.ts";
				cmds.add(cmd);
				//now create the bash command that draws the text on the bit of video specified by the user
				cmd = "avconv -i " + _inputAddr + "2.ts"  + " -vcodec libx264 -acodec aac -vf \"drawtext=fontfile='" + fileSep + "usr" + fileSep + "share" + fileSep + "fonts" + fileSep 
						+ "truetype" + fileSep + "freefont" + fileSep + _titleFont + ".ttf':text='" + _titleText
						+ "':x=" + _titleXPos + ":y=" + _titleYPos + ":fontsize=" + _titleSize +":fontcolor=" + _titleColor + "\" -strict experimental -y "
						+_inputAddr + "4.ts";
				//cmd="avconv -i /afs/ec.auckland.ac.nz/users/y/f/yfu959/unixhome/Desktop/206a3/a.mp42.ts -vcodec libx264 -acodec aac -vf \"drawtext=fontfile='/usr/share/fonts/truetype/freefont/FreeSans.ttf':text='hello':x=100:y=100:fontsize=24:fontcolor=white\" -strict experimental -y /afs/ec.auckland.ac.nz/users/y/f/yfu959/unixhome/Desktop/206a3/a.mp44.ts";
				cmds.add(cmd);
				//now create the bash command that will combine all the split videos together
				cmd = "avconv -i concat:\"" + _inputAddr + "1.ts|" + _inputAddr + "4.ts|" + _inputAddr + "3.ts\"" + " -c copy -bsf:a aac_adtstoasc -y " + _outputAddr;
				//cmd = "avconv -i video.mp42.ts -vcodec libx264 -acodec acc -strict experimental test.mp4";
				cmds.add(cmd);
			} else if (_renderType == RenderType.CLOSING) {
				int endLength = Helper.timeInSec(_endCredits);
				//get duration left (if any) after finish time specified by user
				String timeAtEnd = Helper.formatTime(totalLength - endLength);
				//create the bash command strings that will split the input video into 3 parts depending on start time and
				//finish time specified by user
				String cmd = "avconv -i " + _inputAddr + " -ss 00:00:00 -t " + _startCredits + " -vcodec libx264 -acodec aac "
						+ "-bsf:v h264_mp4toannexb -f mpegts -strict experimental -y " +_inputAddr + "1.ts";
				cmds.add(cmd);
				int duration = Helper.timeInSec(_endCredits)-Helper.timeInSec(_startCredits);
				cmd = "avconv -i " + _inputAddr +" -ss " + _startCredits +" -t " + Helper.formatTime(duration) + " -vcodec libx264 -acodec aac "
						+ "-bsf:v h264_mp4toannexb -f mpegts -strict experimental -y " +_inputAddr + "2.ts";
				cmds.add(cmd);
				cmd = "avconv -i " + _inputAddr +" -ss " + _endCredits +" -t " + timeAtEnd + " -vcodec libx264 -acodec aac "
						+ "-bsf:v h264_mp4toannexb -f mpegts -strict experimental -y " +_inputAddr + "3.ts";
				cmds.add(cmd);
				//now create the bash command that draws the text on the bit of video specified by the user
				cmd = "avconv -i " + _inputAddr + "2.ts"  + " -vcodec libx264 -acodec aac -vf \"drawtext=fontfile='" + fileSep + "usr" + fileSep + "share" + fileSep + "fonts" + fileSep 
						+ "truetype" + fileSep + "freefont" + fileSep + _creditsFont + ".ttf':text='" + _creditsText
						+ "':x=" + _creditsXPos + ":y=" + _creditsYPos + ":fontsize=" + _creditsSize +":fontcolor=" + _creditsColor + "\" -strict experimental -y "
						+_inputAddr + "4.ts";
				//cmd="avconv -i /afs/ec.auckland.ac.nz/users/y/f/yfu959/unixhome/Desktop/206a3/a.mp42.ts -vcodec libx264 -acodec aac -vf \"drawtext=fontfile='/usr/share/fonts/truetype/freefont/FreeSans.ttf':text='hello':x=100:y=100:fontsize=24:fontcolor=white\" -strict experimental -y /afs/ec.auckland.ac.nz/users/y/f/yfu959/unixhome/Desktop/206a3/a.mp44.ts";
				cmds.add(cmd);
				//now create the bash command that will combine all the split videos together
				cmd = "avconv -i concat:\"" + _inputAddr + "1.ts|" + _inputAddr + "4.ts|" + _inputAddr + "3.ts\"" + " -c copy -bsf:a aac_adtstoasc -y " + _outputAddr;
				//cmd = "avconv -i video.mp42.ts -vcodec libx264 -acodec acc -strict experimental test.mp4";
				cmds.add(cmd);
			//otherwise if the user wants both title and credits scenes
			} else {
				int endLength = Helper.timeInSec(_endCredits);
				//get duration left (if any) after finish time specified by user
				String timeAtEnd = Helper.formatTime(totalLength - endLength);
				//create the bash command strings that will split the input video into 5 parts depending on start time and
				//finish time specified by user
				String cmd = "avconv -i " + _inputAddr + " -ss 00:00:00 -t " + _startTitle + " -vcodec libx264 -acodec aac "
						+ "-bsf:v h264_mp4toannexb -f mpegts -strict experimental -y " +_inputAddr + "1.ts";
				cmds.add(cmd);
				int duration = Helper.timeInSec(_endTitle)-Helper.timeInSec(_startTitle);
				cmd = "avconv -i " + _inputAddr +" -ss " + _startTitle +" -t " + Helper.formatTime(duration) + " -vcodec libx264 -acodec aac "
						+ "-bsf:v h264_mp4toannexb -f mpegts -strict experimental -y " +_inputAddr + "2.ts";
				cmds.add(cmd);
				duration = Helper.timeInSec(_startCredits) - Helper.timeInSec(_endTitle);
				cmd = "avconv -i " + _inputAddr +" -ss " + _endTitle +" -t " + Helper.formatTime(duration) + " -vcodec libx264 -acodec aac "
						+ "-bsf:v h264_mp4toannexb -f mpegts -strict experimental -y " +_inputAddr + "3.ts";
				cmds.add(cmd);
				duration = Helper.timeInSec(_endCredits) - Helper.timeInSec(_startCredits);
				cmd = "avconv -i " + _inputAddr +" -ss " + _startCredits +" -t " + Helper.formatTime(duration) + " -vcodec libx264 -acodec aac "
						+ "-bsf:v h264_mp4toannexb -f mpegts -strict experimental -y " +_inputAddr + "4.ts";
				cmds.add(cmd);
				cmd = "avconv -i " + _inputAddr +" -ss " + _endCredits +" -t " + timeAtEnd + " -vcodec libx264 -acodec aac "
						+ "-bsf:v h264_mp4toannexb -f mpegts -strict experimental -y " +_inputAddr + "5.ts";
				cmds.add(cmd);
				//now create the bash command that draws the text on the title section of video specified by the user
				cmd = "avconv -i " + _inputAddr + "2.ts"  + " -vcodec libx264 -acodec aac -vf \"drawtext=fontfile='" + fileSep + "usr" + fileSep + "share" + fileSep + "fonts" + fileSep 
						+ "truetype" + fileSep + "freefont" + fileSep + _titleFont + ".ttf':text='" + _titleText
						+ "':x=" + _titleXPos + ":y=" + _titleYPos + ":fontsize=" + _titleSize +":fontcolor=" + _titleColor + "\" -strict experimental -y "
						+_inputAddr + "6.ts";
				cmds.add(cmd);
				//now create the bash command that draws the text on the credits section of video specified by the user
				cmd = "avconv -i " + _inputAddr + "4.ts"  + " -vcodec libx264 -acodec aac -vf \"drawtext=fontfile='" + fileSep + "usr" + fileSep + "share" + fileSep + "fonts" + fileSep 
						+ "truetype" + fileSep + "freefont" + fileSep + _creditsFont + ".ttf':text='" + _creditsText
						+ "':x=" + _creditsXPos + ":y=" + _creditsYPos + ":fontsize=" + _creditsSize +":fontcolor=" + _creditsColor + "\" -strict experimental -y "
						+_inputAddr + "7.ts";
				//cmd="avconv -i /afs/ec.auckland.ac.nz/users/y/f/yfu959/unixhome/Desktop/206a3/a.mp42.ts -vcodec libx264 -acodec aac -vf \"drawtext=fontfile='/usr/share/fonts/truetype/freefont/FreeSans.ttf':text='hello':x=100:y=100:fontsize=24:fontcolor=white\" -strict experimental -y /afs/ec.auckland.ac.nz/users/y/f/yfu959/unixhome/Desktop/206a3/a.mp44.ts";
				cmds.add(cmd);
				//now create the bash command that will combine all the split videos together
				cmd = "avconv -i concat:\"" + _inputAddr + "1.ts|" + _inputAddr + "6.ts|" + _inputAddr + "3.ts|" + _inputAddr + "7.ts|" + _inputAddr + "5.ts\"" 
						+ " -c copy -bsf:a aac_adtstoasc -y " + _outputAddr;
				//cmd = "avconv -i video.mp42.ts -vcodec libx264 -acodec acc -strict experimental test.mp4";
				cmds.add(cmd);
			}
			return cmds;
		}
	}
	
	public void renderWithTextAsync(RenderType renderType) {
		_renderType = renderType;
		renderWorker.execute();
	}
}
