package swing;
/* github site editing */
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class SwingInterface extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2482747612140472518L;

	public static void main(String[] args) {
		new SwingInterface();
	}
	
	public SwingInterface(){
		super("SwingTest");
		
		setBounds(500, 300, 400, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Container contentPane = this.getContentPane();
		JPanel pane = new JPanel();
		JButton buttonStart = new JButton("Start");
		buttonStart.setMnemonic('S');
		buttonStart.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.exit(0);
			}
			
		});
		
		pane.add(buttonStart);
		contentPane.add(pane);
		
		setVisible(true);
	}
}
