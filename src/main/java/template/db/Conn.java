package template.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import template.models.Employee;
import template.models.Message;

public class Conn {

	public static Connection conn;
	public static Statement statmt;
	public static ResultSet resSet;

	public static void Conn() throws ClassNotFoundException, SQLException {
		conn = null;
		Class.forName("org.sqlite.JDBC");
		conn = DriverManager.getConnection("jdbc:sqlite:smtp.s3db");
		statmt = conn.createStatement();
		System.out.println("База Подключена!");
	}

	public static void CloseDB() throws ClassNotFoundException, SQLException {
		conn.close();
		statmt.close();
		resSet.close();

		System.out.println("Соединения закрыты");
	}

	public static void ReadDB() throws ClassNotFoundException, SQLException {
		resSet = statmt.executeQuery("SELECT * FROM employee");

		while (resSet.next()) {
			int id = resSet.getInt("id");
			String email = resSet.getString("email");

			System.out.println("ID = " + id);
			System.out.println("email = " + email);
			System.out.println();
		}

		System.out.println("Таблица выведена");
	}

	public static List<Employee> getListEmployee() throws ClassNotFoundException, SQLException {
		List<Employee> employeeList = new ArrayList<Employee>();
		resSet = statmt.executeQuery("SELECT * FROM employee");

		while (resSet.next()) {
			int id = resSet.getInt("id");
			String email = resSet.getString("email");
			String lastUpdate = resSet.getString("lastUpdate");
			Employee employee = new Employee();
			employee.setEmail(email);
			employee.setId(id);
			employee.setLastUpdate(Integer.parseInt(lastUpdate));
			
			employeeList.add(employee);
		}

		return employeeList;
	}

	public static void saveMessage(Message message) {
		try {
			String date = new Date().toString();
			
			statmt.execute("INSERT INTO 'messages' ('text', 'subject', 'attachment') VALUES ('" + message.getMessage() + "','"
					+ message.getSubject() + "','" + (message.getAttachment() == null ? "" : message.getAttachment()) + "');");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static List<Message> getMessagesForSynhronization(Employee employee) throws SQLException{
		List<Message> messageList = new ArrayList<Message>();
		resSet = statmt.executeQuery("select * from messages where messages.id > '"+ employee.getLastUpdate() + "';");
		
		while (resSet.next()) {
			int id = resSet.getInt("id");
			String subject = resSet.getString("subject");
			String attachment = resSet.getString("attachment");
			String text = resSet.getString("text");
			
			Message message = new Message();
			message.setId(id);
			message.setSubject(subject);
			message.setAttachment(attachment);
			message.setMessage(text);
			
			messageList.add(message);
		}
		
		return messageList;
	}
	
	public static void saveEmployee(Employee employee, int messageId) {
		try {
			statmt.execute("UPDATE 'employee' set lastUpdate='" + messageId + "' where id='" +  employee.getId() + "';");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
