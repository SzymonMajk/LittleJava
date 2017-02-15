package pl.edu.agh.kis;

/**
 * 
 * @author Szymon Majkut
 * @version %I%, %G%
 *
 */
public class Request {

	private String method;
		
	private String urlPath;
	
	private String host;
	
	private String parameters;
	
	private String acceptCharset;
	
	public String getMethod()
	{
		return method;
	}
	
	public String getUrlPath()
	{
		return urlPath;
	}
	
	public String getHost()
	{
		return host;
	}
	
	public String getParameters()
	{
		return parameters;
	}
	
	public String getAcceptCharset()
	{
		return acceptCharset;
	}
	
	Request()
	{
		this("","","","","");
	}
	
	Request(String method, String urlPath, String host, String parameters, String charset)
	{
		this.method = method;
		this.urlPath = urlPath;
		this.host = host;
		this.parameters = parameters;
		acceptCharset = charset;
	}
	
}
