package visao;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import dao.MusicaDAO;
import dao.UsuarioDAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
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
    private ListView<String> listaMusicas;
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    	for (Musica musica : MusicaDAO.carregar(TelaLoginController.getUsuarioAtual())) {
            listaMusicas.getItems().add(musica.getNome());
        }

		
	}
	
	@FXML
	public void iconeAcao() {
		ContextMenu menu = criarMenu();
        configurarClique(icone, menu);
		
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
        	UsuarioDAO.remover(TelaLoginController.getUsuarioAtual());
            GerenciadorCenas.mudarCena("/visao/TelaLogin.fxml");
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

}


