package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import server.Server;

public class VControllerForgetPassword extends Controller {
	private Server server;
	@FXML
	private Button btnUpdatePassword;
	@FXML
	private Button btnBack;
	@FXML
	private TextField txtHash;
	@FXML
	private TextField txtNewPassword;
	@FXML
	private TextField txtConfirmNewPassword;

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	public VControllerForgetPassword() {
		Server server = new Server();
		this.server = server;
	}

	public void clickUpdatePassword(ActionEvent event) {
		// TODO: Implement auth hash
	}

	public void goLogin(ActionEvent event) {
		cambiarVentana(event, (Stage) ((Node) event.getSource()).getScene().getWindow(), "Login.fxml", "Login");
	}
}
