package panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JPanel;
/**
 * The main panel is displayed on the frame
 * It is composed of 4 other panels that interact
 * with each other in order to present the 
 * functionality of the program.
 * It implements a Border Layout following
 * the NORTH,WEST,EAST,SOUTH sections of the screen.
 * @author Sebastian
 *
 */
public class MainPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	private ControlPanel control;
	private TerminalPanel terminal;
	private MainActivityPanel mainActivity;
	private TopPanel top;
	/**
	 * Constructor of the main panel.
	 * Creates and adds all the other sub panels
	 * that are necessary for the program.
	 */
	public MainPanel()
	{
		super();
		//Creation of the panels.
		terminal = new TerminalPanel();
		mainActivity = new MainActivityPanel();
		control = new ControlPanel(mainActivity,terminal);
		top = new TopPanel();
		//Customisation
		top.setPreferredSize(new Dimension(700,100));
		control.setPreferredSize(new Dimension(250,600));
		terminal.setPreferredSize(new Dimension(700,150));
		mainActivity.setPreferredSize(new Dimension(450,325));
		//Layout
		setLayout(new BorderLayout(10,10));
		add(top,BorderLayout.NORTH);
		add(control,BorderLayout.EAST);	
		add(terminal,BorderLayout.SOUTH);
		add(mainActivity,BorderLayout.CENTER);
		setBackground(Color.BLACK);
	}
}
