package application;
	


import javafx.application.Application;

import javafx.stage.Stage;
import util.GerenciadorCenas;

/**
 * Classe principal que inicia a aplicação JavaFX.
 * Configura o palco principal e muda a cena para a tela de login.
 * 
 * @author Rubens e Wisla
 */
public class Main extends Application {

    /**
     * Método principal que inicia a aplicação JavaFX.
     *
     * @param palcoPrincipal O palco principal da aplicação.
     */
    @Override
    public void start(Stage palcoPrincipal) {
        GerenciadorCenas.setPalcoPrincipal(palcoPrincipal);
        GerenciadorCenas.mudarCena("/visao/TelaLogin.fxml");
    }

    /**
     * Método principal que lança a aplicação.
     *
     * @param args Os argumentos da linha de comando.
     */
    public static void main(String[] args) {
        launch(args);
    }
}