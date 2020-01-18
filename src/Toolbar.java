import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

public class Toolbar extends JPanel implements ActionListener {
	
	private JButton startBtn;
	private JButton stopBtn;
	private JButton randomSetBtn;
	private JButton ascSetBtn;
	private JButton decSetBtn;
	
	private ButtonListener buttonListener;
	
	public Toolbar() {
		setBorder(BorderFactory.createEtchedBorder());
		
		// Creating all the buttons for the toolbar
		startBtn  = new JButton("Start");
		stopBtn = new JButton("Stop");
		randomSetBtn = new JButton("Random Set");
		ascSetBtn = new JButton("Ascending Set");
		decSetBtn = new JButton("Descending Set");
		
		// Adding action listeners to each button
		startBtn.addActionListener(this);
		stopBtn.addActionListener(this);
		randomSetBtn.addActionListener(this);
		ascSetBtn.addActionListener(this);
		decSetBtn.addActionListener(this);
		
		setLayout(new FlowLayout(FlowLayout.LEFT));
		
		// Adding them to the toolbar so that they are visible
		add(startBtn);
		add(stopBtn);
		add(randomSetBtn);
		add(ascSetBtn);
		add(decSetBtn);
	}
	
	public void setButtonListener(ButtonListener listener) {
		this.buttonListener = listener;
	}
	
	// Each button will send an integer value based on their position in the toolbar
	public void actionPerformed(ActionEvent e) {
		JButton clicked = (JButton)e.getSource();
		
		if(clicked == startBtn) {
			buttonListener.buttonSend(0);
		}
		else if(clicked == stopBtn) {
			buttonListener.buttonSend(1);
		}
		else if(clicked == randomSetBtn) {
			buttonListener.buttonSend(2);
		}
		else if(clicked == ascSetBtn) {
			buttonListener.buttonSend(3);
		}
		else {
			buttonListener.buttonSend(4);
		}
	}
}
