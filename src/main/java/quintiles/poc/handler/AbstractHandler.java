package quintiles.poc.handler;

import quintiles.poc.api.IInputStreamRetriever;
import quintiles.poc.api.IMetadata;

public abstract class AbstractHandler {

	protected AbstractHandler handler;
	protected IMetadata metadata;
	protected IInputStreamRetriever retriever;

	public AbstractHandler(IMetadata metadata) {
		this.metadata = metadata;
	}
	
	public AbstractHandler(IMetadata metadata, IInputStreamRetriever retriever) {
		this.metadata = metadata;
		this.retriever = retriever;
	}

	public AbstractHandler(AbstractHandler handler) {
		this.handler = handler;
		this.metadata = handler.getMetadata();
		this.retriever = handler.getRetriever();
	}

	public IMetadata getMetadata() {
		return metadata;
	}

	public IInputStreamRetriever getRetriever() {
		return retriever;
	}

	public void handle() throws Exception {
		if (handler != null) {
			handler.handle();
		}
		
		executeHandlerAction();
	}
	
	abstract protected void executeHandlerAction() throws Exception;
	
}
