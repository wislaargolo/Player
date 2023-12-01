package controle;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import dao.PlaylistDAO;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import modelo.Musica;
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
    private Label nomePlaylist; 
    
	@FXML
    private ProgressBar progressoMusica;
	
	@FXML
	private ListView<Musica> listaMusicas;
	
	@FXML
	private ListView<Musica> listaMusicasPlaylist;
	
	
    @FXML
    private BorderPane telaPlaylist;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ObservableList<Musica> musicas = TelaPrincipalController.getInstance().getListaMusicaItems();
        listaMusicas.setItems(musicas);
        
        nomePlaylist.setText(TelaPrincipalController.getInstance().getPlaylistAtual().getNome());
        
        atualizarMusicasPlaylist();
		
	}
	
	private void atualizarMusicasPlaylist() {
		ArrayList<Musica> musicasCarregadas = TelaPrincipalController.getInstance().getPlaylistAtual().getMusicas();
		listaMusicasPlaylist.getItems().setAll(musicasCarregadas); 
	}
	
	@FXML
	private void btAdicionarMusicaAcao() {
		Musica musicaSelecionada = listaMusicas.getSelectionModel().getSelectedItem();
        if (musicaSelecionada != null && !listaMusicasPlaylist.getItems().contains(musicaSelecionada)) {
            listaMusicasPlaylist.getItems().add(musicaSelecionada);
            PlaylistDAO.adicionarMusica(musicaSelecionada, 
            							TelaPrincipalController.getInstance().getPlaylistAtual(), 
            							(UsuarioVIP) TelaLoginController.getInstance().getUsuarioAtual());
        }
	}
	
	@FXML
	private void btRemoverMusicaAcao() {
		Musica musicaSelecionada = listaMusicasPlaylist.getSelectionModel().getSelectedItem();
        if (musicaSelecionada != null) {
            listaMusicasPlaylist.getItems().remove(musicaSelecionada);
            PlaylistDAO.removerMusica(musicaSelecionada, (UsuarioVIP) TelaLoginController.getInstance().getUsuarioAtual());
        }
	}
	
	@FXML
	private void btVoltarAcao() {
		GerenciadorCenas.mudarCena("../visao/TelaPrincipal.fxml");
	}
	

}
