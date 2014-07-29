import java.awt.AWTException;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
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
import javax.swing.JTextField;
import javax.swing.UIManager;

@SuppressWarnings("serial")
public class Gui extends JFrame {

	private int formxcoord = 876, formycoord = 633, tabIndex = 0;
	private int mousexcoord = 757,  mouseycoord = 482;
	File settingsFile = new File("loginsettings.txt");
	private JPanel infoframe, settingsTab;
	private JButton deleteSettingsButton = new JButton();
	private JButton saveButton = new JButton();
	private JButton dontSaveButton = new JButton();
	JTextField textField1,  textField2, textField3;
	private JLabel label1, label2, label3, label4, label5;
	JTabbedPane tabbedPane1;
	final JButton addNewButton = new JButton("Add New");
	JCheckBox ObscureInfoCheck = new JCheckBox("Obscure Info In File", true);
	final int tabWidth = 50;

	public ArrayList<JButton> buttons = new ArrayList<JButton>();
	ArrayList<JPanel> tabs = new ArrayList<JPanel>();

	LoginButton buttonBeingEdited;

	MyWindowsTabbedPaneUI myWindowsTabbedPaneUI = new MyWindowsTabbedPaneUI();

	public Gui() {

		Image myIcon = new ImageIcon(Gui.class.getResource("icon.png")).getImage();
		setIconImage(myIcon);

		if (settingsFile.exists()) {
			readSettings();
		}
		initComponents();

	}

	public static void main(String[] args) {

		try {
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				new Gui().setVisible(true);
			}
		});
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
		tabbedPane1.setVisible(false);
		infoframe.setVisible(true);
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
		//sb.append("Window Width:" + getWidth());
		//sb.append(System.lineSeparator());
		//sb.append("ObscureInfoCheck.isSelected():" + ObscureInfoCheck.isSelected());

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

			BufferedReader input = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(data)));

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
			input.close();


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

		/*System.out.println("info:");

		System.out.println("buttons:");
		for (int i = 0; i < buttons.size(); i++) {
			System.out.println(buttons.get(i).getText());
		}

		System.out.println("tabs:");
		for (int i = 0; i < tabs.size(); i++) {
			System.out.println(tabs.get(i));
		}

		System.out.println("tabbedPane1.getComponents():");
		for (int i = 0; i < tabbedPane1.getComponents().length; i++) {
			System.out.println(tabbedPane1.getComponents()[i]);
		}
		System.out.println();

		/*System.out.println("tabbedPane1.getComponents():");
		for (int i = 0; i < this.getComponents().length; i++) {
			System.out.println(tabbedPane1.getComponents()[i]);
		}*/

		//int x = 10, y = 5, width = 90, height = 22;
		//int anchor = GridBagConstraints.LAST_LINE_END;
		int gridx = 0, gridy = 0;
		for (int i = 0; i < buttons.size(); i++) {

			/*if (buttons.get(i) instanceof LoginButton) {
				buttons.add(i, new LoginButton());
				buttons.get(i).setText("Login " + (i + 1));
			}*/

			buttons.get(i).setFont(new Font("Tahoma", 1, 8));
			buttons.get(i).setPreferredSize(new Dimension(90, 22));
			buttons.get(i).setMinimumSize(new Dimension(90, 22));
			//buttons.get(i).setBounds(x, y, width, height);
			buttons.get(i).setFocusPainted(false);

			/*if (anchor == GridBagConstraints.FIRST_LINE_START) {
				anchor = GridBagConstraints.LINE_START;
			} else if (anchor == GridBagConstraints.LINE_START) {
				anchor = GridBagConstraints.LAST_LINE_START;
			} else if (anchor == GridBagConstraints.LAST_LINE_START) {
				anchor = GridBagConstraints.FIRST_LINE_START;
			} else if (anchor == GridBagConstraints.FIRST_LINE_START) {
				anchor = GridBagConstraints.LINE_END;
			} else if (anchor == GridBagConstraints.LINE_END) {
				anchor = GridBagConstraints.LAST_LINE_END;
			} else if (anchor == GridBagConstraints.LAST_LINE_END) {
				anchor = GridBagConstraints.FIRST_LINE_START;
			}*/

			GridBagConstraints c = new GridBagConstraints();

			c.insets = new Insets(3,8,0,0); 
			//c.weightx = 0.00000000000000000000000001;
			//c.weighty = 0.5;
			//c.fill = GridBagConstraints.HORIZONTAL;
			c.gridx = gridx;
			c.gridy = gridy;
			//c.gridwidth = 1;
			//c.anchor = anchor;

			if (gridx ==0) {
				gridx = 1;
			} else {				
				gridx = 0;
				gridy++;
				if (gridy > 2) {
					gridy = 0;
				}
			}

			/*if (x == 10) {
				x = 110;
			} else {
				x = 10;
				if (y == 5) {
					y = 30;				
				} else if ( y == 30) {
					y = 55;
				} else if (y == 55) {
					y = 5;
				}
			}*/

			//System.out.println("adding " + buttons.get(i).getText() + " to tab");
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

		//setPreferredSize(new Dimension(myWindowsTabbedPaneUI.getWidthofTabRow(tabbedPane1) + 15, getHeight()));
		//setMinimumSize(new Dimension(myWindowsTabbedPaneUI.getWidthofTabRow(tabbedPane1) + 15, getHeight()));
		//pack();
		//System.out.println("tabbedPane1.getLocation().x: " + tabbedPane1.getLocation().x);
		setSize(myWindowsTabbedPaneUI.getWidthofTabRow(tabbedPane1) + 16, getHeight());
	}

	/*@Override
	public void paint(Graphics g) {
		super.paint(g);
		System.out.println("current width: " + getWidth());
		//System.out.println("current height: " + getHeight());
		//System.out.println("max height: " + getMaximumSize().height);
		if (getHeight() > getMaximumSize().height) {
			setVisible(false);
			setSize(getWidth(), getMaximumSize().height);
			setVisible(true);
		}
	}*/

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
		tabbedPane1 = new JTabbedPane();
		tabbedPane1.setUI(myWindowsTabbedPaneUI);
		tabbedPane1.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		settingsTab = new JPanel();
		textField1 = new JTextField();
		textField2 = new JTextField();
		textField3 = new JTextField();
		infoframe = new JPanel();
		label1 = new JLabel();
		label2 = new JLabel();
		label3 = new JLabel();
		label4 = new JLabel();
		label5 = new JLabel();

		setTitle("Jake's Login");
		setPreferredSize(new Dimension(228, 150));
		setMinimumSize(new Dimension(228, 150));
		setMaximumSize(new Dimension(2000, 150));
		//setBounds(formxcoord, formycoord, 228, 150);
		setAlwaysOnTop(true);
		//setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		//System.out.println("getLayout(): " + getLayout());

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


		JPanel tab1 = new JPanel();
		tab1.setLayout(new GridBagLayout());
		tabs.add(tab1);

		addNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buttonBeingEdited = new LoginButton(Gui.this);
				buttonBeingEdited.setFont(new Font("Tahoma", 1, 8));
				buttonBeingEdited.setFocusPainted(false);
				Gui.this.textField1.setText("");
				Gui.this.textField2.setText("");
				Gui.this.textField3.setText("");
				openEditor();
			}

		});

		buttons.add(addNewButton);

		addTabsAndButtonsToGui();

		settingsTab.setLayout(null);//TODO rewrite using a layout manager

		ObscureInfoCheck.setBounds(3, 20, 160, 25);
		settingsTab.add(ObscureInfoCheck);

		label4.setText("Mouse coordinates: " + mousexcoord + "," + mouseycoord);
		label4.setBounds(5, 0, 160, 25);
		settingsTab.add(label4);

		deleteSettingsButton.setText("Delete Settings File");
		deleteSettingsButton.setFont(new Font("Tahoma", 1, 9));
		deleteSettingsButton.setBounds(40, 55, 130, 22);
		deleteSettingsButton.setFocusPainted(false);
		deleteSettingsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				deleteSettingsButtonActionPerformed(e);
			}
		});
		settingsTab.add(deleteSettingsButton);

		label5.setText("+");
		label5.setBounds(160, 0, 25, 25);
		label5.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				setCursor(new Cursor(1));
				label5.setVisible(false);
			}
			public void mouseReleased(MouseEvent e) {
				setCursor(new Cursor(0));
				mousexcoord = MouseInfo.getPointerInfo().getLocation().x;
				mouseycoord = MouseInfo.getPointerInfo().getLocation().y;
				label4.setText("Mouse coordinates: " + mousexcoord + "," + mouseycoord);
				label5.setVisible(true);
			}
		});
		settingsTab.add(label5);

		infoframe.setLayout(new GridLayout(7, 2));//TODO rewrite using a differnt layout manager

		label1.setText("Username:");
		//label1.setBounds(10, 0, 160, 15);
		//textField1.setBounds(10, 15, 180, 16);
		label2.setText("Password:");
		//label2.setBounds(10, 30, 155, 15);
		//textField2.setBounds(10, 45, 180, 16);
		label3.setText("Display Name:");
		//label3.setBounds(10, 60, 145, 15);
		//textField3.setBounds(10, 75, 180, 16);

		saveButton.setText("Save");
		saveButton.setBounds(10, 95, 90, 20);
		saveButton.setFocusPainted(false);
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveButtonActionPerformed();
			}
		});
		dontSaveButton.setText("Don't Save");
		dontSaveButton.setBounds(105, 95, 100, 20);
		dontSaveButton.setFocusPainted(false);
		dontSaveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dontSaveButtonActionPerformed(e);
			}
		});
		infoframe.add(label1);
		infoframe.add(textField1);
		infoframe.add(label2);
		infoframe.add(textField2);
		infoframe.add(label3);
		infoframe.add(textField3);
		infoframe.add(saveButton);
		infoframe.add(dontSaveButton);

		add(infoframe);
		add(tabbedPane1);
		infoframe.setVisible(false);

		infoframe.setBounds(5, 5, 230, 140);
		//tabbedPane1.setBounds(5, 5, 214, 112);
		//tabbedPane1.setPreferredSize(new Dimension(214, 112));
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

	private void saveButtonActionPerformed() {

		//allow user to reset button to blank
		if ((textField1.getText().isEmpty()) && (textField2.getText().isEmpty()) && 
				(textField3.getText().isEmpty())) {
			removeButtonAndUpdateGui(buttonBeingEdited);
			textField1.setText(null);
			textField2.setText(null);
			textField3.setText(null);
			tabbedPane1.setVisible(true);
			infoframe.setVisible(false);
			return;
		}

		if ((textField1.getText().isEmpty()) || (textField2.getText().isEmpty()) 
				|| (textField3.getText().isEmpty())) {
			JOptionPane.showMessageDialog(this, "One or more text boxes are blank.", 
					"Warning", 
					0);
			return;
		}

		buttonBeingEdited.setText(textField3.getText());
		buttonBeingEdited.setUsername(textField1.getText());
		buttonBeingEdited.setPassword(textField2.getText());
		buttonBeingEdited.setDisplayName(textField3.getText());

		if (!buttons.contains(buttonBeingEdited)) {
			buttons.add(buttons.size() - 1, buttonBeingEdited);
		}


		int tabbedPane1Index = tabbedPane1.getSelectedIndex();
		removeTabsAndButtonsFromGui();
		addTabsAndButtonsToGui();
		if (tabbedPane1Index > -1 && tabbedPane1Index < tabbedPane1.getTabCount()) {
			tabbedPane1.setSelectedIndex(tabbedPane1Index);
		}

		textField1.setText(null);
		textField2.setText(null);
		textField3.setText(null);
		tabbedPane1.setVisible(true);
		infoframe.setVisible(false);
	}

	private void dontSaveButtonActionPerformed(ActionEvent e) {
		tabbedPane1.setVisible(true);
		infoframe.setVisible(false);
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