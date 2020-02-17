package server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Random;

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
		String url = "jdbc:mysql://localhost:3306/javachat?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
		String user = "root";
		String pwd = "";
		String driver = "com.mysql.cj.jdbc.Driver";
		try {
			Class.forName(driver);
			connection = DriverManager.getConnection(url, user, pwd);
		} catch (ClassNotFoundException e) {
			System.out.println("ERROR: DRIVER NOT FOUND ");
			System.exit(-1);

		} catch (SQLException e) {
			System.out.println("ERROR: CONNECTION FAILURE OF DATABASE" + e);
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
			System.out.println("Algo salido mal");
		}
	}

	// LOGIN-REGISTRO
	@Override
	public boolean registerUser(int number, String username, String password, String name) {
		String query = "INSERT INTO users (number,username, password, name,status) VALUES (?, ?, ?, ?,?)";
		try {
			PreparedStatement stmt = connection.prepareStatement(query);
			stmt.setInt(1, number);
			stmt.setString(2, username);
			stmt.setString(3, cipher(password));
			stmt.setString(4, name);
			// when register status = false
			stmt.setBoolean(5, false);
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	// Comprueba si existe un usuario
	public boolean isRegisteredUser(int number) throws SQLException {
		String query = "SELECT number FROM users WHERE number LIKE ?";
		try {
			PreparedStatement stmt = connection.prepareStatement(query);
			stmt.setInt(1, number);
			ResultSet rset = stmt.executeQuery();
			boolean exist = rset.next();
			rset.close();
			stmt.close();
			if (exist) {
				return true;
			} else {
				return false;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean updateUser(int number, String username, String password, String name) throws RemoteException {
		boolean state;
		String query = "UPDATE users SET password = ?, name = ? WHERE number LIKE '" + number + "'";
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
			String query = "DELETE FROM users WHERE number LIKE ?";
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
	public boolean isLogin(int number, String password) {
		correctNumber = 0;
		String correctPassword = " ";

		try {
			String query = "SELECT number, password FROM users WHERE number = ?  AND password = ?";
			PreparedStatement stmt = connection.prepareStatement(query);
			stmt.setInt(1, number);
			stmt.setString(2, cipher(password));
			ResultSet rset = stmt.executeQuery();
			rset.next();

			correctNumber = rset.getInt(1);
			correctPassword = rset.getString(2);
			rset.close();
			stmt.close();
		} catch (SQLException e) {
			login = false;
		}

		login = correctNumber == number && correctPassword.equals(cipher(password));
		if (login) {
			if (isConnected(number)) {
				return true;
			}
		}
		return login;

	}

	private boolean isConnected(int number) {
		String query2 = "UPDATE users set status =  1 WHERE number = '" + number + "'";
		try {
			PreparedStatement stmt = connection.prepareStatement(query2);
			stmt.executeUpdate(query2);
			stmt.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public String logout(int number) {
		if (isDisconnectedUser(number)) {
			connection = null;
			login = false;
			correctNumber = 0;
			return "Logout successfull!";
		} else {
			return "Error disconnecting user, please repeat again in other moment!";
		}
	}

	private boolean isDisconnectedUser(int number) {
		String query2 = "UPDATE users set status =  0 WHERE number = '" + number + "'";
		try {
			PreparedStatement stmt = connection.prepareStatement(query2);
			stmt.executeUpdate(query2);
			stmt.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	private String cipher(String password) {
		MessageDigest md;
		String strCipherPwd = "";
		try {
			md = MessageDigest.getInstance("SHA-256");
			byte[] cipherPwd = md.digest(password.getBytes(StandardCharsets.UTF_8));
			strCipherPwd = bytesToHex(cipherPwd);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return strCipherPwd;
	}

	private String bytesToHex(byte[] hashInBytes) {

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < hashInBytes.length; i++) {
			sb.append(Integer.toString((hashInBytes[i] & 0xff) + 0x100, 16).substring(1));
		}
		return sb.toString();

	}

	@Override
	public boolean resetPassword(int number) throws RemoteException {

		String query = "INSERT INTO resetPassword (number,codigo) VALUES (?, ?)";
		try {
			PreparedStatement stmt = connection.prepareStatement(query);
			stmt.setInt(1, number);
			String codigo = getSaltString();
			String hash = cipher(codigo);
			stmt.setInt(1, number);
			stmt.setString(2, hash);
			stmt.executeUpdate();
			stmt.close();
			PasswordReset passwordreset = new PasswordReset();
			boolean status = passwordreset.sendMailReset(getMail(number), codigo);
			return status;
		} catch (SQLException e) {
			System.out.println("error: " + e);
			return false;
		}
	}

	public String getMail(int number) {
		String query = "SELECT username FROM users WHERE number = ? ";
		String mail;
		try {
			PreparedStatement stmt = connection.prepareStatement(query);
			stmt.setInt(1, number);
			ResultSet rset = stmt.executeQuery();
			rset.next();

			mail = rset.getString(1);
			rset.close();
			stmt.close();
			return mail;
		} catch (SQLException e) {
			System.out.println("Error: " + e);
		}
		return "";
	}

	protected String getSaltString() {
		String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() < 18) { // length of the random string.
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		String saltStr = salt.toString();
		return saltStr;

	}

	@Override
	public boolean changePassword(String code, String password) throws RemoteException {
		if (resetPassword(code, password)) {
			if (deleteHashCode(code)) {
				return true;
			}
		}
		return false;
	}

	private boolean resetPassword(String code, String password) {
		String query = "Update users inner join resetPassword  on users.number = "
				+ "resetPassword.number set users.password = ? where resetPassword.codigo = ?;";
		try {
			PreparedStatement stmt = connection.prepareStatement(query);

			stmt.setString(1, cipher(password));
			stmt.setString(2, cipher(code));
			stmt.executeUpdate();
			stmt.close();
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	private boolean deleteHashCode(String code) {
		String query = "DELETE FROM resetpassword WHERE codigo LIKE'" + cipher(code) + "' ";
		try {
			PreparedStatement stmt2 = connection.prepareStatement(query);
			stmt2.executeUpdate();
			stmt2.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;

		}
		return true;
	}

	public boolean insertNewChatPassword(String password, String key, int port) {
		String query = "INSERT INTO petitions (password,room_key, PORT) VALUES (?, ?, ?)";
		try {
			PreparedStatement stmt = connection.prepareStatement(query);
			stmt.setString(1, password);
			stmt.setString(2, cipher(key));
			stmt.setInt(3, port);
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean isRegisteredPassword(String password) {
		String query = "SELECT key FROM petitions WHERE password LIKE ?";
		try {
			PreparedStatement stmt = connection.prepareStatement(query);
			stmt.setString(1, password);
			ResultSet rset = stmt.executeQuery();
			boolean exist = rset.next();
			rset.close();
			stmt.close();
			if (exist)
				return true;
			else
				return false;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static void main(String[] args) {
		Registry reg = null;
		try {
			System.out.println("Creating register of objects, listening in PORT: 5557");
			reg = LocateRegistry.createRegistry(5557);
		} catch (Exception e) {
			System.out.println("ERROR: We can't create server! ");
			System.out.println("Can't use port 5557, because is in use!");
			System.exit(0);
		}
		System.out.println("Creating server object!");
		Server serverObject = new Server();
		try {
			System.out.println("Registered server object!");
			System.out.println("");
			System.out.println("Unique name: Chat");
			reg.rebind("Chat", (Interfaz) UnicastRemoteObject.exportObject(serverObject, 0));
		} catch (Exception e) {

			System.out.println("ERROR: We can't register server object!");
			e.printStackTrace();
		}
	}
}
