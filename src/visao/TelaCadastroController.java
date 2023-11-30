package visao;

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
import util.GerenciadorCenas;

public class TelaCadastroController implements Initializable {
	@FXML
    private TextField fLogin, fNome;
	
    @FXML
    private PasswordField fSenha;
    
    @FXML
    private Button btCadastro, btVoltar;
    
    @FXML
    private ChoiceBox<String> tipoUsuario;
  
 
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tipoUsuario.getItems().addAll("Comum", "VIP");
        tipoUsuario.getSelectionModel().selectFirst();
    }
    
    
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
