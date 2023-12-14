package controle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import dao.UsuarioDAO;
import excecoes.ExcecaoPersonalizada;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import modelo.UsuarioVIP;
import util.Alertas;
import util.GerenciadorCenas;

/**
 * Controlador para a tela de informações da conta do usuário.
 * Essa classe gerencia a exibição de detalhes da conta do usuário,
 * incluindo seu nome, login e status VIP, bem como fornece opções para remover a conta, voltar à tela principal ou sair da conta.
 * 
 * @author Rubens e Wisla
 * 
 */
public class TelaContaController implements Initializable{

    @FXML
    private Label loginUsuario;

    @FXML
    private Label nomeUsuario;
    
    @FXML
    private ImageView iconVip;
    
    /**
     * Inicializa o controlador automaticamente
     * após o carregamento do arquivo FXML associado.
     * Configura a exibição das informações do usuário e do ícone VIP.
     * 
     * @param arg0 URL utilizada para resolver caminhos relativos para o objeto raiz, ou null se desconhecido.
     * @param arg1 O recurso utilizado para localizar o objeto raiz, ou null se o objeto raiz não foi localizado.
     */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		nomeUsuario.setText(TelaLoginController.getInstance().getUsuarioAtual().getNome());
		loginUsuario.setText(TelaLoginController.getInstance().getUsuarioAtual().getId());
		
		if(!(TelaLoginController.getInstance().getUsuarioAtual() instanceof UsuarioVIP)) {
			iconVip.setVisible(false);
		} 
	}


	/**
     * Ação conectada ao botão para remover a conta do usuário.
     * Remove o usuário atual do sistema e redireciona para a tela de login.
     */
    @FXML
    void btRemoverContaAcao() {
	    try {
	        UsuarioDAO.remover(TelaLoginController.getInstance().getUsuarioAtual());
	        GerenciadorCenas.mudarCena("../visao/TelaLogin.fxml");
	    } catch(ExcecaoPersonalizada e) {
	        Alertas.showAlert("Erro", e.getMessage(), "", Alert.AlertType.ERROR);
	    } catch (IOException e) {
	    	Alertas.showAlert("Erro", "Erro ao remover usuário: "+  e.getMessage(), "", Alert.AlertType.ERROR);
	    }
    }
    
    /**
     * Ação conectada ao botão para voltar à tela principal.
     * Muda a cena para a tela principal da aplicação.
     */
    @FXML
    void btVoltarAcao() { 
    	GerenciadorCenas.mudarCena("../visao/TelaPrincipal.fxml");
    }
    
    /**
     * Ação conectada ao botão para sair da conta.
     * Muda a cena para a tela de login da aplicação.
     */
    @FXML
    void btSairAcao() { 
    	GerenciadorCenas.mudarCena("../visao/TelaLogin.fxml");
    }

}
