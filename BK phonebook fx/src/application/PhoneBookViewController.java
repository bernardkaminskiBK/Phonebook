package application;


import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class PhoneBookViewController implements Initializable {

	@FXML
	private Label label;
	@FXML
	private TextField inputFirstName;
	@FXML
	private TextField inputLastName;
	@FXML
	private TextField inputEmail;
	@FXML
	private Button addNewContactButton;
	@FXML
	private StackPane menuPane;
	@FXML
	private Pane contactPane;
	@FXML
	private Pane exportPane;
	@FXML
	private TableView<Person> table;
	@FXML
	private TextField inputExport;
	@FXML
	private Button exportButton;
	@FXML
	private AnchorPane anchor;
	@FXML
	private SplitPane mainSplit;

	private DB db = new DB();
	private static final String MENU_CONTACTS = "Kontakty";
	private static final String MENU_LIST = "Zoznam";
	private static final String MENU_EXPORT = "Exportovanie";
	private static final String MENU_EXIT = "Koniec";

	private TableColumn<Person, String> firstNameCol;
	private TableColumn<Person, String> lastNameCol;
	private TableColumn<Person, String> emailCol;
	private TableColumn<Person, String> removeCol;
	private final ObservableList<Person> data = FXCollections.observableArrayList();

	@FXML
	private void addContact(ActionEvent event) {
		String email = inputEmail.getText();
		if (email.length() > 3 && email.contains("@") && email.contains(".")) {
			Person newPerson = new Person(inputFirstName.getText(), inputLastName.getText(), email);
			data.add(newPerson);
			db.addNewContact(newPerson);
			inputLastName.clear();
			inputFirstName.clear();
			inputEmail.clear();
		} else {
			alert("Neplatný názov pre e-mailovú adresu.");
		}
	}

	@FXML
	private void exportList(ActionEvent event) {
		String fileName = inputExport.getText();
		fileName = fileName.replaceAll("\\s+", "");
		if (fileName != null && !fileName.equals("")) {
			PdfGeneration pdfCreator = new PdfGeneration();
			pdfCreator.pdfGeneration(fileName, data);
			pdfGenerationDone("PDF súbor bolo úspešne vygenerované");
		} else {
			alert("Zadaj názov súboru.");
		}

	}

	private void setTableData() {
		firstNameCol = new TableColumn<Person, String>("Meno");
		firstNameCol.setMinWidth(150);
		firstNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
		firstNameCol.setCellValueFactory(new PropertyValueFactory<Person, String>("firstName"));
		firstNameCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Person, String>>() {
			@Override
			public void handle(TableColumn.CellEditEvent<Person, String> t) {
				Person actualPerson = ((Person) t.getTableView().getItems().get(t.getTablePosition().getRow()));
				actualPerson.setFirstName(t.getNewValue());
				db.updateContact(actualPerson);
			}
		});

		lastNameCol = new TableColumn<Person, String>("Priezvisko");
		lastNameCol.setMinWidth(150);
		lastNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
		lastNameCol.setCellValueFactory(new PropertyValueFactory<Person, String>("lastName"));
		lastNameCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Person, String>>() {
			@Override
			public void handle(TableColumn.CellEditEvent<Person, String> t) {
				Person actualPerson = ((Person) t.getTableView().getItems().get(t.getTablePosition().getRow()));
				actualPerson.setLastName(t.getNewValue());
				db.updateContact(actualPerson);
			}
		});

		emailCol = new TableColumn<Person, String>("Email");
		emailCol.setMinWidth(250);
		emailCol.setCellFactory(TextFieldTableCell.forTableColumn());
		emailCol.setCellValueFactory(new PropertyValueFactory<Person, String>("email"));
		emailCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Person, String>>() {
			@Override
			public void handle(TableColumn.CellEditEvent<Person, String> t) {
				Person actualPerson = ((Person) t.getTableView().getItems().get(t.getTablePosition().getRow()));
				actualPerson.setEmail(t.getNewValue());
				db.updateContact(actualPerson);
			}
		});

		removeCol = new TableColumn<Person, String>("Zmazanie");
		removeCol.setMinWidth(100);

		Callback<TableColumn<Person, String>, TableCell<Person, String>> cellFactory = new Callback<TableColumn<Person, String>, TableCell<Person, String>>() {
			@Override
			public TableCell<Person, String> call(TableColumn<Person, String> param) {

				final TableCell<Person, String> cell = new TableCell<Person, String>() {

					final Button btn = new Button("Zmazanie");

					@Override
					public void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setGraphic(null);
							setText(null);
						} else {
							btn.setOnAction((ActionEvent event) -> {
								Person person = getTableView().getItems().get(getIndex());
								data.remove(person);
								db.removeContact(person);
							});
							setGraphic(btn);
							setText(null);
						}
					}
				};
				return cell;
			}
		};
		removeCol.setCellFactory(cellFactory);

		table.getColumns().addAll(firstNameCol, lastNameCol, emailCol, removeCol);
		data.addAll(db.getAllContacts());
		table.setItems(data);
	}

	private void setMenuData() {
		TreeItem<String> treeItemRoot1 = new TreeItem<>("Menu");
		TreeView<String> treeView = new TreeView<>(treeItemRoot1);
		treeView.setShowRoot(false);

		TreeItem<String> nodeItemA = new TreeItem<>(MENU_CONTACTS);
		// nodeItemA.setExpanded(true);
		TreeItem<String> nodeItemB = new TreeItem<>(MENU_EXIT);

		Node contactsNode = new ImageView(new Image(getClass().getResourceAsStream("/pics/contacts.png")));
		Node exportNode = new ImageView(new Image(getClass().getResourceAsStream("/pics/export.png")));

		TreeItem<String> nodeItemA1 = new TreeItem<>(MENU_LIST, contactsNode);
		TreeItem<String> nodeItemA2 = new TreeItem<>(MENU_EXPORT, exportNode);

		nodeItemA.getChildren().addAll(nodeItemA1, nodeItemA2);
		treeItemRoot1.getChildren().addAll(nodeItemA, nodeItemB);
		menuPane.getChildren().add(treeView);

		treeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
			public void changed(ObservableValue observable, Object oldValue, Object newValue) {

				TreeItem<String> selectedItem = (TreeItem<String>) newValue;
				String selectedMenu;
				selectedMenu = selectedItem.getValue();

				if (null != selectedMenu) {
					switch (selectedMenu) {
					case MENU_CONTACTS:
						try {
							selectedItem.setExpanded(true);
						} catch (Exception e) {
							// TODO: handle exception
						}
						break;
					case MENU_LIST:
						contactPane.setVisible(false);
						exportPane.setVisible(true);
						break;
					case MENU_EXPORT:
						contactPane.setVisible(true);
						exportPane.setVisible(false);
						break;
					case MENU_EXIT:
						System.exit(0);
						break;
					}
				}

			}
		});
	}
	
	private void pdfGenerationDone(String text) {
		mainSplit.setDisable(true);
		mainSplit.setOpacity(0.4);

		Label label = new Label(text);
		Button succes = new Button("Ok");
		VBox vbox = new VBox(label, succes);
		vbox.setAlignment(Pos.CENTER);

		succes.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				mainSplit.setDisable(false);
				mainSplit.setOpacity(1);
				vbox.setVisible(false);
			}
		});
		anchor.getChildren().add(vbox);
		anchor.setTopAnchor(vbox, 300.0);
		anchor.setLeftAnchor(vbox, 300.0);

	}

	private void alert(String text) {
		mainSplit.setDisable(true);
		mainSplit.setOpacity(0.4);

		Label label = new Label(text);
		Button alertButton = new Button("Ok");
		VBox vbox = new VBox(label, alertButton);
		vbox.setAlignment(Pos.CENTER);

		alertButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				mainSplit.setDisable(false);
				mainSplit.setOpacity(1);
				vbox.setVisible(false);
			}
		});
		anchor.getChildren().add(vbox);
		anchor.setTopAnchor(vbox, 300.0);
		anchor.setLeftAnchor(vbox, 300.0);

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		setTableData();
		setMenuData();
	}

}
