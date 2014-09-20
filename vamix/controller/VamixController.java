package vamix.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.MediaView;

public class VamixController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private AnchorPane tabPane;

	@FXML
	private TabPane tabMenu;

	/*
	 * Varaible for the video tab
	 */

	@FXML
	private TextField titleXPos;

	@FXML
	private TextField endTitle;

	@FXML
	private TextField startTitle;

	@FXML
	private TextField creditsXPos;

	@FXML
	private Button browseBtn;

	@FXML
	private TextField startCredits;

	@FXML
	private TextArea titleText;

	@FXML
	private Button downloadBtn;

	@FXML
	private ColorPicker titleColour;

	@FXML
	private TextArea creditText;

	@FXML
	private TextField videoURL;

	@FXML
	private TextField endCredits;

	@FXML
	private Button setCredits;

	@FXML
	private ColorPicker creditsColour;

	@FXML
	private TextField titleYPos;

	@FXML
	private Button setTitle;

	@FXML
	private TextField creditsYPos;

	@FXML
	private TextField videoPath;

	@FXML
	private Tab videoTab;

	@FXML
	private ChoiceBox<?> titleSize;

	@FXML
	private ChoiceBox<?> creditsSize;

	/*
	 * Varaible for the audio tab
	 */

	@FXML
	private Tab audioTab;

	@FXML
	private TextField startReplace;
	
	@FXML
	private TextField endReplace;
	
	@FXML
	private TextField overlayUseEnd;

	@FXML
	private Button chooseAudioButton;

	@FXML
	private TextField strip_add;

	@FXML
	private Button chooseOverlayBtn;

	@FXML
	private TextField overlayToEnd;

	@FXML
	private Button overlayAudioBtn;
	
	@FXML
	private TextField overlayToStart;

	@FXML
	private TextField overlayUseStart;

	@FXML
	private Button strip_button;

	@FXML
	private Button replaceButton;

	@FXML
	private TextField overlayAdd;

	@FXML
	private TextField replaceAdd;

	/*
	 * Varaible for the preview tab
	 */
	@FXML
	private Tab previewTab;

	@FXML
	private Button renderNoAudioBtn;

	@FXML
	private Button renderWithAudioBtn;

	@FXML
	private TextField outputFilePath;

	@FXML
	private Button saveToBtn;

	/*
	 * Varaible for the section for the media player
	 */

	@FXML
	private MediaView videoMediaView;

	@FXML
	private Button fastForwardBtn;

	@FXML
	private CheckBox muteCheckbox;

	@FXML
	private Slider volumeSlider;

	@FXML
	private Button rewindBtn;

	@FXML
	private Button playPauseBtn;

	@FXML
	private Label videoTime;

	@FXML
	private ProgressBar videoProgress;

	//initialising the functionality of the buttons
	@FXML
	void initialize() {
		MainPanesCheck();
		videoTabCheck();
		audioTabCheck();
		previewTabCheck();
		playerCheck();
		videoTab();
		audioTab();
		previewTab();
		player();
	}

	private void videoTabCheck(){
		/*
		 * Section for the video tab id check
		 */
		assert downloadBtn != null : "fx:id=\"downloadBtn\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert browseBtn != null : "fx:id=\"browseBtn\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert setTitle != null : "fx:id=\"setTitle\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert setCredits != null : "fx:id=\"setCredits\" was not injected: check your FXML file 'VideoView.fxml'.";

		assert videoPath != null : "fx:id=\"videoPath\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert videoURL != null : "fx:id=\"videoURL\" was not injected: check your FXML file 'VideoView.fxml'.";

		assert titleText != null : "fx:id=\"titleText\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert titleXPos != null : "fx:id=\"titleXPos\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert endTitle != null : "fx:id=\"endTitle\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert startTitle != null : "fx:id=\"startTitle\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert titleYPos != null : "fx:id=\"titleYPos\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert titleSize != null : "fx:id=\"titleSize\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert titleColour != null : "fx:id=\"titleColour\" was not injected: check your FXML file 'VideoView.fxml'.";

		assert creditText != null : "fx:id=\"creditText\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert creditsXPos != null : "fx:id=\"creditsXPos\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert creditsYPos != null : "fx:id=\"creditsYPos\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert creditsColour != null : "fx:id=\"creditsColour\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert startCredits != null : "fx:id=\"startCredits\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert creditsSize != null : "fx:id=\"creditsSize\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert endCredits != null : "fx:id=\"endCredits\" was not injected: check your FXML file 'VideoView.fxml'.";
	}

	private void videoTab(){
		/*
		 * Section for the video tab functionality
		 */
		//test mouse action event handler method
		downloadBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evt) {
				videoURL.setText("No, you damn pirate.");
			}
		});
		
		//
	}

	private void audioTabCheck(){
		/*
		 * Section for the audio tab id check
		 */
		assert strip_button != null : "fx:id=\"strip_button\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert replaceButton != null : "fx:id=\"replaceButton\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert overlayAudioBtn != null : "fx:id=\"overlayAudioBtn\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert chooseAudioButton != null : "fx:id=\"chooseAudioButton\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert chooseOverlayBtn != null : "fx:id=\"chooseOverlayBtn\" was not injected: check your FXML file 'VideoView.fxml'.";

		assert strip_add != null : "fx:id=\"strip_add\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert overlayAdd != null : "fx:id=\"overlayAdd\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert replaceAdd != null : "fx:id=\"replaceAdd\" was not injected: check your FXML file 'VideoView.fxml'.";
		
		assert endReplace != null : "fx:id=\"endReplace\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert startReplace != null : "fx:id=\"startReplace\" was not injected: check your FXML file 'VideoView.fxml'.";

		assert overlayUseEnd != null : "fx:id=\"overlayUseEnd\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert overlayUseStart != null : "fx:id=\"overlayUseStart\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert overlayToEnd != null : "fx:id=\"overlayToEnd\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert overlayToStart != null : "fx:id=\"overlayToStart\" was not injected: check your FXML file 'VideoView.fxml'.";
	
	}
	
	private void audioTab(){
		/*
		 * Section for the audio tab functionality
		 */

	}
	
	private void previewTab(){
		/*
		 * Section for the audio tab functionality
		 */

	}
	
	private void previewTabCheck(){
		/*
		 * Section for the preview tab id check
		 */
		assert outputFilePath != null : "fx:id=\"outputFilePath\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert saveToBtn != null : "fx:id=\"saveToBtn\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert renderWithAudioBtn != null : "fx:id=\"renderWithAudioBtn\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert renderNoAudioBtn != null : "fx:id=\"renderNoAudioBtn\" was not injected: check your FXML file 'VideoView.fxml'.";

	}

	private void playerCheck(){
		/*
		 * Section for the media player id checks
		 */
		assert playPauseBtn != null : "fx:id=\"playPauseBtn\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert rewindBtn != null : "fx:id=\"rewindBtn\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert fastForwardBtn != null : "fx:id=\"fastForwardBtn\" was not injected: check your FXML file 'VideoView.fxml'.";

		assert videoTime != null : "fx:id=\"videoTime\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert videoProgress != null : "fx:id=\"videoProgress\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert volumeSlider != null : "fx:id=\"volumeSlider\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert videoMediaView != null : "fx:id=\"videoMediaView\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert muteCheckbox != null : "fx:id=\"muteCheckbox\" was not injected: check your FXML file 'VideoView.fxml'.";
		
	}

	private void player(){
		/*
		 * Section for the media player functionality
		 */

	}
	
	private void MainPanesCheck(){
		assert tabMenu != null : "fx:id=\"tabMenu\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert tabPane != null : "fx:id=\"tabPane\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert videoTab != null : "fx:id=\"videoTab\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert previewTab != null : "fx:id=\"previewTab\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert audioTab != null : "fx:id=\"audioTab\" was not injected: check your FXML file 'VideoView.fxml'.";
	}

}
