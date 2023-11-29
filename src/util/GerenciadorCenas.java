package util;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GerenciadorCenas {
    private static Stage palcoPrincipal;

    public static void setPalcoPrincipal(Stage palco) {
        GerenciadorCenas.palcoPrincipal = palco;
    }

    public static void mudarCena(String arquivoFXML) {
        try {
            Parent novaCena = FXMLLoader.load(GerenciadorCenas.class.getResource(arquivoFXML));
            palcoPrincipal.setScene(new Scene(novaCena));
            palcoPrincipal.show();
        } catch (IOException e) {
            e.printStackTrace();   
        }
    }
    
    public static void abrirNovaJanela(String arquivoFXML) {
        try {
            Parent layout = FXMLLoader.load(GerenciadorCenas.class.getResource(arquivoFXML));
            Stage novaJanela = new Stage();
            novaJanela.setScene(new Scene(layout));
            novaJanela.show(); 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

}

