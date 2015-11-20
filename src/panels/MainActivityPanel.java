package panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import downloader.FileDownloading;
import downloader.FileToDownload;
/**
 * In the main activity panel the lists are displayed.
 * Most importantly this is where the functionality is 
 * achieved.
 * This class connects all the other panels as well,
 * being the heart of the program.
 * @author Sebastian
 *
 */
public class MainActivityPanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	private JTextArea mainArea;
	private JList<FileToDownload> listOfFilesToDownload;
	private JList<FileDownloading> listOfFilesDownloading;
	private JList<String> listOfExtensions;
	public boolean slowDown = false;
	/**
	 * The constructor for the main activity panel class.
	 * It sets up a layout, creates a main area for text display
	 * in case there is a message to be displayed.
	 * It also creates a JList for the files that are going
	 * to be downloaded.
	 */
	public MainActivityPanel()
	{
		super(new GridBagLayout());
		setBackground(new Color(24,24,24));
		mainArea = new JTextArea();
		listOfFilesToDownload = new JList<FileToDownload>();
		mainView();
	}
	/**
	 * This view will create a JList that displays the progress of
	 * download.
	 * It sets up a thread pool where the download actions are
	 * executed.
	 * @param noThreads The number of threads for the thread pool.
	 * @param links The links where the files that need to be downloaded are.
	 */
	public void toDownloadView(int noThreads, ArrayList<FileToDownload> links)
	{
		//Setting everything to pending and putting them in a queue for download.
		ArrayList<FileDownloading> fileResults = new ArrayList<FileDownloading>();
		for(FileToDownload file: links)
		{
			fileResults.add(new FileDownloading(file,"PENDING", 0));
		}
		//Setting up the view with the current pending files to download.
		downloadingListView(toArray(fileResults));
		//Thread pool creation
		ExecutorService executor = Executors.newFixedThreadPool(noThreads);
		//Creating a runnable object for each of the files.
		for(FileDownloading link : fileResults)
		{
			executor.execute(new Runnable()
			{
				@Override
				public void run() {
					link.setStatus("STARTED");
					try
					{
						//Extracting the name and downloading the file.
						String src = link.getFile().getURL();
						String folderPath = link.getFile().getPath();
						int indexname = src.lastIndexOf("/");
						if (indexname == src.length()) {
							src = src.substring(1, indexname);
						}
						indexname = src.lastIndexOf("/");
						String name = src.substring(indexname, src.length());
						URL url = new URL(src);
						InputStream in = url.openStream();
						OutputStream out = new BufferedOutputStream(new FileOutputStream(folderPath + name));
						ArrayList<Integer> bytes = new ArrayList<Integer>();
						for (int b; (b = in.read()) != -1;) {
							bytes.add(b);
						}
						for(int i = 0 ; i < bytes.size() ; i++)
						{
							if(slowDown) Thread.sleep(2);
							out.write(bytes.get(i));
							link.setPercentage((i * 100) / bytes.size());
							listOfFilesDownloading.repaint();
						}
						link.setPercentage(100);
						out.close();
						in.close();
						link.setStatus("COMPLETED");
						listOfFilesDownloading.repaint();

					}
					catch(Exception e)
					{
						link.setStatus("FAILED");
						listOfFilesDownloading.repaint();
					}
				}
			});
		}
		executor.shutdown();
	}
	/**
	 * A class that converts an array list to a normal array.
	 * @param files The array list to be converted.
	 * @return The resulting array.
	 */
	public FileDownloading[] toArray(ArrayList<FileDownloading> files)
	{
		FileDownloading[] newFiles = new FileDownloading[files.size()];
		for(int i = 0 ; i < files.size() ;i++)
		{
			newFiles[i] = files.get(i);
		}
		return newFiles;
	}
	/**
	 * This class will extract the files from specific websites
	 * under some extension constraints.
	 * @param files The files or documents or links that contain files of interest
	 * @param extensions The extensions of the files of interests
	 * @return The files that match the extensions and are in the links of interest.
	 */
	public ArrayList<FileToDownload> extractFilesDisplay(FileToDownload[] files, ArrayList<String> extensions)
	{
		ArrayList<FileToDownload> fileResults = new ArrayList<FileToDownload>();
		Document doc;
		for(FileToDownload file : files)
		{
			for(String extension: extensions)
			{
				try 
				{
					//Looking inside "src" and "href" objects
					doc = Jsoup.connect(file.getURL()).get();
					Elements pngs = doc.select("[src$=."+ extension +"]");
					for(Element el : pngs)
					{
						fileResults.add(new FileToDownload(el.absUrl("src"),file.getPath()));
					}
					Elements href = doc.select("[href$=."+ extension +"]");
					for(Element el : href)
					{
						fileResults.add(new FileToDownload(el.absUrl("href"),file.getPath()));
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		mainView();
		//Displaying the results.
		for(FileToDownload url : fileResults)
		{
			appendMessage(url.toString());
		}
		return fileResults;
		
	}
	/**
	 * This view displays the queue of download.
	 * Setting up the JList, JScrollPane and constraints.
	 * @param messages The queue of download.
	 */
	public void downloadingListView(FileDownloading[] messages)
	{
		if(messages.length > 0)
		{
			removeAll();
			setLayout(new GridBagLayout());
			listOfFilesDownloading = new JList<FileDownloading>(messages);
			listOfFilesDownloading.setBackground(new Color(24,24,24));
			listOfFilesDownloading.setForeground(Color.WHITE);
			listOfFilesDownloading.setFont(new Font(Font.MONOSPACED,Font.PLAIN,15));
			JScrollPane scrollPane = new JScrollPane(listOfFilesDownloading);
	        GridBagConstraints constraints = new GridBagConstraints();
	       	constraints.gridwidth = GridBagConstraints.REMAINDER;
	       	constraints.fill = GridBagConstraints.HORIZONTAL;
	      	constraints.fill = GridBagConstraints.BOTH;
	       	constraints.weightx = 1.0;
	       	constraints.weighty = 1.0;
			add(scrollPane,constraints);
			revalidate();
	       	repaint();
		}
		else
		{
			alertMessage("<<< T h e r e   a r e   n o   f i l e s  "
							 + " t o   b e   d o w n l o a d e d >>>");
		}
	}
	/**
	 * The main view displays a JTextArea where different 
	 * messages can be displayed.
	 * This is helpful for displaying errors or other messages.
	 * It can be used as a replacement of a JList when it is
	 * not needed.
	 */
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
	/**
	 * This method displays all links that need to be download along
	 * with the extensions specified by the user.
	 * @param messages The messages that need to be displayed. (Links or files)
	 * @param extensions The extension constraints of thos links or files.
	 */
	public void filesToDonwloadView(FileToDownload[] messages,String[] extensions)
	{
		if(messages.length > 0)
		{
			removeAll();
			setLayout(new GridBagLayout());
			listOfFilesToDownload = new JList<FileToDownload>(messages);
			listOfFilesToDownload.setBackground(new Color(24,24,24));
			listOfFilesToDownload.setForeground(Color.WHITE);
			listOfFilesToDownload.setFont(new Font(Font.MONOSPACED,Font.PLAIN,15));
			JScrollPane scrollPane = new JScrollPane(listOfFilesToDownload);
	        GridBagConstraints constraints = new GridBagConstraints();
	       	constraints.gridwidth = GridBagConstraints.REMAINDER;
	       	constraints.fill = GridBagConstraints.HORIZONTAL;
	    	constraints.fill = GridBagConstraints.BOTH;
	       	constraints.weightx = 1.0;
	       	constraints.weighty = 1.0;
			listOfExtensions = new JList<String>(extensions);
			listOfExtensions.setBackground(new Color(24,24,24));
			listOfExtensions.setForeground(Color.WHITE);
			listOfExtensions.setFont(new Font(Font.MONOSPACED,Font.PLAIN,15));
			JScrollPane scrollPane2 = new JScrollPane(listOfExtensions);
	        GridBagConstraints constraints2 = new GridBagConstraints();
	       	constraints2.gridwidth = GridBagConstraints.REMAINDER;
	       	constraints2.fill = GridBagConstraints.HORIZONTAL;
	       	constraints2.fill = GridBagConstraints.BOTH;
	       	constraints2.weightx = 1.1;
	       	constraints2.weighty = 1.1;
	       	add(scrollPane,constraints);
			add(scrollPane2,constraints2);
			revalidate();
	       	repaint();
		}
		else
		{
			alertMessage("<<< T h e r e   a r e   n o   f i l e s  "
							 + " t o   b e   d i s p l a y e d >>>");
		}
	}
	/**
	 * A method that append a couple of newline a number of times.
	 * @param times The amount of newlines in the text file.
	 */
	public void initialNewLine(int times)
	{
		for(int i = 0 ; i < times ; i++)
		{
			mainArea.append("\n");
		}
	}
	/**
	 * Displaying an error. It recreates the main view and appends
	 * a message
	 * @param message The error or message to be displayed.
	 */
	public void alertMessage(String message)
	{
		mainView();
		mainArea.setText("");
		initialNewLine(2);
		mainArea.append(message);
	}
	/**
	 * This method appends a message to previous messages or errors
	 * that have been displayed. Can be used as a replacement for
	 * JList.
	 * @param message The message to be displayed.
	 */
	public void appendMessage(String message)
	{
		mainArea.append(message + "\n");
	}
}
