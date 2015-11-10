package downloader;

public class FileDownloading {
	private FileToDownload file;
	private String status;
	public FileDownloading(FileToDownload file, String status)
	{
		this.file = file;
		this.status = status;
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
		return file.toString() + " STATUS: " + status;
	}
}
