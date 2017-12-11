package template.frame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import java.util.Timer;

import javax.swing.JButton;
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
import template.db.Conn;
import template.models.Employee;
import template.scheduler.SchedulerTask;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 7914610169258079989L;
	private List<Employee> employeeList;
	
	public MainFrame() {
		super("Smtp");
		try {
			Conn.Conn();
			//employeeList = Conn.getListEmployee();
			// Conn.CloseDB();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(Constants.sizeWidth, Constants.sizeWidth);
		initPanels();
		initMenu();
		startScheduler();

		

	}

	private void startScheduler() {
		Timer timer = new Timer();

		// Schedule to run after every 3 second(3000 millisecond)
		timer.schedule(new SchedulerTask(), 30, 30000);
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
						Thread thread = new Thread(new Runnable() {

							public void run() {
								try {
									String fileDir = null;
									if(chooser != null && chooser.getDirectory() != null) {
										fileDir = chooser.getDirectory() + chooser.getFile();
									}
									
									/*sentMessage(messageTextArea.getText(), emailText.getText(),
											subjectTextField.getText(), fileDir);*/
									
									template.models.Message msg = new template.models.Message();
									msg.setMessage(messageTextArea.getText());
									msg.setSubject(subjectTextField.getText());
									msg.setAttachment(fileDir);
									Conn.saveMessage(msg);
									messageTextArea.setText("");
									JOptionPane.showMessageDialog(MainFrame.this, "Messages sent success.");
								} catch (Exception e) {

								}
							}
						});
						thread.start();
				} else {
					JOptionPane.showMessageDialog(MainFrame.this, "Please, populate message and e-mail.");
				}
			}
		});

		this.setContentPane(mainPanel);
	}

	
	
}
