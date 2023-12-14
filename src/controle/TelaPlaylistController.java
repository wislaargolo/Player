package controle;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import dao.PlaylistDAO;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import modelo.Musica;
import modelo.UsuarioVIP;
import util.Alertas;
import util.GerenciadorCenas;

/**
 * Controlador para a tela de gerenciamento de playlists.
 *
 * @author Rubens e Wisla
 * 
 */
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
	private Label songLabel;
	
    @FXML
    private BorderPane telaPlaylist;
    
    
    private Timer timer;
	private TimerTask task;
	private boolean running = false;
	private File file;
	private Media media;
	private MediaPlayer mediaPlayer;
	private int indexMusicaPlaylist;
    private boolean controlePrimeiraMusica;
	
    /**
     * Inicializa o controlador automaticamente
     * após o carregamento do arquivo FXML associado.
     * Configura a lista de músicas e prepara o MediaPlayer.
     * 
     * @param arg0 URL utilizada para resolver caminhos relativos para o objeto raiz, ou null se desconhecido.
     * @param arg1 O recurso utilizado para localizar o objeto raiz, ou null se o objeto raiz não foi localizado.
     */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ObservableList<Musica> musicas = TelaPrincipalController.getInstance().getListaMusicaItems();
        listaMusicas.setItems(musicas);
        
        nomePlaylist.setText(TelaPrincipalController.getInstance().getPlaylistAtual().getNome());
        
        atualizarMusicasPlaylist();
        
        if ( !getListaMusicaPlaylist().isEmpty() && getListaMusicaPlaylist().get(0) != null) {
			 file = new File(getListaMusicaPlaylist().get(0).getCaminhoArquivo());
			 media = new Media(file.toURI().toString());
			 mediaPlayer = new MediaPlayer(media);
			 indexMusicaPlaylist = 0;
			 controlePrimeiraMusica = true;
		 }else {
			 controlePrimeiraMusica = false;
		 }
        
        listaMusicasPlaylist.setOnMouseClicked(event -> {
			if (event.getClickCount() == 2 && !listaMusicasPlaylist.getSelectionModel().isEmpty() ) {
				indexMusicaPlaylist = getListaMusicaPlaylist().indexOf(listaMusicasPlaylist.getSelectionModel().getSelectedItem());
				if (mediaPlayer  != null) mediaPlayer.stop();
				if(running) cancelTimer();
				file = new File(getListaMusicaPlaylist().get(indexMusicaPlaylist).getCaminhoArquivo());
				media = new Media(file.toURI().toString());
				mediaPlayer = new MediaPlayer(media);
				if(!controlePrimeiraMusica) controlePrimeiraMusica = true;
				songLabel.setText(getListaMusicaPlaylist().get(indexMusicaPlaylist).getNome());
				playMedia();
			}
		
		 });
        
		telaPlaylist.setOnMouseClicked(event -> {
		    if (!listaMusicasPlaylist.getBoundsInParent().contains(event.getSceneX(), event.getSceneY())) {
		    	listaMusicasPlaylist.getSelectionModel().clearSelection();
		    }
		    if (!listaMusicas.getBoundsInParent().contains(event.getSceneX(), event.getSceneY())) {
		    	listaMusicas.getSelectionModel().clearSelection();
		    }
		});
		
	}
	
	 /**
     * Atualiza a lista de músicas na playlist exibida na interface do usuário.
     */
	private void atualizarMusicasPlaylist() {
		ArrayList<Musica> musicasCarregadas = TelaPrincipalController.getInstance().getPlaylistAtual().getMusicas();
		listaMusicasPlaylist.getItems().setAll(musicasCarregadas); 
	}
	
	/**
     * Ação vinculada ao botão para adicionar uma música à playlist.
     * Adiciona a música selecionada na playlist do usuário VIP atual.
     */
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
	

    /**
     * Ação vinculada ao botão para remover uma música da playlist.
     * Remove a música selecionada da playlist do usuário VIP atual.
     */
	@FXML
	private void btRemoverMusicaAcao() {
		Musica musicaSelecionada = listaMusicasPlaylist.getSelectionModel().getSelectedItem();
        if (musicaSelecionada != null) {
            listaMusicasPlaylist.getItems().remove(musicaSelecionada);
            try {
				PlaylistDAO.removerMusica(musicaSelecionada, (UsuarioVIP) TelaLoginController.getInstance().getUsuarioAtual());
			} catch (IOException e) {
				Alertas.showAlert("Erro", null, "Erro ao remover música: " + e.getMessage(), Alert.AlertType.ERROR);
			}
        }
	}
	
	/**
     * Ação vinculada ao botão para retornar à tela principal.
     */
	@FXML
	private void btVoltarAcao() {
		GerenciadorCenas.mudarCena("../visao/TelaPrincipal.fxml");
	}
	
	/**
     * Retorna a lista de itens de música da playlist exibidos na interface do usuário.
     * 
     * @return ObservableList<Musica> A lista observável de músicas da playlist.
     */
	public ObservableList<Musica> getListaMusicaPlaylist() {
        return listaMusicasPlaylist.getItems();
    }
	
	/**
     * Inicia a reprodução da música selecionada.
     * Ativa um timer para atualizar o progresso da reprodução.
     */
	public void playMedia() {
		beginTimer();
		mediaPlayer.play();
	}
	
	/**
     * Controla a ação de reprodução de músicas da playlist.
     * Se for a primeira música na playlist, configura e inicia a reprodução.
     * Caso contrário, configura o MediaPlayer para a primeira música da playlist e inicia a reprodução.
     */
	@FXML
	public void playMediaPlaylist() {
		if (controlePrimeiraMusica) {
			songLabel.setText(getListaMusicaPlaylist().get(indexMusicaPlaylist).getNome());
			playMedia();
		}else {
			if(!getListaMusicaPlaylist().isEmpty()) {
				file = new File(getListaMusicaPlaylist().get(0).getCaminhoArquivo());
				media = new Media(file.toURI().toString());
				mediaPlayer = new MediaPlayer(media);
				indexMusicaPlaylist = 0;
				controlePrimeiraMusica = true;
				songLabel.setText(getListaMusicaPlaylist().get(indexMusicaPlaylist).getNome());
				playMedia();
			}
		}
	}
	
	/**
     * Para a reprodução da música atual e reinicia a configuração do MediaPlayer.
     */
	public void stopMediaPlaylist() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			if(running) cancelTimer();
			
			file = new File(getListaMusicaPlaylist().get(0).getCaminhoArquivo());
			media = new Media(file.toURI().toString());
			mediaPlayer = new MediaPlayer(media);
			indexMusicaPlaylist = 0;
			songLabel.setText("");
		}
	}
	
	/**
     * Pausa a reprodução da música atual, cancelando a atualização do
     * progresso da música.
     */
	@FXML
	public void pauseMedia() {
		cancelTimer();
		mediaPlayer.pause();
	}
	
	/**
     * Avança para a próxima música na lista.
     * Se estiver na última, volta para a primeira e reinicia inicia a reprodução.
     */
	@FXML
	public void nextMediaPlaylist() {
		if(indexMusicaPlaylist < getListaMusicaPlaylist().size() - 1) {			
			indexMusicaPlaylist++;
			if (mediaPlayer  != null) mediaPlayer.stop();
			if(running) cancelTimer();
			
			file = new File(getListaMusicaPlaylist().get(indexMusicaPlaylist).getCaminhoArquivo());
			media = new Media(file.toURI().toString());
			mediaPlayer = new MediaPlayer(media);
			songLabel.setText(getListaMusicaPlaylist().get(indexMusicaPlaylist).getNome());
			
			playMedia();
		}
		else {
			indexMusicaPlaylist = 0;
			if (mediaPlayer  != null) mediaPlayer.stop();
			
			file = new File(getListaMusicaPlaylist().get(indexMusicaPlaylist).getCaminhoArquivo());
			media = new Media(file.toURI().toString());
			mediaPlayer = new MediaPlayer(media);
			songLabel.setText(getListaMusicaPlaylist().get(indexMusicaPlaylist).getNome());
			
			playMedia();
		}
	}
	
	/**
     * Retrocede para a música anterior na lista.
     * Se estiver na primeira música, vai para a última e inicia a reprodução.
     */
	@FXML
	public void previousMediaPlaylist() {
		if (indexMusicaPlaylist == 0) {
			indexMusicaPlaylist = getListaMusicaPlaylist().size() - 1;
			if (mediaPlayer  != null) mediaPlayer.stop();
			if(running) cancelTimer();
			
			file = new File(getListaMusicaPlaylist().get(indexMusicaPlaylist).getCaminhoArquivo());
			media = new Media(file.toURI().toString());
			mediaPlayer = new MediaPlayer(media);
			songLabel.setText(getListaMusicaPlaylist().get(indexMusicaPlaylist).getNome());
			
			playMedia();
		}else {
			indexMusicaPlaylist--;
			if (mediaPlayer  != null) mediaPlayer.stop();
			if(running) cancelTimer();
			
			file = new File(getListaMusicaPlaylist().get(indexMusicaPlaylist).getCaminhoArquivo());
			media = new Media(file.toURI().toString());
			mediaPlayer = new MediaPlayer(media);
			songLabel.setText(getListaMusicaPlaylist().get(indexMusicaPlaylist).getNome());
			
			playMedia();
		}
	}
	
	/**
     * Alterna o estado de mudo (mute) do MediaPlayer.
     * Se estiver em estado de mudo, desativa o mudo, e vice-versa.
     */
	public void muteMedia() {
	    if (mediaPlayer != null) {
	        mediaPlayer.setMute(!mediaPlayer.isMute());
	    }
	}

	
	/**
     * Inicia um timer que atualiza a cada segundo o progresso da reprodução da música.
     */
	public void beginTimer() {
		timer = new Timer();
		task = new TimerTask() {
			
			public void run() {
				running = true;
				double current = mediaPlayer.getCurrentTime().toSeconds();
				double end = media.getDuration().toSeconds();
				progressoMusica.setProgress(current/end);
				if(current/end >= 1) {
					Platform.runLater(() -> nextMediaPlaylist());
				}
			}
		};
		
		timer.scheduleAtFixedRate(task, 0, 1000);
	}
	
	/**
	 * Cancela o timer atualmente ativo para o progresso da reprodução de mídia.
	 */
	public void cancelTimer() {
		running = false;
		timer.cancel();
	}

}