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
		server.login(Integer.parseInt(txtNumber.getText()), txtPassword.getText());
		if (server.isLogin()) {
			getusernameLogged(txtNumber.getText());
			mostrarVentana(event, (Node) event.getSource(), "App.fxml", "Chat Cifrado", false, true, -1);
		} else {
			dialog(AlertType.INFORMATION, "Informacion", "Error", "Usuario o contrase�a incorrectos");
		}
	}

	public void clickRegistro(ActionEvent event) {
		cambiarVentana(event, (Stage) ((Node) event.getSource()).getScene().getWindow(), "Registro.fxml", "Registro");
	}
}
