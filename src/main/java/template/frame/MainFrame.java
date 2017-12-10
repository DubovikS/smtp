package template.frame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.Timer;

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
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import Constants.Constants;
import template.Utils.Validator;
import template.scheduler.SchedulerTask;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 7914610169258079989L;

	public MainFrame() {
		super("Smtp");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(Constants.sizeWidth, Constants.sizeWidth);
		initPanels();
		initMenu();
		startScheduler();

	}

	private void startScheduler() {
		Timer timer = new Timer();

		// Schedule to run after every 3 second(3000 millisecond)
		timer.schedule(new SchedulerTask(), 30, 60000);
	}

	public void initMenu() {
		JMenuBar menubar = new JMenuBar();
		JMenu menu = new JMenu("File");
		menu.setFont(new Font("Dialog", Font.PLAIN, 17));

		JMenuItem info = new JMenuItem("Info");
		info.setFont(new Font("Dialog", Font.PLAIN, 17));
		menu.add(info);

		JMenuItem close = new JMenuItem("Close");
		close.setFont(new Font("Dialog", Font.PLAIN, 17));
		menu.add(close);

		JMenu menuSettings = new JMenu("Settings");
		menuSettings.setFont(new Font("Dialog", Font.PLAIN, 17));
		JMenuItem settings = new JMenuItem("Settings");
		settings.setFont(new Font("Dialog", Font.PLAIN, 17));
		menuSettings.add(settings);

		menubar.add(menu);
		menubar.add(menuSettings);

		setJMenuBar(menubar);
	}

	public void initPanels() {
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());

		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new GridLayout(1, 1));
		JScrollPane scrollPane = new JScrollPane(centerPanel);
		mainPanel.add(scrollPane, BorderLayout.CENTER);

		JPanel northPanel = new JPanel();
		northPanel.setLayout(new GridLayout(1, 1));
		mainPanel.add(northPanel, BorderLayout.NORTH);

		JPanel southPanel = new JPanel();
		northPanel.setLayout(new FlowLayout());
		mainPanel.add(southPanel, BorderLayout.SOUTH);

		JLabel emailLabel = new JLabel("E-mail:");
		// JLabel emailTextLabel = new JLabel("Here e-mail");
		final JTextField emailText = new JTextField();
		emailText.setPreferredSize(new Dimension(200, 24));
		emailLabel.setHorizontalAlignment(JLabel.CENTER);
		emailLabel.setVerticalAlignment(JLabel.CENTER);
		emailText.setFont(new Font("Dialog", Font.PLAIN, 17));
		emailLabel.setFont(new Font("Dialog", Font.PLAIN, 17));

		final JTextField subjectTextField = new JTextField();
		subjectTextField.setPreferredSize(new Dimension(200, 24));
		subjectTextField.setFont(new Font("Dialog", Font.PLAIN, 17));

		JLabel subjectLabel = new JLabel("Subject:");
		subjectLabel.setFont(new Font("Dialog", Font.PLAIN, 17));

		northPanel.add(emailLabel);
		northPanel.add(emailText);
		northPanel.add(subjectLabel);
		northPanel.add(subjectTextField);

		JLabel textLabel = new JLabel("Message");
		final JTextArea messageTextArea = new JTextArea(15, 10);
		messageTextArea.setFont(new Font("Dialog", Font.PLAIN, 17));
		messageTextArea.setWrapStyleWord(true);
		JButton sentButton = new JButton("Sent");
		sentButton.setFont(new Font("Dialog", Font.PLAIN, 17));
		sentButton.setSize(new Dimension(50, 50));

		JButton chooseFile = new JButton("Add file");
		chooseFile.setFont(new Font("Dialog", Font.PLAIN, 17));
		chooseFile.setSize(new Dimension(50, 50));
		final FileDialog chooser = new FileDialog((Frame) null, "Select File to Open");

		chooseFile.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				chooser.setMode(FileDialog.LOAD);
				chooser.setVisible(true);
				String file = chooser.getDirectory() + chooser.getFile();
				System.out.println(file);
			}
		});

		centerPanel.add(messageTextArea);
		southPanel.add(sentButton);
		southPanel.add(chooseFile);

		sentButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if (messageTextArea.getText() != null && emailText.getText() != null
						&& subjectTextField.getText() != null) {
					if (Validator.validate(emailText.getText())) {
						Thread thread = new Thread(new Runnable() {

							public void run() {
								try {
									sentMessage(messageTextArea.getText(), emailText.getText(),
											subjectTextField.getText(), chooser.getDirectory() + chooser.getFile());
									messageTextArea.setText("");
								} catch (Exception e) {

								}
							}
						});
						thread.start();
					} else {
						JOptionPane.showMessageDialog(MainFrame.this, "Please, enter correct e-mail.");
					}
				} else {
					JOptionPane.showMessageDialog(MainFrame.this, "Please, populate message and e-mail.");
				}
			}
		});

		this.setContentPane(mainPanel);
	}

	private String prepareText(String textIn) {
		String textOut = textIn;
		textOut = textOut.replace("\n", "<br>");
		return textOut;
	}

	private void sentMessage(String text, String recepient, String subject, String attachment) {
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
			message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(recepient));

			

			// Create a multi-part to combine the parts
			Multipart multipart = new MimeMultipart("alternative");

			if (attachment != null) {
		        try {
		        	MimeBodyPart attachmentBodyPart = new MimeBodyPart();
					DataSource source = new FileDataSource(attachment);
					attachmentBodyPart.setDataHandler(new DataHandler(source)); 
					attachmentBodyPart.setFileName(MimeUtility.encodeText(source.getName()));
					multipart.addBodyPart(attachmentBodyPart); 
					
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} 
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
			transport.sendMessage(message, message.getAllRecipients());
			JOptionPane.showMessageDialog(MainFrame.this, "Messages sent success.");
		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
