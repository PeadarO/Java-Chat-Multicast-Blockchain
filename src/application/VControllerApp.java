package application;

import java.rmi.RemoteException;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
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
	private TableView tableView;
	private ObservableList<ObservableList> data;

	@FXML
	private void initialize() {
		server.setTableConnections(tableView);
		refresh(null);
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
		data = FXCollections.observableArrayList();
		tableView = new TableView();
	}

	public void clickNewConnection(ActionEvent event) {
		selecionado = -1;
		openWindow(event, (Node) event.getSource(), "Contacto.fxml", "New connection", false, selecionado);
	}

	public void clickUpdateConnection(ActionEvent event) {
		selecionado = tableView.getSelectionModel().getSelectedIndex();
		if (selecionado != -1) {
			openWindow(event, (Node) event.getSource(), "Contacto.fxml", "Update connection", false, selecionado);
		} else {
			alert(AlertType.WARNING, "Alerta", "No hay ninguna fila seleccionada",
					"Seleccione una fila para continuar");
		}
	}

	public void clickDeleteConnection(ActionEvent event) {
		selecionado = tableView.getSelectionModel().getSelectedIndex();
		if (selecionado != -1) {

			TablePosition pos = (TablePosition) tableView.getSelectionModel().getSelectedCells().get(0);
			int row = pos.getRow();
			// TableColumn col = pos.getTableColumn();
			TableColumn col = (TableColumn) tableView.getColumns().get(4);
			String data = (String) col.getCellObservableValue(row).getValue();

			// alert to confirm
			Optional<ButtonType> option = alert(AlertType.CONFIRMATION, "Delete connection", "",
					"¿Estas seguro de eliminar el numero: " + data);

			if (option.get() == ButtonType.OK) {
				// server.borrarContacto(Integer.parseInt(data), getId());
				System.out.println("contacto con numero: " + data + " borrado");
			}
			refresh(null);
		} else {
			alert(AlertType.WARNING, "Cuidado", "No hay ninguna fila seleccionada",
					"Seleccione una fila para continuar");
		}
	}

	public void clickDeleteALLConnections(ActionEvent event) {
		// alert to confirm
		Optional<ButtonType> option = alert(AlertType.CONFIRMATION, "Delete all connections", "",
				"¿Are you sure delete all connections?, there will not be return");
		if (option.get() == ButtonType.OK) {
			// if (server.borrarTodo("DELETE FROM contacts WHERE ref_user LIKE'" + getId() +
			// "'")) {
			// dialog(AlertType.INFORMATION, "Todos los contactos eliminados", "", "");
			System.out.println("All connections delete ");
		} else {
			alert(AlertType.ERROR, "ERROR", "", "Ha ocurrido algo");
			// }
		}
		refresh(null);
	}

	public void searchConnection(ActionEvent event) {
		try {
			server.readConnections("SELECT * FROM contacts WHERE ref_user LIKE '" + getId() + "' AND" + " name LIKE '%"
					+ txtSearchConnection.getText() + "%'", tableView, data);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public void refresh(ActionEvent event) {
		try {
			server.readConnections("SELECT * FROM contacts WHERE ref_user LIKE '" + getId() + "'", tableView, data);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public void clickProfile(ActionEvent event) {
		openWindow(event, (Node) event.getSource(), "Profile.fxml", "User Profile", false, 0);
	}

	public void clickLogOut(ActionEvent event) {
		int getNumber = Integer.parseInt(getId());
		cerrarSesion(event, getNumber);
	}
}
