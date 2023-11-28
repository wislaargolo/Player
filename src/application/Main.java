package application;
	
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class Main extends Application {
	@Override
	public void start(Stage stage) {
		try {
			 Parent parent = FXMLLoader.load(Main.class.getResource("/visao/TelaLogin.fxml"));
			 Scene scene = new Scene(parent);
			 stage.setScene(scene);
			 stage.show();
		 }
		catch (IOException e) {
			e.printStackTrace();
		 }
	} 
	
	public static void main(String[] args) {
		launch(args);
	}
}
