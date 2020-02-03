package application;

import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import server.Server;

public class VControllerRegistro extends Controller {
	private Server server;
	@FXML
	private Button btnRegister;
	@FXML
	private Button btnCancel;
	@FXML
	private TextField txtNumber;
	@FXML
	private TextField txtEmail;
	@FXML
	private TextField txtPassword;
	@FXML
	private TextField txtConfirmPassword;
	@FXML
	private TextField txtName;

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	public VControllerRegistro() {
		Server server = new Server();
		this.server = server;
	}

	public void clickRegisterUser(ActionEvent event) {
		try {
			if (txtNumber.getText() != "0" && txtNumber.getText() != "" && txtEmail.getText() != ""
					&& txtPassword.getText() != "" && txtName.getText() != "" && txtNumber.getText() != null
					&& txtEmail.getText() != null && txtPassword.getText() != null && txtName.getText() != null) {

				if (txtPassword.getText().equals(txtConfirmPassword.getText())) {
					newUser();
					dialog(AlertType.INFORMATION, "Information", txtEmail.getText(), "Successful registered ");
					goLogin(event);

				} else {
					dialog(AlertType.INFORMATION, "Information", "Error", "The passwords do not match");
				}
			}
		} catch (Exception e) {
			dialog(AlertType.INFORMATION, "Information", "Error", "Ha habido un error en el registro del usuario");
		}
	}

	public void newUser() throws SQLException {
		if (!server.existeUsuario(Integer.parseInt(txtNumber.getText()))) {

			server.registerUser(Integer.parseInt(txtNumber.getText()), txtEmail.getText(), txtPassword.getText(),
					txtName.getText());
		} else {
			dialog(AlertType.INFORMATION, "Information", "Error",
					"This number: '" + txtNumber.getText() + "' is not available");
		}
	}

	public void goLogin(ActionEvent event) {
		cambiarVentana(event, (Stage) ((Node) event.getSource()).getScene().getWindow(), "Login.fxml", "Login");
	}
}
