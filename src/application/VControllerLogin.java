package application;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
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
		boolean isNumberEmpty = txtNumber.getText().equals("");
		boolean isGoodLogin = server.isLogin(Integer.parseInt(txtNumber.getText()), txtPassword.getText());

		if (isNumberEmpty) {
			alert(AlertType.INFORMATION, "Information", "Error", "Telephone number field is empty");
			return;
		}
		if (isGoodLogin) {
			System.out.println("LOGEADO numero:" + txtNumber.getText());
			getusernameLogged(txtNumber.getText());
			openWindow(event, (Node) event.getSource(), "App2.fxml", "Cipher Chat", true, -1);

		} else {
			alert(AlertType.INFORMATION, "Information", "Error", "Please insert number and password");
		}
	}

	public void clickRegistro(ActionEvent event) {
		changeStage(event, (Stage) ((Node) event.getSource()).getScene().getWindow(), "Registro.fxml", "Register");
	}

	public void clickForgetPassword(ActionEvent event) {
		if (txtNumber.getText().equals("")) {
			alert(AlertType.INFORMATION, "Information", "We need yout telephone number", "Telephone field is empty");
			return;
		}
		try {
			if (!server.resetPassword(Integer.parseInt(txtNumber.getText()))) {
				alert(AlertType.INFORMATION, "Information", "Please check your mail!",
						"If your dont receive a message with a code, please reset your internet or try to change your wifi network.");
			}
		} catch (RemoteException ex) {
			Logger.getLogger(VControllerLogin.class.getName()).log(Level.SEVERE, null, ex);
		}
		changeStage(event, (Stage) ((Node) event.getSource()).getScene().getWindow(), "ForgetPassword.fxml",
				"Forget Password");
	}
}
