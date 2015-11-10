package panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class MainActivityPanel extends JPanel implements Observer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextArea mainArea;
	public MainActivityPanel()
	{
		super(new GridBagLayout());
		setBackground(new Color(24,24,24));
		mainArea = new JTextArea();
		mainView();
	}
	public void mainView()
	{
		removeAll();
		setLayout(new GridBagLayout());
		mainArea.setEditable(false);
       	mainArea.setBackground(new Color(24,24,24));
       	mainArea.setForeground(Color.WHITE);
       	mainArea.setFont(new Font(Font.MONOSPACED,Font.PLAIN,15));
		JScrollPane scrollPane = new JScrollPane(mainArea);
        GridBagConstraints constraints = new GridBagConstraints();
       	constraints.gridwidth = GridBagConstraints.REMAINDER;
       	constraints.fill = GridBagConstraints.HORIZONTAL;
      	constraints.fill = GridBagConstraints.BOTH;
       	constraints.weightx = 1.0;
       	constraints.weighty = 1.0;
       	add(scrollPane, constraints);
       	mainArea.setText("");
       	revalidate();
       	repaint();
	}
	public void initialNewLine(int times)
	{
		for(int i = 0 ; i < times ; i++)
		{
			mainArea.append("\n");
		}
	}
	public void alertMessage(String message)
	{
		mainView();
		mainArea.setText("");
		initialNewLine(2);
		mainArea.append(message);
	}
	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}
}
