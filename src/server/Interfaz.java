package server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Interfaz extends Remote {

	// Register user
	public boolean registerUser(int number, String username, String password, String name) throws RemoteException;

	// Modificar user
	public boolean updateUser(int number, String username, String password, String name) throws RemoteException;

	// Delete user
	public boolean deleteUser(int number) throws RemoteException;

	// Login user
	public boolean isLogin(int number, String password) throws RemoteException;

	public boolean resetPassword(int number) throws RemoteException;

	public boolean changePassword(String code, String password) throws RemoteException;

}
