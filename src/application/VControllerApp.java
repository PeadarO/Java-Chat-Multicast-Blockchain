package application;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import server.Server;

public class VControllerApp extends Controller {

	private Server server;
	@FXML
	private Button btnProfile;
	@FXML
	private TextField txtMessage;
	@FXML
	private Button btnLogout;
	@FXML
	private Label lblUser;
	@FXML
	private TextArea textoFinal;
	@FXML
	private Button send;
	private String direccion;
	static String name;
	MulticastSocket socket;
	static volatile boolean finished = false;
	InetAddress group;

	int port;
	String key;
	String texto = "";

	/*
	 * @FXML private void initialize() { lblUser.setText("Bienvenido " +
	 * getId().toUpperCase()); }
	 */
	public VControllerApp() {
		Server server = new Server();
		this.server = server;
		direccion = "239.0.0.0";
		// Aqui os lo he preparado para que le paseis el nombre del usuario y el puerto
		// al que se va a conectar
		if (VControllerCreateConnectChat.PORT != null)
			chat(VControllerCreateConnectChat.PORT, getId());
		else {
			String[] chatParameters = server.getKeyAndPort(VControllerCreateConnectChat.ACCESS_CHAT);
			port = Integer.parseInt(chatParameters[0]);
			key = chatParameters[1];
			System.out.println("PUERTO Y KEY -> " + port + "" + key);
			chat(chatParameters[0], getId());
			// initialize();
		}

	}

	public void chat(String puerto, String name) {
		try {
			System.out.println("hola desde chat");
			group = InetAddress.getByName(direccion);
			port = Integer.parseInt(puerto);
			VControllerApp.name = name;
			socket = new MulticastSocket(port);
			socket.setTimeToLive(0);
			socket.joinGroup(group);

			Thread t2 = new Thread(() -> {
				while (true) {
					byte[] buffer = new byte[1000];
					DatagramPacket datagram = new DatagramPacket(buffer, buffer.length, group, port);
					String message;
					try {
						socket.receive(datagram);
						message = new String(buffer, 0, datagram.getLength(), "UTF-8");
						String decrypt = server.decrypt(key, message);
						texto += decrypt + "\n";
						textoFinal.setText(texto);
					} catch (IOException e) {
						System.out.println("Socket closed!");
					}
				}

			});

			System.out.println("Starting Thread.");
			t2.start();
		} catch (IOException ex) {
			Logger.getLogger(VControllerApp.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	public void clickProfile(ActionEvent event) {
		openWindow(event, (Node) event.getSource(), "Profile.fxml", "User Profile", false);
	}

	public void clickShowInformation(ActionEvent event) {
		alert(AlertType.INFORMATION, "Information of room chat",
				"Chat password: " + VControllerCreateConnectChat.ACCESS_CHAT, "Port in use: " + port);
	}

	public void clickLogOut(ActionEvent event) {
		int getNumber = Integer.parseInt(getId());
		cerrarSesion(event, getNumber);
	}

	@FXML
	private void sendClicked(MouseEvent event) {
		String message = txtMessage.getText();
		System.out.println(message);
		txtMessage.setText("");
		message = name + ": " + message;
		String encrypt = server.encrypt(key, message);

		byte[] buffer = encrypt.getBytes();
		DatagramPacket datagram = new DatagramPacket(buffer, buffer.length, group, port);
		try {
			socket.send(datagram);
		} catch (Exception ex) {
			System.out.println("Error enviando" + ex);

		}
	}

	static void pintar(String message) {
	}
}
