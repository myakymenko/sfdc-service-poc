package quintiles.poc;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class LayoutHandler extends DefaultHandler {
	
	public static final String LAYOUT_ITEM = "layoutItems";
	public static final String LAYOUT_NAME = "field";
	public static final String LAYOUT_SECTION_LABEL = "label";
	public static final String LAYOUT_BEHAVIOR = "behavior";
	public static final String LAYOUT_READONLY = "Readonly";
	public static final String LAYOUT_REQUIRED = "Required";
		
	private List<FieldItem> fieldItemList = new ArrayList<>();
	
	private LayoutDescribe layoutDescribe;
	private FieldItem fieldItem = null;
	private String content = null;
	private String section = null;
	
	public LayoutDescribe getLayoutDescribe() {
		return layoutDescribe;
	}

	public void setLayoutDescribe(LayoutDescribe layoutDescribe) {
		this.layoutDescribe = layoutDescribe;
	}

	public LayoutHandler() {
		this.layoutDescribe = new LayoutDescribe();
		this.fieldItemList = layoutDescribe.getFields();
	}

	@Override
	// Triggered when the start of tag is found.
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		switch (qName) {
		// Create a new FieldItem object when the start tag is found
		case LAYOUT_ITEM:
			fieldItem = new FieldItem();
			fieldItem.setSection(section);
			break;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		switch (qName) {
		// Add the FieldItem to list once end tag is found
		case LAYOUT_ITEM:
			fieldItemList.add(fieldItem);
			break;
		// For all other end tags the FieldItem has to be updated.
		case LAYOUT_NAME:
			fieldItem.setName(content);
			break;
		case LAYOUT_BEHAVIOR:
			if (LAYOUT_REQUIRED.equals(content)) {
				fieldItem.setRequired(true);
			} else if (LAYOUT_READONLY.equals(content)) {
				fieldItem.setReadonly(true);
			}
			break;
		case LAYOUT_SECTION_LABEL:
			section = content;
			break;
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		content = String.copyValueOf(ch, start, length).trim();
	}
}
