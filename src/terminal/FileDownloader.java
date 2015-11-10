package terminal;

import javax.swing.JFrame;
import panels.MainPanel;

public class FileDownloader 
{
	public static void main(String[] args)
	{
		CallHandler.handleArguments(args);
		JFrame terminalFrame = new JFrame("SSC Exercise 4");
		terminalFrame.setResizable(false);
		terminalFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		terminalFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		terminalFrame.setUndecorated(true);
		MainPanel mainPanel = new MainPanel();
		terminalFrame.add(mainPanel);
		terminalFrame.setVisible(true);
		LoadKeyboard.loadEscapeButton();
	}

}
