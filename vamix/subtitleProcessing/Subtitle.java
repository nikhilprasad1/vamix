package vamix.subtitleProcessing;

import javafx.beans.property.SimpleStringProperty;

/**
 * This class represents a subtitle from a .srt subtitle file
 * @author Nikhil Prasad
 */
public class Subtitle {
	
	//components that make up a valid subtitle
	private final SimpleStringProperty subtitleNumber;
	private final SimpleStringProperty startTime;
	private final SimpleStringProperty endTime;
	private final SimpleStringProperty subtitleText;
	
	public Subtitle(String subtitleNumber, String startTime, String endTime, String subtitleText) {
		this.subtitleNumber = new SimpleStringProperty(subtitleNumber);
		this.startTime = new SimpleStringProperty(startTime);
		this.endTime = new SimpleStringProperty(endTime);
		this.subtitleText = new SimpleStringProperty(subtitleText);
	}
	
	/*
	 * Getter and setter methods for each Subtitle property
	 */
	public String getSubtitleNumber() {
		return subtitleNumber.get();
	}
	
	public void setSubtitleNumber(String newSubtitleNumber) {
		subtitleNumber.set(newSubtitleNumber);
	}
	
	public String getStartTime() {
		return startTime.get();
	}
	
	public void setStartTime(String newStartTime) {
		startTime.set(newStartTime);
	}
	
	public String getEndTime() {
		return endTime.get();
	}
	
	public void setEndTime(String newEndTime) {
		endTime.set(newEndTime);
	}
	
	public String getSubtitleText() {
		return subtitleText.get();
	}
	
	public void setSubtitleText(String newText) {
		subtitleText.set(newText);
	}
	
}
