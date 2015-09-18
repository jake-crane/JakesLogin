import java.awt.AWTException;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

@SuppressWarnings("serial")
public class Gui extends JFrame {

	private int formxcoord = 876, formycoord = 633, tabIndex = 0;
	private int mousexcoord = 757,  mouseycoord = 482;
	private File settingsFile = new File("loginsettings.txt");
	private JPanel settingsTab = new JPanel();
	private EditPanel editPanel = null;
	private JButton deleteSettingsButton = new JButton("Delete Settings File");
	private JLabel label4 = new JLabel("Mouse coordinates: " + mousexcoord + "," + mouseycoord);
	private JLabel label5 = new JLabel("+");
	private JTabbedPane tabbedPane1 = new JTabbedPane();
	private JButton addNewButton = new JButton("Add New");
	private JCheckBox ObscureInfoCheck = new JCheckBox("Obscure Info In File", true);
	final int tabWidth = 50;

	public ArrayList<JButton> buttons = new ArrayList<JButton>();
	private ArrayList<JPanel> tabs = new ArrayList<JPanel>();

	private LoginButton buttonBeingEdited;

	private MyWindowsTabbedPaneUI myWindowsTabbedPaneUI = new MyWindowsTabbedPaneUI();

	public Gui() {

		Image myIcon = new ImageIcon(Gui.class.getResource("icon.png")).getImage();
		setIconImage(myIcon);

		if (settingsFile.exists()) {
			readSettings();
		}

		setTitle("Jake's Login");
		setPreferredSize(new Dimension(228, 150));
		setMinimumSize(new Dimension(228, 150));
		setMaximumSize(new Dimension(2000, 150));
		setAlwaysOnTop(true);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent winEvt) {
				formxcoord = getLocation().x;
				formycoord = getLocation().y;
				if (buttons.size() > 1) {
					writeSettings();
				}
			}
		});

		initComponents();

	}

	public EditPanel getEditPanel() {
		return editPanel;
	}

	public JTabbedPane getTabbedPane1() {
		return tabbedPane1;
	}

	public ArrayList<JButton> getButtons() {
		return buttons;
	}

	public LoginButton getButtonBeingEdited() {
		return buttonBeingEdited;
	}

	public void setButtonBeingEdited(LoginButton buttonBeingEdited) {
		this.buttonBeingEdited = buttonBeingEdited;
	}

	void login(String username, String password) {

		try {
			Robot robot = new Robot();
			robot.mouseMove(mousexcoord, mouseycoord);
			robot.mousePress(16);
			robot.mouseRelease(16);
			Thread.currentThread();
			Thread.sleep(350L);
			sendkeys(username);
			Thread.currentThread();
			Thread.sleep(150L);
			robot.keyPress(10);
			robot.keyRelease(10);
			Thread.currentThread();
			Thread.sleep(100L);
			sendkeys(password);
			Thread.currentThread();
			Thread.sleep(100L);
			robot.keyPress(10);
			robot.keyRelease(10);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void openEditor() {
		remove(tabbedPane1);
		add(editPanel);
		validate();//TODO figure out why this is necessary
		repaint();//TODO figure out why this is necessary
	}

	private void sendkeys(String text) {
		try {
			Robot robot = new Robot();
			String uppercase = text.toUpperCase();
			for (char c : uppercase.toCharArray()) {
				if (c == '@') {
					robot.keyPress(KeyEvent.VK_SHIFT);
					robot.keyPress(KeyEvent.VK_2);
					robot.keyRelease(KeyEvent.VK_2);
					robot.keyRelease(KeyEvent.VK_SHIFT);
				} else if (c == '_') {
					robot.keyPress(KeyEvent.VK_SHIFT);
					robot.keyPress(KeyEvent.VK_MINUS);
					robot.keyRelease(KeyEvent.VK_SHIFT);
				} else {
					robot.keyPress(c);
					robot.keyRelease(c);
				}
			}
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	private String getData() {

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < buttons.size() - 1; i++) {
			sb.append("Login " + (i + 1));
			sb.append(System.lineSeparator());
			sb.append("Username:" + ((LoginButton)buttons.get(i)).getUsername());
			sb.append(System.lineSeparator());
			sb.append("Password:" + ((LoginButton)buttons.get(i)).getPassword());
			sb.append(System.lineSeparator());
			sb.append("Displayname:" + ((LoginButton)buttons.get(i)).getDisplayName());
			sb.append(System.lineSeparator());
			sb.append(System.lineSeparator());
		}

		sb.append("Form coordinates:");
		sb.append(System.lineSeparator());
		sb.append("x:" + formxcoord);
		sb.append(System.lineSeparator());
		sb.append("y:" + formycoord);
		sb.append(System.lineSeparator());
		sb.append(System.lineSeparator());
		sb.append("Mouse coordinates:");
		sb.append(System.lineSeparator());
		sb.append("x:" + mousexcoord);
		sb.append(System.lineSeparator());
		sb.append("y:" + mouseycoord);
		sb.append(System.lineSeparator());
		sb.append(System.lineSeparator());
		sb.append("Tab Index:" + tabbedPane1.getSelectedIndex());
		sb.append(System.lineSeparator());

		return sb.toString();

	}

	private static byte[] getFlippedBytes(byte[] bytes) {
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte) (Integer.reverse(bytes[i]) >>> (Integer.SIZE - Byte.SIZE));
		}
		return bytes;
	}

	private void readSettings() {
		byte[] data;
		try {
			data = Files.readAllBytes(new File("loginsettings.txt").toPath());

			if ((char)data[0] != 'L') {
				//System.out.println("flipping bytes");
				data = getFlippedBytes(data);
			} else {
				ObscureInfoCheck.setSelected(false);
			}

			try (BufferedReader input = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(data)))) {

			input.readLine();//System.out.println("skipping " + input.readLine());
			String line = input.readLine();

			while (true) {
				LoginButton button = new LoginButton(this);
				//System.out.println("processing " + line);

				if (line.equals("Username:")) {
					input.readLine();//System.out.println("skipping " + input.readLine());
					input.readLine();//System.out.println("skipping " + input.readLine());
				} else {
					//System.out.println("trying to get username at " + line);
					String username = line.replace("Username:", "");
					String password = input.readLine().replace("Password:", "");
					String displayName = input.readLine().replace("Displayname:", "");
					//System.out.println("setting username to " + username);
					button.setUsername(username);
					button.setPassword(password);
					button.setDisplayName(displayName);
					button.setText(displayName);
					//System.out.println("adding " + button.getUsername());
					buttons.add(button);
					//System.out.println("skipping " + input.readLine());
				}
				input.readLine(); //read newline

				if (input.readLine().contains("Form coordinates:")) { //read login x) {
					break;
				}
				line = input.readLine();
			}

			//System.out.println(line);
			line = input.readLine();
			formxcoord = Integer.parseInt(line.replace("x:", ""));
			line = input.readLine();
			formycoord = Integer.parseInt(line.replace("y:", ""));
			setLocation(formxcoord, formycoord);
			input.readLine();
			input.readLine();
			line = input.readLine();
			mousexcoord = Integer.parseInt(line.replace("x:", ""));
			line = input.readLine();
			mouseycoord = Integer.parseInt(line.replace("y:", ""));
			line = input.readLine();
			try {
				line = input.readLine();
				tabIndex = Integer.parseInt(line.replace("Tab Index:", ""));
				//line = input.readLine();
				//ObscureInfoCheck.setSelected(Boolean.parseBoolean(line.replace("ObscureInfoCheck.isSelected:", "")));
			} catch (Exception e) {
				System.out.println("invalid settings file");
			}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void removeButtonAndUpdateGui(LoginButton myButton) {
		buttons.remove(myButton);
		removeTabsAndButtonsFromGui();
		addTabsAndButtonsToGui();
	}

	/**
	 * leaves 1 tab that gets cleared
	 *
	 */
	public void removeTabsAndButtonsFromGui() {
		while (tabs.size() > 1) {
			tabs.remove(1);
		}
		tabs.get(0).removeAll();
		tabbedPane1.removeAll();
	}

	public void addTabsAndButtonsToGui() {

		int gridx = 0, gridy = 0;
		for (int i = 0; i < buttons.size(); i++) {

			buttons.get(i).setFont(new Font("Tahoma", 1, 8));
			buttons.get(i).setPreferredSize(new Dimension(90, 22));
			buttons.get(i).setMinimumSize(new Dimension(90, 22));
			buttons.get(i).setFocusPainted(false);

			GridBagConstraints c = new GridBagConstraints();

			c.insets = new Insets(3,8,0,0);
			c.gridx = gridx;
			c.gridy = gridy;

			if (gridx ==0) {
				gridx = 1;
			} else {
				gridx = 0;
				gridy++;
				if (gridy > 2) {
					gridy = 0;
				}
			}

			for (int j = 0; j < tabs.size(); j++) {
				if (tabs.get(j).getComponentCount() < 6) {
					tabs.get(j).add(buttons.get(i), c);
					break;
				}
				if (j == tabs.size() - 1) {
					JPanel newTab = new JPanel();
					newTab.setLayout(new GridBagLayout());
					newTab.add(buttons.get(i), c);
					tabs.add(newTab);
					tabbedPane1.add(newTab);
					break;
				}
			}

		}

		int startRange = 1;
		for (int i = 0; i < tabs.size(); i++) {
			String tabTitle = startRange + "-" + (startRange + 5);
			tabbedPane1.addTab(tabTitle, tabs.get(i));
			startRange += 6;
		}

		tabbedPane1.addTab("Settings", settingsTab);

		setSize(myWindowsTabbedPaneUI.getWidthofTabRow(tabbedPane1) + 16, getHeight());
	}

	private void writeSettings() {

		try (BufferedWriter output = new BufferedWriter(new FileWriter(settingsFile))) {

			String data = getData();

			if (ObscureInfoCheck.isSelected()) {
				byte[] bytes = data.getBytes();
				bytes = getFlippedBytes(bytes);
				output.write(new String(bytes));
			} else {
				output.write(data);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initComponents() {

		editPanel = new EditPanel(this);

		tabbedPane1.setUI(myWindowsTabbedPaneUI);
		tabbedPane1.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

		JPanel tab1 = new JPanel();
		tab1.setLayout(new GridBagLayout());
		tabs.add(tab1);

		addNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonBeingEdited = new LoginButton(Gui.this);
				buttonBeingEdited.setFont(new Font("Tahoma", 1, 8));
				buttonBeingEdited.setFocusPainted(false);
				Gui.this.editPanel.getTextField1().setText("");
				Gui.this.editPanel.getTextField2().setText("");
				Gui.this.editPanel.getTextField3().setText("");
				openEditor();
			}

		});

		buttons.add(addNewButton);

		addTabsAndButtonsToGui();

		settingsTab.setLayout(null);//TODO rewrite using a layout manager

		ObscureInfoCheck.setBounds(3, 20, 160, 25);
		settingsTab.add(ObscureInfoCheck);

		label4.setBounds(5, 0, 160, 25);
		settingsTab.add(label4);

		deleteSettingsButton.setFont(new Font("Tahoma", 1, 9));
		deleteSettingsButton.setBounds(40, 55, 130, 22);
		deleteSettingsButton.setFocusPainted(false);
		deleteSettingsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				deleteSettingsButtonActionPerformed(e);
			}
		});
		settingsTab.add(deleteSettingsButton);

		label5.setBounds(160, 0, 25, 25);
		label5.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				setCursor(new Cursor(1));
				label5.setVisible(false);
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				setCursor(new Cursor(0));
				mousexcoord = MouseInfo.getPointerInfo().getLocation().x;
				mouseycoord = MouseInfo.getPointerInfo().getLocation().y;
				label4.setText("Mouse coordinates: " + mousexcoord + "," + mouseycoord);
				label5.setVisible(true);
			}
		});

		settingsTab.add(label5);

		//editPanel.setVisible(false);
		add(editPanel);

		add(tabbedPane1);

		tabbedPane1.setSelectedIndex(tabIndex);
	}

	int getButtonNumber(LoginButton button) {
		for (int i = 0; i < buttons.size(); i++) {
			if (buttons.get(i) == button) {
				return i + 1;
			}
		}
		return -1;
	}

	private void deleteSettingsButtonActionPerformed(ActionEvent e) {
		if (settingsFile.exists()) {
			int response = JOptionPane.showConfirmDialog(null,
					"Would you like to delete it?", "old Settings file found.",
					0);
			if (response == JOptionPane.YES_OPTION) {
				settingsFile.delete();
				JOptionPane.showMessageDialog(this, "Settings file has been deleted.",
						"It's gone.",
						-1);
			} else {
				JOptionPane.showMessageDialog(this, "Settings file has been kept.",
						"File safe.",
						-1);
			}
		} else {
			JOptionPane.showMessageDialog(this, "Settings file does not exist",
					"File Not Found",
					0);
		}
	}

}
