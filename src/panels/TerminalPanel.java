package panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;
/**
 * The terminal panel is designed to display messages
 * from the background processes.
 * @author Sebastian
 *
 */
public class TerminalPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextArea mainArea;
	/**
	 * Constructor for the terminal panel.
	 * Sets up a text area on a scrollpane
	 */
	public TerminalPanel()
	{
		super(new GridBagLayout());
		setBackground(new Color(190,190,190));
		mainArea = new JTextArea();
		mainArea.setEditable(false);
       	mainArea.setBackground(new Color(190,190,190));
       	mainArea.setForeground(Color.BLACK);
       	mainArea.setFont(new Font(Font.MONOSPACED,Font.PLAIN,15));
		JScrollPane scrollPane = new JScrollPane(mainArea);
		//A couple of constraints to keep everything in place
        GridBagConstraints constraints = new GridBagConstraints();
       	constraints.gridwidth = GridBagConstraints.REMAINDER;
       	constraints.fill = GridBagConstraints.HORIZONTAL;
      	constraints.fill = GridBagConstraints.BOTH;
       	constraints.weightx = 1.0;
       	constraints.weighty = 1.0;
       	((DefaultCaret) mainArea.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
       	add(scrollPane, constraints);
	}
	/**
	 * Appending a message to the terminal
	 * @param message The message to be appended.
	 */
	public void appendMessage(String message)
	{
		mainArea.append(message + "\n");
	}
}
