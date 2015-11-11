package panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import downloader.FileDownloading;
import downloader.FileToDownload;

public class MainActivityPanel extends JPanel implements Observer {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextArea mainArea;
	private JList<FileToDownload> listOfFilesToDownload;
	private JList<FileDownloading> listOfFilesDownloading;
	private JList<String> listOfExtensions;
	public boolean slowDown = false;
	public MainActivityPanel()
	{
		super(new GridBagLayout());
		setBackground(new Color(24,24,24));
		mainArea = new JTextArea();
		listOfFilesToDownload = new JList<FileToDownload>();
		mainView();
	}
	public void toDownloadView(int noThreads, ArrayList<FileToDownload> links)
	{
		ArrayList<FileDownloading> fileResults = new ArrayList<FileDownloading>();
		for(FileToDownload file: links)
		{
			fileResults.add(new FileDownloading(file,"PENDING", 0));
		}
		downloadingListView(toArray(fileResults));
		ExecutorService executor = Executors.newFixedThreadPool(noThreads);
		for(FileDownloading link : fileResults)
		{
			executor.execute(new Runnable()
			{
				@Override
				public void run() {
					link.setStatus("STARTED");
					try
					{
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
	public FileDownloading[] toArray(ArrayList<FileDownloading> files)
	{
		FileDownloading[] newFiles = new FileDownloading[files.size()];
		for(int i = 0 ; i < files.size() ;i++)
		{
			newFiles[i] = files.get(i);
		}
		return newFiles;
	}
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
					doc = Jsoup.connect(file.getURL()).get();
					Elements pngs = doc.select("[src$=."+ extension +"]");
					for(Element el : pngs)
					{
						fileResults.add(new FileToDownload(el.absUrl("src"),file.getPath()));
					}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		mainView();
		for(FileToDownload url : fileResults)
		{
			appendMessage(url.toString());
		}
		return fileResults;
		
	}
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
	public void appendMessage(String message)
	{
		mainArea.append(message + "\n");
	}
	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}
}
