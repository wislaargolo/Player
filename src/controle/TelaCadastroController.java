package controle;

import java.net.URL;
import java.util.ResourceBundle;

import dao.UsuarioDAO;
import excecoes.ExcecaoPersonalizada;
import javafx.fxml.FXML;
import javafx.stage.Stage;

import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import modelo.Usuario;
import modelo.UsuarioVIP;
import util.Alertas;

/**
 * Controlador para a tela de cadastro de usuários.
 * Essa classe gerencia o cadastro de novos usuários, permitindo criar contas comuns ou VIP.
 * 
 * @author Rubens e Wisla
 * 
 */
public class TelaCadastroController implements Initializable {
	@FXML
    private TextField fLogin, fNome;
	
    @FXML
    private PasswordField fSenha;
    
    @FXML
    private Button btCadastro, btVoltar;
    
    @FXML
    private ChoiceBox<String> tipoUsuario;
  
    /**
     * Inicializa o controlador automaticamente
     * após o carregamento do arquivo FXML associado.
     * Configura a ChoiceBox com as opções de tipo de usuário.
     * 
     * @param url URL utilizada para resolver caminhos relativos para o objeto raiz, ou null se desconhecido.
     * @param resourceBundle O recurso utilizado para localizar o objeto raiz, ou null se o objeto raiz não foi localizado.
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tipoUsuario.getItems().addAll("Comum", "VIP");
        tipoUsuario.getSelectionModel().selectFirst();
    }
    
    /**
     * Ação conectada ao botão de cadastro.
     * Cria um novo usuário com as informações fornecidas e o adiciona ao sistema.
     * Exibe um alerta em caso de erro ou confirmação de sucesso.
     */
    @FXML
    private void btCadastroAcao() {
    	
    	String login = fLogin.getText();
        String nome = fNome.getText();
        String senha = fSenha.getText();

        if (login.isEmpty() || nome.isEmpty() || senha.isEmpty()) {
            Alertas.showAlert("Erro", null, "Todos os campos devem ser preenchidos!", Alert.AlertType.ERROR);
            return ;
        }
        
    	
    	String tipoSelecionado = tipoUsuario.getSelectionModel().getSelectedItem();

        Usuario aux;
        
        if ("VIP".equals(tipoSelecionado)) {
            aux = new UsuarioVIP(login, nome, senha);
        } else {
            aux = new Usuario(login, nome, senha);
        }
        
        try {
        	UsuarioDAO.adicionar(aux);
        	Alertas.showAlert("Cadastro", null, "Usuário cadastrado com sucesso!", Alert.AlertType.INFORMATION);
        	((Stage) btCadastro.getScene().getWindow()).close();
        } catch (ExcecaoPersonalizada e) {
        	Alertas.showAlert("Erro", e.getMessage(), "", Alert.AlertType.ERROR);
        } catch (Exception e) {
        	Alertas.showAlert("Erro", e.getMessage(), "", Alert.AlertType.ERROR);
        }
        
    }
    
    
    

    
}
