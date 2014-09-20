package vamix.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import uk.co.caprica.vlcj.binding.internal.libvlc_state_t;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
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
import javafx.scene.input.MouseEvent;
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

	private String videoFileAdd="";

	//initialising the functionality of the buttons
	@FXML
	void initialize() {
		loadMedia();
		mainPanesCheck();
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
		downloadBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evt) {
				videoURL.setText("No, you damn pirate.");
			}
		});
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
		//load video but dont play yet
		vamix.view.Main.vid.prepareMedia(videoFileAdd);
		//System.out.println(vamix.view.Main.vid.getMediaPlayerState()+"");
		playPauseBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evt) {
				if (vamix.view.Main.vid.getMediaPlayerState()==libvlc_state_t.libvlc_NothingSpecial){
					vamix.view.Main.vid.play();
					playPauseBtn.setText("Pause");
				}else if(vamix.view.Main.vid.getMediaPlayerState()==libvlc_state_t.libvlc_Ended){
					vamix.view.Main.vid.startMedia(videoFileAdd);
				}else if (vamix.view.Main.vid.getMediaPlayerState()==libvlc_state_t.libvlc_Playing){
					vamix.view.Main.vid.pause();
					playPauseBtn.setText("Play");
				}else{
					vamix.view.Main.vid.pause();
					playPauseBtn.setText("Pause");
				}
			}
		});

		muteCheckbox.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				if (!(vamix.view.Main.vid.getMediaPlayerState()==libvlc_state_t.libvlc_NothingSpecial)){
					vamix.view.Main.vid.mute();
				}				
			}

		});

		fastForwardBtn.setOnMouseDragged(new EventHandler<MouseEvent>() {
			/*final Timer skiptimer=new Timer();
			final SkipForward tasker=new SkipForward();
			@Override
			public void handle(MouseEvent arg0) {
				 skiptimer.scheduleAtFixedRate(tasker, 0, 250);
			}*/
			@Override
			public void handle(MouseEvent arg0) {
				vamix.view.Main.vid.skip(2000);
			}
		});
		rewindBtn.setOnMouseDragged(new EventHandler<MouseEvent>() {
			/*final Timer skiptimer=new Timer();
			final SkipForward tasker=new SkipForward();
			@Override
			public void handle(MouseEvent arg0) {
				 skiptimer.scheduleAtFixedRate(tasker, 0, 250);
			}*/
			@Override
			public void handle(MouseEvent arg0) {
				if (vamix.view.Main.vid.getMediaPlayerState()==libvlc_state_t.libvlc_Ended){
					vamix.view.Main.vid.startMedia(videoFileAdd);
				}else{
					vamix.view.Main.vid.skip(-2000);
				}
			}
		});
		
		volumeSlider.setOnMouseDragged(new EventHandler<MouseEvent>() {
			//volume slider set volume continously when slide
			@Override
			public void handle(MouseEvent arg0) {
				//if (vamix.view.Main.vid.getMediaPlayerState()==libvlc_state_t.libvlc_Ended){
					vamix.view.Main.vid.setVolume((int) (2*volumeSlider.getValue()));
				//}
			}
		});
		
		volumeSlider.setOnMouseClicked(new EventHandler<MouseEvent>() {
			//volume slider set volume continously when click
			@Override
			public void handle(MouseEvent arg0) {
				//if (vamix.view.Main.vid.getMediaPlayerState()==libvlc_state_t.libvlc_Ended){
					vamix.view.Main.vid.setVolume((int) (2*volumeSlider.getValue()));
				//}
			}
		});
		
		/*fastForwardBtn.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {

			}

		});*/
		/*

		@FXML
		private Label videoTime;

		@FXML
		private ProgressBar videoProgress;
		 */
	}
	private class SkipForward extends TimerTask{

		@Override
		public void run() {
			vamix.view.Main.vid.skip(1000);		
		}

	}

	private void mainPanesCheck(){
		assert tabMenu != null : "fx:id=\"tabMenu\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert tabPane != null : "fx:id=\"tabPane\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert videoTab != null : "fx:id=\"videoTab\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert previewTab != null : "fx:id=\"previewTab\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert audioTab != null : "fx:id=\"audioTab\" was not injected: check your FXML file 'VideoView.fxml'.";
	}

	private void loadMedia(){
		//intiliase validness varaible code reuse from a2
		boolean valid=false; //
		boolean isAudio=false; //
		String partial=""; //variable for partial of path ie just the name of file
		JOptionPane.showMessageDialog(null, "Please select the video to edit.");
		//get input file
		while(!valid){
			//setup file chooser
			JFileChooser chooser = new JFileChooser(Constants.CURRENT_DIR);
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Video file","avi","mov","mp4");
			chooser.setFileFilter(filter); //set mp3 filter
			//get save path
			chooser.showOpenDialog(null);
			File file =chooser.getSelectedFile();
			try{
				videoFileAdd=file.getAbsolutePath();//get path address
			}catch(NullPointerException e){
				valid=false; //cant return nothing
			}

			//now get the path of file and just file name
			Matcher m=Pattern.compile("(.*"+File.separator+")(.*)$").matcher(videoFileAdd);
			if(m.find()){
				partial=m.group(2); //get file name
			}

			if(partial.equals("")){
				//error message of empty file name
				JOptionPane.showMessageDialog(null, "You have entered a empty file name. Please input a valid file name.");
			}else{
				//check if the file exist locally
				if (Helper.fileExist(videoFileAdd)){
					String bash =File.separator+"bin"+File.separator+"bash";
					String cmd ="echo $(file "+videoFileAdd+")";
					ProcessBuilder builder=new ProcessBuilder(bash,"-c",cmd); 
					builder.redirectErrorStream(true);

					try{
						Process process = builder.start();
						InputStream stdout = process.getInputStream();
						BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
						String line;
						while((line=stdoutBuffered.readLine())!=null){
							//System.out.println(line);//debug file type
							Matcher macth =Pattern.compile("(video)|Media").matcher(line);
							if(macth.find()){
								isAudio=true;
							}
						}
					}catch(Exception e){
						e.printStackTrace();
					}
					//check if audio using bash commands
					if (isAudio){
						valid=true;
					}else{
						//file is not audio/mpeg type
						JOptionPane.showMessageDialog(null, "You have entered a non-video file please enter a valid file.");
					}
				}else{
					//file does not exist so give error
					JOptionPane.showMessageDialog(null,"You have entered a non-existing file. Please input a valid file type.");
				}
			}
		}

	}

}
