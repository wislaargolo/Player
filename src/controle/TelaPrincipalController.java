package controle;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import dao.DiretorioDAO;
import dao.MusicaDAO;
import dao.PlaylistDAO;
import excecoes.ExcecaoPersonalizada;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import modelo.Musica;
import modelo.Playlist;
import modelo.UsuarioVIP;
import util.Alertas;
import util.GerenciadorCenas;

/**
 * Controlador para a tela principal da aplicação.
 * Gerencia a interação do usuário com elementos da interface gráfica, 
 * incluindo a reprodução de músicas, gerenciamento de músicas, playlists (para o usuário VIP) e navegação na aplicação.
 * 
 * @author Rubens e Wisla
 * 
 */
public class TelaPrincipalController implements Initializable {
	 // Campos anotados com @FXML representam os elementos da interface gráfica
	
	@FXML
	private ImageView icone;
	
	@FXML
    private ProgressBar progressoMusica;

    @FXML
    private ListView<Musica> listaMusicas;
    
    @FXML
    private Button btPlaylist;
    
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
    private VBox playlists;
    
    @FXML
    private BorderPane telaPrincipal;
    
    @FXML
    private ListView<Playlist> listaPlaylists;
    
    @FXML
	private Label songLabel;
  
    
    private static TelaPrincipalController instance = new TelaPrincipalController();
    
    private Playlist playlistAtual;
    
    //-----------------------
    
    private Timer timer;
	private TimerTask task;
	private boolean running = false;
	private File file;
	private Media media;
	private MediaPlayer mediaPlayer;
	private int indexMusicaGeral;
    private boolean controlePrimeiraMusica;
    
    /**
     * Inicializa o controlador automaticamente
     * após o carregamento do arquivo FXML associado.
     * Configura a lista de músicas e playlists (para usuário VIP) e prepara o MediaPlayer.
     * 
     * @param arg0 URL utilizada para resolver caminhos relativos para o objeto raiz, ou null se desconhecido.
     * @param arg1 O recurso utilizado para localizar o objeto raiz, ou null se o objeto raiz não foi localizado.
     */
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    	try {
			DiretorioDAO.carregar(TelaLoginController.getInstance().getUsuarioAtual());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Alertas.showAlert("Erro", null, e.getMessage(), Alert.AlertType.ERROR);
		}
    	
    	atualizarMusicas();
    	
		ContextMenu menu = menuMusica();
		listaMusicas.setContextMenu(menu);
		
		 if (TelaLoginController.getInstance().getUsuarioAtual() instanceof UsuarioVIP) {
			playlists.setVisible(true);
		    menu = menuPlaylist();
			listaPlaylists.setContextMenu(menu);
			
			instance.listaPlaylists = listaPlaylists;
			
			atualizarPlaylists();
			
			listaPlaylists.setOnMouseClicked(event -> {
	            if (event.getClickCount() == 2 && !listaPlaylists.getSelectionModel().isEmpty()) {
	                Playlist playlistSelecionada = listaPlaylists.getSelectionModel().getSelectedItem();
	                instance.playlistAtual = playlistSelecionada;
	                GerenciadorCenas.mudarCena("../visao/TelaPlaylist.fxml");
	                
	            }
	            
	        });
			
	    } else {
	    	playlists.setVisible(false);
	    	playlists.setManaged(false);
	    }
		 
		telaPrincipal.setOnMouseClicked(event -> {
		    if (!listaMusicas.getBoundsInParent().contains(event.getSceneX(), event.getSceneY())) {
		        listaMusicas.getSelectionModel().clearSelection();
		    }
		    if (!listaPlaylists.getBoundsInParent().contains(event.getSceneX(), event.getSceneY())) {
		        listaPlaylists.getSelectionModel().clearSelection();
		    }
		});
		 
		 instance.listaMusicas = listaMusicas;
		 
		 if ( !getListaMusicaItems().isEmpty() && getListaMusicaItems().get(0) != null) {
			 file = new File(getListaMusicaItems().get(0).getCaminhoArquivo());
			 media = new Media(file.toURI().toString());
			 mediaPlayer = new MediaPlayer(media);
			 indexMusicaGeral = 0;		 
			 controlePrimeiraMusica = true;
		 }else {
			 controlePrimeiraMusica = false;
		 }
		 
		 listaMusicas.setOnMouseClicked(event -> {
			if (event.getClickCount() == 2 && !listaMusicas.getSelectionModel().isEmpty() ) {
				indexMusicaGeral = getListaMusicaItems().indexOf(listaMusicas.getSelectionModel().getSelectedItem());
				if (mediaPlayer  != null) mediaPlayer.stop();
				if(running) cancelTimer();
				file = new File(getListaMusicaItems().get(indexMusicaGeral).getCaminhoArquivo());
				media = new Media(file.toURI().toString());
				mediaPlayer = new MediaPlayer(media);
				if(!controlePrimeiraMusica) controlePrimeiraMusica = true;
				songLabel.setText(getListaMusicaItems().get(indexMusicaGeral).getNome());
				playMedia();
			}
			
		 });
	}
    
    /**
     * Atualiza a lista de músicas exibida na interface.
     * Para isso, carrega músicas do usuário atual e atualiza a lista.
     */
	private void atualizarMusicas() {
		ArrayList<Musica> musicasCarregadas;
		try {
			musicasCarregadas = MusicaDAO.carregar(TelaLoginController.getInstance().getUsuarioAtual());
			listaMusicas.getItems().setAll(musicasCarregadas);
		} catch (IOException e) {
			Alertas.showAlert("Erro", null, "Erro ao carregar músicas: " + e.getMessage(), Alert.AlertType.ERROR);
		}

	}
	
	/**
     * Atualiza a lista de playlists exibida na interface.
     * Para isso, carrega playlists do usuário atual, se ele for um usuário VIP, e atualiza os itens da lista.
     */
	public void atualizarPlaylists() {
		ArrayList<Playlist> playlistsCarregadas;
		try {
			playlistsCarregadas = PlaylistDAO.carregar((UsuarioVIP) TelaLoginController.getInstance().getUsuarioAtual());
			listaPlaylists.getItems().setAll(playlistsCarregadas);
		} catch (IOException e) {
			Alertas.showAlert("Erro", null, "Erro ao carregar playlists: " + e.getMessage(), Alert.AlertType.ERROR);
		}
		
	}
	
	/**
     * Define a ação para o clique no ícone.
     * Abre um menu de contexto com opções em relação a conta para o usuário, incluindo a
     * opção de ver as informações da conta e sair.
     */
	@FXML
	private void iconeAcao() {
		ContextMenu menu = menuUsuario();
		configuraClique(icone,menu);
		
	}
	
	/**
     * Cria e retorna um menu de contexto para o usuário.
     * O menu possui as opções 'Conta' para visualizar informações da conta
     * e 'Sair'.
     * 
     * @return ContextMenu O menu de contexto criado para o usuário.
     */
    protected ContextMenu menuUsuario() {
        ContextMenu menu = new ContextMenu();

        MenuItem opcao1 = new MenuItem("Conta");
        opcao1.setOnAction(event -> {
        	GerenciadorCenas.mudarCena("../visao/TelaConta.fxml");
        });

        MenuItem opcao2 = new MenuItem("Sair");
        opcao2.setOnAction(event -> {
            GerenciadorCenas.mudarCena("../visao/TelaLogin.fxml");
        });
        
        menu.getItems().addAll(opcao1, opcao2);

        return menu;
    }
    
    /**
     * Cria e retorna um menu de contexto para músicas.
     * O menu possui a opção 'Remover' para permitir a remoção
     * de uma música selecionada.
     * 
     * @return ContextMenu O menu de contexto criado para músicas.
     */
    private ContextMenu menuMusica() {
    	ContextMenu menu = new ContextMenu();
    	
    	MenuItem removerItem = new MenuItem("Remover");
    	menu.getItems().add(removerItem);

    	removerItem.setOnAction(event -> {
    	    Musica musicaSelecionada = listaMusicas.getSelectionModel().getSelectedItem();
    	    if (musicaSelecionada != null) {
    	    	removerMusica(musicaSelecionada);
    	    }
    	});
        

        return menu;
    }
    
    /**
     * Cria e retorna um menu de contexto para playlists.
     * O menu possui a opção 'Remover' para permitir a remoção
     * de uma playlist selecionada do usuário VIP.
     * 
     * @return ContextMenu O menu de contexto criado para playlists.
     */
    private ContextMenu menuPlaylist() {
    	ContextMenu menu = new ContextMenu();
    	
    	MenuItem removerItem = new MenuItem("Remover");
    	menu.getItems().add(removerItem);
    	
    	removerItem.setOnAction(event -> {
    	    Playlist playlistSelecionada = listaPlaylists.getSelectionModel().getSelectedItem();
    	    if (playlistSelecionada != null) {
    	    	removerPlaylist(playlistSelecionada);
    	    }
    	});
    	
        return menu;
    }
    
    /**
     * Recebe uma música e remove dos arquivos .txt e 
     * da lista de músicas na interface do usuário.
     * 
     * @param musicaSelecionada A música a ser removida.
     */
    private void removerMusica(Musica musicaSelecionada) {
    	try {
			MusicaDAO.remover(TelaLoginController.getInstance().getUsuarioAtual(), musicaSelecionada);
			listaMusicas.getItems().remove(musicaSelecionada);
		} catch (IOException e) {
			Alertas.showAlert("Erro", null, "Erro ao remover música: " + e.getMessage(), Alert.AlertType.ERROR);
		}
        
    }
    
    /**
     * Recebe uma playlist e remove dos arquivos .txt e 
     * da lista de playlists na interface do usuário.
     * 
     * @param playlistSelecionada A playlist a ser removida.
     */
    private void removerPlaylist(Playlist playlistSelecionada) {
    	try {
			PlaylistDAO.remover(playlistSelecionada, (UsuarioVIP) TelaLoginController.getInstance().getUsuarioAtual());
	        listaPlaylists.getItems().remove(playlistSelecionada);
		} catch (IOException e) {
			Alertas.showAlert("Erro", null, "Erro ao remover playlist: " + e.getMessage(), Alert.AlertType.ERROR);
		}
        
    }
   

    /**
     * Configura uma ação de clique para um objeto da interface do usuário, associando-o a um menu.
     * 
     * @param objeto O objeto para o qual a ação de clique será configurada.
     * @param menu O menu de contexto a ser exibido quando o objeto for clicado.
     */
    protected void configuraClique(Node objeto, ContextMenu menu) {
       objeto.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                menu.show(objeto, event.getScreenX(), event.getScreenY());
            }
        });
    }
	
    /**
     * Ação conectada a um elemento da interface do usuário para adicionar um diretório.
     * Permite que seja aberta uma janela do dispositivo para o usuário selecionar um diretório que 
     * é adicionado a lista de músicas desse usuário por meio do DiretorioDAO e a lista de músicas é atualizada.
     * Em caso de erro, um alerta é exibido.
     */
	@FXML
	private void adicionarDiretorioAcao() {
        File diretorio = escolherDiretorio();
  
        if(diretorio != null) {
	        try {
	           DiretorioDAO.adicionar(TelaLoginController.getInstance().getUsuarioAtual(), diretorio.getAbsolutePath());
	           atualizarMusicas();
	        } catch (ExcecaoPersonalizada e) {
	            Alertas.showAlert("Erro", e.getMessage(), "", Alert.AlertType.ERROR);
	        } catch (Exception e) {
	            Alertas.showAlert("Erro", "Erro ao remover a música.", e.getMessage(), Alert.AlertType.ERROR);
	        } 
        }
	}
	
	 /**
     * Ação conectada a um botão da interface do usário para remover um diretório.
     * Permite que seja aberta uma janela do dispositivo para o usuário selecionar um diretório que 
     * é removido, de forma que as músicas desse diretório são excluídas da lista de músicas desse 
     * usuário por meio de DiretorioDAO.
     * Em caso de erro, um alerta é exibido.
     */
	@FXML
	public void removerDiretorioAcao() {
        File diretorio = escolherDiretorio();
        
       if(diretorio != null) {
    	   try {
			DiretorioDAO.remover(diretorio.getAbsolutePath(), TelaLoginController.getInstance().getUsuarioAtual());
		} catch (IOException e) {
			Alertas.showAlert("Erro", null, "Erro ao remover diretório: " + e.getMessage(), Alert.AlertType.ERROR);
		}
           atualizarMusicas();
       }
	}
	
	/**
     * Permite a escolha de um diretório do dispositivo e retorna o diretório selecionado.
     * 
     * @return File O diretório escolhido pelo usuário, ou null se nenhum foi selecionado.
     */
    private File escolherDiretorio() {
    	DirectoryChooser buscaDiretorio = new DirectoryChooser();
    	buscaDiretorio.setTitle("Selecionar Diretório");
        return buscaDiretorio.showDialog(null);
    }
    
    /**
     * Permite a escolha de um arquivo do dispositivo por um FileChooser, que
     * é configurado para aceitar apenas arquivos MP3.
     * 
     * @return File O arquivo escolhido pelo usuário, ou null se nenhum foi selecionado.
     */
    private File escolherArquivo() {
    	FileChooser buscaArquivo = new FileChooser();
    	buscaArquivo.setTitle("Selecionar Música");
    	buscaArquivo.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Arquivos MP3", "*.mp3")
        );
    	return buscaArquivo.showOpenDialog(null);
    }
    
    /**
     * Ação conectada a um botão da interface gráfica para adicionar uma música.
     * Permite que seja aberta uma janela do dispositivo para que o usuário selecione
     * uma música que será adicionada à sua lista de músicas, se ainda não estiver presente.
     */
    @FXML
    private void adicionarMusicaAcao() {
  
        File arquivo = escolherArquivo();
        
        if(arquivo != null) {
        	
        	Musica novaMusica = new Musica(arquivo.getName(),arquivo.getAbsolutePath());
            try {
				MusicaDAO.adicionar(TelaLoginController.getInstance().getUsuarioAtual(), novaMusica);
				
				if(!listaMusicas.getItems().contains(novaMusica)) {
	            	listaMusicas.getItems().add(novaMusica);
	            }
				
			} catch (IOException e) {
				Alertas.showAlert("Erro", null, "Erro ao adicionar música: " + e.getMessage(), Alert.AlertType.ERROR);
			}
        }
    }
    
    /**
     * Ação conectada a um elemento da interface do usuário para adicionar uma playlist.
     * Abre uma nova janela para a criação de uma playlist.
     */
    @FXML
    private void adicionarPlaylistAcao() {
    	GerenciadorCenas.abrirNovaJanela("/visao/TelaAdicionarPlaylist.fxml");
    }
    
    /**
     * Retorna a instância atual do controlador da tela principal.
     * 
     * @return TelaPrincipalController A instância atual do controlador.
     */
	public static TelaPrincipalController getInstance() {
		return instance;
	}
	
	/**
     * Retorna a playlist atualmente selecionada ou em uso.
     * 
     * @return Playlist A playlist atual.
     */
	public Playlist getPlaylistAtual() {
		return playlistAtual;
	}
	
	/**
     * Retorna a lista de itens de música exibidos na interface do usuário.
     * 
     * @return ObservableList<Musica> A lista observável de músicas.
     */
	public ObservableList<Musica> getListaMusicaItems() {
        return listaMusicas.getItems();
    }
	
	/**
     * Inicia a reprodução da música atual.
     * Ativa um timer para atualizar o progresso da reprodução e inicia o MediaPlayer.
     */
	public void playMedia() {
		beginTimer();
		mediaPlayer.play();
	}
	
	/**
     * Controla a ação de reprodução de música com base na seleção atual.
     * Se for a primeira música, configura e inicia a reprodução.
     * Caso contrário, configura o MediaPlayer para a primeira música da lista e inicia a reprodução.
     */
	@FXML
	public void playMediaGeral() {
		if (controlePrimeiraMusica) {
			songLabel.setText(getListaMusicaItems().get(indexMusicaGeral).getNome());
			playMedia();
		}else {
			file = new File(getListaMusicaItems().get(0).getCaminhoArquivo());
			media = new Media(file.toURI().toString());
			mediaPlayer = new MediaPlayer(media);
			indexMusicaGeral = 0;
			controlePrimeiraMusica = true;
			songLabel.setText(getListaMusicaItems().get(indexMusicaGeral).getNome());
			playMedia();
		}
	}
	
	/**
     * Para a reprodução da música atual e reinicia a configuração do MediaPlayer.
     */
	@FXML
	public void stopMediaGeral() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			if(running) cancelTimer();
			
			file = new File(getListaMusicaItems().get(0).getCaminhoArquivo());
			media = new Media(file.toURI().toString());
			mediaPlayer = new MediaPlayer(media);
			indexMusicaGeral = 0;
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
	public void nextMediaGeral() {
		if(indexMusicaGeral < getListaMusicaItems().size() - 1) {			
			indexMusicaGeral++;
			if (mediaPlayer  != null) mediaPlayer.stop();
			if(running) cancelTimer();
			
			file = new File(getListaMusicaItems().get(indexMusicaGeral).getCaminhoArquivo());
			media = new Media(file.toURI().toString());
			mediaPlayer = new MediaPlayer(media);
			songLabel.setText(getListaMusicaItems().get(indexMusicaGeral).getNome());
			
			playMedia();
		}
		else {
			indexMusicaGeral = 0;
			if (mediaPlayer  != null) mediaPlayer.stop();
			
			file = new File(getListaMusicaItems().get(indexMusicaGeral).getCaminhoArquivo());
			media = new Media(file.toURI().toString());
			mediaPlayer = new MediaPlayer(media);
			songLabel.setText(getListaMusicaItems().get(indexMusicaGeral).getNome());
			
			playMedia();
		}
	}
	
	/**
     * Retrocede para a música anterior na lista.
     * Se estiver na primeira música, vai para a última e inicia a reprodução.
     */
	@FXML
	public void previousMediaGeral() {
		if (indexMusicaGeral == 0) {
			indexMusicaGeral = getListaMusicaItems().size() - 1;
			if (mediaPlayer  != null) mediaPlayer.stop();
			if(running) cancelTimer();
			
			file = new File(getListaMusicaItems().get(indexMusicaGeral).getCaminhoArquivo());
			media = new Media(file.toURI().toString());
			mediaPlayer = new MediaPlayer(media);
			songLabel.setText(getListaMusicaItems().get(indexMusicaGeral).getNome());
			
			playMedia();
		}else {
			indexMusicaGeral--;
			if (mediaPlayer  != null) mediaPlayer.stop();
			if(running) cancelTimer();
			
			file = new File(getListaMusicaItems().get(indexMusicaGeral).getCaminhoArquivo());
			media = new Media(file.toURI().toString());
			mediaPlayer = new MediaPlayer(media);
			songLabel.setText(getListaMusicaItems().get(indexMusicaGeral).getNome());
			
			playMedia();
		}
	}
	
	/**
     * Alterna o estado de mudo (mute) do MediaPlayer.
     * Se estiver em estado de mudo, desativa o mudo, e vice-versa.
     */
	public void muteMedia() {
	    if (mediaPlayer != null) {
	        mediaPlayer.setMute(!mediaPlayer.isMute()); // Inverte o estado de mudo (mute)
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
					Platform.runLater(() -> nextMediaGeral());
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

