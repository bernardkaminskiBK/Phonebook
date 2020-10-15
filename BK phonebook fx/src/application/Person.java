package application;


import javafx.beans.property.SimpleStringProperty;

public class Person {

	private final SimpleStringProperty firstName;
	private final SimpleStringProperty lastName;
	private final SimpleStringProperty email;
	private final SimpleStringProperty id;

	public Person() {
		this.firstName = new SimpleStringProperty("");
		this.lastName = new SimpleStringProperty("");
		this.email = new SimpleStringProperty("");
		this.id = new SimpleStringProperty("");
	}

	public Person(String firstName, String lastName, String email) {
		this.firstName = new SimpleStringProperty(firstName);
		this.lastName = new SimpleStringProperty(lastName);
		this.email = new SimpleStringProperty(email);
		this.id = new SimpleStringProperty("");
	}

	public Person(Integer id, String firstName, String lastName, String email) {
		this.firstName = new SimpleStringProperty(firstName);
		this.lastName = new SimpleStringProperty(lastName);
		this.email = new SimpleStringProperty(email);
		this.id = new SimpleStringProperty(String.valueOf(id));
	}

	public String getFirstName() {
		return firstName.get();
	}

	public void setFirstName(String FName) {
		firstName.set(FName);
	}

	public String getLastName() {
		return lastName.get();
	}

	public void setLastName(String LName) {
		lastName.set(LName);
	}

	public String getEmail() {
		return email.get();
	}

	public void setEmail(String mail) {
		email.set(mail);
	}

	public String getId() {
		return id.get();
	}

	public void setId(String fId) {
		id.set(fId);
	}

}
