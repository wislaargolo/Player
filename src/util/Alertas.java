package util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Classe Alertas para facilitar a criação de alertas na interface de usuário.
 */
public class Alertas {

	/**
     * Exibe um alerta com as informações especificadas.
     *
     * @param title   O título do alerta.
     * @param header  O cabeçalho do alerta, ou seja, uma explicação breve sobre o alerta.
     * @param content O conteúdo principal do alerta, contendo a mensagem a ser exibida.
     * @param type    O tipo do alerta, como definido em {@link AlertType}.
     */
	public static void showAlert(String title, String header, String content, AlertType type) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.show();
	}
}
