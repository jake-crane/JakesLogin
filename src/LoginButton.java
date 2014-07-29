import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class LoginButton extends JButton {
	
	private String username;
	private String password;
	private String displayName;
	
	
	public LoginButton(final Gui gui) {	
		final LoginButton myButton = this;
		
		setComponentPopupMenu(new MyMenu(myButton, gui));
		
		this.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gui.login(username, password);
			}});
	}
	
	public LoginButton(String username, String password, String displayName) {
		this.username = username;
		this.password = password;
		this.displayName = displayName;
		setText(displayName);
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	public String getDisplayName() {
		return displayName;
	}
	
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
}