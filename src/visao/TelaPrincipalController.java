package visao;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import dao.MusicaDAO;
import dao.UsuarioDAO;
import excecoes.ExcecaoPersonalizada;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.stage.FileChooser;
import modelo.Musica;

public class TelaPrincipalController implements Initializable {
	
	@FXML
	private ImageView icone;
	
	@FXML
    private ProgressBar progressoMusica;

    @FXML
    private Button btAnterior;
    
    @FXML
    private Button btPlay;
    
    @FXML
    private Button btProximo;
    
    @FXML
    private Button btMutar;

    @FXML
    private Button btParar;
    
    @FXML
    private Button btAdicionarMusica, btRemoverMusica;
    
    @FXML
    private ListView<Musica> listaMusicas;
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    	for (Musica musica : MusicaDAO.carregar(TelaLoginController.getUsuarioAtual())) {
            listaMusicas.getItems().add(musica);
        }

	}
	
	@FXML
	public void iconeAcao() {
		ContextMenu menu = criarMenu();
        configurarClique(icone, menu);
		
	}

	private void excluirUsuario() {
	    try {
	        UsuarioDAO.remover(TelaLoginController.getUsuarioAtual());
	        GerenciadorCenas.mudarCena("/visao/TelaLogin.fxml");
	    } catch(ExcecaoPersonalizada e) {
	        Alertas.showAlert("Erro", e.getMessage(), "", Alert.AlertType.ERROR);
	    }
	}
	
    private ContextMenu criarMenu() {
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

    private void configurarClique(ImageView icone, ContextMenu menu) {
        icone.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                menu.show(icone, event.getScreenX(), event.getScreenY());
            }
        });
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
    public void btAdicionarAcao() {
  
        File arquivo = escolherArquivo();

        try {
            Musica novaMusica = new Musica(arquivo.getName(),arquivo.getAbsolutePath());
            MusicaDAO.adicionar(TelaLoginController.getUsuarioAtual(), novaMusica);
            listaMusicas.getItems().add(novaMusica);
        } catch (ExcecaoPersonalizada e) {
        	Alertas.showAlert("Atenção", e.getMessage(), "", Alert.AlertType.INFORMATION);
        } catch (Exception e) {
        	Alertas.showAlert("Erro", e.getMessage(), "", Alert.AlertType.ERROR);
        }
    }
    
    @FXML
    public void btRemoverAcao() {
  
        
    }

}


