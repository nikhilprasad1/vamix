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

public class Main extends Application {	
	//private Stage initStage;
	private JFrame frame;
	private EmbeddedMediaPlayerComponent mediaPlayerComponent;
	public static EmbeddedMediaPlayer vid;

	@Override
	public void start(Stage initStage) {
		try{
			setupGUI(); //setup the gui for javafx
		} catch(Exception ex){
			//error
		}
	}

	public void setupGUI(){
		try {
			mediaPlayerComponent=new EmbeddedMediaPlayerComponent();
			vid=mediaPlayerComponent.getMediaPlayer();
			frame =new JFrame("npra508 VAMIX Project 1.1b");

			final JFXPanel fxP=new JFXPanel();
			FXMLLoader loader = new FXMLLoader();
			
			loader.setLocation(Main.class.getResource("VideoView.fxml"));
			AnchorPane TabPane = (AnchorPane) loader.load();

			fxP.setBounds(0, 0, 1050, 675);
			frame.getLayeredPane().add(fxP, JLayeredPane.DEFAULT_LAYER);
			Scene scene = new Scene(TabPane);
			fxP.setScene(scene);

			mediaPlayerComponent.setBounds(400, 30, 647, 545);
			frame.getLayeredPane().add(mediaPlayerComponent, JLayeredPane.PALETTE_LAYER);
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
			frame.setSize(1051, 690);
			frame.setResizable(false);
			frame.setVisible(true);
		} catch (IOException e) {
			//e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

}
