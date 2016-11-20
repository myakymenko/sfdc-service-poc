package quintiles.poc.handler.layout;

import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.sforce.soap.partner.PartnerConnection;
import com.sforce.soap.partner.QueryResult;
import com.sforce.soap.partner.sobject.SObject;

import quintiles.poc.api.IHandler;
import quintiles.poc.api.IInputStreamRetriever;
import quintiles.poc.api.IMetadata;
import quintiles.poc.container.LayoutItem;
import quintiles.poc.container.LayoutMetadata;
import quintiles.poc.heroku.Utils;

public class RecordTypeIdHandler implements IHandler {

	private IHandler handler;
	private IMetadata metadata;
	private IInputStreamRetriever retriever;
	private static PartnerConnection connection;

	public RecordTypeIdHandler(IMetadata metadata, PartnerConnection connection) throws ParserConfigurationException, SAXException {
		this.metadata = metadata;
		this.connection = connection;
	}

	public RecordTypeIdHandler(IHandler handler, PartnerConnection connection) {
		this.handler = handler;
		this.metadata = handler.getMetadata();
		this.retriever = handler.getRetriever();
		this.connection = connection;
	}

	@Override
	public IMetadata getMetadata() {
		return metadata;
	}

	@Override
	public IInputStreamRetriever getRetriever() {
		return retriever;
	}

	@Override
	public void handle() throws Exception {
		if (handler != null) {
			handler.handle();
		}
		LayoutMetadata layoutMetadata = (LayoutMetadata) metadata;

		ArrayList<LayoutItem> layouts = layoutMetadata.getProcessedLayouts();

		ArrayList<String> rtNames = new ArrayList<>();
		for (LayoutItem layoutItem : layouts) {
			String rtName = layoutItem.getSubtype();
			if (!Utils.isBlankString(rtName)) {
				rtNames.add(rtName);
			}
		}

		String recordTypes = String.join("','", rtNames);
		String sObjects = String.join("','", layoutMetadata.getProcessedObjects());
		String query = "SELECT Id, DeveloperName, SobjectType FROM RecordType WHERE DeveloperName IN ('" + recordTypes + "') AND SobjectType IN ('" + sObjects + "')";
		QueryResult queryResults = connection.query(query);
		
		SObject[] foundSObjects = queryResults.getRecords();

		HashMap<String, String> layoutToRt = new HashMap<>();

		for (int i = 0; i < foundSObjects.length; i++) {
			SObject recordType = foundSObjects[i];

			String key = (String) recordType.getField("SobjectType") + (String) recordType.getField("DeveloperName");
			String value = recordType.getId();
			layoutToRt.put(key, value);
		}

		for (LayoutItem layoutItem : layouts) {
			String key = layoutItem.getType() + layoutItem.getSubtype();

			layoutItem.setRecordTypeId(layoutToRt.get(key));
		}
	}
}
