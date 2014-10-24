package vamix.controller;

import javafx.scene.paint.Color;

import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.filechooser.FileNameExtensionFilter;

import uk.co.caprica.vlcj.binding.internal.libvlc_state_t;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventAdapter;
import util.FileBrowsers;
import vamix.controller.FadeVideo.FadeType;
import vamix.controller.TextEdit.RenderType;
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
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 * Controller class for the VAMIX GUI (VideoView.fxml). GUI was built using JavaFX 2.0 and SceneBuilder. SceneBuilder requires the GUI has
 * only one controller class and since this class must deal with all user input and user interactions with the GUI, it had to be a large class.
 * @author Nikhil Prasad and Guyver (Yeu-Shin) Fu
 **/
public class VamixController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;
	
	/*
	 * Two main containers for the GUI
	 */
	@FXML
	private AnchorPane tabPane;

	@FXML
	private TabPane tabMenu;
	
	/*
	 * GUI components for the menu bar
	 */	
	@FXML
	private MenuItem saveStateBtn;
	
	@FXML
	private MenuItem loadStateBtn;
	
	@FXML
	private MenuItem loadFiles;
	
	@FXML
	private MenuItem playWithSubtitlesBtn;
	
	/*
	 * GUI components for the video tab
	 */
	@FXML
	private TextField titleXPos;

	@FXML
	private TextField durationTitle;
	
	@FXML
	private TextField creditsXPos;

	@FXML
	private Button browseBtn;

	@FXML
	private TextField durationCredits;
	
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
	
	@FXML
	private TextField rotateAngle;
	
	@FXML
	private Button rotateBtn;
	
	@FXML
	private TextField startFadeIn;
	
	@FXML
	private TextField endFadeIn;
	
	@FXML
	private TextField startFadeOut;
	
	@FXML
	private TextField endFadeOut;
	
	@FXML
	private CheckBox includeFadeIn;
	
	@FXML
	private CheckBox includeFadeOut;
	
	@FXML
	private Button fadeBtn;
	
	@FXML
	private TextField startTrim;
	
	@FXML
	private TextField endTrim;
	
	@FXML
	private Button trimBtn;
	
	@FXML
	private TextField cropXPos;
	
	@FXML
	private TextField cropYPos;
	
	@FXML
	private TextField cropHeight;
	
	@FXML
	private TextField cropWidth;
	
	@FXML
	private Button cropBtn;

	/*
	 * GUI components for the audio tab
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
	 * GUI components for the render tab
	 */
	@FXML
	private Tab renderTab;

	@FXML
	private Button renderWithAudioBtn;

	@FXML
	private TextField outputFilePath;

	@FXML
	private Button saveToBtn;
	
	@FXML
	private CheckBox includeTitle;
	
	@FXML
	private CheckBox includeCredits;
	
	/*
	 * GUI components for the Subtitles tab
	 */
	
	@FXML
	private Button newSubtitleFileBtn;
	
	@FXML
	private Button editSubtitleFileBtn;
	
	@FXML
	private Button saveSubtitleFileBtn;
	
	@FXML
	private TableView<Subtitle> subtitleTable;
	
	@FXML
	private TableColumn<Subtitle, String> subtitleNumber;
	
	@FXML
	private TableColumn<Subtitle, String> subtitleStart;
	
	@FXML
	private TableColumn<Subtitle, String> subtitleEnd;
	
	@FXML
	private TableColumn<Subtitle, String> subtitleText;
	
	@FXML
	private Button addSubtitleBtn;
	
	@FXML
	private Button deleteSubtitleBtn;
	
	@FXML
	private TextField subtitleToAdd;
	
	@FXML
	private TextField subtitleToDelete;
	
	@FXML
	private Label subtitlePathLabel;

	/*
	 * GUI components for the media player
	 */
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
	private ImageView playPauseImage;

	@FXML
	private Label videoTime;

	@FXML
	private ProgressBar videoProgress;
	
	@FXML
	private Label subtitlesPlaying;

	//holds the file path of the video currently being played
	private String videoFileAdd="";
	
	//custom SwingWorker object that deals with real-time play back of video in media player
	//does the rewinding, playing and fast forwarding
	private PlaybackWorker playbackWorker;
	
	//object that allows editing of a .srt subtitles file
	private SubtitlesEditor subtitlesEditor;
	
	//holds the file path of the .srt file being edited
	private String subtitleFilePath;
	
	/*
	 * Method which contains all controller logic for the VAMIX GUI.
	 * It firstly checks that all GUI components from VideoView.fxml have been correctly injected and then
	 * runs all the implementation behind them.
	 */
	@FXML
	void initialize() {
		loadMedia();
		mainPanesCheck();
		videoTabCheck();
		audioTabCheck();
		renderTabCheck();
		subtitlesTabCheck();
		playerCheck();
		menuCheck();
		setToolTips();
		menuActions();
		videoTab();
		audioTab();
		renderTab();
		subtitlesTab();
		player();
		if (!(videoFileAdd.equals(""))){
			videoPath.setText(videoFileAdd);
		}
	}
	
	/*
	 * Method that checks if all of the video tab GUI components have been injected correctly from the VideoView.fxml file
	 */
	private void videoTabCheck(){
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

		assert creditText != null : "fx:id=\"creditText\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert creditsXPos != null : "fx:id=\"creditsXPos\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert creditsYPos != null : "fx:id=\"creditsYPos\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert creditsColour != null : "fx:id=\"creditsColour\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert durationCredits != null : "fx:id=\"durationCredits\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert creditsSize != null : "fx:id=\"creditsSize\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert startCredits != null : "fx:id=\"startCredits\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert endCredits != null : "fx:id=\"endCredits\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert creditsFont != null : "fx:id=\"creditsFont\" was not injected: check your FXML file 'VideoView.fxml'.";
		
		assert rotateAngle != null : "fx:id=\"rotateAngle\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert rotateBtn != null : "fx:id=\"rotateBtn\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert startFadeIn != null : "fx:id=\"startFadeIn\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert endFadeIn != null : "fx:id=\"endFadeIn\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert startFadeOut != null : "fx:id=\"startFadeOut\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert endFadeOut != null : "fx:id=\"endFadeOut\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert includeFadeIn != null : "fx:id=\"includeFadeIn\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert includeFadeOut != null : "fx:id=\"includeFadeOut\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert fadeBtn != null : "fx:id=\"fadeBtn\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert startTrim != null : "fx:id=\"startTrim\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert endTrim != null : "fx:id=\"endTrim\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert trimBtn != null : "fx:id=\"trimBtn\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert cropXPos != null : "fx:id=\"cropXPos\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert cropYPos != null : "fx:id=\"cropYPos\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert cropHeight != null : "fx:id=\"cropHeight\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert cropWidth != null : "fx:id=\"cropWidth\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert cropBtn != null : "fx:id=\"cropBtn\" was not injected: check your FXML file 'VideoView.fxml'.";		
	}
	
	/*
	 * Method that contains for the video tab, all user input checking logic, GUI event handling logic
	 * and delegating method calls to worker classes for all long-running tasks.
	 */
	private void videoTab(){
		
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
		titleSize.setValue("20");
		creditsSize.setValue("20");
		
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
				//send url to download object and invoke download function
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
				//check that the user hasn't selected the file currently being played
				if (!(previousFile.equals(videoPath.getText()))&Helper.validInFile(videoPath.getText(),"(video)|Media|Audio|MPEG|ISO Media|ogg|ogv")){
					vamix.view.Main.vid.prepareMedia(videoPath.getText());
					videoFileAdd=videoPath.getText();
				}else if (previousFile.equals(videoPath.getText())){
					JOptionPane.showMessageDialog(null,"You have selected the file that is currently loaded.");
				}
			}
		});
		
		//same functionality as above button but it does not open a file dialog, it attempts to use the 
		//file path in the text field next to itself to load a video
		loadBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evt) {
				//play media if its different from the current one
				String previousFile =videoFileAdd;//get current file
				if (!(previousFile.equals(videoPath.getText()))&Helper.validInFile(videoPath.getText(),"(video)|Media|Audio|MPEG|ISO Media|ogg|ogv")){
					vamix.view.Main.vid.prepareMedia(videoPath.getText());
					videoFileAdd=videoPath.getText();
				}else if (previousFile.equals(videoPath.getText())){
					JOptionPane.showMessageDialog(null,"You have selected the file that is currently loaded.");
				}
			}
		});
		
		//if the text field where video path is entered is double clicked then do the same action as if the browse button was clicked
		videoPath.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				if ((arg0.getClickCount()>=2)&& !arg0.isConsumed()){
					//load media
					String previousFile =videoFileAdd;//get current file
					loadMedia();
					//check that the user hasn't selected the file currently being played
					if (!(previousFile.equals(videoPath.getText()))&Helper.validInFile(videoPath.getText(),"(Audio)|Video|MPEG")){
						vamix.view.Main.vid.prepareMedia(videoPath.getText());
						videoFileAdd=videoPath.getText();
					}else if (previousFile.equals(videoPath.getText())){
						JOptionPane.showMessageDialog(null,"You have selected the file that is currently loaded.");
					}
				}
			}
		});
		
		//the functionality for the set title text button
		setTitle.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evt) {
				if (!(videoFileAdd.equals(""))) {
					//first check if the inputs entered by user for this function are valid, if they are then continue
					if (checkTitleInputs()) {
						//difficult to show progress for this process so instead notify user with a 3 second pop-up message
						JOptionPane pane = new JOptionPane("Please wait while VAMIX loads your preview");
						final JDialog dialog = pane.createDialog("Please wait");
						Timer timer = new Timer(3000, new ActionListener() {
							@Override
							public void actionPerformed(java.awt.event.ActionEvent e) {
								dialog.dispose();							
							}
						});
						timer.setRepeats(false);
						timer.start();
						dialog.setVisible(true);
						//use custom SwingWorker class to create the preview
						TextEdit textEditor = new TextEdit(titleText.getText(), titleFont.getValue(), titleSize.getValue(), titleColour.getValue().toString(),
								startTitle.getText(), endTitle.getText(), titleXPos.getText(), titleYPos.getText(), null, null,
								null, null, null, null, null, null, videoFileAdd, null, "title");
						textEditor.showScenePreviewAsync();
					}
				//otherwise if no video is present, give an error
				} else {
					JOptionPane.showMessageDialog(null, "Please load a video into VAMIX first", "Missing input", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		//the functionality for the set credits text button
		setCredits.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evt) {
				if (!(videoFileAdd.equals(""))) {
					//first check if the inputs entered by user for this function are valid, if they are then continue
					if (checkCreditsInputs()) {
						//difficult to show progress for this process so instead notify user with a 3 second pop-up message
						JOptionPane pane = new JOptionPane("Please wait while VAMIX loads your preview");
						final JDialog dialog = pane.createDialog("Please wait");
						Timer timer = new Timer(3000, new ActionListener() {
							@Override
							public void actionPerformed(java.awt.event.ActionEvent e) {
								dialog.dispose();							
							}
						});
						timer.setRepeats(false);
						timer.start();
						dialog.setVisible(true);
						//use custom SwingWorker class to create the preview
						TextEdit textEditor = new TextEdit(null, null, null, null, null, null, null, null,
								creditText.getText(), creditsFont.getValue(), creditsSize.getValue(), creditsColour.getValue().toString(), 
								startCredits.getText(), endCredits.getText(), creditsXPos.getText(), creditsYPos.getText(), 
								videoFileAdd, null, "credits");
						textEditor.showScenePreviewAsync();
					}
				//otherwise if no video is present, give an error
				} else {
					JOptionPane.showMessageDialog(null, "Please load a video into VAMIX first", "Missing input", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		//the functionality for the fade video button
		fadeBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evt) {
				//check if the user wants to only fade in, only fade out or both
				if (includeFadeIn.isSelected() && includeFadeOut.isSelected()) {
					//if they want both, check the inputs for both fade in and fade out
					if (checkFadeInputs(true) && checkFadeInputs(false)) {
						//check the fade in and fade out times do not overlap
						int endFadein = Helper.timeInSec(endFadeIn.getText());
						int startFadeout = Helper.timeInSec(startFadeOut.getText());
						if (endFadein <= startFadeout) {
							//use custom SwingWorker object to fade the video
							FadeVideo fader = new FadeVideo(startFadeIn.getText(), endFadeIn.getText(), startFadeOut.getText(), endFadeOut.getText(), videoFileAdd);
							fader.fadeVideoAsync(FadeType.BOTH);
						} else {
							JOptionPane.showMessageDialog(null, "The two fade sequences cannot overlap.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
						}
					}
				//else if the user only wants to fade in
				} else if (includeFadeIn.isSelected()) {
					if (checkFadeInputs(true)) {
						FadeVideo fader = new FadeVideo(startFadeIn.getText(), endFadeIn.getText(), null, null, videoFileAdd);
						fader.fadeVideoAsync(FadeType.IN);
					}
				//else if the user only wants to fade out
				} else if (includeFadeOut.isSelected()) {
					if (checkFadeInputs(false)) {
						FadeVideo fader = new FadeVideo(null, null, startFadeOut.getText(), endFadeOut.getText(), videoFileAdd);
						fader.fadeVideoAsync(FadeType.OUT);
					}
				//otherwise if nothing has been selected, give error
				} else {
					JOptionPane.showMessageDialog(null, "Please select to a fade option", "Missing input", JOptionPane.ERROR_MESSAGE);
				}
			}			
		});
		
		//the functionality for the trim video button
		trimBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evt) {
				//check a video is actually loaded into VAMIX
				if (!(videoFileAdd.equals(""))) {
					//check if the input video file is a valid video file (is either mp4, avi or flv)
					if(Helper.validVideoFile(videoFileAdd,"(MPEG v4)|Video")){
						//check that the start time is valid
						if (Helper.timeValidTypeChecker(startTrim.getText(), "start time")) {
							//check that the end time is valid
							if (Helper.timeValidTypeChecker(endTrim.getText(), "end time")) {
								//check that the start time is valid relative to the end time
								if (Helper.timeValidChecker(startTrim.getText(), endTrim.getText(), "trimming")) {
									//check that both times are less than or equal to length of the input video
									if ((Helper.timeLessThanVideo(startTrim.getText()) && (Helper.timeLessThanVideo(endTrim.getText())))) {
										//use a custom SwingWorker class to trim the video in the background
										TrimVideo trimmer = new TrimVideo(startTrim.getText(), endTrim.getText(), videoFileAdd);
										trimmer.trimVideoAsync();
									}
								}
							}
						}
					}
				} else {
					JOptionPane.showMessageDialog(null, "Please load a video into VAMIX first", "Missing input", JOptionPane.ERROR_MESSAGE);
				}
			}			
		});
		
		//the functionality for the crop video button
		cropBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evt) {
				boolean allFull = true;
				//check a video is actually loaded into VAMIX
				if (!(videoFileAdd.equals(""))) {
					//check if the input video file is a valid video file (is either mp4, avi or flv)
					if(Helper.validVideoFile(videoFileAdd,"(MPEG v4)|Video")){
						//check that all fields have a value
						if ((cropHeight.getText().equals("")) || (cropWidth.getText().equals("")) || (cropXPos.getText().equals("")) || (cropYPos.getText().equals(""))) {
							allFull = false;
						}
						if (allFull) {
							//now get the dimensions of the video and make sure that the width, height, x and y values
							//specified by the user lie within the dimensions
							int width = vamix.view.Main.vid.getVideoDimension().width;
							int height = vamix.view.Main.vid.getVideoDimension().height;
							int cropheight = Integer.parseInt(cropHeight.getText());
							int cropwidth = Integer.parseInt(cropWidth.getText());
							int x = Integer.parseInt(cropXPos.getText());
							int y = Integer.parseInt(cropYPos.getText());
							//if the height and width specified by user is within video dimensions, continue
							if ((cropheight <= height) && (cropwidth <= width)) {
								//now check that the position (x,y) specified, in combination with height and width, does not exceed video dimensions
								if (((cropheight + y) <= height) && ((cropwidth + x) <= width)) {
									//if it doesn't, continue with the crop operation
									CropVideo cropper = new CropVideo(cropWidth.getText(), cropHeight.getText(), cropXPos.getText(), cropYPos.getText(), videoFileAdd);
									cropper.cropVideoAsync();
								} else {
									JOptionPane.showMessageDialog(null, "The (x,y) position specified in combination with the height and width specified "
											+ "exceeds the video dimensions. Please change these values to comply.", "Invalid input", JOptionPane.ERROR_MESSAGE);
								}
							} else {
								JOptionPane.showMessageDialog(null, "Height and width specified must be within video dimensions\n"
										+ "Dimensions are h = " + String.valueOf(height) + ", w = " + String.valueOf(width), "Invalid input", JOptionPane.ERROR_MESSAGE);
							}
						} else {
							JOptionPane.showMessageDialog(null, "Atleast one field has no input.", "Missing input", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			}
		});
		
		
		//the functionality for the rotate video button
		rotateBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evt) {
				//check a video is actually loaded into VAMIX
				if (!(videoFileAdd.equals(""))) {
					//check if the input video file is a valid video file (is either mp4, avi or flv)
					if(Helper.validVideoFile(videoFileAdd,"(MPEG v4)|Video")){
						//check that the angle given by the user is either 90, 180 or 270 degrees
						String angle = rotateAngle.getText();
						if (!(angle.equals("90") || angle.equals("180") || angle.equals("270"))) {
							JOptionPane.showMessageDialog(null, "The angle to rotate must be either 90, 180 or 270 degrees.", "Invalid input", JOptionPane.ERROR_MESSAGE);
						} else {
							RotateVideo rotateVideo = new RotateVideo(angle, videoFileAdd);
							rotateVideo.rotateVideoAsync();
						}
					}
				}
			}
		});
	}

	/*
	 * Method that checks if all of the audio tab GUI components have been injected correctly from the VideoView.fxml file
	 */
	private void audioTabCheck(){
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

	/*
	 * Method that contains for the audio tab, GUI event handling logic
	 * and delegating method calls to worker classes for all long-running tasks.
	 */
	private void audioTab(){
		
		//strip audio function button
		strip_button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evt) {
				//send input file and output file destination to custom SwingWorker object then invoke background function
				ExtractAudio stripAudio =new ExtractAudio(videoFileAdd,strip_add.getText());
				stripAudio.stripFunction();
			}
		});
		
		//strip audio add when user double clicks this text field a file browser comes up
		strip_add.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				if ((arg0.getClickCount()>=2)&& !arg0.isConsumed()){
					//get the destination of file to be saved
					String temp=Helper.saveFileChooser("MP3 file","mp3");
					strip_add.setText(temp);
				}
			}
		});
		
		//save file chooser when button clicked
		save_file_chooser.setOnMouseClicked(new EventHandler<MouseEvent>() {
		@Override
		public void handle(MouseEvent arg0) {
			//get the destination of file to be saved
			String temp=Helper.saveFileChooser("MP3 file","mp3");
			strip_add.setText(temp);
		}
	});
		
		//replace audio function button
		replaceButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evt) {
				//send input file and output destination to custom SwingWorker object then invoke background function
				ReplaceAudio r=new ReplaceAudio(videoFileAdd,replaceAdd.getText(),startReplace.getText(),endReplace.getText(),
						startReplace2.getText(),endReplace2.getText());
				r.replaceAudioFunction();
			}
		});
		
		//replace file chooser button
		chooseAudioButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evt) {
				//open file dialog to let user choose a replacement audio file
				String temp=Helper.audioFileChooser();
				replaceAdd.setText(temp);
			}
		});
		
		//when the user double clicks this text field a file browser will show up
		replaceAdd.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				if ((arg0.getClickCount()>=2)&& !arg0.isConsumed()){
					//open file dialog to let user choose a replacement audio file
					String temp=Helper.audioFileChooser();
					replaceAdd.setText(temp);
				}
			}
		});
		
		//when the user double clicks this text field a file browser will show up
		overlayAdd.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				if ((arg0.getClickCount()>=2)&& !arg0.isConsumed()){
					//open file dialog to let user choose an audio file to overlay onto the current video
					String temp=Helper.audioFileChooser();
					overlayAdd.setText(temp);
				}
			}
		});
		
		//overlay file chooser button
		chooseOverlayBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				//open file dialog to let user choose an audio file to overlay onto the current video
				String temp=Helper.audioFileChooser();
				overlayAdd.setText(temp);
			}
		});
		
		//when the overlay audio button is clicked, attempt to overlay chosen audio onto video
		overlayAudioBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evt) {
				//send input file and output destination to custom SwingWorker object then invoke function for overlay
				OverlayAudio o=new OverlayAudio(videoFileAdd,overlayAdd.getText(),overlayUseStart.getText(),overlayUseEnd.getText(),overlayToStart.getText(),overlayToEnd.getText());
				o.overlayAudioFunction();
			}
		});
	}

	/*
	 * Method that contains for the render tab, all user input checking logic, GUI event handling logic
	 * and delegating method calls to worker classes for all long-running tasks.
	 */
	private void renderTab(){
		
		//When the render button is clicked, check all user inputs before passing required data to custom SwingWorker object which will do the rendering
		renderWithAudioBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evt) {
				TextEdit textRenderer = null;
				String overwrite = "";
				int choice = -1;
				boolean cancelled = false;
				if (!(videoFileAdd.equals(""))) {
					//check if the input video file is a valid video file (is either mp4, avi or flv)
					if(Helper.validVideoFile(videoFileAdd,"(MPEG v4)|Video")){
						//check the output file DIRECTORY exists
						String outDir = Helper.pathGetter(outputFilePath.getText());
						String outFileName = Helper.fileNameGetter(outputFilePath.getText());
						if (Helper.fileExist(outDir)) {
							//if the directory does exist, check if output file exists, if it does ask user if they want to overwrite
							if (Helper.fileExist(outputFilePath.getText())) {
								Object[] options = {"Overwrite", "Cancel"};
								choice = JOptionPane.showOptionDialog(null, "File " + outFileName +" already exists. Do you wish to overwrite it or cancel and choose another destination?",
										"Override?",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,options, options[0]);
								if (choice == 0) {
									//space MUST come after 'y' or concat bash command in rendering will fail
									overwrite = "-y ";
								} else {
									cancelled = true;
								}
							}
							if (!cancelled) {
								//check which text scenes the user wants to render with the video
								if (includeTitle.isSelected() && includeCredits.isSelected()) {
									if (checkTitleInputs() && checkCreditsInputs()) {
										//if the user wants to render both text scenes make sure the credits scene comes after the title scene and 
										//no overlap occurs
										int endOfTitle = Helper.timeInSec(endTitle.getText());
										int startOfCredits = Helper.timeInSec(startCredits.getText());
										if (endOfTitle < startOfCredits) {
											//pass all required data to the renderer object so rendering can be done in the background
											textRenderer = new TextEdit(titleText.getText(), titleFont.getValue(), titleSize.getValue(), titleColour.getValue().toString(),
													startTitle.getText(), endTitle.getText(), titleXPos.getText(), titleYPos.getText(), creditText.getText(), 
													creditsFont.getValue(), creditsSize.getValue(), creditsColour.getValue().toString(), startCredits.getText(), endCredits.getText(),
													creditsXPos.getText(), creditsYPos.getText(), videoFileAdd, outputFilePath.getText(), null);
											textRenderer.renderWithTextAsync(RenderType.BOTH, overwrite);
										} else {
											JOptionPane.showMessageDialog(null, "The credits scene must start after the title scene has finished", "Overlapping scenes",
													JOptionPane.ERROR_MESSAGE);
										}
									}
								//if the user only wants to save the title text scene
								} else if (includeTitle.isSelected()) {
									//check only the title text inputs
									if (checkTitleInputs()) {
										//pass all required data to the renderer object so rendering can be done in the background
										textRenderer = new TextEdit(titleText.getText(), titleFont.getValue(), titleSize.getValue(), titleColour.getValue().toString(),
												startTitle.getText(), endTitle.getText(), titleXPos.getText(), titleYPos.getText(), null, null, null, null, null, null,
												null, null, videoFileAdd, outputFilePath.getText(), null);
										textRenderer.renderWithTextAsync(RenderType.OPENING, overwrite);
									}
								//if the user only wants to save the credits text scene
								} else if (includeCredits.isSelected()) {
									//check only the credits text inputs
									if (checkCreditsInputs()) {
										//pass all required data to the renderer object so rendering can be done in the background
										textRenderer = new TextEdit(null, null, null, null, null, null, null, null, creditText.getText(), 
												creditsFont.getValue(), creditsSize.getValue(), creditsColour.getValue().toString(), startCredits.getText(), endCredits.getText(),
												creditsXPos.getText(), creditsYPos.getText(), videoFileAdd, outputFilePath.getText(), null);
										textRenderer.renderWithTextAsync(RenderType.CLOSING, overwrite);
									}
								//otherwise they have not selected any text scenes to display an error
								} else {
									JOptionPane.showMessageDialog(null, "Please select a text scene(s) to render with the video", "Select Text Scene",
											JOptionPane.ERROR_MESSAGE);
								}
							}
						} else {
							JOptionPane.showMessageDialog(null, "The output directory specified does not exist", "Invalid output location",
									JOptionPane.ERROR_MESSAGE);
						}
					}
				} else {
					JOptionPane.showMessageDialog(null, "Please load a video into VAMIX first", "Missing input", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		//if the rendered destination path text field is clicked twice, open a file browser
		outputFilePath.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				if ((arg0.getClickCount()>=2)&& !arg0.isConsumed()){
					//choose the output file 
					String temp=Helper.saveFileChooser("MP4 files Only", "mp4");
					outputFilePath.setText(temp);
				}
			}
		});
		
		//if the save file button is clicked open a file browser
		saveToBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				//choose the output file
				String temp=Helper.saveFileChooser("MP4 files Only", "mp4");
				outputFilePath.setText(temp);
			}
		});
	}

	/*
	 * Method that checks if all of the render tab GUI components have been injected correctly from the VideoView.fxml file
	 */
	private void renderTabCheck() {
		assert outputFilePath != null : "fx:id=\"outputFilePath\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert saveToBtn != null : "fx:id=\"saveToBtn\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert renderWithAudioBtn != null : "fx:id=\"renderWithAudioBtn\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert includeTitle != null : "fx:id=\"includeTitle\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert includeCredits != null : "fx:id=\"includeCredits\" was not injected: check your FXML file 'VideoView.fxml'.";
	}
	
	/*
	 * Method that checks if all of the subtitles tab GUI components have been injected correctly from the VideoView.fxml file
	 */
	private void subtitlesTabCheck() {
		assert newSubtitleFileBtn != null : "fx:id=\"newSubtitleFileBtn\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert editSubtitleFileBtn != null : "fx:id=\"editSubtitleFileBtn\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert saveSubtitleFileBtn != null : "fx:id=\"saveSubtitleFileBtn\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert subtitleTable != null : "fx:id=\"subtitleTable\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert subtitleNumber != null : "fx:id=\"subtitleNumber\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert subtitleStart != null : "fx:id=\"subtitleStart\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert subtitleEnd != null : "fx:id=\"subtitleEnd\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert subtitleText != null : "fx:id=\"subtitleText\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert addSubtitleBtn != null : "fx:id=\"addSubtitleBtn\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert deleteSubtitleBtn != null : "fx:id=\"deleteSubtitleBtn\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert subtitleToAdd != null : "fx:id=\"subtitleToAdd\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert subtitleToDelete != null : "fx:id=\"subtitleToDelete\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert subtitlePathLabel != null : "fx:id=\"subtitlePathLabel\" was not injected: check your FXML file 'VideoView.fxml'.";
	}
	
	/*
	 * Method that contains for the subtitles tab, all user input checking logic, GUI event handling logic
	 * and delegating method calls to worker classes for main tasks.
	 */
	private void subtitlesTab() {
		
		//when the create a new subtitle button is clicked, open a file browser
		newSubtitleFileBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				//open file browser to let user select a subtitle file for editing
				subtitleFilePath = FileBrowsers.saveFileBrowser("Subtitle files", "srt");
				//check the output file DIRECTORY exists
				String outDir = Helper.pathGetter(subtitleFilePath);
				if (Helper.fileExist(outDir)) {
					//create a subtitle editor
					subtitlesEditor = new SubtitlesEditor(subtitleTable, subtitleNumber, subtitleStart, subtitleEnd, subtitleText);
					subtitlesEditor.createSubtitlesFile(subtitleFilePath);
					
					//enable all buttons once a valid file has been chosen
					saveSubtitleFileBtn.setDisable(false);
					addSubtitleBtn.setDisable(false);
					deleteSubtitleBtn.setDisable(false);
					
					//update 'editing' label with file path of new file
					subtitlePathLabel.setText(subtitleFilePath);
				//if it doesn't exist, give error to user
				} else {
					JOptionPane.showMessageDialog(null, "File path entered does not exist.", "Invalid file path",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		//open a file browser and allow user to select a subtitle file to edit
		editSubtitleFileBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				//open file browser to let user select a subtitle file for editing
				subtitleFilePath = FileBrowsers.subtitleFileBrowser();
				//check if the file chosen exists
				if (Helper.fileExist(subtitleFilePath)) {
					//create a subtitle editor
					subtitlesEditor = new SubtitlesEditor(subtitleTable, subtitleNumber, subtitleStart, subtitleEnd, subtitleText);
					subtitlesEditor.readSubtitlesFile(subtitleFilePath);
					
					//enable all buttons once a valid file has been chosen
					saveSubtitleFileBtn.setDisable(false);
					addSubtitleBtn.setDisable(false);
					deleteSubtitleBtn.setDisable(false);
					
					//update 'editing' label with file path of new file
					subtitlePathLabel.setText(subtitleFilePath);
				//if it doesn't exist, give an error
				} else {
					JOptionPane.showMessageDialog(null, "Please enter a valid subtitle file", "Invalid file path",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		//add a subtitle 'row' to the table to let user edit
		addSubtitleBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				//check user input is actually a number
				if (subtitleToAdd.getText().matches("\\d+")) {
					//tell subtitle editor which subtitle user wants to add
					int successful = subtitlesEditor.addSubtitle(Integer.parseInt(subtitleToAdd.getText()));
					if (successful == -1) {
						JOptionPane.showMessageDialog(null, "You have entered a subtitle number that is invalid. \nIt cannot be less than 1 or more than 1 greater than "
								+ "the last subtitle number on the list", "Invalid subtitle number",
								JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(null, "Please enter a subtitle number. It must be greater than zero and at most, 1 more than "
							+ "the last subtitle number in the list", "Invalid input",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		//delete the specified subtitle row from the table
		deleteSubtitleBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				//check user input is actually a number
				if (subtitleToDelete.getText().matches("\\d+")) {
					//tell subtitle editor which subtitle user wants to delete
					int successful = subtitlesEditor.deleteSubtitle(Integer.parseInt(subtitleToDelete.getText()));
					if (successful == -1) {
						JOptionPane.showMessageDialog(null, "You have entered a subtitle number that is not on the list", "Invalid subtitle number",
								JOptionPane.ERROR_MESSAGE);
					}
				} else {
					JOptionPane.showMessageDialog(null, "Please enter a subtitle number. It must a subtitle number from the list", "Invalid input",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		//save changes to the subtitle file
		saveSubtitleFileBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				//tell subtitle editor to save changes made to .srt file being edited
				subtitlesEditor.saveSubtitles(subtitleFilePath);
				//give message to user telling them their changes have been saved
				JOptionPane.showMessageDialog(null, "Your changes have been saved", "Saved",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});
	}
	
	/*
	 * Method that checks if all of the media player GUI components have been injected correctly from the VideoView.fxml file
	 */
	private void playerCheck(){
		/*
		 * Section for the media player id checks
		 */
		assert playPauseBtn != null : "fx:id=\"playPauseBtn\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert playPauseImage != null : "fx:id=\"playPauseImage\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert rewindBtn != null : "fx:id=\"rewindBtn\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert fastForwardBtn != null : "fx:id=\"fastForwardBtn\" was not injected: check your FXML file 'VideoView.fxml'.";

		assert videoTime != null : "fx:id=\"videoTime\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert videoProgress != null : "fx:id=\"videoProgress\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert volumeSlider != null : "fx:id=\"volumeSlider\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert muteCheckbox != null : "fx:id=\"muteCheckbox\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert subtitlesPlaying != null : "fx:id=\"subtitlesPlaying\" was not injected: check your FXML file 'VideoView.fxml'.";

	}

	/*
	 * Method that contains for the media player, all user input checking logic, GUI event handling logic
	 * and delegating method calls to worker classes for all long-running tasks. This includes rewinding, playing,
	 * pausing, fast forwarding, muting, volume control and updating the video progress bar and video time
	 */
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
				        	if (currentTime>=VidTime){//when reach end of video/audio loop to the start again
				        		vamix.view.Main.vid.playMedia(videoFileAdd); 
				        		if (!(vamix.view.Main.vid.getMediaPlayerState()==libvlc_state_t.libvlc_Playing)){
				        			Image pauseImg = new Image(getClass().getResourceAsStream("/resources/pause.png"));
									playPauseImage.setImage(pauseImg);
				        		}
				        		String videTime= Helper.timeOfVideo(currentTime,VidTime);
				        		videoTime.setText(videTime);
				        	}else{ 
				        		String videTime= Helper.timeOfVideo(currentTime,VidTime);
				        		videoTime.setText(videTime);
				        	}
			        	}else if(vamix.view.Main.vid.getMediaPlayerState()==libvlc_state_t.libvlc_NothingSpecial){
			        		//create event listener to see if new media is load then load it
			        		vamix.view.Main.vid.addMediaPlayerEventListener(new MediaPlayerEventAdapter(){
			        			@Override
			        			public void newMedia(MediaPlayer arg0){
    			        			String temp=vamix.view.Main.vid.mrl().substring(7);
    			        			videoFileAdd=temp;
    			        			videoPath.setText(temp);
			        			}
			        		});
			        		
			        		//if a video has been selected
			        		if (!(videoFileAdd.equals(""))){   			
				        		vamix.view.Main.vid.start();
				        		try {//sleep thread so can execute next command when previous finish
				        			Thread.sleep(50);
				        		} catch (InterruptedException e1) {
				        		}
				        		vamix.view.Main.vid.pause();
				        		Image playImg = new Image(getClass().getResourceAsStream("/resources/play.png"));
								playPauseImage.setImage(playImg);
				        		videoProgress.setProgress(0.0);
				        		if (vamix.view.Main.vid.isMute()){
				        			vamix.view.Main.vid.mute();
				        		}				        	
				        		if (!(videoFileAdd.equals(videoPath.getText()))){
				        			videoPath.setText(videoFileAdd);
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
		
		//play or pause the video when the play/pause button is pressed
		videoTimer.start();
		playPauseBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evt) {//before video even got played
				if(vamix.view.Main.vid.getMediaPlayerState()==libvlc_state_t.libvlc_Ended){
					vamix.view.Main.vid.startMedia(videoFileAdd);
				}else if (vamix.view.Main.vid.getMediaPlayerState()==libvlc_state_t.libvlc_Playing){
					//if playing, then pause the video
					vamix.view.Main.vid.pause();
					Image playImg = new Image(getClass().getResourceAsStream("/resources/play.png"));
					playPauseImage.setImage(playImg);
				}else if (vamix.view.Main.vid.getMediaPlayerState()==libvlc_state_t.libvlc_Paused){
					//if paused, then play the video
					vamix.view.Main.vid.pause();
					Image pauseImg = new Image(getClass().getResourceAsStream("/resources/pause.png"));
					playPauseImage.setImage(pauseImg);
				}
			}
		});
		
		//if the mute checkbox is ticked/unticked, toggle mute on the video
		muteCheckbox.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
			//toggle mute of the video
				if (!(vamix.view.Main.vid.getMediaPlayerState()==libvlc_state_t.libvlc_NothingSpecial)){
					vamix.view.Main.vid.mute();
				}				
			}

		});
		
		///fast forward when mouse pressed using a custom SwingWorker class as it can continuously skip forward
		fastForwardBtn.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				
				playbackWorker=new PlaybackWorker((long)(vamix.view.Main.vid.getLength()*0.01));
				playbackWorker.execute();
				//cancel rewind when mouse leave the button even if the user didn't release
				fastForwardBtn.setOnMouseExited(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent arg0) {
						playbackWorker.cancel(true);
					}
				});
			}
		});
		
		//cancel fast forward when mouse released
		fastForwardBtn.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				playbackWorker.cancel(true);
			}
		});
		
		//rewind when mouse pressed using a custom SwingWorker class as it can continuously skip
		rewindBtn.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				playbackWorker=new PlaybackWorker(-(long)(vamix.view.Main.vid.getLength()*0.01));
				playbackWorker.execute();
				//cancel rewind when mouse leave the button even if the user didn't release
				rewindBtn.setOnMouseExited(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent arg0) {
						playbackWorker.cancel(true);
					}
				});
			}
		});
		
		//cancel rewind when mouse released
		rewindBtn.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				playbackWorker.cancel(true);
			}
		});
		
		//update video volume when the volume slider is dragged
		volumeSlider.setOnMouseDragged(new EventHandler<MouseEvent>() {
			//volume slider set volume continuously when slide
			@Override
			public void handle(MouseEvent arg0) {
				vamix.view.Main.vid.setVolume((int) (2*volumeSlider.getValue()));
			}
		});
		
		//update video volume when the volume slider is clicked
		volumeSlider.setOnMouseClicked(new EventHandler<MouseEvent>() {
			//volume slider set volume continuously when click
			//note volume is 0-200 so need to times 2 as slider is only to 100
			@Override
			public void handle(MouseEvent arg0) {
				vamix.view.Main.vid.setVolume((int) (2*volumeSlider.getValue()));
			}
		});
		
		//update the video track time when the video progress bar is dragged
		videoProgress.setOnMouseDragged(new EventHandler<MouseEvent>() {
			//video slider set volume continuously when slide
			//setTime is in (ms) of time, get width is in the pixel unit
			@Override
			public void handle(MouseEvent arg0) {
				vamix.view.Main.vid.setTime((long)arg0.getX()*vamix.view.Main.vid.getLength()/(long)videoProgress.getWidth());
			}
		});
		
		//update the video track time when the video progress bar is clicked
		videoProgress.setOnMouseClicked(new EventHandler<MouseEvent>() {
			//video slider set volume discrete when slide
			@Override
			public void handle(MouseEvent arg0) {
				vamix.view.Main.vid.setTime((long)(arg0.getX()*vamix.view.Main.vid.getLength()/(long)videoProgress.getWidth()));
			}
		});
	}
	
	/*
	 * Method that sets the tool tips for all GUI objects in VAMIX that require clarification for the user.
	 */
	private void setToolTips() {
		
		//set the tool tip for the buttons which the user clicks to open the file browser
		Tooltip openTip = new Tooltip("Click to open file browser");
		browseBtn.setTooltip(openTip);
		saveToBtn.setTooltip(openTip);
		save_file_chooser.setTooltip(openTip);
		chooseAudioButton.setTooltip(openTip);
		chooseOverlayBtn.setTooltip(openTip);
				
		//tool tip for entering download URL
		Tooltip urlTip = new Tooltip("Enter URL to download from");
		videoURL.setTooltip(urlTip);
		
		//tool tip for (x,y) text position fields
		Tooltip xPosTip = new Tooltip("Enter the x coordinate for positioning the text on the video");
		titleXPos.setTooltip(xPosTip);
		creditsXPos.setTooltip(xPosTip);
		Tooltip yPosTip = new Tooltip("Enter the y coordinate for positioning the text on the video");
		titleYPos.setTooltip(yPosTip);
		creditsYPos.setTooltip(yPosTip);
		
		//tool tips to clarify to user what time input is required for text editing (those fields which require time in hh:mm:ss)
		Tooltip startTextTip = new Tooltip("Enter time on video to begin showing text at");
		startTitle.setTooltip(startTextTip);
		startCredits.setTooltip(startTextTip);
		Tooltip endTextTip = new Tooltip("Enter time on video to stop showing text at");
		endTitle.setTooltip(endTextTip);
		endCredits.setTooltip(endTextTip);
		//set the tool tip for the field where user enters the angle to rotate the video by
		Tooltip rotateTip = new Tooltip("Valid angles are 90, 180 and 270 degrees");
		rotateAngle.setTooltip(rotateTip);
		
		//tool tips to clarify to user what time inputs are required for fading video
		Tooltip startFadeTip = new Tooltip("Enter time on video to begin fading");
		startFadeIn.setTooltip(startFadeTip);
		startFadeOut.setTooltip(startFadeTip);
		Tooltip endFadeTip = new Tooltip("Enter time on video to end fading");
		endFadeIn.setTooltip(endFadeTip);
		endFadeOut.setTooltip(endFadeTip);
		
		//tool tips to clarify to user what time inputs are required for trimming video
		Tooltip startTrimTip = new Tooltip("Enter time on video to start trimming at");
		startTrim.setTooltip(startTrimTip);
		Tooltip endTrimTip = new Tooltip("Enter time on video to end trimming at");
		endTrim.setTooltip(endTrimTip);
		
		//tool tips to clarify to user what inputs are required for cropping a video
		Tooltip xCropTip = new Tooltip("Specify the x coordinate of where to start cropping\n(top left hand corner of output video)");
		cropXPos.setTooltip(xCropTip);
		Tooltip yCropTip = new Tooltip("Specify the y coordinate of where to start cropping\n(top left hand corner of output video)");
		cropYPos.setTooltip(yCropTip);
		Tooltip cropHeightTip = new Tooltip("Enter the height to crop from the (x,y) position specified");
		cropHeight.setTooltip(cropHeightTip);
		Tooltip cropWidthTip = new Tooltip("Enter the width to crop from the (x,y) position specified");
		cropWidth.setTooltip(cropWidthTip);
		
		
	}

	/*
	 * Method that checks if all of the main container GUI components have been injected correctly from the VideoView.fxml file
	 */
	private void mainPanesCheck(){
		assert tabMenu != null : "fx:id=\"tabMenu\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert tabPane != null : "fx:id=\"tabPane\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert videoTab != null : "fx:id=\"videoTab\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert renderTab != null : "fx:id=\"renderTab\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert audioTab != null : "fx:id=\"audioTab\" was not injected: check your FXML file 'VideoView.fxml'.";
	}
	
	/*
	 * Method that checks if all of the menu bar GUI components have been injected correctly from the VideoView.fxml file
	 */
	private void menuCheck() {
		assert saveStateBtn != null : "fx:id=\"saveStateBtn\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert loadStateBtn != null : "fx:id=\"loadStateBtn\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert loadFiles != null : "fx:id=\"loadFiles\" was not injected: check your FXML file 'VideoView.fxml'.";
		assert playWithSubtitlesBtn != null : "fx:id=\"playWithSubtitlesBtn\" was not injected: check your FXML file 'VideoView.fxml'.";
	}
	
	/*
	 * Method that contains for the menu bar, all user input checking logic, GUI event handling logic
	 * and delegating method calls to worker classes for all long-running tasks.
	 */
	private void menuActions() {
		//action for the save menu item, will save the state of vamix to file
		saveStateBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evt) {
				saveState();
			}
		});
		
		//action for the load menu item, will load the state of vamix from file
		loadStateBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evt) {
				loadState();
			}
		});
		
		//when load file menu item clicked
		loadFiles.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evt) {
				//load video function button when click browse
				//load media
				String previousFile =videoFileAdd;//get current file
				loadMedia();
				//check that the file selected is not the same one that is currently playing
				if (!(previousFile.equals(videoPath.getText()))&Helper.validInFile(videoPath.getText(),"(video)|Media|Audio|MPEG|ISO Media|ogg|ogv")){
					vamix.view.Main.vid.prepareMedia(videoPath.getText());
					videoFileAdd=videoPath.getText();
				}else if (previousFile.equals(videoPath.getText())){
					JOptionPane.showMessageDialog(null,"You have entered the file that is already loaded.");
				}
			}
		});
		
		//action for the play with subtitles menu item, will set the media player to use a specified subtitles file
		playWithSubtitlesBtn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evt) {
				//open file browser to let user select a subtitle file for editing
				String subtitleToUse = FileBrowsers.subtitleFileBrowser();
				//check if the file chosen exists
				if (Helper.fileExist(subtitleToUse)) {
					//set the media player to use this subtitles file
					vamix.view.Main.vid.setSubTitleFile(subtitleToUse);
					//show in corner of player name of subtitle file being used
					subtitlesPlaying.setText(Helper.fileNameGetter(subtitleToUse));
				//if it doesn't exist, give an error
				} else {
					JOptionPane.showMessageDialog(null, "Please enter a valid subtitle file", "Invalid file path",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}
	
	/*
	 * Method that opens a file dialog which asks the user to select a video or audio file.
	 * It then checks the validity of the file and if all is good it will allow VAMIX to continue
	 */
	private void loadMedia(){
		//initialize validness variable code reuse from a2
		boolean valid=false; //if file is valid
		boolean isAudio=false; //boolean for if file is video or audio
		boolean hasSpaces = false;	//true if the input address has spaces - not allowed
		String tempVideoFileAdd="";//initialize the video file address
		String partial=""; //variable for partial of path i.e. just the name of file
		JOptionPane.showMessageDialog(null, "Please select the video or audio file to edit.");
		//get input file
		while(!valid){
			//setup file chooser
			JFileChooser chooser = new JFileChooser(Constants.CURRENT_DIR);
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Video/audio file","avi","mov","mp4"
					,"mp3","wav","wmv","mov");
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
				valid=true; //can't return nothing
			}
			if (tempVideoFileAdd.contains(" ")) {hasSpaces = true;}
			//now get the path of file and just file name
			Matcher m=Pattern.compile("(.*"+File.separator+")(.*)$").matcher(tempVideoFileAdd);
			if(m.find()){
				partial=m.group(2); //get file name
			}
			if (!hasSpaces) {
				if(partial.equals("")){
					//error message of empty file name
					JOptionPane.showMessageDialog(null, "You have entered an empty file name. Please input a valid file name.");
				}else{
					//check if the file exist locally
					if (Helper.fileExist(tempVideoFileAdd)){
						String bash =File.separator+"bin"+File.separator+"bash";
						String cmd ="echo $(file \""+tempVideoFileAdd+"\")";
						ProcessBuilder builder=new ProcessBuilder(bash,"-c",cmd); 
						builder.redirectErrorStream(true);
	
						try{
							Process process = builder.start();
							InputStream stdout = process.getInputStream();
							BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
							String line;
							while((line=stdoutBuffered.readLine())!=null){
								//System.out.println(line);//debug file type
								Matcher macth =Pattern.compile(Constants.VIDEO_AUDIO_TYPE,Pattern.CASE_INSENSITIVE).matcher(line);
								if(macth.find()){
									isAudio=true;
								}
							}
						}catch(Exception e){
							//e.printStackTrace();
						}
						//check if audio using bash commands
						if (isAudio){
							videoFileAdd=tempVideoFileAdd;
							videoPath.setText(videoFileAdd);
							valid=true;
						}else{
							//file is not audio/mpeg type
							JOptionPane.showMessageDialog(null, "You have entered a non-video file or the file type is not supported.\n Please enter a valid file.");
						}
					}else{
						//file does not exist so give error
						JOptionPane.showMessageDialog(null,"You have entered a non-existing file. Please input a valid file type.");
					}
				}
			} else {
				JOptionPane.showMessageDialog(null,"Your input file cannot have spaces in the path and name.");
			}
		}

	}
	
	//method that checks the user inputs for the title scene filter
	private boolean checkTitleInputs() {
		//first check if the user has entered text to be overlaid
		if (titleText.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "Please enter text to overlay on video", "Missing Input",
					JOptionPane.WARNING_MESSAGE);
		//otherwise if text has been entered, continue with function
		} else {
			//check if the text entered has not exceeded the word limit of 30 words
			String[] words = titleText.getText().split(" ");
			if (words.length <= 30) { 
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
						//if both times are valid continue with drawing text on video
						if (bothValid) {
							return true;
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
			} else {
				//The word limit of 30 was used because when using the max allowed font size (24), if more than 30 words were entered then the text
				//on the screen starts to wrap if it is too close to the edge of the video frame. Since this started to get quite messy, a word
				//limit of 30 words was used.
				JOptionPane.showMessageDialog(null, "The number of words to print on title screen is too large, you cannot have more than 30.",
						"Exceeded word limit", JOptionPane.ERROR_MESSAGE);
			}
		}
		return false;
	}
	
	//method that checks the user inputs for the credits scene filter
	private boolean checkCreditsInputs() {
		//first check if the user has entered text to be overlaid
		if (creditText.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "Please enter text to overlay on video", "Missing Input",
					JOptionPane.WARNING_MESSAGE);
		//otherwise if text has been entered, continue with function
		} else {
			//check if the text entered has not exceeded the word limit of 30 words
			String[] words = titleText.getText().split(" ");
			if (words.length <= 30) { 
				//check if the duration entered by user is in format required
				String timeFormat = "^\\d{2}:\\d{2}:\\d{2}$";
				boolean startMatched = false;
				//boolean to show if both times entered by user are valid (within length of video)
				boolean bothValid = true;
				//first check the start time and if it matches check the finish time
				Matcher matcher = Pattern.compile(timeFormat).matcher(startCredits.getText());
				if (matcher.find()) {
					startMatched = true;
				}
				if (startMatched) {
					matcher = Pattern.compile(timeFormat).matcher(endCredits.getText());
					//if the finish time also matches now go on to check if the times entered are
					//less than or equal to the length of the video
					if (matcher.find()) {
						String[] startTimeSplit = startCredits.getText().split(":");
						String[] endTimeSplit = endCredits.getText().split(":");
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
						//if both times are valid continue with drawing text on video
						if (bothValid) {
							return true;
						//otherwise display an error message to the user telling them times entered are invalid
						} else {
							JOptionPane.showMessageDialog(null, "Please enter a valid start time and end time for displaying the credits text",
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
			} else {
				//The word limit of 30 was used because when using the max allowed font size (24), if more than 30 words were entered then the text
				//on the screen starts to wrap if it is too close to the edge of the video frame. Since this started to get quite messy, a word
				//limit of 30 words was used.
				JOptionPane.showMessageDialog(null, "The number of words to print on credits screen is too large, you cannot have more than 30.",
						"Exceeded word limit", JOptionPane.ERROR_MESSAGE);
			}
		}
		return false;
	}
	
	//method that will check the user inputs for the fade functionality
	private boolean checkFadeInputs(boolean isFadeIn) {
		boolean isValid = false;
		TextField start = null, end = null;
		if (isFadeIn) {
			start = startFadeIn;
			end = endFadeIn;
		} else {
			start = startFadeOut;
			end = endFadeOut;
		}
		//check a video is actually loaded into VAMIX
		if (!(videoFileAdd.equals(""))) {
			//check if the input video file is a valid video file (is either mp4, avi or flv)
			if(Helper.validVideoFile(videoFileAdd,"(MPEG v4)|Video")){
				//check that the start time is valid
				if (Helper.timeValidTypeChecker(start.getText(), "start time")) {
					//check that the end time is valid
					if (Helper.timeValidTypeChecker(end.getText(), "end time")) {
						//check that the start time is valid relative to the end time
						if (Helper.timeValidChecker(start.getText(), end.getText(), "fading")) {
							//check that both times are less than or equal to length of the input video
							if ((Helper.timeLessThanVideo(start.getText()) && (Helper.timeLessThanVideo(end.getText())))) {
								isValid = true;
							}
						}
					}
				}
			}
		} else {
			JOptionPane.showMessageDialog(null, "Please load a video into VAMIX first", "Missing input", JOptionPane.ERROR_MESSAGE);
		}
		return isValid;
	}
	
	//method that will save the state of vamix to a txt file so it can be loaded on request of the user
	//NEEDS TO BE UPDATED IF NEW GUI COMPONENTS ARE ADDED
	private void saveState() {
		//save the state in the .vamix hidden folder
		Writer writer = null;

		try {
			Helper.genTempFolder();
		    writer = new BufferedWriter(new OutputStreamWriter(
		          new FileOutputStream(Constants.LOG_DIR + File.separator + "state.txt"), "UTF-8"));
		    //write the values currently in the video tab gui objects into state file
		    writer.write(videoPath.getText() + "\n");
		    writer.write(videoURL.getText() + "\n");
		    writer.write(titleText.getText() + "\n");
		    writer.write(titleFont.getValue() + "\n");
		    writer.write(titleSize.getValue() + "\n");
		    writer.write(titleColour.getValue().toString() + "\n");
		    writer.write(titleXPos.getText() + "\n");
		    writer.write(titleYPos.getText() + "\n");
		    writer.write(startTitle.getText() + "\n");
		    writer.write(endTitle.getText() + "\n");
		    writer.write(creditText.getText() + "\n");
		    writer.write(creditsFont.getValue() + "\n");
		    writer.write(creditsSize.getValue() + "\n");
		    writer.write(creditsColour.getValue().toString() + "\n");
		    writer.write(creditsXPos.getText() + "\n");
		    writer.write(creditsYPos.getText() + "\n");
		    writer.write(startCredits.getText() + "\n");
		    writer.write(endCredits.getText() + "\n");
		    writer.write(rotateAngle.getText() + "\n");
		    writer.write(startFadeIn.getText() + "\n");
		    writer.write(endFadeIn.getText() + "\n");
		    writer.write(startFadeOut.getText() + "\n");
		    writer.write(endFadeOut.getText() + "\n");
		    writer.write(startTrim.getText() + "\n");
		    writer.write(endTrim.getText() + "\n");
		    writer.write(cropXPos.getText() + "\n");
		    writer.write(cropYPos.getText() + "\n");
		    writer.write(cropHeight.getText() + "\n");
		    writer.write(cropWidth.getText() + "\n");
		    //now write the audio tab values into state file
		    writer.write(strip_add.getText() + "\n");
		    writer.write(replaceAdd.getText() + "\n");
		    writer.write(startReplace.getText() + "\n");
		    writer.write(endReplace.getText() + "\n");
		    writer.write(startReplace2.getText() + "\n");
		    writer.write(endReplace2.getText() + "\n");
		    writer.write(overlayAdd.getText() + "\n");
		    writer.write(overlayUseStart.getText() + "\n");
		    writer.write(overlayUseEnd.getText() + "\n");
		    writer.write(overlayToStart.getText() + "\n");
		    writer.write(overlayToEnd.getText() + "\n");
		    //now write the render tab values into state file (don't include the check boxes as they affect rendering)
		    writer.write(outputFilePath.getText() + "\n");
		    //notify user that the state has been saved
		    JOptionPane.showMessageDialog(null, "The current session has been saved", "State saved", JOptionPane.INFORMATION_MESSAGE);
		} catch (IOException ex) {
		  //do nothing
		} finally {
			   try {writer.close();} catch (Exception ex) {}
		}
	}
	
	//method that will read from the saved VAMIX state file and populate the GUI objects with values from previous session
	//NEEDS TO BE UPDATED IF NEW GUI COMPONENTS ARE ADDED
	private void loadState() {
		try {
			if (Helper.fileExist(Constants.LOG_DIR + File.separator + "state.txt")) {
				//read all the GUI object values from the previous session with VAMIX and assign to objects
				List<String> values = Files.readAllLines(Paths.get(Constants.LOG_DIR + File.separator + "state.txt"), Charset.forName("UTF-8"));
				//read the video tab values from the state file
				videoPath.setText(values.get(0));
				//check if the video from the last session still exists, if not tell user and if it does, load it into the media player
				if (Helper.fileExist(videoPath.getText())) {
					vamix.view.Main.vid.prepareMedia(videoPath.getText());
					videoFileAdd = videoPath.getText();
				} else {
					JOptionPane.showMessageDialog(null , "The previous video file that was being edited used to be located at " + videoPath.getText() + "\nIt is no longer there and will not be loaded.",
							"Missing Video", JOptionPane.ERROR_MESSAGE);
					//clear the video path gui field
					videoPath.setText("");
				}
				videoURL.setText(values.get(1));
			    titleText.setText(values.get(2));
			    titleFont.setValue(values.get(3));
			    titleSize.setValue(values.get(4));
			    titleColour.setValue(Color.valueOf(values.get(5)));
			    titleXPos.setText(values.get(6));
			    titleYPos.setText(values.get(7));
			    startTitle.setText(values.get(8));
			    endTitle.setText(values.get(9));
			    creditText.setText(values.get(10));
			    creditsFont.setValue(values.get(11));
			    creditsSize.setValue(values.get(12));
			    creditsColour.setValue(Color.valueOf(values.get(13)));
			    creditsXPos.setText(values.get(14));
			    creditsYPos.setText(values.get(15));
			    startCredits.setText(values.get(16));
			    endCredits.setText(values.get(17));
			    rotateAngle.setText(values.get(18));
			    startFadeIn.setText(values.get(19));
			    endFadeIn.setText(values.get(20));
			    startFadeOut.setText(values.get(21));
			    endFadeOut.setText(values.get(22));
			    startTrim.setText(values.get(23));
			    endTrim.setText(values.get(24));
			    cropXPos.setText(values.get(25));
			    cropYPos.setText(values.get(26));
			    cropHeight.setText(values.get(27));
			    cropWidth.setText(values.get(28));
			    //now read the audio tab values from the state file
			    strip_add.setText(values.get(29));
			    replaceAdd.setText(values.get(30));
			    startReplace.setText(values.get(31));
			    endReplace.setText(values.get(32));
			    startReplace2.setText(values.get(33));
			    endReplace2.setText(values.get(34));
			    overlayAdd.setText(values.get(35));
			    overlayUseStart.setText(values.get(36));
			    overlayUseEnd.setText(values.get(37));
			    overlayToStart.setText(values.get(38));
			    overlayToEnd.setText(values.get(39));
			    //now read the render tab values from the state file
			    outputFilePath.setText(values.get(40));
			} else {
				JOptionPane.showMessageDialog(null, "No previous state exists.", "Load unavailable", JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (IOException e) {
			//e.printStackTrace();
		}
	}
	
}
