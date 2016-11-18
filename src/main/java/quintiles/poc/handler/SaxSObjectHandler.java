package quintiles.poc.handler;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import quintiles.poc.container.FieldItem;
import quintiles.poc.container.LayoutMetadata;
import quintiles.poc.container.SObjectItem;

public class SaxSObjectHandler extends DefaultHandler {
	public static final String OBJ_TAG_FIELDS = "fields";
	public static final String OBJ_TAG_FULLNAME = "fullName";
	public static final String OBJ_TAG_LABEL = "label";

	public static final ArrayList<String> SKIP_TAGS = new ArrayList<String>(){
		{
			add("valueSet");
			add("recordTypes");
			add("businessProcesses");
		}
	};
	
	private ArrayList<FieldItem> fieldItemList = new ArrayList<>();
	private FieldItem fieldItem = null;
	private String content = null;
	private boolean ignoreTags = false;

	public SaxSObjectHandler(LayoutMetadata layoutMetadata, String processedSobjectName) {
		SObjectItem sobject = new SObjectItem(processedSobjectName);
		layoutMetadata.setSObject(sobject);
		this.fieldItemList = sobject.getFields();
	}

	@Override
	// Triggered when the start of tag is found.
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		switch (qName) {
		// Create a new FieldItem object when the start tag is found
		case OBJ_TAG_FIELDS:
			fieldItem = new FieldItem();
			break;
		default:
			if (SKIP_TAGS.contains(qName))
				ignoreTags = true;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		switch (qName) {
		// Add the FieldItem to list once end tag is found
		case OBJ_TAG_FIELDS:
			
			fieldItemList.add(fieldItem);
			break;
		// For all other end tags the FieldItem has to be updated.
		case OBJ_TAG_FULLNAME:
			if (!ignoreTags)
				fieldItem.setName(content);
			break;
		case OBJ_TAG_LABEL:
			if (!ignoreTags)
				fieldItem.setLabel(content);
			break;
		default:
			if (SKIP_TAGS.contains(qName))
				ignoreTags = false;
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		content = String.copyValueOf(ch, start, length).trim();
	}
}
