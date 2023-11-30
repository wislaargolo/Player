package visao;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import dao.PlaylistDAO;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import modelo.Musica;
import modelo.Playlist;
import modelo.UsuarioVIP;

public class TelaPlaylistController implements Initializable {
	
    @FXML
    private Button btPlay;
    
    @FXML
    private Button btAnterior;
    
    @FXML
    private Button btProximo;
    
    @FXML
    private Button btPause;
    
    @FXML
    private Button btMutar;
    
    @FXML
    private Button btParar;
    
    @FXML
    private Button btVoltar;
    
	@FXML
    private ProgressBar progressoMusica;
	
	@FXML
	private ListView<Musica> listaMusicas;
	
	@FXML
	private ListView<Musica> listaMusicasPlaylist;
	
	private Playlist playlistAtual = TelaPrincipalController.getInstance().getPlaylistAtual();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ObservableList<Musica> musicas = TelaPrincipalController.getInstance().getListaMusicaItems();
        listaMusicas.setItems(musicas);
        
        atualizarMusicasPlaylist();
		
	}
	
	private void atualizarMusicasPlaylist() {
		ArrayList<Musica> musicasCarregadas = playlistAtual.getMusicas();
		listaMusicasPlaylist.getItems().setAll(musicasCarregadas); 
	}

}
