import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

@SuppressWarnings("serial")
public class MyMenu extends JPopupMenu {

	LoginButton myButton;

	public MyMenu(final LoginButton myButton, final Gui gui) {

		this.myButton = myButton;

		JMenuItem editItem = new JMenuItem("edit");
		editItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gui.setButtonBeingEdited(myButton);
				gui.getEditPanel().getTextField1().setText(myButton.getUsername());
				gui.getEditPanel().getTextField2().setText(myButton.getPassword());
				gui.getEditPanel().getTextField3().setText(myButton.getDisplayName());
				gui.openEditor();
			}

		});
		add(editItem);

		JMenuItem deleteItem = new JMenuItem("delete");
		deleteItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int response = JOptionPane.showConfirmDialog(null, 
						"Are you sure You want to delete the account " + myButton.getDisplayName() + "?", "Permenently delete Account?", 
						0);
				if (response == JOptionPane.YES_OPTION) {
					gui.removeButtonAndUpdateGui(myButton);
				}
			}

		});
		add(deleteItem);

		add(new JMenuItem("cancel"));

	}
}