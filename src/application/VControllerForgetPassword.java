package application;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
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
		try {
			if (txtNewPassword.getText().equals(txtConfirmNewPassword.getText())) {
				server.changePassword(txtHash.getText(), txtNewPassword.getText());
				dialog(AlertType.INFORMATION, "Information", "Update password successful",
						"Now your password is: " + txtNewPassword.getText());
				goLogin(event);
			} else {
				dialog(Alert.AlertType.INFORMATION, "Information", "Error", "Please check your password!!!");
			}
		} catch (RemoteException ex) {
			Logger.getLogger(VControllerForgetPassword.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void goLogin(ActionEvent event) {
		cambiarVentana(event, (Stage) ((Node) event.getSource()).getScene().getWindow(), "Login.fxml", "Login");
	}
}
