package controle;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import auxiliar.BotoesMusica;
import dao.DiretorioDAO;
import dao.MusicaDAO;
import dao.PlaylistDAO;
import dao.UsuarioDAO;
import excecoes.ExcecaoPersonalizada;
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
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import modelo.Musica;
import modelo.Playlist;
import modelo.Usuario;
import modelo.UsuarioVIP;
import util.Alertas;
import util.GerenciadorCenas;

public class TelaPrincipalController implements Initializable {
	
	@FXML
	private ImageView icone;
	
	@FXML
	private Label tituloPlaylist;
	
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
    private ListView<Playlist> listaPlaylists;
    
    private static TelaPrincipalController instance = new TelaPrincipalController();
    
    private Usuario usuarioAtual = TelaLoginController.getInstance().getUsuarioAtual();
    
    private Playlist playlistAtual;
    
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    	DiretorioDAO.carregar(usuarioAtual);
    	atualizarMusicas();
    	
		ContextMenu menu = menuMusica();
		listaMusicas.setContextMenu(menu);
		
		 if (usuarioAtual instanceof UsuarioVIP) {
			btPlaylist.setVisible(true);
			tituloPlaylist.setVisible(true);
		    listaPlaylists.setVisible(true);
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
	    	listaPlaylists.setVisible(false);
	    	btPlaylist.setVisible(false);
	    	tituloPlaylist.setVisible(false);
	    }
		 
		 instance.listaMusicas = listaMusicas;
		 
	}
    
	private void atualizarMusicas() {
		ArrayList<Musica> musicasCarregadas = MusicaDAO.carregar(TelaLoginController.getInstance().getUsuarioAtual());
		listaMusicas.getItems().setAll(musicasCarregadas);
	}
	
	public void atualizarPlaylists() {
		ArrayList<Playlist> playlistsCarregadas = PlaylistDAO.carregar((UsuarioVIP) usuarioAtual);
		listaPlaylists.getItems().setAll(playlistsCarregadas);
	}
	
	@FXML
	public void iconeAcao() {
		ContextMenu menu = menuUsuario();
		configuraClique(icone,menu);
		
	}
	
    private ContextMenu menuUsuario() {
        ContextMenu menu = new ContextMenu();

        MenuItem opcao1 = new MenuItem("Conta");
        opcao1.setOnAction(event -> {
            // adicionar tela de infos da conta
        });

        MenuItem opcao2 = new MenuItem("Sair");
        opcao2.setOnAction(event -> {
            GerenciadorCenas.mudarCena("../visao/TelaLogin.fxml");
        });
        
        MenuItem opcao3 = new MenuItem("Excluir Conta");
        opcao3.setOnAction(event -> {
        	excluirUsuario();
        });

        menu.getItems().addAll(opcao1, opcao2, opcao3);

        return menu;
    }
    
    protected ContextMenu menuMusica() {
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
    
    private ContextMenu menuPlaylist() {
    	ContextMenu menu = new ContextMenu();
    	
    	MenuItem removerItem = new MenuItem("Remover");
    	menu.getItems().add(removerItem);
    	
    	removerItem.setOnAction(event -> {
    	    Playlist playlistSelcionada = listaPlaylists.getSelectionModel().getSelectedItem();
    	    if (playlistSelcionada != null) {
    	    	removerPlaylist(playlistSelcionada);
    	    }
    	});
    	
    	MenuItem editarItem = new MenuItem("Editar");
    	menu.getItems().add(editarItem);

    	editarItem.setOnAction(event -> {
    	    Playlist playlistSelcionada = listaPlaylists.getSelectionModel().getSelectedItem();
    	    if (playlistSelcionada != null) {
    	    	editarPlaylist(playlistSelcionada);
    	    }
    	});
        

        return menu;
    }
    
    
    private void removerMusica(Musica musicaSelecionada) {
    	MusicaDAO.remover(usuarioAtual, musicaSelecionada);
        listaMusicas.getItems().remove(musicaSelecionada);

        
    }
    
    private void removerPlaylist(Playlist playlistSelecionada) {
    	PlaylistDAO.remover(playlistSelecionada, (UsuarioVIP) usuarioAtual);
        listaPlaylists.getItems().remove(playlistSelecionada);
        
    }
    
    private void editarPlaylist(Playlist playlistSelecionada) 
    {
    	instance.playlistAtual = playlistSelecionada;
    	GerenciadorCenas.abrirNovaJanela("/visao/TelaEditarPlaylist.fxml");
        
    }

    private void configuraClique(Node objeto, ContextMenu menu) {
       objeto.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                menu.show(objeto, event.getScreenX(), event.getScreenY());
            }
        });
    }
    
	
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
	
	@FXML
	public void removerDiretorioAcao() {
        File diretorio = escolherDiretorio();
        
       if(diretorio != null) {
    	   DiretorioDAO.remover(diretorio.getAbsolutePath(), TelaLoginController.getInstance().getUsuarioAtual());
           atualizarMusicas();
       }
	}

	private void excluirUsuario() {
	    try {
	        UsuarioDAO.remover(TelaLoginController.getInstance().getUsuarioAtual());
	        GerenciadorCenas.mudarCena("/visao/TelaLogin.fxml");
	    } catch(ExcecaoPersonalizada e) {
	        Alertas.showAlert("Erro", e.getMessage(), "", Alert.AlertType.ERROR);
	    }
	}
	
    private File escolherDiretorio() {
    	DirectoryChooser buscaDiretorio = new DirectoryChooser();
    	buscaDiretorio.setTitle("Selecionar Diretório");
        return buscaDiretorio.showDialog(null);
    }
    
    private File escolherArquivo() {
    	FileChooser buscaArquivo = new FileChooser();
    	buscaArquivo.setTitle("Selecionar Música");
    	buscaArquivo.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Arquivos MP3", "*.mp3")
        );
    	return buscaArquivo.showOpenDialog(null);
    }
    
    @FXML
    private void adicionarMusicaAcao() {
  
        File arquivo = escolherArquivo();
        
        if(arquivo != null) {
        	
        	Musica novaMusica = new Musica(arquivo.getName(),arquivo.getAbsolutePath());
            MusicaDAO.adicionar(TelaLoginController.getInstance().getUsuarioAtual(), novaMusica);
            if(!listaMusicas.getItems().contains(novaMusica)) {
            	listaMusicas.getItems().add(novaMusica);
            }
        }
    }
    
    @FXML
    private void adicionarPlaylistAcao() {
    	GerenciadorCenas.abrirNovaJanela("/visao/TelaAdicionarPlaylist.fxml");
    }
    
	public static TelaPrincipalController getInstance() {
		return instance;
	}
	
	public Playlist getPlaylistAtual() {
		return playlistAtual;
	}
	
	public ObservableList<Musica> getListaMusicaItems() {
        return listaMusicas.getItems();
    }
	
	BotoesMusica botoesMusica = new BotoesMusica();
	int indexMusicaGeral = 0;
	int indexMusicaPlaylist = 0;

    @FXML
    public void tocarMusicasGerais() {
    	for (; indexMusicaGeral < getListaMusicaItems().size(); indexMusicaGeral++) {
    		botoesMusica.comcarMusica(getListaMusicaItems().get(indexMusicaGeral));
    		if (indexMusicaGeral != getListaMusicaItems().size() -1) {
    			indexMusicaGeral = 0;
    		}
    	}  	
    }
    
    @FXML
    public void passarMusicaGeral() {
    	if (indexMusicaGeral == getListaMusicaItems().size() -1) {
    		botoesMusica.stopMusic();
        	indexMusicaGeral = 0;
        	tocarMusicasGerais();
    	} else {
    		botoesMusica.stopMusic();
        	indexMusicaGeral++;
        	tocarMusicasGerais();
    	}
    }
    
    @FXML
    public void voltarMusicaGeral() {
    	if (indexMusicaGeral == 0) {
    		botoesMusica.stopMusic();
        	indexMusicaGeral = getListaMusicaItems().size() -1;
        	tocarMusicasGerais();
    	} else {
    		botoesMusica.stopMusic();
        	indexMusicaGeral--;
        	tocarMusicasGerais();
    	}
    }
    
    @FXML
    public void tocarMusicasPlaylist() {
    	int qtdDeItens = playlistAtual.getMusicas().size();
    	for (; indexMusicaPlaylist < qtdDeItens; indexMusicaPlaylist++) {
    		botoesMusica.comcarMusica(playlistAtual.getMusicas().get(indexMusicaPlaylist));
    		if (indexMusicaPlaylist != qtdDeItens -1) {
    			indexMusicaPlaylist = 0;
    		}
    	}
    }
    
    @FXML
    public void passarMusicaPlaylist() {
    	if (indexMusicaPlaylist == playlistAtual.getMusicas().size() -1) {
    		botoesMusica.stopMusic();
    		indexMusicaPlaylist = 0;
    		tocarMusicasPlaylist();
    	}else {
    		botoesMusica.stopMusic();
    		indexMusicaPlaylist++;
    		tocarMusicasPlaylist();
    	}
    }
    
    @FXML
    public void voltarMusicaPlaylist() {
    	if (indexMusicaPlaylist == 0) {
    		botoesMusica.stopMusic();
    		indexMusicaPlaylist = playlistAtual.getMusicas().size() -1;
    		tocarMusicasPlaylist();
    	}else {
    		botoesMusica.stopMusic();
    		indexMusicaPlaylist--;
    		tocarMusicasPlaylist();
    	}
    }

    @FXML
    public void parar() {
    	botoesMusica.stopMusic();
    }

}

