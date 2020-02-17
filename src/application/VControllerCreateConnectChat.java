package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import server.Server;

public class VControllerCreateConnectChat extends Controller {

	public static String ACCESS_CHAT;
	public static String PORT;
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
		boolean isPortEmpty = txtPort.getText().isEmpty();
		if (!isNewPasswordEmpty && !isPortEmpty) {
			String key = server.getKey();
			int port = Integer.parseInt(txtPort.getText());
			server.insertNewChatPassword(txtNewPassword.getText(), key, port);
			VControllerCreateConnectChat.PORT = txtPort.getText();
			changeStage(event, (Stage) ((Node) event.getSource()).getScene().getWindow(), "RoomChat.fxml", "Room Chat");
			return "Created room in port:" + txtPort.getText();
		} else {
			alert(AlertType.WARNING, "Error creating room", "", "Please empty the other textfield to create room.");
			return null;
		}
	}

	public String clickConnectRoom(ActionEvent event) {
		boolean isConnectPasswordEmpty = txtValidatorPassword.getText().isEmpty();
		if (!isConnectPasswordEmpty) {
			if (server.isRegisteredPassword(txtValidatorPassword.getText())) {
				ACCESS_CHAT = txtValidatorPassword.getText();
				changeStage(event, (Stage) ((Node) event.getSource()).getScene().getWindow(), "RoomChat.fxml",
						"Room Chat");
				return "Successful enter in Room.";
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
