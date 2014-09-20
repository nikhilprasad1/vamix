package vamix.view;

import java.awt.Dimension;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JSplitPane;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import javafx.application.Application;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {	
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
			JFrame frame =new JFrame("yfu959 npra508 VAMIX Assignment 3");
			JSplitPane splitPane= new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
			final JFXPanel fxP=new JFXPanel();
            FXMLLoader loader = new FXMLLoader();
            System.out.println("testin4");

            loader.setLocation(Main.class.getResource("VideoView.fxml"));
            AnchorPane TabPane = (AnchorPane) loader.load();
            System.out.println("testin3");

            frame.setContentPane(splitPane);
            System.out.println("testin2");
            EmbeddedMediaPlayerComponent mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
    		final EmbeddedMediaPlayer vid=mediaPlayerComponent.getMediaPlayer();
    		Dimension minimumSize = new Dimension(500, 500);

    		fxP.setMinimumSize(minimumSize);
    		//vid.playMedia(videoFileAdd);
            splitPane.add(fxP,0);
    		splitPane.add(mediaPlayerComponent);

            frame.setSize(1001, 631);
            frame.setResizable(false);
            frame.setVisible(true);
            
            //get version number of javafx
            //System.out.println(com.sun.javafx.runtime.VersionInfo.getRuntimeVersion());
            // Set person overview into the center of root layout.
            Scene scene = new Scene(TabPane);
			fxP.setScene(scene);
	        //initStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
