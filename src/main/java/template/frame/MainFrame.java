package template.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Timer;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JDialog;
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
import template.dialog.SettingDialog;
import template.models.Employee;
import template.models.User;
import template.scheduler.SchedulerTask;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 7914610169258079989L;

	public MainFrame() {
		super("Smtp");

		try {
			Conn.Conn();
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

		settings.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				SettingDialog dialog = new SettingDialog();
				Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
				int sizeWidth = Constants.sizeWidthDialog;
				int sizeHeight = Constants.sizeHeightDialog;
				int locationX = (screenSize.width - sizeWidth) / 2;
				int locationY = (screenSize.height - sizeHeight) / 2;
				dialog.setBounds(locationX, locationY, sizeWidth, sizeHeight);
				dialog.setVisible(true);
			}
		});

		menubar.add(menu);
		menubar.add(menuSettings);

		setJMenuBar(menubar);
	}

	public void initPanels() {
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());

		JPanel centerPanel = new JPanel() {

			private static final long serialVersionUID = 6991988118493670816L;

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Image img;
				try {
					img = ImageIO.read(new File("src/main/resources/2.jpg"));
					g.drawImage(img, 0, 0, this);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		};
		centerPanel.setLayout(new GridLayout(1, 1));
		JScrollPane scrollPane = new JScrollPane(centerPanel);
		mainPanel.add(scrollPane, BorderLayout.CENTER);

		JPanel northPanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Image img;
				try {
					img = ImageIO.read(new File("src/main/resources/1.jpg"));
					g.drawImage(img, 0, 0, this);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		northPanel.setLayout(new GridLayout(1, 1));
		mainPanel.add(northPanel, BorderLayout.NORTH);

		JPanel southPanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Image img;
				try {
					img = ImageIO.read(new File("src/main/resources/1.jpg"));
					g.drawImage(img, 0, 0, this);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		
		northPanel.setLayout(new FlowLayout());
		mainPanel.add(southPanel, BorderLayout.SOUTH);

		JLabel emailLabel = new JLabel("E-mail:");
		// JLabel emailTextLabel = new JLabel("Here e-mail");
		User user = Conn.getUser();
		final JLabel emailText = new JLabel(user.getEmail());
		// emailText.setPreferredSize(new Dimension(200, 24));
		// emailLabel.setHorizontalAlignment(JLabel.LEFT);
		// emailLabel.setVerticalAlignment(JLabel.CENTER);
		emailText.setFont(new Font("Times New Roman", Font.BOLD, 17));
		emailLabel.setFont(new Font("Monotype Corsiva", Font.PLAIN, 20));

		final JTextField subjectTextField = new JTextField() {
			
		};
		subjectTextField.setPreferredSize(new Dimension(200, 24));
		subjectTextField.setFont(new Font("Times New Roman", Font.PLAIN, 17));

		JLabel subjectLabel = new JLabel("Subject:");
		subjectLabel.setFont(new Font("Monotype Corsiva", Font.PLAIN, 20));

		northPanel.add(emailLabel);
		northPanel.add(emailText);
		northPanel.add(subjectLabel);
		northPanel.add(subjectTextField);

		final JTextArea messageTextArea = new JTextArea(15, 10);
		messageTextArea.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		messageTextArea.setWrapStyleWord(true);
		JButton sentButton = new JButton("Send") {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Image img;
				try {
					img = ImageIO.read(new File("src/main/resources/123.png"));
					g.drawImage(img, 0, 0, this);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		sentButton.setFont(new Font("Sylfaen", Font.PLAIN, 20));
		sentButton.setSize(new Dimension(50, 50));

		JButton chooseFile = new JButton("Add file") {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Image img;
				try {
					img = ImageIO.read(new File("src/main/resources/folder_images.png"));
					g.drawImage(img, 0, 0, this);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		chooseFile.setFont(new Font("Sylfaen", Font.PLAIN, 19));
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
				if (messageTextArea.getText() != null && subjectTextField.getText() != null) {
					Thread thread = new Thread(new Runnable() {

						public void run() {
							try {
								String fileDir = null;
								if (chooser != null && chooser.getDirectory() != null) {
									fileDir = chooser.getDirectory() + chooser.getFile();
								}

								/*
								 * sentMessage(messageTextArea.getText(), emailText.getText(),
								 * subjectTextField.getText(), fileDir);
								 */

								template.models.Message msg = new template.models.Message();
								msg.setMessage(messageTextArea.getText());
								msg.setSubject(subjectTextField.getText());
								msg.setAttachment(fileDir);
								Conn.saveMessage(msg);
								messageTextArea.setText("");
								chooser.setDirectory(null);
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
