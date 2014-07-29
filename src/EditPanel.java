import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class EditPanel extends JPanel {
	
	JPanel buttonPanel = new JPanel();
	private JButton saveButton = new JButton("Save");
	private JButton dontSaveButton = new JButton("Don't Save");
	JTextField textField1 = new JTextField();
	JTextField textField2 = new JTextField();
	JTextField textField3 = new JTextField();
	
	private JLabel label1 = new JLabel("Username:");
	private JLabel label2 = new JLabel("Password:");
	private JLabel label3 = new JLabel("Display Name:");
	
	private Gui gui;
	
	public EditPanel(Gui gui) {
		
		this.gui = gui;
		
		setLayout(new GridBagLayout());

		GridBagConstraints labelConstraints = new GridBagConstraints();
		
		labelConstraints.anchor = GridBagConstraints.LINE_END;
		labelConstraints.insets = new Insets(0, 0, 0, 5);
		labelConstraints.weightx = .2;
		
		labelConstraints.gridx = 0;
		labelConstraints.gridy = 0;
		add(label1, labelConstraints);
		labelConstraints.gridy = 1;
		add(label2, labelConstraints);
		labelConstraints.gridy = 2;
		add(label3, labelConstraints);
		
		GridBagConstraints textFieldConstraints = new GridBagConstraints();
		
		textFieldConstraints.insets = new Insets(2, 5, 0, 5);
		textFieldConstraints.fill = GridBagConstraints.BOTH;
		textFieldConstraints.weightx = 2;
		
		textFieldConstraints.gridx = 1;
		textFieldConstraints.gridy = 0;
		add(textField1, textFieldConstraints);
		textFieldConstraints.gridy = 1;
		add(textField2, textFieldConstraints);
		textFieldConstraints.gridy = 2;
		add(textField3, textFieldConstraints);
		
		saveButton.setBounds(10, 95, 90, 20);
		saveButton.setFocusPainted(false);
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveButtonActionPerformed();
			}
		});

		dontSaveButton.setBounds(105, 95, 100, 20);
		dontSaveButton.setFocusPainted(false);
		dontSaveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				closeEditPanelOpenTabbedPAnel();
			}
		});
		
		buttonPanel.setLayout(new GridBagLayout());
		
		GridBagConstraints buttonConstraints = new GridBagConstraints();
		
		buttonConstraints.insets = new Insets(5, 0, 0, 0);
		buttonConstraints.gridx = 0;
		buttonConstraints.gridy = 0;
		buttonPanel.add(saveButton, buttonConstraints);
		buttonConstraints.gridx = 1;
		buttonPanel.add(dontSaveButton, buttonConstraints);
		
		GridBagConstraints buttonPanelConstraints = new GridBagConstraints();
		
		buttonPanelConstraints.fill = GridBagConstraints.BOTH;
		buttonPanelConstraints.weightx = 1;
		buttonPanelConstraints.gridx = 0;
		buttonPanelConstraints.gridy = 3;
		buttonPanelConstraints.gridwidth = 2;

		add(buttonPanel, buttonPanelConstraints);
	}
	
	public JTextField getTextField1() {
		return textField1;
	}

	public JTextField getTextField2() {
		return textField2;
	}

	public JTextField getTextField3() {
		return textField3;
	}

	private void closeEditPanelOpenTabbedPAnel() {
		gui.add(gui.getTabbedPane1());
		gui.remove(this);
		gui.repaint(); //TODO figure out why this is necessary
	}
	
	private void saveButtonActionPerformed() {

		//allow user to delete button button by saving when textboxes are empty
		if ((textField1.getText().isEmpty()) && (textField2.getText().isEmpty()) && 
				(textField3.getText().isEmpty())) {
			gui.removeButtonAndUpdateGui(gui.getButtonBeingEdited());
			textField1.setText(null);
			textField2.setText(null);
			textField3.setText(null);
			closeEditPanelOpenTabbedPAnel();
			return;
		}

		if ((textField1.getText().isEmpty()) || (textField2.getText().isEmpty()) 
				|| (textField3.getText().isEmpty())) {
			JOptionPane.showMessageDialog(this, "One or more text boxes are blank.", 
					"Warning", 
					0);
			return;
		}

		gui.getButtonBeingEdited().setText(textField3.getText());
		gui.getButtonBeingEdited().setUsername(textField1.getText());
		gui.getButtonBeingEdited().setPassword(textField2.getText());
		gui.getButtonBeingEdited().setDisplayName(textField3.getText());

		if (!gui.getButtons().contains(gui.getButtonBeingEdited())) {
			gui.getButtons().add(gui.getButtons().size() - 1, gui.getButtonBeingEdited());
		}


		int tabbedPane1Index = gui.getTabbedPane1().getSelectedIndex();
		gui.removeTabsAndButtonsFromGui();
		gui.addTabsAndButtonsToGui();
		if (tabbedPane1Index > -1 && tabbedPane1Index < gui.getTabbedPane1().getTabCount()) {
			gui.getTabbedPane1().setSelectedIndex(tabbedPane1Index);
		}

		textField1.setText(null);
		textField2.setText(null);
		textField3.setText(null);
		closeEditPanelOpenTabbedPAnel();
	}

}
