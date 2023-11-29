package visao;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import dao.DiretorioDAO;
import dao.PlaylistDAO;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import modelo.Musica;
import modelo.Playlist;
import modelo.UsuarioVIP;

public class TelaPrincipalVIPController extends TelaPrincipalController {
    @FXML
    private ListView<Playlist> listaPlaylists;
    
    @FXML
    private ListView<Musica> listaMusicasPlaylist;
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    	DiretorioDAO.carregar(TelaLoginController.getInstance().getUsuarioAtual());
    	atualizarMusicas();
    	atualizarPlaylists();
    	
    	listaPlaylists.getSelectionModel().selectedItemProperty().addListener((observable, valorAntigo, novoValor) -> {
            if (novoValor != null) {
            	atualizarMusicasPlaylist(novoValor);
            }
        });
	}
    
    private void atualizarPlaylists() {
		ArrayList<Playlist> playlistsCarregadas = PlaylistDAO.carregar((UsuarioVIP) TelaLoginController.getInstance().getUsuarioAtual());
		listaPlaylists.getItems().removeIf(playlist -> !playlistsCarregadas.contains(playlist));
		
	    for (Playlist playlist : playlistsCarregadas) {
	        if (!listaPlaylists.getItems().contains(playlist)) {
	        	listaPlaylists.getItems().add(playlist);
	        }
	    }
	}
    
    private void atualizarMusicasPlaylist(Playlist playlist) {
        ArrayList<Musica> musicas = playlist.getMusicas();
        listaMusicasPlaylist.getItems().setAll(musicas);
    }

    
    
}
