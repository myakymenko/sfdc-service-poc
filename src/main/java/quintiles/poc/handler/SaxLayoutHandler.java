package quintiles.poc.handler;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import quintiles.poc.container.FieldItem;
import quintiles.poc.container.LayoutItem;
import quintiles.poc.container.LayoutMetadata;
import quintiles.poc.container.SObjectItem;
import quintiles.poc.container.SectionItem;

public class SaxLayoutHandler extends DefaultHandler {
	
	public static final String LAYOUT_TAG_SECTION = "layoutSections";
	public static final String LAYOUT_TAG_ITEM = "layoutItems";
	public static final String LAYOUT_TAG_NAME = "field";
	public static final String LAYOUT_TAG_SECTION_LABEL = "label";
	public static final String LAYOUT_TAG_BEHAVIOR = "behavior";
	public static final String LAYOUT_VAL_READONLY = "Readonly";
	public static final String LAYOUT_VAL_REQUIRED = "Required";
		
	private ArrayList<FieldItem> fieldItemList = new ArrayList<>();
	private LayoutMetadata layoutMetadata = null;
	
	private LayoutItem layoutItem = null;
	private SectionItem sectionItem = null;
	
	private FieldItem fieldItem = null;
	private String content = null;
	private String behavior = null;
	private String section = null;
	private String sObjectName = null;
	private SObjectItem sObjectItem = null; 
	
	
	public SaxLayoutHandler(LayoutMetadata layoutMetadata, String processedLayoutName) {
		this.layoutMetadata = layoutMetadata;
		this.layoutItem = layoutMetadata.getLayoutByName(processedLayoutName);
		this.sObjectItem = layoutMetadata.getSObjectByName(layoutItem.getType());
		//this.rtName = layoutMetadata.getRecordType();
	}
	
	/*public LayoutDescribe getLayoutDescribe() {
		return layoutDescribe;
	}

	public void setLayoutDescribe(LayoutDescribe layoutDescribe) {
		this.layoutDescribe = layoutDescribe;
	}*/

	public SaxLayoutHandler() {
		//this.layoutDescribe = new LayoutDescribe();
		//this.fieldItemList = layoutDescribe.getFields();
	}

	@Override
	// Triggered when the start of tag is found.
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		switch (qName) {
		// Create a new FieldItem object when the start tag is found
		case LAYOUT_TAG_SECTION:
			sectionItem = new SectionItem();
			section = null;
			break;
		case LAYOUT_TAG_ITEM:
			fieldItem = null;
			behavior = null;
			break;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		switch (qName) {
		// Add the FieldItem to list once end tag is found
		case LAYOUT_TAG_ITEM:
			if (fieldItem != null) {
				if (LAYOUT_VAL_REQUIRED.equals(behavior)) {
					fieldItem.setRequired(true);
				} else if (LAYOUT_VAL_READONLY.equals(behavior)) {
					fieldItem.setReadonly(true);
				}
				
				sectionItem.setField(fieldItem);
			}
			break;
		// For all other end tags the FieldItem has to be updated.
		case LAYOUT_TAG_NAME:
			fieldItem = sObjectItem.getFieldByName(content);
			break;
		case LAYOUT_TAG_BEHAVIOR:
			behavior = content;
			/*if (LAYOUT_VAL_REQUIRED.equals(content)) {
				fieldItem.setRequired(true);
			} else if (LAYOUT_VAL_READONLY.equals(content)) {
				fieldItem.setReadonly(true);
			}*/
			break;
		case LAYOUT_TAG_SECTION_LABEL:
			section = content;
			break;
		case LAYOUT_TAG_SECTION:
			sectionItem.setName(section);
			layoutItem.setSection(sectionItem);
			break;
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		content = String.copyValueOf(ch, start, length).trim();
	}
}
