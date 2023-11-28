package visao;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;

public class TelaPrincipalVIPController implements Initializable {
	
	@FXML
	private ImageView icone;
	 
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		
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

        menu.getItems().addAll(opcao1, opcao2);

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


