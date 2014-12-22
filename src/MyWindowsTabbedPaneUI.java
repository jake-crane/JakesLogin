import javax.swing.JTabbedPane;

import com.sun.java.swing.plaf.windows.WindowsTabbedPaneUI;

public class MyWindowsTabbedPaneUI extends WindowsTabbedPaneUI {

	public int getWidthofTabRow(JTabbedPane tabbedPane) {
		int width = 0;
		for (int i = 0; i < tabbedPane.getTabCount(); i++) {
			width += getTabBounds(tabbedPane, i).width;
		}
		return width;
	}

}
