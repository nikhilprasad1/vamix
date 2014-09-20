package vamix.view;


import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {	
	private Stage initStage;
	@Override
	public void start(Stage initStage) {
		try{
			this.initStage=initStage;
            this.initStage.setTitle("yfu959 npra508 VAMIX Assignment 3"); //set name as vamix
            setupGUI(); //setup the gui for javafx
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
            //System.out.println(com.sun.javafx.runtime.VersionInfo.getRuntimeVersion());
            // Set person overview into the center of root layout.
            Scene scene = new Scene(TabPane);
			initStage.setScene(scene);
	        initStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}
	
}
