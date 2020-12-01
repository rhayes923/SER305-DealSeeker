import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Display extends JFrame {

	private static final long serialVersionUID = 1L;

	public Display(int width, int height, String deals) {
		super("DealSeeker");
		JPanel panel = new JPanel();
		JLabel label = new JLabel(deals);
		panel.add(label);
		add(panel);
		setSize(width, height);
		pack();
		setResizable(true);
		setVisible(true);

	}
}
