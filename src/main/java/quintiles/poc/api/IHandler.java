package quintiles.poc.api;

public interface IHandler {
	
	public IMetadata getMetadata();
	
	public IInputStreamRetriever getRetriever();
	
	public void handle() throws Exception ;
}
