package Constants;

//����� ��� ������ � ���������� �������������
import javax.swing.*;
public class BoxLayoutUtils
{
	// ������������ ����������� �� ��� X ��� ������ �����������
	public static void setGroupAlignmentX(JComponent[] components, float alignment) {
		for (int i = 0; i < components.length; i++)
			components[i].setAlignmentX(alignment);
	}
	// ������������ ����������� �� ��� Y ��� ������ �����������
	public static void setGroupAlignmentY(JComponent[] components, float alignment) {
		for (int i = 0; i < components.length; i++)
			components[i].setAlignmentY(alignment);
	}
	// �������� ������ � ������������ �������������
	public static JPanel createVerticalPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		return panel;
	}
	// �������� ������ � �������������� �������������
	public static JPanel createHorizontalPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		return panel;
	}
}
