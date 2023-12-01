package controle;

import java.net.URL;
import java.util.ResourceBundle;

import dao.UsuarioDAO;
import excecoes.ExcecaoPersonalizada;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import util.Alertas;
import util.GerenciadorCenas;

public class TelaContaController implements Initializable{

    @FXML
    private Label loginUsuario;

    @FXML
    private Label nomeUsuario;
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		nomeUsuario.setText(TelaLoginController.getInstance().getUsuarioAtual().getNome());
		loginUsuario.setText(TelaLoginController.getInstance().getUsuarioAtual().getId());
		
	}


    @FXML
    void btRemoverContaAcao() {
	    try {
	        UsuarioDAO.remover(TelaLoginController.getInstance().getUsuarioAtual());
	        GerenciadorCenas.mudarCena("../visao/TelaLogin.fxml");
	    } catch(ExcecaoPersonalizada e) {
	        Alertas.showAlert("Erro", e.getMessage(), "", Alert.AlertType.ERROR);
	    }
    }
    
    @FXML
    void btVoltarAcao() { 
    	GerenciadorCenas.mudarCena("../visao/TelaPrincipal.fxml");
    }
    
    @FXML
    void btSairAcao() { 
    	GerenciadorCenas.mudarCena("../visao/TelaLogin.fxml");
    }

}
