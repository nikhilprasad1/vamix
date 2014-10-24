package vamix.controller;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

/**
 * This class takes a table with 4 columns as input and allows creation and editing of .srt subtitle files
 * 4 columns required are Subtitle number, start time , end time and subtitle text
 * @author Nikhil Prasad
 *
 */
public class SubtitlesEditor {
	
	private TableView _subtitlesTable;
	private TableColumn _subtitleNumber, _startTimes, _endTimes, _subtitlesText;
	
	//ObservableList enables tracking of any changes to its elements and so the corresponding subtitles table automatically
	//updates whenever the data changes
	private final ObservableList<Subtitle> _subtitlesData = FXCollections.observableArrayList();
	private ArrayList<Integer> _subNumbers = new ArrayList<Integer>();
	
	/**
	 * SubtitlesEditor constructor that takes a table and its 4 columns as its parameter
	 * @param subtitlesTable
	 * @param subtitlesNumber
	 * @param startTimes
	 * @param endTimes
	 * @param subtitlesText
	 */
	public SubtitlesEditor(TableView subtitlesTable, TableColumn subtitlesNumber, TableColumn startTimes, TableColumn endTimes, TableColumn subtitlesText) {
		_subtitlesTable = subtitlesTable;
		_subtitleNumber = subtitlesNumber;
		_startTimes = startTimes;
		_endTimes = endTimes;
		_subtitlesText = subtitlesText;
		
	}
	
	@SuppressWarnings("unchecked")
	public void createSubtitlesFile(String path) {
		
		//initialize subtitles data with one subtitle to display in table
		Subtitle newSubtitle = new Subtitle("1", "", "", "");
		_subtitlesData.add(newSubtitle);
		_subNumbers.add(1);
		
		//associate columns of subtitles table with data from each Subtitle object
		associateColumns();
		
		//allow editing of each table column
		allowEditing();
		
		//now populate the subtitles table with the data
		_subtitlesTable.setItems(_subtitlesData);
	}
	
	/**
	 * Method that reads an existing .srt file and parses data into subtitle table to allowing editing
	 * @param path - absolute path of .srt file to read from, assumes file exists.
	 */
	@SuppressWarnings("unchecked")
	public void readSubtitlesFile(String path) {
		try {
			//read contents of file
			List<String> fileContents = Files.readAllLines(Paths.get(path), Charset.forName("UTF-8"));
			//get number of lines read
			int numberOfLines = fileContents.size();
			int i = 0;
			
			//loop through all lines of file
			while (i < numberOfLines) {
				//get current line
				String line = fileContents.get(i);
				//if the line is a single number (i.e. start of a subtitle set) then create a new subtitle object using the subtitle data
				if (line.matches("\\d+")) {
					//store the subtitle number to make checking for duplicates easier when adding subtitles
					_subNumbers.add(Integer.parseInt(line));
					//split following line using "-->" to get start time and end time
					String times = fileContents.get(i+1);
					String[] timesSplit = times.split("-->");
					Subtitle subtitle = new Subtitle(fileContents.get(i), timesSplit[0], timesSplit[1], fileContents.get(i+2));
					_subtitlesData.add(subtitle);
					//increment i by 4 to get to the next subtitle set in .srt file
					i = i + 4;
				}
			}
			
			//associate columns of subtitles table with data from each Subtitle object
			associateColumns();
			
			//allow editing of each table column
			allowEditing();
			
			//now populate the subtitles table with the data
			_subtitlesTable.setItems(_subtitlesData);	
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method to save changes made to a .srt subtitle file
	 * @param path - absolute file path of .srt file to save to, assumes file exists.
	 */
	public void saveSubtitles(String path) {
		
		Writer writer = null;
		
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), "UTF-8"));
			for (Subtitle s : _subtitlesData) {
				writer.write(s.getSubtitleNumber() + "\n");
				writer.write(s.getStartTime() + "-->" + s.getEndTime() + "\n");
				writer.write(s.getSubtitleText() + "\n");
				writer.write("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			   try {writer.close();} catch (Exception ex) {}
		}
	}
	
	Comparator<? super Subtitle> subtitleComparator = new Comparator<Subtitle>() {
		@Override
		public int compare(Subtitle subtitle, Subtitle subtitle1) {
			return Integer.parseInt(subtitle.getSubtitleNumber()) - Integer.parseInt(subtitle1.getSubtitleNumber());
		}
	};
	
	/**
	 * Method that adds a subtitle, given the subtitle number
	 * @param subtitleToAdd - number of subtitle to add
	 * @return Integer signaling if subtitle number given was valid (and therefore adding was successful, then it is 0)
	 * otherwise if number was invalid (too large or less than 1) then -1 is returned
	 */
	public int addSubtitle(int subtitleToAdd) {
		
		int exitCode = 0;
		
		//create the new subtitle 
		Subtitle newSubtitle = new Subtitle(String.valueOf(subtitleToAdd), "", "", "");
		
		//check if the subtitles list already contains this subtitle number
		if (_subNumbers.contains(new Integer(subtitleToAdd))) {
			//if it does then in the observablelist, update every subtitle that comes after where this one would go
			//by adding one to their subtitle number
			for (int i = subtitleToAdd - 1; i < _subNumbers.size(); i++) {
				String newSubNumber = String.valueOf(Integer.parseInt(_subtitlesData.get(i).getSubtitleNumber()) + 1);
				_subtitlesData.get(i).setSubtitleNumber(newSubNumber);
			}
			//now add the subtitle and then sort the observablelist back into order
			_subtitlesData.add(newSubtitle);
			FXCollections.sort(_subtitlesData, subtitleComparator);
			
		//otherwise if this subtitle number is new, then check if it follows the last number in the list so far
		} else {
			if ((subtitleToAdd < 1) || (subtitleToAdd > (_subNumbers.size() + 1))) {
				exitCode = -1;
			} else {
				_subtitlesData.add(newSubtitle);
			}
		}
		
		if (exitCode == 0) {
			//update subtitle numbers list to contain new subtitle number
			_subNumbers.add(_subNumbers.size() + 1);
		}
		
		return exitCode;
	}
	
	/**
	 * Method that deletes a subtitle, given the subtitle number
	 * @param subtitleToDelete - number of subtitle to delete from list
	 * @return returns whether or not subtitle specified existed or not
	 */
	public int deleteSubtitle(int subtitleToDelete) {
		
		//check if the subtitles list does actually contain this subtitle number
		if (_subNumbers.contains(new Integer(subtitleToDelete))) {
			//update every subtitle that comes after the to-be-deleted one by subtracting 1 from their subtitle numbers
			for (int i = subtitleToDelete - 1; i < _subNumbers.size(); i++) {
				String newSubNumber = String.valueOf(Integer.parseInt(_subtitlesData.get(i).getSubtitleNumber()) - 1);
				_subtitlesData.get(i).setSubtitleNumber(newSubNumber);
			}
			//delete the subtitle specified 
			_subtitlesData.remove(subtitleToDelete - 1);
			//update subtitle numbers list
			_subNumbers.remove(_subNumbers.size() - 1);
			return 0;
		//otherwise if subtitle number specified does not exist, return error code of -1
		} else {
			return -1;
		}
	}
	
	/*
	 * Method that associates each column in the table with its corresponding data from the Subtitle class
	 */
	@SuppressWarnings("unchecked")
	private void associateColumns() {
		
		_subtitleNumber.setCellValueFactory(new PropertyValueFactory<Subtitle, String>("subtitleNumber"));
		_startTimes.setCellValueFactory(new PropertyValueFactory<Subtitle, String>("startTime"));
		_endTimes.setCellValueFactory(new PropertyValueFactory<Subtitle, String>("endTime"));
		_subtitlesText.setCellValueFactory(new PropertyValueFactory<Subtitle, String>("subtitleText"));
	}
	
	/*
	 * Method that contains the actions for each table column when one of its cells are edited
	 */
	@SuppressWarnings("unchecked")
	private void allowEditing() {
		
		_subtitlesTable.setEditable(true);
		_subtitlesTable.setDisable(false);
		
		//if a cell in the start times column is edited, update the corresponding subtitle object
		_startTimes.setCellFactory(TextFieldTableCell.forTableColumn());
		_startTimes.setOnEditCommit(new EventHandler<CellEditEvent<Subtitle, String>>() {
            @Override
            public void handle(CellEditEvent<Subtitle, String> t) {
                ((Subtitle) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                        ).setStartTime(t.getNewValue());
            }
		});
		
		//if a cell in the end times column is edited, update the corresponding subtitle object
		_endTimes.setCellFactory(TextFieldTableCell.forTableColumn());
		_endTimes.setOnEditCommit(new EventHandler<CellEditEvent<Subtitle, String>>() {
            @Override
            public void handle(CellEditEvent<Subtitle, String> t) {
                ((Subtitle) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                        ).setEndTime(t.getNewValue());
            }
		});
				
		//if a cell in the subtitle text column is edited, update the corresponding subtitle object
		_subtitlesText.setCellFactory(TextFieldTableCell.forTableColumn());
		_subtitlesText.setOnEditCommit(new EventHandler<CellEditEvent<Subtitle, String>>() {
	        @Override
	        public void handle(CellEditEvent<Subtitle, String> t) {
	            ((Subtitle) t.getTableView().getItems().get(
	                    t.getTablePosition().getRow())
	                    ).setSubtitleText(t.getNewValue());
	        }
		});
	}	
}
