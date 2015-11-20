package downloader;
/**
 * This is a class that represents a file that is currently
 * being downloaded. It provides an interface for progress view 
 * and status.
 * @author Sebastian
 *
 */
public class FileDownloading {
	private FileToDownload file;
	private String status;
	private int percentage;
	/**
	 * The constructor for the FileDownloading class.
	 * @param file The file that is being downloaded.
	 * @param status The status of the download action.
	 * @param percentage The progress for a scale from 0 - 100.
	 */
	public FileDownloading(FileToDownload file, String status, int percentage)
	{
		this.file = file;
		this.status = status;
		this.percentage = percentage;
	}
	public void setPercentage(int newPercentage)
	{
		this.percentage = newPercentage;
	}
	public void setStatus(String newStatus)
	{
		this.status = newStatus;
	}
	public FileToDownload getFile()
	{
		return file;
	}
	public String toString()
	{
		if(status!="COMPLETED")
		{
			if(percentage < 10)
			return percentage +"  % Done -  " + status + "   -\n " +getName(file.getURL()) + " to: " +file.getPath();
			if(percentage >= 10 && percentage <100)
			return percentage +" % Done -  " + status + "   -\n " +getName(file.getURL()) + " to: " +file.getPath();
			else
			return percentage +"% Done -  " + status + "   -\n " +getName(file.getURL()) + " to: " +file.getPath();
		}
		else
		{
			if(percentage < 10)
			return percentage +"  % Done - " + status + "  -\n " +getName(file.getURL()) + " to: " +file.getPath();
			if(percentage >= 10 && percentage <100)
			return percentage +" % Done - " + status + "  -\n " +getName(file.getURL()) + " to: " +file.getPath();
			else
			return percentage +"% Done - " + status + "  -\n " +getName(file.getURL()) + " to: " +file.getPath();
		}
	}
	public String getName(String src)
	{
		int indexname = src.lastIndexOf("/");
		if (indexname == src.length()) {
			src = src.substring(1, indexname);
		}
		indexname = src.lastIndexOf("/");
		String name = src.substring(indexname, src.length());
		return name;
	}
	//PENDING
	//STARTED
	//COMPTLETED
}
