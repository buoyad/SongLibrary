package view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
	    private Button confirmTextField;

	    @FXML
	    private Button cancelTextField;

	
	@FXML
	ListView<String> listView;
	
	public SongList sl;
	public Song ss; // Selected song
	
	private final String saveFileName = "songlist.ser";
	
	private ObservableList<String> obsList;

	public void start(Stage mainstage) {
		if (!loadSongList()) {
			this.sl = new SongList();
		} // Song list is loaded
		obsList = FXCollections.observableArrayList(sl.getSongTitles());
		listView.setItems(obsList);		
	}
	
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
