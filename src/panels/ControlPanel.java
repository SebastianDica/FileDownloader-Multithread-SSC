package panels;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;






import downloader.FileDownloading;
import downloader.FileToDownload;
import terminal.CallHandler;

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
	public ControlPanel(MainActivityPanel activity, TerminalPanel terminal)
	{
		super();
		this.terminal = terminal;
		this.activity = activity;
		extensions = new ArrayList<String>();
		setBackground(Color.GRAY);
		createMainControlPanel();
	}
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
	public String[] toArray(ArrayList<String> files)
	{
		String[] newFiles = new String[files.size()];
		for(int i = 0 ; i < files.size() ;i++)
		{
			newFiles[i] = files.get(i);
		}
		return newFiles;
	}
	public void selectionView()
	{
		activity.filesToDonwloadView(filesToDownload,toArray(extensions));
		removeAll();
		JButton back = new JButton("Back");
		back.addActionListener(e ->
		{
			createMainControlPanel();
		});
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
		JButton addExtensions = new JButton("Add extension");
		addExtensions.addActionListener(e ->
		{
			String extensionString = JOptionPane.showInputDialog("Enter an extension");
			extensions.add(extensionString);
			activity.filesToDonwloadView(filesToDownload, toArray(extensions));
		});
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
