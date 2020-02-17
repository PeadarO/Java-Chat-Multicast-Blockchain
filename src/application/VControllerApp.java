package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import server.Server;

public class VControllerApp extends Controller {
	private Server server;
	@FXML
	private Button btnSearch;
	@FXML
	private Button btnRegisterConnection;
	@FXML
	private Button btnUpdateConnection;
	@FXML
	private Button btnDeleteConnection;
	@FXML
	private Button btnDeleteAll;
	@FXML
	private Button btnRefresh;
	@FXML
	private Button btnProfile;
	@FXML
	private Button btnLogout;
	@FXML
	private TextField txtSearchConnection;
	@FXML
	private Label lblUser;

	@FXML
	private void initialize() {
		lblUser.setText("Bienvenido " + getId().toUpperCase());
	}

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	public VControllerApp() {
		Server server = new Server();
		this.server = server;
	}
	
	public void clickShowPassword(ActionEvent event) {
		alert(AlertType.INFORMATION, "Room Password", "", "Password: ");
	}

	public void clickProfile(ActionEvent event) {
		openWindow(event, (Node) event.getSource(), "Profile.fxml", "User Profile", false);
	}

	public void clickLogOut(ActionEvent event) {
		int getNumber = Integer.parseInt(getId());
		cerrarSesion(event, getNumber);
	}
}
