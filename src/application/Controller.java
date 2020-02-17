package application;

import java.io.IOException;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import server.Server;

public class Controller {
	private Server server;
	private static String userlogged;
	static protected int selecionado;

	public Server getServer() {
		return server;
	}

	public void setModel(Server model) {
		this.server = model;
	}

	public Controller() {
		Server server = new Server();
		this.server = server;
		new FXMLLoader();

	}

	public String getusernameLogged(String myusername) {
		userlogged = myusername;
		return userlogged;
	}

	public String getId() {
		return userlogged;
	}

	public void openWindow(ActionEvent event, Node node, String fxml, String title, boolean hide) {
		Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource(fxml));
			Stage stage = new Stage();
			stage.setTitle(title);
			stage.setScene(new Scene(root));
			stage.getIcons().add(new Image("file:Resources/logo.png"));
			stage.setResizable(false);
			if (hide) {
				((Node) (event.getSource())).getScene().getWindow().hide();

			} else {
				((Node) (event.getSource())).getScene().getWindow();
			}
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void changeStage(ActionEvent event, Stage stage, String fxml, String title) {
		Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource(fxml));
			stage.setTitle(title);
			stage.setScene(new Scene(root));
			stage.setHeight(stage.getHeight());
			stage.setWidth(stage.getWidth());
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Optional<ButtonType> alert(AlertType type, String title, String header, String content) {
		Alert alert = new Alert(type);
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("file:Resources/images/logo.png"));
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.initStyle(StageStyle.UTILITY);
		Optional<ButtonType> option = alert.showAndWait();
		return option;
	}

	public void cerrarSesion(ActionEvent event, int number) {
		server.logout(number);
		openWindow(event, (Node) event.getSource(), "Login.fxml", "Inicar sesion", true);
		cancelar(event);
	}

	public void cancelar(ActionEvent event) {
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.close();

	}
}
