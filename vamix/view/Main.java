package vamix.view;

import java.io.IOException;

import javax.swing.JComponent;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application {
	
	private Stage initStage;

	@Override
	public void start(Stage initStage) {
		try{
			this.initStage=initStage;
            this.initStage.setTitle("VAMIX");//set name as vamix
            setupGUI(); //setup the gui
		} catch(Exception ex){
			//error
		}
	}
	
	public void setupGUI(){
		try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("VideoView.fxml"));
            AnchorPane TabPane = (AnchorPane) loader.load();
            //get version number of javafx
            //System.out.print(com.sun.javafx.runtime.VersionInfo.getRuntimeVersion());
            // Set person overview into the center of root layout.
            Scene scene = new Scene(TabPane);
			initStage.setScene(scene);
	        initStage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		launch(args);
	}
}
