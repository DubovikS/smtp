package template.dialog;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import Constants.BoxLayoutUtils;
import Constants.Constants;
import Constants.GUITools;
import template.Utils.Validator;
import template.db.Conn;
import template.models.User;

public class SettingDialog extends JDialog {

	private static final long serialVersionUID = -8649450421020737788L;

	public SettingDialog() {
		setTitle("Settings");
		setResizable(true);
		setSize(Constants.sizeWidthDialog, Constants.sizeHeightDialog);
		setDefaultCloseOperation(HIDE_ON_CLOSE);

		initPanel();

		pack();
	}

	private void initPanel() {
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new FlowLayout());

		JLabel emailLabel = new JLabel("Email:");
		JTextField emailText = new JTextField(15);

		JLabel passwordLabel = new JLabel("Password");
		JTextField passwordText = new JTextField(15);

		JButton apply = new JButton("Apply");

		mainPanel.add(emailLabel);
		mainPanel.add(emailText);
		mainPanel.add(passwordLabel);
		mainPanel.add(passwordText);
		mainPanel.add(apply);

		this.setContentPane(createGUI());
	}

	public JTextField tfLogin;
	public JPasswordField tfPassword;
	public JButton btnOk, btnCancel;
	private boolean isShowPassword = false;
	
	private JPanel createGUI() {
		final User user = Conn.getUser();
		// Создание панели для размещение компонентов
		JPanel panel = BoxLayoutUtils.createVerticalPanel();
		// Определение отступов от границ ранели. Для этого используем пустую рамку
		panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
		// Создание панели для размещения метки и текстового поля логина
		JPanel name = BoxLayoutUtils.createHorizontalPanel();
		JLabel nameLabel = new JLabel("Name:");
		nameLabel.setFont(new Font("Times New Roman", Font.PLAIN, 19));
		nameLabel.setSize(new Dimension(50, 50));

		name.add(nameLabel);
		name.add(Box.createHorizontalStrut(12));
		tfLogin = new JTextField(15);
		name.add(tfLogin);
		// Создание панели для размещения метки и текстового поля пароля
		JPanel password = BoxLayoutUtils.createHorizontalPanel();
		JLabel passwrdLabel = new JLabel("Password:");
		passwrdLabel.setFont(new Font("Times New Roman", Font.PLAIN, 19));
		passwrdLabel.setSize(new Dimension(50, 50));
		password.add(passwrdLabel);
		password.add(Box.createHorizontalStrut(12));
		tfPassword = new JPasswordField(15);

		password.add(tfPassword);
		// Создание панели для размещения кнопок управления
		JPanel flow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
		JPanel grid = new JPanel(new GridLayout(1, 2, 5, 0));
		btnOk = new JButton("Apply");
		btnCancel = new JButton("Show password");
		grid.add(btnOk);
		grid.add(btnCancel);
		flow.add(grid);
		// Выравнивание вложенных панелей по горизонтали
		BoxLayoutUtils.setGroupAlignmentX(new JComponent[] { name, password, panel, flow }, Component.LEFT_ALIGNMENT);
		// Выравнивание вложенных панелей по вертикали
		BoxLayoutUtils.setGroupAlignmentY(new JComponent[] { tfLogin, tfPassword, nameLabel, passwrdLabel },
				Component.CENTER_ALIGNMENT);
		// Определение размеров надписей к текстовым полям
		GUITools.makeSameSize(new JComponent[] { nameLabel, passwrdLabel });
		// Определение стандартного вида для кнопок
		GUITools.createRecommendedMargin(new JButton[] { btnOk, btnCancel });
		// Устранение "бесконечной" высоты текстовых полей
		GUITools.fixTextFieldSize(tfLogin);
		GUITools.fixTextFieldSize(tfPassword);

		// Сборка интерфейса
		panel.add(name);
		panel.add(Box.createVerticalStrut(12));
		panel.add(password);
		panel.add(Box.createVerticalStrut(17));
		panel.add(flow);

		tfLogin.setFont(new Font("Times New Roman", Font.PLAIN, 19));
		tfLogin.setSize(new Dimension(60, 60));

		tfPassword.setFont(new Font("Times New Roman", Font.PLAIN, 19));
		tfPassword.setSize(new Dimension(90, 90));

		btnOk.setFont(new Font("Times New Roman", Font.PLAIN, 19));
		btnOk.setSize(new Dimension(50, 50));
		btnCancel.setFont(new Font("Times New Roman", Font.PLAIN, 19));
		btnCancel.setSize(new Dimension(50, 50));

		btnCancel.addMouseListener(new MouseListener() {

			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			public void mousePressed(MouseEvent e) {

			}

			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			public void mouseClicked(MouseEvent e) {
				isShowPassword = !isShowPassword;
				if (isShowPassword) {
					tfPassword.setEchoChar((char) 0);
				} else {
					tfPassword.setEchoChar('*');
				}

			}
		});
		if (user != null) {
			tfLogin.setText(user.getEmail());
			tfPassword.setText(user.getPassword());
		}
		
		btnOk.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				User newUser = new User();
				
				newUser.setEmail(Validator.validate(tfLogin.getText()) ? tfLogin.getText() : user.getEmail());
				newUser.setPassword(tfPassword.getText());
				newUser.setId(user.getId());
				
				Conn.saveUser(newUser);
				setVisible(false);
			}
		});
		// готово
		return panel;
	}

}
