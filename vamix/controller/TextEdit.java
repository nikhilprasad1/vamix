package vamix.controller;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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
	
	private String _titleText, _titleFont, _titleSize, _titleColor, _startTitle, _titleXPos, _titleYPos;
	private String _durationTitle, _durationCredits;
	private String _creditsText, _creditsFont, _creditsSize, _creditsColor, _startCredits,_creditsXPos, _creditsYPos;
	private String _titleOrCredits, _inputAddr, _outputAddr;
	
	private String fileSep = File.separator;
	
	//renderType tells RenderWorker if both or only one (of opening/closing scenes) need to be rendered
	public enum RenderType {OPENING, CLOSING, BOTH};
	private RenderType _renderType = null; 
	
	//workers which are executed depending on if a preview is required or full text-on-video render
	private PreviewWorker previewWorker = new PreviewWorker();
	private RenderWorker renderWorker = new RenderWorker();
	
	public TextEdit(String titleText, String titleFont, String titleSize, String titleColor, String startTitle, String titleXPos, String titleYPos,
			String creditsText, String creditsFont, String creditsSize, String creditsColor, String startCredits, String creditsXPos, String creditsYPos,
			String inputAddr, String outputAddr, String titleOrCredits, String durationTitle, String durationCredits) {
		_titleText = titleText;
		_titleFont = titleFont;
		_titleSize = titleSize;
		_titleColor = titleColor;
		_startTitle = startTitle;
		_titleXPos = titleXPos;
		_titleYPos = titleYPos;
		_creditsText = creditsText;
		_creditsFont = creditsFont;
		_creditsSize = creditsSize;
		_creditsColor = creditsColor;
		_startCredits = startCredits;
		_creditsXPos = creditsXPos;
		_creditsYPos = creditsYPos;
		_titleOrCredits = titleOrCredits;
		_inputAddr = inputAddr;
		_outputAddr = outputAddr;
		_durationTitle = durationTitle;
		_durationCredits = durationCredits;
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
			} catch(Exception e) {
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
			List<String> cmds = new ArrayList<String>();
			for (String cmd : cmds) {
				ProcessBuilder builder = new ProcessBuilder(bash, "-c", cmd);
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
			if (_renderType == RenderType.OPENING) {
				cmds.add(buildRenderCommand(_titleText, _startTitle, _durationTitle, _titleFont, _titleSize, _titleColor, _titleXPos, _titleYPos));
			} else if (_renderType == RenderType.CLOSING) {
				cmds.add(buildRenderCommand(_creditsText, _startCredits, _durationCredits, _creditsFont, _creditsSize, _creditsColor, _creditsXPos, _creditsYPos));
			} else {
				cmds.add(buildRenderCommand(_titleText, _startTitle, _durationTitle, _titleFont, _titleSize, _titleColor, _titleXPos, _titleYPos));
				cmds.add(buildRenderCommand(_creditsText, _startCredits, _durationCredits, _creditsFont, _creditsSize, _creditsColor, _creditsXPos, _creditsYPos));
			}
			return cmds;
		}
		
		protected String buildRenderCommand(String text, String start, String duration, String font, String size, String color, String xPos, String yPos) {
			String cmd = "avconv -i " + _inputAddr + " -ss " + start + " -t " + duration + " -vcodec libx264 -acodec copy"
					+ " -vf \"drawtext=fontfile='" + fileSep + "usr" + fileSep + "share" + fileSep + "fonts" + fileSep 
					+ "truetype" + fileSep + "freefont" + fileSep + font + ".ttf':text='" + text
					+ "':x=" + xPos + ":y=" + yPos + ":fontsize=" + size +":fontcolor=" + color + "\" "
					+ _outputAddr;
			return cmd;
		}
	}
	
	public void renderWithTextAsync(RenderType renderType) {
		_renderType = renderType;
		renderWorker.execute();
	}
}
