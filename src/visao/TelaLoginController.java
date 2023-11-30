package visao;

import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;

import java.util.NoSuchElementException;

import dao.UsuarioDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import modelo.Usuario;
import modelo.UsuarioVIP;
import util.Alertas;
import util.GerenciadorCenas;

public class TelaLoginController {

	@FXML
    private TextField fLogin;

    @FXML
    private PasswordField fSenha;
    
    @FXML
    private Button btLogin;
    
    @FXML
    private Hyperlink cadastro;
    
    private Usuario usuarioAtual;
    
    private static TelaLoginController instance = new TelaLoginController();
    
    
    public TelaLoginController() {
    	usuarioAtual = new Usuario("","","");
    }
    
    private Usuario autenticar(String login, String senha) {
    	for(Usuario usuario : UsuarioDAO.carregar()){
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
        	Usuario usuarioAtual = autenticar(login, senha);
        	instance.setUsuarioAtual(usuarioAtual);
        	
        	GerenciadorCenas.mudarCena("/visao/TelaPrincipal.fxml");
        	
        } catch (NoSuchElementException e) {
        	Alertas.showAlert("Erro", e.getMessage(), "", Alert.AlertType.ERROR);
        }

    }
    
    @FXML
    private void hyperAcao() {
    	GerenciadorCenas.abrirNovaJanela("/visao/TelaCadastro.fxml");
    }
    
    public Usuario getUsuarioAtual() {
        return usuarioAtual;
    }
    
    public void setUsuarioAtual(Usuario usuario) {
        this.usuarioAtual = usuario;
    }

	public static TelaLoginController getInstance() {
		// TODO Auto-generated method stub
		return instance;
	}
    
    
    
    
   
 
    
}
