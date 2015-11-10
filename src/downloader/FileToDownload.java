package downloader;

public class FileToDownload {
	private String url = "";
	private String filePath = "";
	public FileToDownload(String url, String filePath)
	{
		this.url = url;
		this.filePath = filePath;
	}
	public String getPath()
	{
		return filePath;
	}
	public String getURL()
	{
		return url;
	}
	public void setURL(String newURL)
	{
		this.url = newURL;
	}
	public void setPath(String newPath)
	{
		this.filePath = newPath;
	}
	public String toString()
	{
		return "From: " + url + " to: " + filePath;
	}
}
