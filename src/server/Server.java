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
        String pass = "password";
        String driver = "com.mysql.cj.jdbc.Driver";
        try {
            System.out.println(user);
            System.out.println(pass);
            Class.forName(driver);
            connection = DriverManager.getConnection(url, user, pass);
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
            System.out.println("Algo sali� mal");
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
    public boolean existeUsuario(int number) throws SQLException {
        String query = "SELECT number FROM users WHERE number LIKE ?";
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
    public boolean login(int number, String password) {
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
            // status == true
            return true;
        } else {
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
            // TODO Auto-generated catch block
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

    public boolean isLogin() {
        return login;
    }

    public void logout() {
        connection = null;
        login = false;
        correctNumber = 0;
    }

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
            System.out.println("Se le da un nombre �nico: Chat");
            reg.rebind("Chat", (Interfaz) UnicastRemoteObject.exportObject(serverObject, 0));
        } catch (Exception e) {

            System.out.println("ERROR:No se ha podido inscribir el objeto servidor.");
            e.printStackTrace();
        }
    }
}
