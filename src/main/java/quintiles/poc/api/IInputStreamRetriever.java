package quintiles.poc.api;

import java.io.InputStream;

public interface IInputStreamRetriever {
	
	public void setSourceItem(String sourceItem);
	
	public InputStream getInputStream() throws Exception ;
}
