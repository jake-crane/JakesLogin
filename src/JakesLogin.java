import java.awt.EventQueue;

import javax.swing.UIManager;

public class JakesLogin {
	public static void main(String[] args) {

		try {
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
}
