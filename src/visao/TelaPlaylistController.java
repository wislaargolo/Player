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
import util.GerenciadorCenas;

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
    private Button btAdicionarMusica;
    
    @FXML
    private Button btRemoverMusica;
    
    @FXML
    private Button btVoltar; 
    
	@FXML
    private ProgressBar progressoMusica;
	
	@FXML
	private ListView<Musica> listaMusicas;
	
	@FXML
	private ListView<Musica> listaMusicasPlaylist;
	
	private Playlist playlistAtual = TelaPrincipalController.getInstance().getPlaylistAtual();
	private UsuarioVIP usuarioAtual = (UsuarioVIP) TelaLoginController.getInstance().getUsuarioAtual();
	
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
	
	@FXML
	private void btAdicionarMusicaAcao() {
		Musica musicaSelecionada = listaMusicas.getSelectionModel().getSelectedItem();
        if (musicaSelecionada != null && !listaMusicasPlaylist.getItems().contains(musicaSelecionada)) {
            listaMusicasPlaylist.getItems().add(musicaSelecionada);
            PlaylistDAO.adicionarMusica(musicaSelecionada, playlistAtual, usuarioAtual);
        }
	}
	
	@FXML
	private void btRemoverMusicaAcao() {
		Musica musicaSelecionada = listaMusicasPlaylist.getSelectionModel().getSelectedItem();
        if (musicaSelecionada != null) {
            listaMusicasPlaylist.getItems().remove(musicaSelecionada);
            PlaylistDAO.removerMusica(musicaSelecionada, usuarioAtual);
        }
	}
	
	@FXML
	private void btVoltarAcao() {
		GerenciadorCenas.mudarCena("/visao/TelaPrincipal.fxml");
	}

}
