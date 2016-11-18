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

public class SaxProfileHandler extends DefaultHandler {
	public static final String PROF_TAG_ITEM = "fieldPermissions";
	public static final String PROF_TAG_ASSIGNMENT = "layoutAssignments";
	
	public static final String PROF_TAG_LAYOUT_NAME = "layout";
	public static final String PROF_TAG_RT_NAME = "recordType";
	public static final String PROF_TAG_FIELD_NAME = "field";
	public static final String FIELD_VAL_READABLE = "readable";
	public static final String FIELD_VAL_EDITABLE = "editable";
	
	public static final String PROF_DOT_SEPARATOR = ".";
	public static final String PROF_MINUS_SEPARATOR = "-";
	
	private LayoutMetadata layoutMetadata = null;
	private List<FieldItem> fieldItems = new ArrayList<>();

	private FieldItem fieldItem = null;
	private LayoutItem layoutItem = null;
	private String content = null;
	private String sObject = null;
	private String rtName = null;
	private String foundLayoutName = null;
	private String foundRtName = null;
	private boolean shouldBeRemoved = false;
	private boolean readable = true;
	private boolean readOnly = true;

	public SaxProfileHandler(LayoutMetadata layoutMetadata) {
		this.layoutMetadata = layoutMetadata;
		this.rtName = layoutMetadata.getRecordType();
	}
	
	@Override
	// Triggered when the start of tag is found.
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		switch (qName) {
		// Create a new FieldItem object when the start tag is found
		case PROF_TAG_ITEM:
			fieldItems = null; 
			fieldItem = null;
			sObject = null;
			shouldBeRemoved = true;
			readable = true;
			readOnly = false;
			break;
		case PROF_TAG_ASSIGNMENT:
			sObject = null;
			layoutItem = new LayoutItem();
			foundLayoutName = null;
			foundRtName = null;
			break;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		switch (qName) {
		// Add the FieldItem to list once end tag is found
		case PROF_TAG_ITEM:
			if (fieldItem != null) {
				if (shouldBeRemoved) {
					//fieldItems.add(fieldItem);
					fieldItems.remove(fieldItem);
				} else {
					fieldItem.setReadonly(readOnly);
				}
			}
			break;
		// For all other end tags the FieldItem has to be updated.
		case PROF_TAG_FIELD_NAME:
			/*if (content.startsWith(sObject + ".") && readable) {
				shouldBeAdded = true;
				fieldItem.setName(content.substring(sObject.length()+1));
			}*/
			sObject = content.substring(0, content.indexOf(PROF_DOT_SEPARATOR));
			
			if (layoutMetadata.hasProcessedObject(sObject) && readable) {
				SObjectItem sObjectItem = layoutMetadata.getSObjectByName(sObject);
				
				String fieldName = content.substring(content.indexOf(PROF_DOT_SEPARATOR)+1);
				fieldItems = sObjectItem.getFields();
				fieldItem = sObjectItem.getFieldByName(fieldName);
				
				shouldBeRemoved = false;
			}
			
			break;
		case FIELD_VAL_EDITABLE:
			readOnly = !Boolean.parseBoolean(content);
			break;
		case FIELD_VAL_READABLE:
			readable = Boolean.parseBoolean(content);
			if (!readable) {
				shouldBeRemoved = true;
			}
			break;
		case PROF_TAG_LAYOUT_NAME:
			foundLayoutName = content;
			break;
		case PROF_TAG_RT_NAME:
			foundRtName = content;
			break;
		case PROF_TAG_ASSIGNMENT:
			sObject = foundLayoutName.substring(0, foundLayoutName.indexOf(PROF_MINUS_SEPARATOR));
			
			foundRtName = foundRtName != null ? foundRtName.substring(foundRtName.indexOf(PROF_DOT_SEPARATOR)+1) : foundRtName;
			//String layoutName = foundLayoutName.substring(foundLayoutName.indexOf(PROF_MINUS_SEPARATOR)+1);
			
			if (layoutMetadata.hasProcessedObject(sObject)) {
				//layoutItem.setName(layoutName);
				layoutItem.setName(foundLayoutName);
				layoutItem.setType(sObject);
				layoutItem.setSubtype(foundRtName);
				
				if ((rtName != null && rtName.equals(foundRtName)) || (rtName == null)) {
					layoutMetadata.setLayout(layoutItem);
				} 				
			}
			break;
		}
	}
	
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		content = String.copyValueOf(ch, start, length).trim();
	}
}
