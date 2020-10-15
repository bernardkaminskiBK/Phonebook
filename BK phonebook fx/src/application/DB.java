package application;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DB {

	private static final String URL = "jdbc:derby:sampleDB;create=true";
	private static final String USERNAME = "";
	private static final String PASSWORD = "";

	// Zostrojime spojenie (most)
	private Connection conn = null;
	private Statement createStatement = null;
	private DatabaseMetaData dbmd = null;

	public DB() {
		// Skuska spojenia
		try {
			conn = DriverManager.getConnection(URL);
			System.out.println("Connection Successful.");
		} catch (SQLException e) {
			System.out.println("" + e);
			System.out.println("There is something wrong with the connection.");
			Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, e);
		}

		// Ak je spojenie tak vytvorime "dodavku".
		if (conn != null) {
			try {
				createStatement = conn.createStatement();
			} catch (SQLException e) {
				System.out.println("There is something wrong with creating the create statement");
				e.printStackTrace();
			}
		}
		// Testujeme databasu ci je prazdny ? Checkujeme ci existuje data table.
		try {
			dbmd = conn.getMetaData();
		} catch (SQLException e) {
			System.out.println("There is something wrong with creating DatabaseMetaData.");
		}

		try {
			ResultSet rs = dbmd.getTables(null, "APP", "CONTACTS", null);
			if (!rs.next()) {
				String sql = "create table contacts(id INT not null primary key GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),firstName varchar(20), lastName varchar(20), email varchar(30))";
				createStatement.execute(sql);
			}
		} catch (SQLException e) {
			System.out.println("There is something wrong with creating the data table.");
			e.printStackTrace();
		}
	}

	public ArrayList<Person> getAllContacts() {
		String sql = "select * from contacts";
		ArrayList<Person> users = null;
		try {
			ResultSet rs = createStatement.executeQuery(sql);
			users = new ArrayList<Person>();
			while (rs.next()) {
				String firstName = rs.getString("firstName");
				String lastName = rs.getString("lastName");
				String email = rs.getString("email");
				int id = rs.getInt("id");
				Person actualPerson = new Person(id, firstName, lastName, email);
				users.add(actualPerson);
			}
		} catch (SQLException e) {
			System.out.println("There is something wrong with get all contacts.");
			e.printStackTrace();
		}
		return users;
	}

	public void addNewContact(Person person) {
		String sql = "insert into contacts (firstName, lastName, email) values (?,?,?)";
		try {
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, person.getFirstName());
			preparedStatement.setString(2, person.getLastName());
			preparedStatement.setString(3, person.getEmail());
			preparedStatement.execute();
		} catch (SQLException e) {
			System.out.println("There is something wrong with add new contact to database.");
			e.printStackTrace();
		}
	}

	public void updateContact(Person person) {
		String sql = "update contacts set firstName = ?, lastName = ?, email = ? where id = ?";
		try {
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setString(1, person.getFirstName());
			preparedStatement.setString(2, person.getLastName());
			preparedStatement.setString(3, person.getEmail());
			preparedStatement.setInt(4, Integer.parseInt(person.getId()));
			preparedStatement.execute();
		} catch (SQLException e) {
			System.out.println("There is something wrong with update new contact to database.");
			e.printStackTrace();
		}
	}

	public void removeContact(Person person) {
		String sql = "delete from contacts where id = ?";
		try {
			PreparedStatement preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setInt(1, Integer.parseInt(person.getId()));
			preparedStatement.execute();
		} catch (SQLException e) {
			System.out.println("There is something wrong with delete contacts from database.");
			e.printStackTrace();
		}
	}
}
