package panels;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import downloader.FileToDownload;
import terminal.CallHandler;
/**
 * The control panel represents the interaction
 * of the user with the system.
 * It puts together the buttons, text files or
 * path choosers that the user might need in order
 * to provide input to the system.
 * It has several forms with different buttons or 
 * input components that prove to be useful during
 * the interaction with the system.
 * @author Sebastian
 *
 */
public class ControlPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MainActivityPanel activity;
	private TerminalPanel terminal;
	private String overallDestionation = "";
	private FileToDownload[] filesToDownload = new FileToDownload[0];
	private ArrayList<String> extensions;
	/**
	 * The constructor of the control panel.
	 * @param activity The activity panel, where the process happens.
	 * @param terminal The terminal panel, where messages are displayed.
	 */
	public ControlPanel(MainActivityPanel activity, TerminalPanel terminal)
	{
		super();
		this.terminal = terminal;
		this.activity = activity;
		extensions = new ArrayList<String>();
		setBackground(Color.GRAY);
		createMainControlPanel();
	}
	/**
	 * The main control panel represents the first layer
	 * of interaction with the user.
	 * It contains an exit button, and a "begin button"
	 * called "Download files". This fires the process.
	 */
	public void createMainControlPanel()
	{
		terminal.appendMessage("Entering main view.");
		removeAll();
		activity.mainView();
		JButton exit = new JButton("Exit System");
		exit.addActionListener(e ->
		{
			int exitOptionPane = JOptionPane.YES_NO_OPTION;
    		int answer = JOptionPane.showConfirmDialog (null, 
    				"Would you like to close this program?",
    				"Please don't close me :'(",exitOptionPane);
    		if(answer == JOptionPane.YES_OPTION)
    			{
    				System.exit(CallHandler.EC_SUCCESS);
    			}
		});
		JButton downloader = new JButton("Download files");
		downloader.addActionListener(e ->
		{
			selectionView();
		});
		setLayout(new GridLayout(10,1,10,10));
		add(exit);
		add(downloader);
		revalidate();
		repaint();
	}
	/**
	 * This method converts an array list to a normal array.
	 * @param files The array list to be converted.
	 * @return The converted array.
	 */
	public String[] toArray(ArrayList<String> files)
	{
		String[] newFiles = new String[files.size()];
		for(int i = 0 ; i < files.size() ;i++)
		{
			newFiles[i] = files.get(i);
		}
		return newFiles;
	}
	/**
	 * The selection view is crucial for the system.
	 * It contains a back button that leads back to the
	 * main control panel where you can exit the system.
	 * It contains options for setting the destination and 
	 * entering a url. Also extensions can be inputed as well.
	 * The option of slowing down is provided in order to see
	 * the threads pool. Usually this happens to fast.
	 * 
	 * It also provides a way of setting the destination of all
	 * the files or only specific ones. User decides.
	 */
	public void selectionView()
	{
		activity.filesToDonwloadView(filesToDownload,toArray(extensions));
		removeAll();
		JButton back = new JButton("Back");
		back.addActionListener(e ->
		{
			createMainControlPanel();
		});
		//Destination setting.
		JButton setDestination = new JButton("Set destination");
		setDestination.addActionListener(e ->
		{
			 JFileChooser chooser = new JFileChooser(); 
			 chooser.setCurrentDirectory(new java.io.File("."));
			 chooser.setDialogTitle("Select a path");
			 chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			 chooser.setAcceptAllFileFilterUsed(false);
			 if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) 
			 {
				  overallDestionation = chooser.getSelectedFile().getAbsolutePath();
			 } 
		});
		setDestination.setEnabled(false);
		JCheckBox applyAll = new JCheckBox("Same destination for all");
		applyAll.setSelected(false);
		applyAll.addActionListener(e ->
		{
			setDestination.setEnabled(!setDestination.isEnabled());
		});
		applyAll.setBackground(Color.GRAY);
		//Extension setting
		JButton addExtensions = new JButton("Add extension");
		addExtensions.addActionListener(e ->
		{
			String extensionString = JOptionPane.showInputDialog("Enter an extension");
			extensions.add(extensionString);
			activity.filesToDonwloadView(filesToDownload, toArray(extensions));
		});
		//URL addition
		JButton addURL = new JButton("Add URL");
		addURL.addActionListener(e ->
		{
			if(applyAll.isSelected())
			{
				String url = JOptionPane.showInputDialog("Enter a url");
				if(overallDestionation != "")
				{
					FileToDownload newFile = new FileToDownload(url,overallDestionation);
					filesToDownload = addFile(filesToDownload,newFile);
					activity.filesToDonwloadView(filesToDownload,toArray(extensions));
				}
				else
				{
					JOptionPane.showMessageDialog(this, "Please untick the overall destination" +
				" checkbox or specify an overall path first.");
				}
				
			}
			else
			{
				String url = JOptionPane.showInputDialog("Enter a url");
				String path = "";
				JFileChooser chooser = new JFileChooser(); 
				chooser.setCurrentDirectory(new java.io.File("."));
				chooser.setDialogTitle("Select a path");
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.setAcceptAllFileFilterUsed(false);
				if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) 
				{
				  path = chooser.getSelectedFile().getAbsolutePath();
				} 		
				FileToDownload newFile = new FileToDownload(url,path);
				filesToDownload = addFile(filesToDownload,newFile);
				activity.filesToDonwloadView(filesToDownload,toArray(extensions));
			}
		});
		//Download execution.
		JButton download = new JButton("Proceed");
		download.addActionListener(e -> 
		{
			filesExtracted();
		});
		JCheckBox slowDown = new JCheckBox("Slow Down Download");
		slowDown.setSelected(false);
		slowDown.addActionListener(e ->
		{
			activity.slowDown = !activity.slowDown;
		});
		slowDown.setBackground(Color.GRAY);
		add(back);
		add(applyAll);
		add(addURL);
		add(setDestination);
		add(addExtensions);
		add(slowDown);
		add(download);
		revalidate();
		repaint();
	}
	/**
	 * The files extracted method represents the point
	 * where all the files from the given URLs and given
	 * constraints such as extensions are provided.
	 * This will interact with the main activity panel
	 * in order to display all of them.
	 */
	public void filesExtracted()
	{
		removeAll();
		ArrayList<FileToDownload> links = activity.extractFilesDisplay(filesToDownload,extensions);
		JButton back = new JButton("Back");
		back.addActionListener(e->
		{
			selectionView();
		});
		JButton download = new JButton("Download");
		download.addActionListener(e->
		{
			boolean count = false; int result = 0;
			while(count == false)
			{
				String number = JOptionPane.showInputDialog("Enter number of threads");
				try
				{
					result = Integer.parseInt(number);
					count = true;
				}
				catch(Exception ne)
				{
					JOptionPane.showMessageDialog(this.getParent(), "Only numbers/digits!");
				}
			}
			downloading(result,links);
		});
		add(back);
		add(download);
		revalidate();
		repaint();
	}
	/**
	 * Downloading view represents the moment of thread pool
	 * execution where the files are being downloaded.
	 * @param noThreads The number of threads for the pool.
	 * @param toDownload The files to be downloaded.
	 */
	public void downloading(int noThreads, ArrayList<FileToDownload> toDownload)
	{
		removeAll();
		activity.toDownloadView(noThreads,toDownload);
		JButton back = new JButton("Back/Abort");
		back.addActionListener(e ->{
			filesExtracted();
		});
		add(back);
		revalidate();
		repaint();
	}
	/**
	 * Adding a file to an array of files.
	 * @param files The array of files that needs an addition.
	 * @param file The additional file.
	 * @return The resulting array of files after the addition.
	 */
	public FileToDownload[] addFile(FileToDownload[] files, FileToDownload file)
	{
		FileToDownload[] newArray = new FileToDownload[files.length + 1];
		for(int i = 0 ; i < files.length; i++)
		{
			newArray[i] = files[i];
		}
		newArray[files.length] = file;
		return newArray;
	}
}
