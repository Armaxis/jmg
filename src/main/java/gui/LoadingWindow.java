package gui;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class LoadingWindow {
	
	public static JDialog LOADING_PANE;
	
	public static void showLoadingWindow()
	{
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		} 
		JOptionPane pane = new JOptionPane("Загрузка. Пожалуйста, подождите...", JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
		
		LOADING_PANE = new JDialog();
		LOADING_PANE.setModal(false);

		LOADING_PANE.setContentPane(pane);

		LOADING_PANE.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		LOADING_PANE.setLocation(100, 200);
		LOADING_PANE.pack();
		LOADING_PANE.setVisible(true);
	}
	
	public static void hideLoadingWindow()
	{
		LOADING_PANE.dispose();
		LOADING_PANE.setVisible(false);
		LOADING_PANE = null;
	}
}
