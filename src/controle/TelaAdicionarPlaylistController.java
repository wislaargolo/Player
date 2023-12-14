package controle;


import java.io.IOException;

import dao.PlaylistDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import modelo.Playlist;
import modelo.UsuarioVIP;
import util.Alertas;

/**
 * Controlador para a tela de adicionar playlists.
 * Essa classe gerencia a criação de novas playlists por usuários VIP,
 * permitindo que eles nomeiem e adicionem a playlist ao sistema.
 */
public class TelaAdicionarPlaylistController {
	
	@FXML
	private TextField nomePlaylist;
	
	/**
	 * Ação conectada ao botão de adicionar playlist.
	 * Cria uma nova playlist com o nome fornecido e a adiciona ao sistema
	 * para o usuário VIP atual. Em caso de erro, exibe um alerta.
	 */
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
