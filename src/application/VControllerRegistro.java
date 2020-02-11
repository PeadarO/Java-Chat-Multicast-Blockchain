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
			boolean isNumberDistincThan0 = txtNumber.getText() != "0";
			boolean isNotEmptyNumber = txtNumber.getText() != "";
			boolean isNotEmptyEmail = txtEmail.getText() != "";
			boolean isNotEmptyPassword = txtPassword.getText() != "";
			boolean isNotEmptyName = txtName.getText() != "";
			boolean isNumberNotNull = txtNumber.getText() != null;
			boolean isEmailNotNull = txtEmail.getText() != null;
			boolean isPasswordNotNull = txtPassword.getText() != null;
			boolean isNameNotNull = txtName.getText() != null;
			boolean isPasswordEqualConfirmPassword = txtPassword.getText().equals(txtConfirmPassword.getText());

			if (isNumberDistincThan0 && isNotEmptyNumber && isNotEmptyEmail && isNotEmptyPassword && isNotEmptyName
					&& isNumberNotNull && isEmailNotNull && isPasswordNotNull && isNameNotNull) {

				if (isPasswordEqualConfirmPassword) {
					createNewUser();
					alert(AlertType.INFORMATION, "Information", txtEmail.getText(), "Successful registered ");
					goLogin(event);

				} else {
					alert(AlertType.INFORMATION, "Information", "Error", "The passwords do not match");
				}
			}
		} catch (Exception e) {
			alert(AlertType.INFORMATION, "Information", "Error", "Ha habido un error en el registro del usuario");
		}
	}

	public void createNewUser() throws SQLException {
		int number = Integer.parseInt(txtNumber.getText());
		if (!server.isRegisteredUser(number)) {
			server.registerUser(number, txtEmail.getText(), txtPassword.getText(), txtName.getText());
			
		} else {
			alert(AlertType.INFORMATION, "Information", "Error",
					"This number: '" + txtNumber.getText() + "' is not available");
		}
	}

	public void goLogin(ActionEvent event) {
		changeStage(event, (Stage) ((Node) event.getSource()).getScene().getWindow(), "Login.fxml", "Login");
	}
}
