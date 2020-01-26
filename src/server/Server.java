package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;
import java.util.Map.Entry;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.util.Callback;

public class Server implements Interfaz, Remote {
	private Connection connection;
	private File config;
	private Properties properties;
	private InputStream input;
	private OutputStream output;
	private boolean login;
	private int correctNumber;

	public Server() {
		config = new File("src/configuration.ini");
		properties = new Properties();
		output = null;
		login = false;
		getConnection();
	}

	// Conexion base de datos sql desde configuacion.ini
	public Connection getConnection() {
		String url = getProperty("url");
		String user = getProperty("user");
		String pass = getProperty("pass");
		String driver = "com.mysql.cj.jdbc.Driver";
		try {
			Class.forName(driver);
			connection = DriverManager.getConnection(url, user, pass);
		} catch (ClassNotFoundException e) {
			System.out.println("ERROR: DRIVER NOT FOUND ");
			System.exit(-1);

		} catch (SQLException e) {
			System.out.println("ERROR: CONNECTION FAILURE OF DATABASE");
			System.exit(-1);
		} catch (Exception e) {
			System.out.println("ERROR: UNKNOWN");
			e.printStackTrace();
			System.exit(-1);
		}
		return connection;
	}

	public String getProperty(String key) {
		input = null;
		try {
			input = new FileInputStream(config);
			properties.load(input);
		} catch (Exception e) {
			System.out.println("CONFIGURATION READING FAILURE (.INI)" + e);
		}
		String property = properties.getProperty(key);
		return property;
	}

	public void setProperty(String key, String value) {
		properties.setProperty(key, value);
		try {
			output = new FileOutputStream(config);
			properties.store(output, "");

		} catch (IOException e) {
			System.out.println("Algo salió mal");
		}
	}

	// LOGIN-REGISTRO
	@Override
	public boolean registerUser(int number, String username, String password, String name) {
		String query = "INSERT INTO users (username, password, name, surname) VALUES (?, ?, ?, ?)";
		try {
			PreparedStatement stmt = connection.prepareStatement(query);

			stmt.setString(1, username);
			stmt.setString(2, password);
			stmt.setString(3, name);
			// when register status = false
			stmt.setBoolean(4, false);
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	// Comprueba si existe un usuario
	public boolean existeUsuario(String username) throws SQLException {
		String query = "SELECT username FROM users WHERE username LIKE ?";
		PreparedStatement stmt = connection.prepareStatement(query);
		stmt.setString(1, username);
		ResultSet rset = stmt.executeQuery();
		boolean exist = rset.next();
		rset.close();
		stmt.close();
		if (exist) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean updateUser(int number, String username, String password, String name) throws RemoteException {
		boolean state;

		String query = "UPDATE users SET password = ?, name = ?, surname = ?  WHERE number LIKE '" + number + "'";
		try {
			PreparedStatement stmt = connection.prepareStatement(query);

			stmt.setString(1, password);
			stmt.setString(2, name);

			stmt.executeUpdate();
			stmt.close();
			state = true;
		} catch (SQLException e) {
			e.printStackTrace();
			state = false;
		}
		return state;
	}

	@Override
	public boolean deleteUser(int number) throws RemoteException {
		try {
			String query = "DELETE FROM users WHERE username LIKE ?";
			PreparedStatement stmt = connection.prepareStatement(query);
			stmt.setInt(1, number);
			stmt.executeUpdate();
			stmt.close();
			return true;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}

	}

	@Override
	public boolean login(int number, String password) {
		correctNumber = 0;
		String correctPassword = " ";

		try {
			String query = "SELECT number, password FROM users WHERE number = ?  AND password = ?";
			PreparedStatement stmt = connection.prepareStatement(query);
			stmt.setInt(1, number);
			stmt.setString(2, password);
			ResultSet rset = stmt.executeQuery();
			rset.next();
			correctNumber = rset.getInt(1);
			correctPassword = rset.getString(2);
			rset.close();
			stmt.close();
		} catch (SQLException s) {
			login = false;
		}

		login = correctNumber == number && correctPassword.equals(password);
		if (login) {
			// status == true
			return true;
		} else {
			return false;
		}

	}

	public boolean isLogin() {
		return login;
	}

	public void logout() {
		connection = null;
		login = false;
		correctNumber = 0;
	}

	/**
	 * Permite llenar una tabla con consultas que no requieran de una comprobacion,
	 * como obtener todos los datos de una tabla, o hacer una busqueda
	 * 
	 * @param sql       La consulta con la que se llena la tabla
	 * @param tableView La tabla que va a ser completada con los datos de la
	 *                  consulta
	 * @param data      El ObservableList que utiliza la tabla para obtener los
	 *                  datos
	 */

	// MAIN
	public static void main(String[] args) {
		Registry reg = null;
		try {
			System.out.println("Creando el registro de objetos,escuchando en el puerto 5557");
			reg = LocateRegistry.createRegistry(5557);
		} catch (Exception e) {
			System.out.println("ERROR:No se ha podido crear el registro");
			e.printStackTrace();
		}
		System.out.println("Creando el objeto servidor");
		Server serverObject = new Server();
		try {
			System.out.println("Inscribiendo el objeto servidor en el registro");
			System.out.println("");
			System.out.println("Se le da un nombre único: Chat");
			reg.rebind("Chat", (Interfaz) UnicastRemoteObject.exportObject(serverObject, 0));
		} catch (Exception e) {

			System.out.println("ERROR:No se ha podido inscribir el objeto servidor.");
			e.printStackTrace();
		}
	}
}
