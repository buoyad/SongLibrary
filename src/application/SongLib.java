/**
 * @author Daniel Ayoub, Marcus Lomi
 */

package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import model.SongList;
import view.SongLibController;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class SongLib extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(
				getClass().getResource("/view/SongLibGUI.fxml"));
		AnchorPane root = (AnchorPane)loader.load();
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setMinWidth(650);
		primaryStage.setMinHeight(580);
		primaryStage.setScene(scene);
		
		SongLibController slc = loader.getController();
		slc.start(primaryStage);
		
		primaryStage.show();
	}

	public static void main(String[] args) {		
		launch(args);
	}

}
