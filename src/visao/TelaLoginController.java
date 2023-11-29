package visao;

import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import java.net.URL;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;

import dao.UsuarioDAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import modelo.Usuario;
import modelo.UsuarioVIP;

public class TelaLoginController implements Initializable{

	@FXML
    private TextField fLogin;

    @FXML
    private PasswordField fSenha;
    
    @FXML
    private Button btLogin;
    
    @FXML
    private Hyperlink cadastro;
    
    private Usuario usuarioAtual;
    private ArrayList<Usuario> usuarios;
    
    public void initialize(URL url, ResourceBundle resourceBundle) {
        usuarios = UsuarioDAO.carregar();
    }
    
    private Usuario autenticar(String login, String senha) {
    	for(Usuario usuario : usuarios){
            if(usuario.getId().equals(login) && usuario.getSenha().equals(senha)){
              return usuario;
            }
        }
        throw new NoSuchElementException("Argumentos de login inválidos");
    }

    @FXML
    private void btLoginAction() {
    	String login = fLogin.getText();
        String senha = fSenha.getText();
        
        try {
        	usuarioAtual = autenticar(login, senha);
        	
        	if(usuarioAtual instanceof UsuarioVIP) {
            	GerenciadorCenas.mudarCena("/visao/TelaPrincipalVIP.fxml");
            } else {
            	GerenciadorCenas.mudarCena("/visao/TelaPrincipal.fxml");
            }
        	
        } catch (NoSuchElementException e) {
        	Alertas.showAlert("Erro", e.getMessage(), "", Alert.AlertType.ERROR);
        }

    }
    
    @FXML
    private void hyperAcao() {
    	GerenciadorCenas.mudarCena("/visao/TelaCadastro.fxml");
    }
    
    
    
    
   
 
    
}
