import java.awt.BorderLayout;
import javax.swing.JFrame;

public class MainFrame extends JFrame {
	
	private Toolbar toolbar;
	private FormPanel menuPanel;
	private SortPanel sortPanel;
	
	public MainFrame() {
		
		super("Sorting Algorithm Visualizer");
		
		menuPanel = new FormPanel();
		toolbar = new Toolbar();
		sortPanel = new SortPanel();
		
		setLayout(new BorderLayout());
		
		toolbar.setButtonListener(new ButtonListener() {

			public void buttonSend(int index) {
				if(index == 0) {
					sortPanel.sort(menuPanel.getAlgorithmIndex());
				}
				else if(index == 1) {
					sortPanel.stop();
				}
				else if(index == 2) {
					sortPanel.randomSet();
				}
				else if(index == 3) {
					sortPanel.ascSet();
				}
				else {
					sortPanel.descSet();
				}
			}
			
		});
	
		menuPanel.setSliderListener(new SliderListener() {
			public void sendNewBlockTotal(int total) {
				sortPanel.setTotal(total);
			}

			public void sendNewSpeed(int speed) {
				sortPanel.setSpeed(speed);
			}
			
		});
		
		
		add(toolbar, BorderLayout.NORTH);
		add(menuPanel, BorderLayout.WEST);
		add(sortPanel, BorderLayout.EAST);
		
		setSize(1200, 720);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
		
		
	}
}
