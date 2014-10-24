package vamix.view;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import vamix.controller.Constants;
import vamix.controller.Helper;
import javafx.application.Application;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * This is the main launcher class for the VAMIX application.
 * It initializes the GUI, loads the integrated VLCJ media player and then launches the application.
 * @author Nikhil Prasad and Guyver (Yeu-Shin) Fu
 *
 */
public class Main extends Application {	
	
	//the main frame object
	private JFrame frame;
	//integrated singleton media player
	private EmbeddedMediaPlayerComponent mediaPlayerComponent;
	public static EmbeddedMediaPlayer vid;
	
	/**
	 * Method to start a JavaFX application
	 */
	@Override
	public void start(Stage initStage) {
		try{
			setupGUI(); //setup the gui for javafx
		} catch(Exception ex){
			//error
		}
	}

	/**
	 * Sets up the VAMIX GUI by loading the VideoView.fxml file
	 * Initiates and places the VLCJ media player on the GUI afterwards
	 */
	public void setupGUI(){
		
		try {
			//create the singleton vlcj media player object
			mediaPlayerComponent=new EmbeddedMediaPlayerComponent();
			vid=mediaPlayerComponent.getMediaPlayer();
			//create the main JFrame object
			frame =new JFrame("npra508 VAMIX Project Final");
			
			//load the fxml GUI file
			final JFXPanel fxP=new JFXPanel();
			FXMLLoader loader = new FXMLLoader();
			
			loader.setLocation(Main.class.getResource("VideoView.fxml"));
			AnchorPane TabPane = (AnchorPane) loader.load();

			fxP.setBounds(0, 0, 1208, 774);
			frame.getLayeredPane().add(fxP, JLayeredPane.DEFAULT_LAYER);
			Scene scene = new Scene(TabPane);
			fxP.setScene(scene);
			
			//place the media player on the JFrame object
			mediaPlayerComponent.setBounds(412, 29, 795, 650);
			frame.getLayeredPane().add(mediaPlayerComponent, JLayeredPane.PALETTE_LAYER);
			
			//When VAMIX is closed then delete any temporary files used for avconv processing
			//though make sure not to delete the saved state file.
			frame.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e){
					//remove mediaplayer when close}
					mediaPlayerComponent.release();
					//delete to be override file
					if(Helper.fileExist(Constants.LOG_DIR)){
						File file=new File(Constants.LOG_DIR);
						//get all files in it
						String[] fileList=file.list();
						if (fileList!=null){//delete file in dir first
							for (String toDeletefile:fileList){
								File temp=new File(file.getPath(),toDeletefile);
								//check that the file to be deleted is not the saved state file
								if (!(temp.getName().contains("state"))) {
									temp.delete();
								}
							}
						}
						file.delete();
					}
				}
			});
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setSize(1208, 789);
			frame.setResizable(false);
			frame.setVisible(true);
		} catch (IOException e) {
			//e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		//launch VAMIX
		launch(args);
	}

}
