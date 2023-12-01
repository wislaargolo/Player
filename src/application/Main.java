package application;
	


import javafx.application.Application;

import javafx.stage.Stage;
import util.GerenciadorCenas;


public class Main extends Application {
	@Override
	 public void start(Stage palcoPrincipal) {
        GerenciadorCenas.setPalcoPrincipal(palcoPrincipal);
        GerenciadorCenas.mudarCena("/visao/TelaLogin.fxml");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
