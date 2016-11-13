package quintiles.poc.heroku;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SobjectHandler extends DefaultHandler {
	
	public static final String OBJ_VALUESET = "valueSet";
	
	private SobjectDescribe sobjectDescribe;
	private ArrayList<FieldItem> fieldItemList = new ArrayList<>();
	private FieldItem fieldItem = null;
	private String content = null;
	private boolean ignoreTags = false;

	public SobjectDescribe getSobjectDescribe() {
		return sobjectDescribe;
	}
	
	public SobjectHandler() {
		this.sobjectDescribe = new SobjectDescribe();
		this.fieldItemList = sobjectDescribe.getFields();
	}

	@Override
	// Triggered when the start of tag is found.
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		switch (qName) {
		// Create a new FieldItem object when the start tag is found
		case "fields":
			fieldItem = new FieldItem();
			ignoreTags = false;
			break;
		case OBJ_VALUESET:
			ignoreTags = true;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		switch (qName) {
		// Add the FieldItem to list once end tag is found
		case "fields":
			fieldItemList.add(fieldItem);
			System.out.println(fieldItem.getLabel() + " " + fieldItem.getName());
			//System.out.println("Readonly = " + fieldItem.isReadonly() + "; Required = " + fieldItem.isRequired());
			break;
		// For all other end tags the FieldItem has to be updated.
		case "fullName":
			if (!ignoreTags)
				fieldItem.setName(content);
			break;
		case "label":
			fieldItem.setLabel(content);
			break;
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		content = String.copyValueOf(ch, start, length).trim();
	}
}
