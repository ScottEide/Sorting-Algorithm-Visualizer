import java.awt.BorderLayout;
import javax.swing.JFrame;

public class MainFrame extends JFrame {
	
	private Toolbar toolbar;
	private FormPanel menuPanel;
	private SortPanel sortPanel;
	
	public MainFrame() {
		// Name of the window
		super("Sorting Algorithms Visualizer");
		
		// Setting up the panels
		menuPanel = new FormPanel();
		toolbar = new Toolbar();
		sortPanel = new SortPanel();
		
		setLayout(new BorderLayout());
		
		// Adding a listener to the toolbar for button presses
		toolbar.setButtonListener(new ButtonListener() {
			
			// index is the position of each button in the toolbar.  index = 0 is the first button, 1 the second, etc.
			public void buttonSend(int index) {
				if(index == 0) {
					sortPanel.start(menuPanel.getAlgorithmIndex());
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
		
		// Adding a listener to the sliders in the menu, which will send the new values to sortPanel when changed
		menuPanel.setSliderListener(new SliderListener() {
			public void sendNewBlockTotal(int total) {
				sortPanel.setTotal(total);
			}

			public void sendNewSpeed(int speed) {
				sortPanel.setSpeed(speed);
			}
			
		});
		
		// Adding the panels to the window
		add(toolbar, BorderLayout.NORTH);
		add(menuPanel, BorderLayout.WEST);
		add(sortPanel, BorderLayout.EAST);
		
		// Window settings
		setSize(1200, 720);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}
}
