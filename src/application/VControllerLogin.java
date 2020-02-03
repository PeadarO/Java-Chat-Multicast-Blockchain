package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import server.Server;

public class VControllerLogin extends Controller {

	private Server server;
	@FXML
	private TextField txtNumber;
	@FXML
	private TextField txtPassword;
	@FXML
	private Button btnLogin;
	@FXML
	private Button btnRegistrarUsuario;

	public VControllerLogin() {
		Server server = new Server();
		this.server = server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	public void clickLogin(ActionEvent event) {
		if (txtNumber.getText().equals("")) {
			dialog(AlertType.INFORMATION, "Information", "Error", "Telephone field is empty");

			return;

		}
		if (server.login(Integer.parseInt(txtNumber.getText()), txtPassword.getText())) {
			if (server.isLogin()) {
				System.out.println("LOGEADO");
				getusernameLogged(txtNumber.getText());
				mostrarVentana(event, (Node) event.getSource(), "App.fxml", "Cipher Chat", false, true, -1);
			} else {
				dialog(AlertType.INFORMATION, "Information", "Error", "Incorrect number or password");
			}
		} else {
			dialog(AlertType.INFORMATION, "Information", "Error", "Please insert number and password");
		}
	}

	public void clickRegistro(ActionEvent event) {
		cambiarVentana(event, (Stage) ((Node) event.getSource()).getScene().getWindow(), "Registro.fxml", "Registro");
	}
}
