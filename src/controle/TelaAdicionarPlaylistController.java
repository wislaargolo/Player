package controle;


import java.io.IOException;

import dao.PlaylistDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import modelo.Playlist;
import modelo.UsuarioVIP;
import util.Alertas;

public class TelaAdicionarPlaylistController {
	
	@FXML
	private TextField nomePlaylist;
	
	@FXML
    private void adicionarPlaylistAcao() {
		
		String playlist = nomePlaylist.getText();

        if (playlist.isEmpty()) {
            Alertas.showAlert("Erro", null, "O campo deve ser preenchido!", Alert.AlertType.ERROR);
            return ;
        }
        
		try {
			PlaylistDAO.adicionar(new Playlist(playlist),(UsuarioVIP) TelaLoginController.getInstance().getUsuarioAtual());
		} catch (IOException e) {
			Alertas.showAlert("Erro", null, "Erro ao adicionar playlist: "+ e.getMessage(), Alert.AlertType.ERROR);
		}
		
		if (TelaPrincipalController.getInstance() != null) {
			TelaPrincipalController.getInstance().atualizarPlaylists();
	    }
		
	 
	    
		
	}
	
	


}
