package vamix.controller;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.filechooser.FileNameExtensionFilter;

import uk.co.caprica.vlcj.binding.internal.libvlc_state_t;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
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

/**
 * Controller class for the VAMIX GUI
 **/
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
	private TextField durationTitle;
	
//	@FXML
//	private TextField titleBGAddr;
//	
//	@FXML
//	private Button titleBGBtn;
	
	@FXML
	private TextField creditsXPos;

	@FXML
	private Button browseBtn;

	@FXML
	private TextField durationCredits;
	
//	@FXML
//	private TextField creditsBGAddr;
//	
//	@FXML
//	private Button creditsBGBtn;
	
	@FXML
	private TextArea titleText;

	@FXML
	private Button downloadBtn;

	@FXML
	private ColorPicker titleColour;
	
	@FXML
	private ComboBox<String> titleFont;

	@FXML
	private TextArea creditText;
	
	@FXML
	private ComboBox<String> creditsFont;
	
	@FXML
	private TextField videoURL;

	@FXML
	private Button setCredits;

	@FXML
	private ColorPicker creditsColour;
	
	@FXML
	private TextField startTitle;
	
	@FXML
	private TextField endTitle;
	
	@FXML
	private TextField startCredits;
	
	@FXML
	private TextField endCredits;
	
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
	private ComboBox<String> titleSize;

	@FXML
	private ComboBox<String> creditsSize;
	
	@FXML
	private Button loadBtn;

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
	private TextField startReplace2;

	@FXML
	private TextField endReplace2;
	
	@FXML
	private TextField overlayUseEnd;

	@FXML
	private Button chooseAudioButton;

	@FXML
	private TextField strip_add;
	
	@FXML
	private Button save_file_chooser;

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
	 * Varaible for the render tab
	 */
	@FXML
	private Tab renderTab;

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
	
	private SkipWorker sW;
	
	//initialising the functionality of the buttons
	@FXML
	void initialize() {
		loadMedia();
		mainPanesCheck();
		videoTabCheck();
		audioTabCheck();
		renderTabCheck();
		playerCheck();
		videoTab();
		audioTab();
		renderTab();
		player();
		if (!(videoFileAdd.equals(""))){
			videoPath.setText(videoFileAdd);
		}
	}

	private void videoTabCheck(){
		/*
		 * Section for the video tab id check
		 */
		assert downloadBtn != null : "fx:id=\"downloadBtn\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert loadBtn != null : "fx:id=\"loadBtn\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert browseBtn != null : "fx:id=\"browseBtn\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert setTitle != null : "fx:id=\"setTitle\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert setCredits != null : "fx:id=\"setCredits\" was not injected: check your FXML file 'VideoView.fxml'.";

		assert videoPath != null : "fx:id=\"videoPath\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert videoURL != null : "fx:id=\"videoURL\" was not injected: check your FXML file 'VideoView.fxml'.";

		assert titleText != null : "fx:id=\"titleText\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert titleXPos != null : "fx:id=\"titleXPos\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert durationTitle != null : "fx:id=\"durationTitle\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert titleYPos != null : "fx:id=\"titleYPos\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert titleSize != null : "fx:id=\"titleSize\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert titleColour != null : "fx:id=\"titleColour\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert startTitle != null : "fx:id=\"startTitle\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert endTitle != null : "fx:id=\"endTitle\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert titleFont != null : "fx:id=\"titleFont\" was not injected: check your FXML file 'VideoView.fxml'.";
//		assert titleBGAddr != null : "fx:id=\"titleBGAddr\" was not injected: check your FXML file 'VideoView.fxml'.";
//		assert titleBGBtn != null : "fx:id=\"titleBGBtn\" was not injected: check your FXML file 'VideoView.fxml'.";

		assert creditText != null : "fx:id=\"creditText\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert creditsXPos != null : "fx:id=\"creditsXPos\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert creditsYPos != null : "fx:id=\"creditsYPos\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert creditsColour != null : "fx:id=\"creditsColour\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert durationCredits != null : "fx:id=\"durationCredits\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert creditsSize != null : "fx:id=\"creditsSize\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert startCredits != null : "fx:id=\"startCredits\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert endCredits != null : "fx:id=\"endCredits\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert creditsFont != null : "fx:id=\"creditsFont\" was not injected: check your FXML file 'VideoView.fxml'.";
//		assert creditsBGAddr != null : "fx:id=\"creditsBGAddr\" was not injected: check your FXML file 'VideoView.fxml'.";
//		assert creditsBGBtn != null : "fx:id=\"creditsBGBtn\" was not injected: check your FXML file 'VideoView.fxml'.";
	}

	private void videoTab(){
		/*
		 * Section for the video tab functionality
		 */
		
		//populate font size combo boxes with font sizes
		ObservableList<String> fontSizes = FXCollections.observableArrayList(
			"4",
			"6",
			"8",
			"10",
			"12",
			"14",
			"16",
			"18",
			"20",
			"22",
			"24"
		);
		titleSize.setItems(fontSizes);
		creditsSize.setItems(fontSizes);
		//set their defaults
		titleSize.setValue("14");
		creditsSize.setValue("14");
		
		//populate font combo boxes with free fonts
		ObservableList<String> fonts = FXCollections.observableArrayList(
			"FreeMono",
			"FreeMonoBold",
			"FreeMonoBoldOblique",
			"FreeMonoOblique",
			"FreeSans",
			"FreeSansBold",
			"FreeSansBoldOblique",
			"FreeSansOblique",
			"FreeSerif",
			"FreeSerifBold",
			"FreeSerifBoldItalic",
			"FreeSerifItalic"
		);
		titleFont.setItems(fonts);
		creditsFont.setItems(fonts);
		//set their defaults
		titleFont.setValue("FreeSans");
		creditsFont.setValue("FreeSans");
		
		//download function button
		downloadBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evt) {
				//send url to dl obj and invoke dl function
				Download dl =new Download(videoURL.getText());
				dl.downloadFunction();
			}
		});
		
		//load video function button when click browse
		browseBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evt) {
				//load media
				String previousFile =videoFileAdd;//get current file
				loadMedia();
				if (!(previousFile.equals(videoPath.getText()))&Helper.validInFile(videoPath.getText(),"(Audio)|Video|MPEG")){
					vamix.view.Main.vid.prepareMedia(videoPath.getText());
					videoFileAdd=videoPath.getText();
				}else if (previousFile.equals(videoPath.getText())){
					JOptionPane.showMessageDialog(null,"You have entered the file thats already been play.");
				}
			}
		});
		
		//load video function button when click browse
		loadBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evt) {
				//play media if its different from the current one
				String previousFile =videoFileAdd;//get current file
				if (!(previousFile.equals(videoPath.getText()))&Helper.validInFile(videoPath.getText(),"(Audio)|Video|MPEG")){
					vamix.view.Main.vid.prepareMedia(videoPath.getText());
					videoFileAdd=videoPath.getText();
				}else if (previousFile.equals(videoPath.getText())){
					JOptionPane.showMessageDialog(null,"You have selected the file thats is currently playing.");
				}
			}
		});
		
		videoPath.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				if ((arg0.getClickCount()>=2)&& !arg0.isConsumed()){
					//load media
					String previousFile =videoFileAdd;//get current file
					loadMedia();
					if (!(previousFile.equals(videoPath.getText()))&Helper.validInFile(videoPath.getText(),"(Audio)|Video|MPEG")){
						vamix.view.Main.vid.prepareMedia(videoPath.getText());
						videoFileAdd=videoPath.getText();
					}else if (previousFile.equals(videoPath.getText())){
						JOptionPane.showMessageDialog(null,"You have selected the file thats is currently playing.");
					}
				}
			}
		});
		
		//the functionality for the set title text button
		setTitle.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evt) {
				//first check if the user has entered text to be overlaid
				if (titleText.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Please enter text to overlay on background image", "Missing Input",
							JOptionPane.WARNING_MESSAGE);
				//otherwise if text has been entered, continue with function
				} else {
					//check if the duration entered by user is in format required
					String timeFormat = "^\\d{2}:\\d{2}:\\d{2}$";
					boolean startMatched = false;
					//boolean to show if both times entered by user are valid (within length of video)
					boolean bothValid = true;
					//first check the start time and if it matches check the finish time
					Matcher matcher = Pattern.compile(timeFormat).matcher(startTitle.getText());
					if (matcher.find()) {
						startMatched = true;
					}
					if (startMatched) {
						matcher = Pattern.compile(timeFormat).matcher(endTitle.getText());
						//if the finish time also matches now go on to check if the times entered are
						//less than or equal to the length of the video
						if (matcher.find()) {
							String[] startTimeSplit = startTitle.getText().split(":");
							String[] endTimeSplit = endTitle.getText().split(":");
							//calculate the length of the start and end times and make sure they are
							//less than length of the video and end time is greater than start time
							long startLength = Long.parseLong(startTimeSplit[0])*3600000 + Long.parseLong(startTimeSplit[1])*60000
									+ Long.parseLong(startTimeSplit[2])*1000;
							long endLength = Long.parseLong(endTimeSplit[0])*3600000 + Long.parseLong(endTimeSplit[1])*60000
									+ Long.parseLong(endTimeSplit[2])*1000;
							long videoLength = vamix.view.Main.vid.getLength();
							if ((startLength > videoLength) || (endLength > videoLength) || (endLength < startLength)) {
								bothValid = false;
							}
							//if both times are valid continue with drawing text on video (font and font size still need to be implemented)
							if (bothValid) {
								System.out.println(titleColour.getValue().toString());
								TextEdit textEditor = new TextEdit(titleText.getText(), titleFont.getValue(), titleSize.getValue(), titleColour.getValue().toString(),
										startTitle.getText(), titleXPos.getText(), titleYPos.getText(), null, null,
										null, null, null, null, null, videoFileAdd, null, "title", null, null);
								textEditor.showScenePreviewAsync();
							//otherwise display an error message to the user telling them times entered are invalid
							} else {
								JOptionPane.showMessageDialog(null, "Please enter a valid start time and end time for displaying the title text",
								"Invalid time entered", JOptionPane.ERROR_MESSAGE);
							}	
						} else {
							JOptionPane.showMessageDialog(null, "Please enter the end time in the format hh:mm:ss",
							"Invalid time format", JOptionPane.ERROR_MESSAGE);
						}
					} else {
						JOptionPane.showMessageDialog(null, "Please enter the start time in the format hh:mm:ss",
						"Invalid time format", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
	}

	private void audioTabCheck(){
		/*
		 * Section for the audio tab id check
		 */
		
		assert strip_button != null : "fx:id=\"strip_button\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert save_file_chooser != null : "fx:id=\"save_file_chooser\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert replaceButton != null : "fx:id=\"replaceButton\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert overlayAudioBtn != null : "fx:id=\"overlayAudioBtn\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert chooseAudioButton != null : "fx:id=\"chooseAudioButton\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert chooseOverlayBtn != null : "fx:id=\"chooseOverlayBtn\" was not injected: check your FXML file 'VideoView.fxml'.";

		assert strip_add != null : "fx:id=\"strip_add\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert overlayAdd != null : "fx:id=\"overlayAdd\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert replaceAdd != null : "fx:id=\"replaceAdd\" was not injected: check your FXML file 'VideoView.fxml'.";

		assert endReplace != null : "fx:id=\"endReplace\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert startReplace != null : "fx:id=\"startReplace\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert endReplace2 != null : "fx:id=\"endReplace2\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert startReplace2 != null : "fx:id=\"startReplace2\" was not injected: check your FXML file 'VideoView.fxml'.";
		
		assert overlayUseEnd != null : "fx:id=\"overlayUseEnd\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert overlayUseStart != null : "fx:id=\"overlayUseStart\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert overlayToEnd != null : "fx:id=\"overlayToEnd\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert overlayToStart != null : "fx:id=\"overlayToStart\" was not injected: check your FXML file 'VideoView.fxml'.";

	}

	private void audioTab(){
		/*
		 * Section for the audio tab functionality
		 */
		//strip audio function button
		strip_button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evt) {
				//send in and out file to obj then invoke function
				Strip stripAudio =new Strip(videoFileAdd,strip_add.getText());
				stripAudio.stripFunction();
			}
		});
		
		/*
		 * Section for the audio tab functionality
		 */
		//strip audio add when double click file chooser comes up
		strip_add.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				if ((arg0.getClickCount()>=2)&& !arg0.isConsumed()){
					//get the address of file to be save
					String temp=Helper.saveFileChooser();
					strip_add.setText(temp);
				}
			}
		});
		
		//save file chooser when button clicked
		save_file_chooser.setOnMouseClicked(new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent arg0) {
			//get the address of file to be save
			String temp=Helper.saveFileChooser();
			strip_add.setText(temp);
		}
	});
		
		//replace audio function button
		replaceButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evt) {
				//send in and out file to obj then invoke function
				ReplaceAudio r=new ReplaceAudio(videoFileAdd,replaceAdd.getText(),startReplace.getText(),endReplace.getText(),startReplace2.getText(),endReplace2.getText());
				r.replaceAudioFunction();
			}
		});
		
		//replace file chooser button
		chooseAudioButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evt) {
				//choose replace audio
				String temp=Helper.audioFileChooser();
				replaceAdd.setText(temp);
			}
		});
		
		//replace file chooser address
		replaceAdd.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				if ((arg0.getClickCount()>=2)&& !arg0.isConsumed()){
					//choose replace audio
					String temp=Helper.audioFileChooser();
					replaceAdd.setText(temp);
				}
			}
		});
		
		//overlay file chooser when address clicked
		overlayAdd.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				if ((arg0.getClickCount()>=2)&& !arg0.isConsumed()){
					//choose replace audio
					String temp=Helper.audioFileChooser();
					overlayAdd.setText(temp);
				}
			}
		});
		
		//overlay file chooser button
		chooseOverlayBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				//choose replace audio
				String temp=Helper.audioFileChooser();
				overlayAdd.setText(temp);
			}
		});
		
		overlayAudioBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evt) {
				//send in and out file to obj then invoke function for overlay
				OverlayAudio o=new OverlayAudio(videoFileAdd,overlayAdd.getText(),overlayUseStart.getText(),overlayUseEnd.getText(),overlayToStart.getText(),overlayToEnd.getText());
				o.overlayAudioFunction();
			}
		});
	}

	private void renderTab(){
		/*
		 * Section for the preview tab functionality
		 * Section for the render tab functionality
		 */

	}

	private void renderTabCheck(){
		/*
		 * Section for the render tab id check
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
		if (!(videoFileAdd.equals(""))){
			vamix.view.Main.vid.prepareMedia(videoFileAdd);
		}
		
		//start counter for the video
		Timer videoTimer=new Timer(200, new ActionListener() {
			@Override //when clock interval met run following
			public void actionPerformed(java.awt.event.ActionEvent e) {
				Platform.runLater(new Runnable() { //need to invoke on javafx thread
			        @Override
			        public void run() { //set the time text and progress bar
			        	if (!(vamix.view.Main.vid.getMediaPlayerState()==libvlc_state_t.libvlc_NothingSpecial)){
				        	double currentTime=(vamix.view.Main.vid.getTime()/1000.0);
				        	double VidTime=(vamix.view.Main.vid.getLength()/1000.0);
				        	videoProgress.setProgress((currentTime/VidTime));
				        	if (currentTime>=VidTime){//when reach end of file loop
				        		vamix.view.Main.vid.playMedia(videoFileAdd); 
				        		if (!(vamix.view.Main.vid.getMediaPlayerState()==libvlc_state_t.libvlc_Playing)){
				        			playPauseBtn.setText("Pause");
				        		}
				        		String videTime= Helper.timeOfVideo(currentTime,VidTime);
				        		videoTime.setText(videTime);
				        	}else{ 
				        		String videTime= Helper.timeOfVideo(currentTime,VidTime);
				        		videoTime.setText(videTime);
				        	}
			        	}else if(vamix.view.Main.vid.getMediaPlayerState()==libvlc_state_t.libvlc_NothingSpecial){
			        		if (!(videoFileAdd.equals(""))){
				        		vamix.view.Main.vid.start();
				        		try {//sleep thread so can execute next command when previous finish
				        			Thread.sleep(50);
				        		} catch (InterruptedException e1) {
				        		}
				        		vamix.view.Main.vid.pause();
				        		playPauseBtn.setText("Play");
				        		videoProgress.setProgress(0.0);
				        		if (vamix.view.Main.vid.isMute()){
				        			vamix.view.Main.vid.mute();
				        		}
				        		vamix.view.Main.vid.setVolume(100);
				        		volumeSlider.setValue(50);
				        		//set video to unmute
				        		muteCheckbox.setSelected(false);	
			        		}
			        	}
			        }
			   });
				
			}
		});
		
		videoTimer.start();
		//System.out.println(vamix.view.Main.vid.getMediaPlayerState()+"");
		playPauseBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evt) {//before video even got played
				/*if (vamix.view.Main.vid.getMediaPlayerState()==libvlc_state_t.libvlc_NothingSpecial){
					vamix.view.Main.vid.play();
					playPauseBtn.setText("Pause"); //when video ended play video
				}else */if(vamix.view.Main.vid.getMediaPlayerState()==libvlc_state_t.libvlc_Ended){
					vamix.view.Main.vid.startMedia(videoFileAdd);
				}else if (vamix.view.Main.vid.getMediaPlayerState()==libvlc_state_t.libvlc_Playing){
					//when play pause the video
					vamix.view.Main.vid.pause();
					playPauseBtn.setText("Play");
				}else if (vamix.view.Main.vid.getMediaPlayerState()==libvlc_state_t.libvlc_Paused){
					//when pause play the video
					vamix.view.Main.vid.pause();
					playPauseBtn.setText("Pause");
				}
			}
		});

		muteCheckbox.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
			//toggle mute of the video
				if (!(vamix.view.Main.vid.getMediaPlayerState()==libvlc_state_t.libvlc_NothingSpecial)){
					vamix.view.Main.vid.mute();
				}				
			}

		});
		
		///fast forward when mouse pressed using swingworker as it can continuosly skip
		fastForwardBtn.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				sW=new SkipWorker(Constants.SKIP_RATE);
				sW.execute();
				//cancel rewind when mouse leave the button even didnt release
				fastForwardBtn.setOnMouseExited(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent arg0) {
						sW.cancel(true);
					}
				});
			}
		});
		
		//cancel fast forward when mouse released
		fastForwardBtn.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				sW.cancel(true);
			}
		});
		
		//rewind when mouse pressed using swingworker as it can continuously skip
		rewindBtn.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				sW=new SkipWorker(-Constants.SKIP_RATE);
				sW.execute();
				//cancel rewind when mouse leave the button even didnt release
				rewindBtn.setOnMouseExited(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent arg0) {
						sW.cancel(true);
					}
				});
			}
		});
		
		//cancel rewind when mouse released
		rewindBtn.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				sW.cancel(true);
			}
		});

		volumeSlider.setOnMouseDragged(new EventHandler<MouseEvent>() {
			//volume slider set volume continuously when slide
			@Override
			public void handle(MouseEvent arg0) {
				//if (vamix.view.Main.vid.getMediaPlayerState()==libvlc_state_t.libvlc_Ended){
					vamix.view.Main.vid.setVolume((int) (2*volumeSlider.getValue()));
				//}
			}
		});
		
		volumeSlider.setOnMouseClicked(new EventHandler<MouseEvent>() {
			//volume slider set volume continuously when click
			//note volume is 0-200 so need to times 2 as slider is only to 100
			@Override
			public void handle(MouseEvent arg0) {
				//if (vamix.view.Main.vid.getMediaPlayerState()==libvlc_state_t.libvlc_Ended){
					vamix.view.Main.vid.setVolume((int) (2*volumeSlider.getValue()));
				//}
			}
		});
		
		videoProgress.setOnMouseDragged(new EventHandler<MouseEvent>() {
			//video slider set volume continuously when slide
			//settime is in (ms) of time, get width is in the pixel unit
			@Override
			public void handle(MouseEvent arg0) {
				//if (vamix.view.Main.vid.getMediaPlayerState()==libvlc_state_t.libvlc_Ended){
				vamix.view.Main.vid.setTime((long)arg0.getX()*vamix.view.Main.vid.getLength()/(long)videoProgress.getWidth());
				//}
			}
		});
		
		videoProgress.setOnMouseClicked(new EventHandler<MouseEvent>() {
			//video slider set volume discrete when slide
			@Override
			public void handle(MouseEvent arg0) {
				vamix.view.Main.vid.setTime((long)(arg0.getX()*vamix.view.Main.vid.getLength()/(long)videoProgress.getWidth()));
			}
		});
	}

	private void mainPanesCheck(){
		assert tabMenu != null : "fx:id=\"tabMenu\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert tabPane != null : "fx:id=\"tabPane\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert videoTab != null : "fx:id=\"videoTab\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert renderTab != null : "fx:id=\"renderTab\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert audioTab != null : "fx:id=\"audioTab\" was not injected: check your FXML file 'VideoView.fxml'.";
	}

	private void loadMedia(){
		//intiliase validness varaible code reuse from a2
		boolean valid=false; //if file is valid
		boolean isAudio=false; //boolean for if file is video or audio
		String tempVideoFileAdd="";//initialse the video file address
		String partial=""; //variable for partial of path ie just the name of file
		JOptionPane.showMessageDialog(null, "Please select the video or audio to edit.");
		//get input file
		while(!valid){
			//setup file chooser
			JFileChooser chooser = new JFileChooser(Constants.CURRENT_DIR);
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Video/audio file","avi","mov","mp4"
					,"mp3","wav","wmv");
			chooser.setFileFilter(filter); //set mp3 filter
			//get save path
			int choice=chooser.showOpenDialog(null);
			if (choice==JFileChooser.CANCEL_OPTION){
				break;
			}
			File file =chooser.getSelectedFile();
			try{
				tempVideoFileAdd=file.getAbsolutePath();//get path address
			}catch(NullPointerException e){
				valid=true; //cant return nothing
			}

			//now get the path of file and just file name
			Matcher m=Pattern.compile("(.*"+File.separator+")(.*)$").matcher(tempVideoFileAdd);
			if(m.find()){
				partial=m.group(2); //get file name
			}

			if(partial.equals("")){
				//error message of empty file name
				JOptionPane.showMessageDialog(null, "You have entered a empty file name. Please input a valid file name.");
			}else{
				//check if the file exist locally
				if (Helper.fileExist(tempVideoFileAdd)){
					String bash =File.separator+"bin"+File.separator+"bash";
					String cmd ="echo $(file "+tempVideoFileAdd+")";
					ProcessBuilder builder=new ProcessBuilder(bash,"-c",cmd); 
					builder.redirectErrorStream(true);

					try{
						Process process = builder.start();
						InputStream stdout = process.getInputStream();
						BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
						String line;
						while((line=stdoutBuffered.readLine())!=null){
							//System.out.println(line);//debug file type
							Matcher macth =Pattern.compile("(video)|Media|Audio|MPEG").matcher(line);
							if(macth.find()){
								isAudio=true;
							}
						}
					}catch(Exception e){
						e.printStackTrace();
					}
					//check if audio using bash commands
					if (isAudio){
						videoFileAdd=tempVideoFileAdd;
						videoPath.setText(videoFileAdd);
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
