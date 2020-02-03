package application;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import server.Interfaz;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.image.Image;

public class Client extends Application {

	private Stage primaryStage;
	private Pane rootLayout;

	@Override
	public void start(Stage primaryStage) {
		try {
			this.primaryStage = primaryStage;
			this.primaryStage.setTitle("Iniciar Sesion");
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Client.class.getResource("Login.fxml"));
			rootLayout = (Pane) loader.load();
			// Show the scene containing the root layout.
			Scene scene = new Scene(rootLayout);
			scene.getStylesheets().add(getClass().getResource("SecureApp.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.getIcons().add(new Image("file:Resources/logo.png"));
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Interfaz chat = null;
		try {
			Registry registry = LocateRegistry.getRegistry("localhost", 5557);
			System.out.println("Obteniendo el stub del objeto remoto");
			chat = (Interfaz) registry.lookup("Chat");
		} catch (Exception e) {
			System.out.println("---------------------------------------");
			System.out.println("SERVIDOR NO ENCONTRADO");
			System.exit(-1);
		}
		if (chat != null) {
			System.out.println("ABRIENDO APLICACION");
			// lanzamos aplicacion visual
			launch(args);
			System.out.println("Java chat cerrado");

		}
	}
}
