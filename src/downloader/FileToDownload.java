package downloader;
/**
 * This class represents the files that have the
 * potential of being downloaded but not yet downloaded.
 * @author Sebastian
 *
 */
public class FileToDownload 
{
	private String url = "";
	private String filePath = "";
	/**
	 * The constructor of the FileToDownload class.
	 * @param url The location of the file.
	 * @param filePath The path where it is supposed to be saved.
	 */
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
