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
    private Label addVideoLabel;

    @FXML
    private TextField startCredits;

    @FXML
    private TextField endReplace;

    @FXML
    private TextArea titleText;

    @FXML
    private Button chooseAudioButton;

    @FXML
    private TextField Strip_add;

    @FXML
    private Button chooseOverlayBtn;

    @FXML
    private MediaView videoMediaView;

    @FXML
    private CheckBox muteCheckbox;

    @FXML
    private TextArea creditText;

    @FXML
    private Tab PreviewTab;

    @FXML
    private TextField overlayToEnd;

    @FXML
    private Button rewindBtn;

    @FXML
    private TextField overlayToStart;

    @FXML
    private TextField titleYPos;

    @FXML
    private Button setTitle;

    @FXML
    private Button fastForwardBtn;

    @FXML
    private Label StripAudioLabel;

    @FXML
    private TextField creditsYPos;

    @FXML
    private TextField overlayUseStart;

    @FXML
    private Button strip_button;

    @FXML
    private TextField videoPath;

    @FXML
    private Label StipeText;

    @FXML
    private Tab videoTab;

    @FXML
    private TextField OverlayAdd;

    @FXML
    private Button renderNoAudioBtn;

    @FXML
    private TextField ReplaceAdd;

    @FXML
    private Button saveToBtn;

    @FXML
    private Button renderWithAudioBtn;

    @FXML
    private ChoiceBox<?> titleSize;

    @FXML
    private TextField startReplace;

    @FXML
    private ChoiceBox<?> creditsSize;

    @FXML
    private Label OverlayAudioLabel;

    @FXML
    private Button overlayAudioBtn;

    @FXML
    private TextField outputFilePath;

    @FXML
    private Button ReplaceButton;

    @FXML
    private Slider volumeSlider;

    @FXML
    private TextField videoURL;

    @FXML
    private Button setCredits;

    @FXML
    private ColorPicker creditsColour;

    @FXML
    private Label ReplaceAudioLabel;

    @FXML
    private TextField endCredits;

    @FXML
    private AnchorPane TabPane;

    @FXML
    private Label addVideoLabel11;

    @FXML
    private Tab audioTab;

    @FXML
    private TabPane tabMenu;

    @FXML
    private TextField overlayUseEnd;

    @FXML
    private Button downloadBtn;

    @FXML
    private ColorPicker titleColour;

    @FXML
    private Button playPauseBtn;
    
    @FXML
    private Label videoTime;
    
    @FXML
    private ProgressBar videoProgress;

    @FXML
    void initialize() {
        assert titleXPos != null : "fx:id=\"titleXPos\" was not injected: check your FXML file 'VideoView.fxml'.";
        assert endTitle != null : "fx:id=\"endTitle\" was not injected: check your FXML file 'VideoView.fxml'.";
        assert startTitle != null : "fx:id=\"startTitle\" was not injected: check your FXML file 'VideoView.fxml'.";
        assert creditsXPos != null : "fx:id=\"creditsXPos\" was not injected: check your FXML file 'VideoView.fxml'.";
        assert browseBtn != null : "fx:id=\"browseBtn\" was not injected: check your FXML file 'VideoView.fxml'.";
        assert addVideoLabel != null : "fx:id=\"addVideoLabel\" was not injected: check your FXML file 'VideoView.fxml'.";
        assert startCredits != null : "fx:id=\"startCredits\" was not injected: check your FXML file 'VideoView.fxml'.";
        assert endReplace != null : "fx:id=\"endReplace\" was not injected: check your FXML file 'VideoView.fxml'.";
        assert titleText != null : "fx:id=\"titleText\" was not injected: check your FXML file 'VideoView.fxml'.";
        assert chooseAudioButton != null : "fx:id=\"chooseAudioButton\" was not injected: check your FXML file 'VideoView.fxml'.";
        assert Strip_add != null : "fx:id=\"Strip_add\" was not injected: check your FXML file 'VideoView.fxml'.";
        assert chooseOverlayBtn != null : "fx:id=\"chooseOverlayBtn\" was not injected: check your FXML file 'VideoView.fxml'.";
        assert videoMediaView != null : "fx:id=\"videoMediaView\" was not injected: check your FXML file 'VideoView.fxml'.";
        assert muteCheckbox != null : "fx:id=\"muteCheckbox\" was not injected: check your FXML file 'VideoView.fxml'.";
        assert creditText != null : "fx:id=\"creditText\" was not injected: check your FXML file 'VideoView.fxml'.";
        assert PreviewTab != null : "fx:id=\"PreviewTab\" was not injected: check your FXML file 'VideoView.fxml'.";
        assert overlayToEnd != null : "fx:id=\"overlayToEnd\" was not injected: check your FXML file 'VideoView.fxml'.";
        assert rewindBtn != null : "fx:id=\"rewindBtn\" was not injected: check your FXML file 'VideoView.fxml'.";
        assert overlayToStart != null : "fx:id=\"overlayToStart\" was not injected: check your FXML file 'VideoView.fxml'.";
        assert titleYPos != null : "fx:id=\"titleYPos\" was not injected: check your FXML file 'VideoView.fxml'.";
        assert setTitle != null : "fx:id=\"setTitle\" was not injected: check your FXML file 'VideoView.fxml'.";
        assert fastForwardBtn != null : "fx:id=\"fastForwardBtn\" was not injected: check your FXML file 'VideoView.fxml'.";
        assert StripAudioLabel != null : "fx:id=\"StripAudioLabel\" was not injected: check your FXML file 'VideoView.fxml'.";
        assert creditsYPos != null : "fx:id=\"creditsYPos\" was not injected: check your FXML file 'VideoView.fxml'.";
        assert overlayUseStart != null : "fx:id=\"overlayUseStart\" was not injected: check your FXML file 'VideoView.fxml'.";
        assert strip_button != null : "fx:id=\"strip_button\" was not injected: check your FXML file 'VideoView.fxml'.";
        assert videoPath != null : "fx:id=\"videoPath\" was not injected: check your FXML file 'VideoView.fxml'.";
        assert StipeText != null : "fx:id=\"StipeText\" was not injected: check your FXML file 'VideoView.fxml'.";
        assert videoTab != null : "fx:id=\"videoTab\" was not injected: check your FXML file 'VideoView.fxml'.";
        assert OverlayAdd != null : "fx:id=\"OverlayAdd\" was not injected: check your FXML file 'VideoView.fxml'.";
        assert renderNoAudioBtn != null : "fx:id=\"renderNoAudioBtn\" was not injected: check your FXML file 'VideoView.fxml'.";
        assert ReplaceAdd != null : "fx:id=\"ReplaceAdd\" was not injected: check your FXML file 'VideoView.fxml'.";
        assert saveToBtn != null : "fx:id=\"saveToBtn\" was not injected: check your FXML file 'VideoView.fxml'.";
        assert renderWithAudioBtn != null : "fx:id=\"renderWithAudioBtn\" was not injected: check your FXML file 'VideoView.fxml'.";
        assert titleSize != null : "fx:id=\"titleSize\" was not injected: check your FXML file 'VideoView.fxml'.";
        assert startReplace != null : "fx:id=\"startReplace\" was not injected: check your FXML file 'VideoView.fxml'.";
        assert creditsSize != null : "fx:id=\"creditsSize\" was not injected: check your FXML file 'VideoView.fxml'.";
        assert OverlayAudioLabel != null : "fx:id=\"OverlayAudioLabel\" was not injected: check your FXML file 'VideoView.fxml'.";
        assert overlayAudioBtn != null : "fx:id=\"overlayAudioBtn\" was not injected: check your FXML file 'VideoView.fxml'.";
        assert outputFilePath != null : "fx:id=\"outputFilePath\" was not injected: check your FXML file 'VideoView.fxml'.";
        assert ReplaceButton != null : "fx:id=\"ReplaceButton\" was not injected: check your FXML file 'VideoView.fxml'.";
        assert volumeSlider != null : "fx:id=\"volumeSlider\" was not injected: check your FXML file 'VideoView.fxml'.";
        assert videoURL != null : "fx:id=\"videoURL\" was not injected: check your FXML file 'VideoView.fxml'.";
        assert setCredits != null : "fx:id=\"setCredits\" was not injected: check your FXML file 'VideoView.fxml'.";
        assert creditsColour != null : "fx:id=\"creditsColour\" was not injected: check your FXML file 'VideoView.fxml'.";
        assert ReplaceAudioLabel != null : "fx:id=\"ReplaceAudioLabel\" was not injected: check your FXML file 'VideoView.fxml'.";
        assert endCredits != null : "fx:id=\"endCredits\" was not injected: check your FXML file 'VideoView.fxml'.";
        assert TabPane != null : "fx:id=\"TabPane\" was not injected: check your FXML file 'VideoView.fxml'.";
        assert addVideoLabel11 != null : "fx:id=\"addVideoLabel11\" was not injected: check your FXML file 'VideoView.fxml'.";
        assert audioTab != null : "fx:id=\"audioTab\" was not injected: check your FXML file 'VideoView.fxml'.";
        assert tabMenu != null : "fx:id=\"tabMenu\" was not injected: check your FXML file 'VideoView.fxml'.";
        assert overlayUseEnd != null : "fx:id=\"overlayUseEnd\" was not injected: check your FXML file 'VideoView.fxml'.";
        assert downloadBtn != null : "fx:id=\"downloadBtn\" was not injected: check your FXML file 'VideoView.fxml'.";
        assert titleColour != null : "fx:id=\"titleColour\" was not injected: check your FXML file 'VideoView.fxml'.";
        assert playPauseBtn != null : "fx:id=\"playPauseBtn\" was not injected: check your FXML file 'VideoView.fxml'.";
        assert videoTime != null : "fx:id=\"videoTime\" was not injected: check your FXML file 'VideoView.fxml'.";
        assert videoProgress != null : "fx:id=\"videoProgress\" was not injected: check your FXML file 'VideoView.fxml'.";
        
        //actually works hahaha
        downloadBtn.setOnAction(new EventHandler<ActionEvent>() {
        	@Override
        	public void handle(ActionEvent evt) {
        		videoURL.setText("No, you damn pirate.");
        	}
        });
    }
}
