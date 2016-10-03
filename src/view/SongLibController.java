/**
 * @author Daniel Ayoub, Marcus Lomi
 */

package view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ListView; 
import javafx.stage.Stage;
import model.*;

public class SongLibController {

	@FXML
	private Button addSong;

	@FXML
	private Button editSong;

	@FXML
	private Button deleteSong;

	@FXML
	private TextField enterSongName;

	@FXML
	private TextField enterSongArtist;

	@FXML
	private TextField enterSongAlbum;

	@FXML
	private TextField enterSongYear;

	@FXML
	private TextArea songInfoField;

	@FXML
	private Button confirmTextField;

	@FXML
	private Button cancelTextField;


	@FXML
	private ListView<String> listView;

	public SongList sl;	
	private Song editedSong;
	private int fcnId = 0;

	private final String saveFileName = "songlist.ser";

	private ObservableList<String> obsList;

	public void start(Stage mainstage) {
		if (!loadSongList()) {
			this.sl = new SongList();
		} // Song list is loaded
		updateListData();
		
		listView.getSelectionModel().selectFirst();
		updateList();
		

		// Disable things we don't need right now
		setButtonsDisabled(true);
		setFieldsDisabled(true);

		listView.setOnMouseClicked((e) -> {
			updateList();		
		});

		deleteSong.setOnAction((e) -> {
			int i = listView.getSelectionModel().getSelectedIndex();
			Song target = sl.getSongs()[i];
			sl.removeSong(target.key);
			listView.getSelectionModel().clearSelection();
			updateList();
			updateListData();
			if(listView.getSelectionModel().isEmpty()){
				songInfoField.setText("");
			}	
			setButtonsDisabled(true);
			setFieldsDisabled(true); 
			
		});		

		editSong.setOnAction((e) -> {
			confirmTextField.setText("Edit");
			int i = listView.getSelectionModel().getSelectedIndex();
			setFieldsDisabled(false);
			Song target = sl.getSongs()[i];
			editedSong = target;
			enterSongName.setText(target.name);
			enterSongArtist.setText(target.artist);
			enterSongAlbum.setText(target.album);
			enterSongYear.setText(target.year + "");
			fcnId = 1;
		});

		addSong.setOnAction((e) -> {
			confirmTextField.setText("Add");
			setFieldsDisabled(false);
			enterSongName.setText("");
			enterSongArtist.setText("");
			enterSongAlbum.setText("");
			enterSongYear.setText("");
			fcnId = 2;
		});

		
		enterSongYear.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					enterSongYear.setText(newValue.replaceAll("[^\\d]", ""));
				}
			}
		});

		confirmTextField.setOnAction((e) -> {
			String nName = enterSongName.getText().trim();
			String nArtist = enterSongArtist.getText().trim();
			String nAlbum = enterSongAlbum.getText().trim();
			int nYear = -1;
			try {
				nYear = Integer.parseInt(enterSongYear.getText());
			} catch (Exception ex) { /* Year field must be blank, leave as -1 */ }
			if (nName.equals("") || nArtist.equals("")) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.initOwner(mainstage);
				alert.setHeaderText("Need Artist / Name");
				alert.showAndWait();
				return;
			}
			Song nSong = new Song(nName, nArtist, nAlbum, nYear);

			if (fcnId == 1) {
				String nKey = (nName + nArtist).toLowerCase();
				if (nKey.equals(editedSong.key)) {
					sl.updateSong(nSong);
				} else {
					try {
						sl.addSong(nSong);
					} catch (Exception ex) {
						Alert alert = new Alert(AlertType.ERROR);
						alert.initOwner(mainstage);
						alert.setHeaderText("Song already exists");
						alert.showAndWait();
						return;
					}
					sl.removeSong(editedSong.key);
				}
			} else if (fcnId == 2) {
				try {
					sl.addSong(nSong);
				} catch (Exception ex) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.initOwner(mainstage);
					alert.setHeaderText("Song already exists");
					alert.showAndWait();
					return;
				}
			}
			updateList();
			updateListData();
			setFieldsDisabled(true);
		});

		cancelTextField.setOnAction((e) -> {
			setFieldsDisabled(true);
		});
		
		mainstage.setOnCloseRequest((e) -> {
			saveSongList();
		});
	}
	
	/**
	 * Updates the list to correctly display the songs that are currently in it.
	 */
	public void updateList() {
		int i = listView.getSelectionModel().getSelectedIndex();
		if (i >= 0) {
			Song selected = sl.getSongs()[i];
			StringBuilder sb = new StringBuilder("Title: ");
			sb.append(selected.name);
			sb.append("\nArtist: ");
			sb.append(selected.artist);
			if (!selected.album.equals("")) {
				sb.append("\nAlbum: ");
				sb.append(selected.album);
			}
			if (selected.year != -1) {
				sb.append("\nYear: ");
				sb.append(selected.year);
			}
			songInfoField.setText(sb.toString());
			setButtonsDisabled(false);
			
		}
	}
	
	
	public void updateListData() {
		obsList = FXCollections.observableArrayList(sl.getSongTitles());
		listView.setItems(obsList);	
	}

	/**
	 * Disables the delete and edit buttons
	 * @param d
	 */
	public void setButtonsDisabled(boolean d) {
		deleteSong.setDisable(d);
		editSong.setDisable(d);
	}

	/**
	 * Disables the text fields to enter the song name, artist, year and album.
	 * @param d
	 */
	public void setFieldsDisabled(boolean d) {
		if (d) {
			enterSongName.setText("Required");
			enterSongAlbum.setText("Optional");
			enterSongArtist.setText("Required");
			enterSongYear.setText("");
			confirmTextField.setText("Ok");	
		}
		enterSongName.setDisable(d);
		enterSongAlbum.setDisable(d);
		enterSongArtist.setDisable(d);
		enterSongYear.setDisable(d);
		confirmTextField.setDisable(d);
		cancelTextField.setDisable(d);
	}
	
	/**
	 * Saves the song list to an external file.
	 */
	public void saveSongList() {
		try {
			FileOutputStream outTest = new FileOutputStream(saveFileName);
			PrintWriter pw = new PrintWriter(outTest);
			pw.close(); // Empty out existing file
			FileOutputStream outFile = new FileOutputStream(saveFileName);
			ObjectOutputStream out = new ObjectOutputStream(outFile);
			out.writeObject(sl);
			out.close();
			//outFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Loads the song list from an external file
	 * @return boolean true or false depending on whether or not the file was found
	 */
	public boolean loadSongList() {
		File f = new File(saveFileName);
		if (!f.exists()) return false;
		try {
			FileInputStream inFile = new FileInputStream(f);
			ObjectInputStream in = new ObjectInputStream(inFile);
			sl = (SongList) in.readObject();
			in.close();
			inFile.close();
			return true;
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}
}
