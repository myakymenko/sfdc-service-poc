package quintiles.poc;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ProfileHandler extends DefaultHandler {

	public static final String PROFILE_ITEM = "fieldPermissions";
	public static final String PROFILE_ASSIGNMENT = "layoutAssignments";
	
	public static final String PROFILE_LAYOUT_NAME = "layout";
	public static final String PROFILE_RT_NAME = "recordType";
	public static final String PROFILE_FIELD_NAME = "field";
	//public static final String LAYOUT_SECTION_LABEL = "label";
	//public static final String LAYOUT_BEHAVIOR = "behavior";
	public static final String PROFILE_READABLE = "readable";
	public static final String PROFILE_EDITABLE = "editable";

	private List<FieldItem> fieldItemList = new ArrayList<>();

	private ProfileDescribe profileDescribe;
	private FieldItem fieldItem = null;
	private String content = null;
	private String sObject = null;
	private String rtName = null;
	private String foundLayoutName = null;
	private String foundRtName = null;
	private boolean shouldBeAdded = false;
	private boolean readable = true;

	public ProfileDescribe getProfileDescribe() {
		return profileDescribe;
	}

	public ProfileHandler(String sObject, String rtName) {
		this.sObject = sObject;
		this.rtName = rtName;
		this.profileDescribe = new ProfileDescribe();
		this.fieldItemList = profileDescribe.getFields();
	}

	@Override
	// Triggered when the start of tag is found.
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		switch (qName) {
		// Create a new FieldItem object when the start tag is found
		case PROFILE_ITEM:
			fieldItem = new FieldItem();
			shouldBeAdded = false;
			readable = true;
			break;
		case PROFILE_ASSIGNMENT:
			foundLayoutName = null;
			foundRtName = null;
			break;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		switch (qName) {
		// Add the FieldItem to list once end tag is found
		case PROFILE_ITEM:
			if (shouldBeAdded) {
				fieldItemList.add(fieldItem);
			}
			break;
		// For all other end tags the FieldItem has to be updated.
		case PROFILE_FIELD_NAME:
			if (content.startsWith(sObject + ".") && readable) {
				shouldBeAdded = true;
				fieldItem.setName(content.substring(sObject.length()+1));
			}
			break;
		case PROFILE_READABLE:
			
			readable = Boolean.parseBoolean(content);
			if (!readable) {
				shouldBeAdded = false;
			}
			break;
		case PROFILE_LAYOUT_NAME:
			foundLayoutName = content;
			break;
			
		case PROFILE_RT_NAME:
			foundRtName = content;
			break;
		case PROFILE_ASSIGNMENT:
			String rtObject = sObject + "." + rtName;
			if (rtObject.equals(foundRtName)) {
				profileDescribe.setLayoutName(foundLayoutName);
			}
			break;
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		content = String.copyValueOf(ch, start, length).trim();
	}
}
