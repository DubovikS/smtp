package template.scheduler;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.TimerTask;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import template.db.Conn;
import template.models.Employee;

public class SchedulerTask extends TimerTask {
	int count = 1;

	private List<Employee> employeeList;

	public SchedulerTask() {
		
	}

	// run is a abstract method that defines task performed at scheduled time.
	@Override
	public void run() {
		System.out.println(count + " : Sinhronization " + new Date());
		count++;
		execute();
	}

	private void execute() {
		try {
			Conn.Conn();
			employeeList = Conn.getListEmployee();
			// Conn.CloseDB();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		for (Employee e : employeeList) {
			try {
				List<template.models.Message> messagesList = Conn.getMessagesForSynhronization(e);
				for (template.models.Message m : messagesList) {
					String attachment =  m.getAttachment() != null ? m.getAttachment() : null;
					sentMessage(m.getMessage(), e.getEmail(), m.getSubject(), null, e, m.getId());
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	private void sentMessage(String text, String recepient, String subject, String attachment, Employee employee, int messageId) throws UnsupportedEncodingException {
		Properties props = System.getProperties();
		props.put("mail.smtp.starttls.enable", true); // added this line
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.user", "noreplay123456789vovan@gmail.com");
		props.put("mail.smtp.password", "vovan123");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", true);

		Session session = Session.getInstance(props, null);
		MimeMessage message = new MimeMessage(session);

		System.out.println("Port: " + session.getProperty("mail.smtp.port"));

		try {
			InternetAddress from = new InternetAddress("noreplay123456789vovan@gmail.com");
			message.setSubject(subject);
			message.setFrom(from);

			// Create a multi-part to combine the parts
			Multipart multipart = new MimeMultipart("alternative");

		
				if (attachment != null && attachment.length() > 0) {

					MimeBodyPart attachmentBodyPart = new MimeBodyPart();
					DataSource source = new FileDataSource(attachment);
					attachmentBodyPart.setDataHandler(new DataHandler(source));
					attachmentBodyPart.setFileName(MimeUtility.encodeText(source.getName()));
					multipart.addBodyPart(attachmentBodyPart);

				}
			

			// Create your text message part
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setText(prepareText(text));

			// Add the text part to the multipart
			multipart.addBodyPart(messageBodyPart);

			// Create the html part
			messageBodyPart = new MimeBodyPart();
			String htmlMessage = "Our html text";
			messageBodyPart.setContent(prepareText(text), "text/html");

			// Add html part to multi part
			multipart.addBodyPart(messageBodyPart);

			// Associate multi-part with message
			message.setContent(multipart);

			// Send message
			Transport transport = session.getTransport("smtp");
			transport.connect("smtp.gmail.com", "noreplay123456789vovan", "vovan123");
			System.out.println("Transport: " + transport.toString());

			message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(recepient));
			transport.sendMessage(message, message.getAllRecipients());

			Conn.saveEmployee(employee, messageId);

		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String prepareText(String textIn) {
		String textOut = textIn;
		textOut = textOut.replace("\n", "<br>");
		return textOut;
	}

}
