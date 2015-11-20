package terminal;

import javax.swing.JFrame;
import panels.MainPanel;
/**
 * This is the main class of SSC Exercise 4
 * Its functionalities are:
 * 		- handling the arguments
 * 		- setting up the frame
 * 		- setting up the main panel
 * 		- customising the frame
 * 		- loading the keyboard listeners
 * @author Sebastian
 *
 */
public class FileDownloader 
{
	public static void main(String[] args)
	{
		//Handling arguments
		CallHandler.handleArguments(args);
		
		//Setting up and customizing frame
		JFrame terminalFrame = new JFrame("SSC Exercise 4");
		terminalFrame.setResizable(false);
		terminalFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		terminalFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		terminalFrame.setUndecorated(true);
		
		//Main panel creation and visibility
		MainPanel mainPanel = new MainPanel();
		terminalFrame.add(mainPanel);
		terminalFrame.setVisible(true);
		
		//Loading the keyboard listeners
		LoadKeyboard.loadEscapeButton();
	}

}
