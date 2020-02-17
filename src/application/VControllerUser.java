package application;

import java.rmi.RemoteException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import server.Server;

public class VControllerUser extends Controller {
	private Server server;
	@FXML
	private TextField txtOld;
	@FXML
	private TextField txtNew;
	@FXML
	private TextField txtName;
	@FXML
	private TextField txtEmail;
	@FXML
	private Button btnDelete;
	@FXML
	private Button btnSave;
	@FXML
	private Button btnCancel;

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	public VControllerUser() {
		Server server = new Server();
		this.server = server;
	}

	private void updateUser() {
		try {
			server.updateUser(Integer.parseInt(getId()), txtNew.getText(), txtName.getText(), txtEmail.getText());
			System.out.println("Updating user");
		} catch (RemoteException e) {
			alert(AlertType.WARNING, "Ha ocurrido algo", "Fallo modificando los datos",
					"Pruebe a introducir de nuevo los datos, si el error persiste, introduce unos nuevos");
			e.printStackTrace();
		}
	}

	public void clickSave(ActionEvent event) {
		try {
			boolean isOldDifferentNew = txtOld.getText() != txtNew.getText();
			if (isOldDifferentNew) {
				updateUser();
				alert(AlertType.INFORMATION, "EXIT", "Cambios realizados", "");
			} else {
				alert(AlertType.WARNING, "Datos introduccidos no aceptados", "La contraseña es igual a la anterior",
						"Pruebe a introducir otra contraseña diferente");
			}

		} catch (Exception e) {
			alert(AlertType.ERROR, "Error", "Error", "Ha habido un error al guardar los cambios");
		}
		Stage stage = (Stage) btnSave.getScene().getWindow();
		stage.close();
	}

	public void clickCancel(ActionEvent event) {
		cancelar(event);
	}

	public void clickDelete(ActionEvent event) {
		try {
			System.out.println("Deleting user");
			server.deleteUser(Integer.parseInt(getId()));
			Stage stage = (Stage) btnSave.getScene().getWindow();
			stage.close();
			System.exit(0);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

	}
}
