package server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

public interface Interfaz extends Remote {

<<<<<<< Updated upstream
	// Register user
	public boolean registerUser(int number, String username, String password, String name) throws RemoteException;

	// Modificar user
	public boolean updateUser(int number, String username, String password, String name) throws RemoteException;
=======
    // Register user
    public boolean registerUser(int number, String username, String password, String name)
            throws RemoteException;

    // Modificar user
    public boolean updateUser(int number, String username, String password, String name)
            throws RemoteException;
>>>>>>> Stashed changes

    // Delete user
    public boolean deleteUser(int number) throws RemoteException;

    // Login user
    public boolean login(int number, String password) throws RemoteException;

    public boolean passwordReset(int number) throws RemoteException;

}
