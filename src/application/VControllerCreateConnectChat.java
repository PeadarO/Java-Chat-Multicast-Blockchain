package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import server.Server;

public class VControllerCreateConnectChat extends Controller {
	private Server server;
	@FXML
	private TextField txtNewPassword;
	@FXML
	private TextField txtPort;
	@FXML
	private TextField txtValidatorPassword;
	@FXML
	private Button btnCreateRoom;
	@FXML
	private Button btnConnectRoom;

	public VControllerCreateConnectChat() {
		Server server = new Server();
		this.server = server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	public String clickCreateRoom(ActionEvent event) {
		boolean isNewPasswordEmpty = txtNewPassword.getText().isEmpty();
		boolean isConnectPasswordEmpty = txtValidatorPassword.getText().isEmpty();
		if (!isNewPasswordEmpty && isConnectPasswordEmpty) {
			String key = null;// server.generatorKey();
			int port = Integer.parseInt(txtPort.getText());
			server.insertNewChatPassword(txtNewPassword.getText(), key, port);
			openWindow(event, (Node) event.getSource(), "App2.fxml", "Room chat", false);
			return "Created room in port:" + txtPort.getText();
		} else {
			alert(AlertType.WARNING, "Error creating room", "", "Please empty the other textfield to create room.");
			return null;
		}
	}

	public String clickConnectRoom(ActionEvent event) {
		boolean isNewPasswordEmpty = txtNewPassword.getText().isEmpty();
		boolean isConnectPasswordEmpty = txtValidatorPassword.getText().isEmpty();
		if (!isConnectPasswordEmpty && isNewPasswordEmpty) {
			if (server.isRegisteredPassword(txtValidatorPassword.getText())) {
				openWindow(event, (Node) event.getSource(), "App2.fxml", "Room chat", false);
				return "Successful enter in Room. PORT: " + txtPort.getText();
			} else {
				alert(Alert.AlertType.ERROR, "", "", "");
			}
		} else {
			alert(AlertType.WARNING, "Error connecting to the room", "",
					"Please empty the other textfield to connect room.");
		}
		return null;
	}

}
