package visao;

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
    

}

