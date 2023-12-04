package controle;

import java.io.IOException;

import dao.PlaylistDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import modelo.UsuarioVIP;
import util.Alertas;

public class TelaEditarPlaylistController {

    @FXML
    private TextField novoNome;

    @FXML
    void salvarAcao() {
    	String nome = novoNome.getText();
    	
    	if (nome.isEmpty()) {
            Alertas.showAlert("Erro", null, "O campo deve ser preenchido!", Alert.AlertType.ERROR);
            return ;
        }
    	
    	UsuarioVIP usuario = (UsuarioVIP) TelaLoginController.getInstance().getUsuarioAtual();
    
    	if (TelaPrincipalController.getInstance() != null) {
    		try {
				PlaylistDAO.atualizar(TelaPrincipalController.getInstance().getPlaylistAtual(), usuario, nome);
			} catch (IOException e) {
				Alertas.showAlert("Erro", null, e.getMessage(), Alert.AlertType.ERROR);
			}
			TelaPrincipalController.getInstance().atualizarPlaylists();
	    }
    }


}