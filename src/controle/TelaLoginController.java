package controle;

import java.io.IOException;
import java.util.NoSuchElementException;

import dao.UsuarioDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import modelo.Usuario;
import util.Alertas;
import util.GerenciadorCenas;

/**
 * Controlador para a tela de login.
 * Esta classe gerencia a autenticação do usuário, permitindo o acesso à tela principal da aplicação.
 * 
 * @author Rubens e Wisla
 *  
 */
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
    
    /**
     * Construtor padrão que inicializa um novo usuário com valores padrão.
     */
    public TelaLoginController() {
    	usuarioAtual = new Usuario("","","");
    }
    
    /**
     * Autentica um usuário com base no login e senha fornecidos.
     * 
     * @param login O login do usuário.
     * @param senha A senha do usuário.
     * @return Usuario Retorna o usuário autenticado.
     * @throws NoSuchElementException Se os argumentos de login forem inválidos.
     */
    private Usuario autenticar(String login, String senha) {
    	try {
			for(Usuario usuario : UsuarioDAO.carregar()){
			    if(usuario.getId().equals(login) && usuario.getSenha().equals(senha)){
			      return usuario;
			    }
			}
		} catch (IOException e) {
			Alertas.showAlert("Erro", null, "Erro ao carregar usuários do sistema: " + e.getMessage(), Alert.AlertType.ERROR);
		}
        throw new NoSuchElementException("Argumentos de login inválidos");
    }

    /**
     * Ação conectada ao botão de login.
     * Autentica o usuário e, se bem-sucedido, muda para a tela principal.
     * Em caso de falha, exibe um alerta de erro.
     */
    @FXML
    private void btLoginAcao() {
    	String login = fLogin.getText();
        String senha = fSenha.getText();
        
        try {
        	Usuario usuarioAtual = autenticar(login, senha);
        	instance.setUsuarioAtual(usuarioAtual);
        	
        	GerenciadorCenas.mudarCena("../visao/TelaPrincipal.fxml");
        	
        } catch (NoSuchElementException e) {
        	Alertas.showAlert("Erro", e.getMessage(), "", Alert.AlertType.ERROR);
        }

    }
    
    /**
     * Ação vinculada ao hyperlink para cadastro.
     * Abre a tela de cadastro em uma nova janela.
     */
    @FXML
    private void hyperAcao() {
    	GerenciadorCenas.abrirNovaJanela("../visao/TelaCadastro.fxml");

    }
    
    /**
     * Retorna o usuário atualmente autenticado.
     * 
     * @return Usuario O usuário atual.
     */
    public Usuario getUsuarioAtual() {
        return usuarioAtual;
    }
    
    /**
     * Define o usuário atualmente autenticado.
     * 
     * @param usuario O usuário a ser definido como o atual.
     */
    public void setUsuarioAtual(Usuario usuario) {
        this.usuarioAtual = usuario;
    }

    /**
     * Retorna a instância atual do controlador de tela de login.
     * 
     * @return TelaLoginController A instância atual do controlador.
     */
	public static TelaLoginController getInstance() {
		// TODO Auto-generated method stub
		return instance;
	}
    
    
    
    
   
 
    
}
