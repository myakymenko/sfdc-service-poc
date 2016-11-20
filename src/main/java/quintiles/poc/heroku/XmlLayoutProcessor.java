package quintiles.poc.heroku;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.sforce.soap.partner.PartnerConnection;

import quintiles.poc.api.IHandler;
import quintiles.poc.api.IMetadata;
import quintiles.poc.api.IRequestProcessor;
import quintiles.poc.container.LayoutItem;
import quintiles.poc.container.LayoutMetadata;
import quintiles.poc.handler.layout.FieldAccessibilityHandler;
import quintiles.poc.handler.layout.FieldOptionsProcessor;
import quintiles.poc.handler.layout.RecordTypeIdHandler;
import quintiles.poc.handler.layout.XmlLayoutHandler;
import quintiles.poc.handler.layout.XmlProfileHandler;
import quintiles.poc.handler.layout.XmlSObjectHandler;
import quintiles.poc.retriever.FilesystemInputStreamRetriever;

public class XmlLayoutProcessor implements IRequestProcessor {

	private IMetadata metadata;
	private PartnerConnection connection;

	public XmlLayoutProcessor(IMetadata metadata, PartnerConnection connection) {
		this.metadata = metadata;
		this.connection = connection;
	}

	@Override
	public String process() throws Exception {
		FilesystemInputStreamRetriever retriever = new FilesystemInputStreamRetriever();

		IHandler handler = new XmlSObjectHandler(metadata, retriever);
		handler = new FieldOptionsProcessor(handler);
		handler = new XmlProfileHandler(handler);
		handler = new XmlLayoutHandler(handler);
		handler = new FieldAccessibilityHandler(handler);
		handler = new RecordTypeIdHandler(handler, connection);
		handler.handle();

		LayoutMetadata layoutMetadata = (LayoutMetadata) metadata;

		return getJSON(layoutMetadata.getProcessedLayouts());
	}

	private String getJSON(ArrayList<LayoutItem> layouts) {
		String jsonString = "";
		if (layouts == null || layouts.isEmpty())
			return jsonString;

		if (layouts.size() > 1) {
			jsonString = new Gson().toJson(layouts);
		} else {
			jsonString = new Gson().toJson(layouts.get(0));
		}

		return jsonString;
	}
}
