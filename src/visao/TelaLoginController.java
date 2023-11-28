package visao;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import java.net.URL;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;

import dao.UsuarioDAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class TelaLoginController implements Initializable{

	@FXML
    private TextField fLogin;

    @FXML
    private PasswordField fSenha;
    
    @FXML
    private Button btLogin;
    
    private UsuarioDAO usuarios;
    
    public void initialize(URL url, ResourceBundle resourceBundle) {
        usuarios = UsuarioDAO.getInstance();
        usuarios.carregar();
    }

    @FXML
    private void btLoginAction() {
    	String login = fLogin.getText();
        String senha = fSenha.getText();
        
        System.out.println(login);
        
        try {
        	usuarios.autenticar(login, senha);
        } catch (NoSuchElementException e) {
      	  Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setHeaderText(e.getMessage());
            alerta.setTitle("Erro");
            alerta.show();
        }
    }
    
    
   
 
    
}
