package visao;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import dao.DiretorioDAO;
import dao.MusicaDAO;
import dao.PlaylistDAO;
import dao.UsuarioDAO;
import excecoes.ExcecaoPersonalizada;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
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

public class TelaPrincipalController implements Initializable {
	
	@FXML
	private ImageView icone;
	
	@FXML
    private ProgressBar progressoMusica;

    @FXML
    private Button btAnterior, btPlay, btProximo;

    @FXML
    private Button btMutar, btParar;
    
    @FXML
    private Button btAdicionarMusica;
    
    @FXML
    private Button btRemoverDiretorio;
    
    @FXML
    private Button btAdicionarDiretorio;

    @FXML
    private ListView<Musica> listaMusicas;
    
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    	DiretorioDAO.carregar(TelaLoginController.getInstance().getUsuarioAtual());
    	atualizarMusicas();
    	
		ContextMenu menu = menuMusica();
		listaMusicas.setContextMenu(menu);
	}
	
	@FXML
	public void iconeAcao() {
		ContextMenu menu = menuUsuario();
		configuraClique(icone,menu);
		
	}
	
	@FXML
	public void adicionarDiretorioAcao() {
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
	
	protected void atualizarMusicas() {
		ArrayList<Musica> musicasCarregadas = MusicaDAO.carregar(TelaLoginController.getInstance().getUsuarioAtual());
		 listaMusicas.getItems().removeIf(musica -> !musicasCarregadas.contains(musica));
		
	    for (Musica musica : musicasCarregadas) {
	        if (!listaMusicas.getItems().contains(musica)) {
	        	listaMusicas.getItems().add(musica);
	        }
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
	
    private ContextMenu menuUsuario() {
        ContextMenu menu = new ContextMenu();

        MenuItem opcao1 = new MenuItem("Conta");
        opcao1.setOnAction(event -> {
            // adicionar tela de infos da conta
        });

        MenuItem opcao2 = new MenuItem("Sair");
        opcao2.setOnAction(event -> {
            GerenciadorCenas.mudarCena("/visao/TelaLogin.fxml");
        });
        
        MenuItem opcao3 = new MenuItem("Excluir Conta");
        opcao3.setOnAction(event -> {
        	excluirUsuario();
        });

        menu.getItems().addAll(opcao1, opcao2, opcao3);

        return menu;
    }
    
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

    private void configuraClique(Node objeto, ContextMenu menu) {
       objeto.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                menu.show(objeto, event.getScreenX(), event.getScreenY());
            }
        });
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
            listaMusicas.getItems().add(novaMusica);
        }
    }
    
    private void removerMusica(Musica musicaSelecionada) {
    	Musica musicaSelecionada1 = listaMusicas.getSelectionModel().getSelectedItem();

    	MusicaDAO.remover(TelaLoginController.getInstance().getUsuarioAtual(), musicaSelecionada1);
        listaMusicas.getItems().remove(musicaSelecionada1);

        
    }

}


