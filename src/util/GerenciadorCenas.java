package util;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class GerenciadorCenas {
    private static Stage palcoPrincipal;

    public static void setPalcoPrincipal(Stage palco) {
        GerenciadorCenas.palcoPrincipal = palco;
    }

    public static void mudarCena(String arquivoFXML) {
        try {
            Parent novaCena = FXMLLoader.load(GerenciadorCenas.class.getResource(arquivoFXML));
            
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            Scene cena = new Scene(novaCena, screenBounds.getWidth()*0.7, screenBounds.getHeight()*0.9);
            
            palcoPrincipal.setScene(cena);
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

