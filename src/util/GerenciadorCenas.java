package util;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * Classe utilitária para gerenciar mudanças de cena e janelas na aplicação JavaFX.
 */
public class GerenciadorCenas {
    private static Stage palcoPrincipal;

    /**
     * Define o palco principal para a aplicação.
     * 
     * @param palco O palco principal da aplicação.
     */
    public static void setPalcoPrincipal(Stage palco) {
        GerenciadorCenas.palcoPrincipal = palco;
    }

    /**
     * Muda a cena atual no palco principal para a cena especificada.
     * 
     * @param arquivoFXML O caminho do arquivo FXML da nova cena.
     */
    public static void mudarCena(String arquivoFXML) {
        try {
        	
        	Scene cenaAtual = GerenciadorCenas.palcoPrincipal.getScene();
        	Parent novaCena = FXMLLoader.load(GerenciadorCenas.class.getResource(arquivoFXML));
        	Rectangle2D limitesTela = Screen.getPrimary().getVisualBounds();
        	 
            if (cenaAtual != null) {
            	cenaAtual.setRoot(novaCena);
            } else {
                cenaAtual = new Scene(novaCena, limitesTela.getWidth()*0.8, limitesTela.getHeight()*0.9);
                GerenciadorCenas.palcoPrincipal.setScene(cenaAtual);
            }
 
            palcoPrincipal.show();
        } catch (IOException e) {
            e.printStackTrace();   
        }
    }
    
    /**
     * Abre uma nova janela com a cena especificada.
     * 
     * @param arquivoFXML O caminho do arquivo FXML da nova janela.
     */
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

    /**
     * Retorna o palco principal atual da aplicação.
     * 
     * @return O palco principal.
     */
	public static final Stage getPalcoPrincipal() {
		return palcoPrincipal;
	}
    

}

