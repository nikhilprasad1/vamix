package vamix.controller;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

/*
 * Class that takes care of merging the opening scene and closing scene with the video being edited
 * Does it in the background so to not freeze the GUI
 */
public class TextEdit {
	
	private String _titleText, _titleFont, _titleSize, _titleColor, _startTitle, _endTitle, _titleXPos, _titleYPos;
	private String _creditsText, _creditsFont, _creditsSize, _creditsColor, _startCredits, _endCredits, _creditsXPos, _creditsYPos;
	//_type tells TextEdit if a preview is wanted or the actual rendering needs to be done.
	private String _type, _titleOrCredits, _inputAddr, _outputAddr;
	
	private String fileSep = File.separator;
	
	private TextWorker textWorker = new TextWorker();
	
	public TextEdit(String titleText, String titleFont, String titleSize, String titleColor, String startTitle, String endTitle, String titleXPos, String titleYPos,
			String creditsText, String creditsFont, String creditsSize, String creditsColor, String startCredits, String endCredits, String creditsXPos, String creditsYPos,
			String type, String inputAddr, String outputAddr, String titleOrCredits) {
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
		_startCredits = startCredits;
		_endCredits = endCredits;
		_creditsXPos = creditsXPos;
		_creditsYPos = creditsYPos;
		_type = type;
		_titleOrCredits = titleOrCredits;
		_inputAddr = inputAddr;
		_outputAddr = outputAddr;
	}
	
	class TextWorker extends SwingWorker<Void, Integer> {
		
		JLabel picLabel;
		
		@Override
		protected Void doInBackground() {
			//if a preview is only wanted
			if (_type.equals("PREVIEW")) {
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
				} catch(Exception e) {
					e.printStackTrace();
				}
				
			//otherwise if the rendering needs to be done
			} else {
				
			}
			return null;
		}
		
		@Override
		protected void done() {
			//if a preview is only wanted
			if (_type.equals("PREVIEW")) {
				JOptionPane.showMessageDialog(null, picLabel, "Preview of Text", JOptionPane.PLAIN_MESSAGE, null);
			}
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
	 * Method to execute the SwingWorker class within TextEdit
	 */
	public void setText() {textWorker.execute();}
}
