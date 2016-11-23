package quintiles.poc.service;

import com.sforce.soap.partner.PartnerConnection;

import quintiles.poc.api.IMetadata;
import quintiles.poc.handler.AbstractHandler;
import quintiles.poc.handler.layout.FieldAccessibilityHandler;
import quintiles.poc.handler.layout.FieldOptionsProcessor;
import quintiles.poc.handler.layout.RecordTypeIdHandler;
import quintiles.poc.handler.layout.XmlLayoutHandler;
import quintiles.poc.handler.layout.XmlProfileHandler;
import quintiles.poc.handler.layout.XmlSObjectHandler;
import quintiles.poc.retriever.FilesystemInputStreamRetriever;

public class MetadataProcessingService {
	
	public MetadataProcessingService() {
	}

	public void processLayoutMetadata(IMetadata metadata, PartnerConnection connection) throws Exception {
		FilesystemInputStreamRetriever retriever = new FilesystemInputStreamRetriever();

		AbstractHandler handler = new XmlSObjectHandler(metadata, retriever);
		handler = new FieldOptionsProcessor(handler);
		handler = new XmlProfileHandler(handler);
		handler = new XmlLayoutHandler(handler);
		handler = new FieldAccessibilityHandler(handler);
		handler = new RecordTypeIdHandler(handler, connection);
		handler.handle();
	}
}
