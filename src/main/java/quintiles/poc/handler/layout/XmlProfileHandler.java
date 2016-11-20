package quintiles.poc.handler.layout;

import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import quintiles.poc.api.IHandler;
import quintiles.poc.api.IInputStreamRetriever;
import quintiles.poc.api.IMetadata;
import quintiles.poc.container.FieldItem;
import quintiles.poc.container.LayoutItem;
import quintiles.poc.container.LayoutMetadata;
import quintiles.poc.container.ProfileItem;
import quintiles.poc.container.SObjectItem;
import quintiles.poc.heroku.Consts;

public class XmlProfileHandler implements IHandler {

	private IInputStreamRetriever retriever;
	private IHandler handler;
	private IMetadata metadata;

	public XmlProfileHandler(IMetadata metadata, IInputStreamRetriever retriever) throws ParserConfigurationException, SAXException {
		this.retriever = retriever;
		this.metadata = metadata;
	}

	public XmlProfileHandler(IHandler handler) {
		this.handler = handler;
		this.metadata = handler.getMetadata();
		this.retriever = handler.getRetriever();
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
		SAXParserFactory parserFactory = SAXParserFactory.newInstance();
		SAXParser parser = parserFactory.newSAXParser();
		
		LayoutMetadata layoutMetadata = (LayoutMetadata) metadata;
		SaxProfileHandler handler = new SaxProfileHandler(layoutMetadata);

		String metadataFileName = layoutMetadata.getProfileName() + Consts.METADATA_PROFILE_EXT;

		retriever.setSourceItem(metadataFileName);
		InputStream foundSObject = retriever.getInputStream();
		parser.parse(foundSObject, handler);
		
		System.out.println("Finish profile handler");
	}

	private class SaxProfileHandler extends DefaultHandler {
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
		private ProfileItem profile = null;

		private FieldItem fieldItem = null;
		private LayoutItem layoutItem = null;
		private String content = null;
		private String sObject = null;
		private String rtName = null;
		private String foundLayoutName = null;
		private String foundRtName = null;
		private boolean visible = true;
		private boolean readOnly = true;

		public SaxProfileHandler(LayoutMetadata layoutMetadata) {
			this.layoutMetadata = layoutMetadata;
			this.rtName = layoutMetadata.getRecordType();
			this.profile = new ProfileItem(layoutMetadata.getProfileName());
			layoutMetadata.setProfile(profile);
		}

		@Override
		// Triggered when the start of tag is found.
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

			switch (qName) {
			// Create a new FieldItem object when the start tag is found
			case PROF_TAG_ITEM:
				//fieldItems = null;
				fieldItem = null;
				sObject = null;
				//shouldBeRemoved = true;
				visible = true;
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
					/*if (shouldBeRemoved) {
						// fieldItems.add(fieldItem);
						fieldItems.remove(fieldItem);
					} else {
						fieldItem.setReadonly(readOnly);
					}*/
					fieldItem.setReadonly(readOnly);
					fieldItem.setVisible(visible);
				}
				break;
			// For all other end tags the FieldItem has to be updated.
			case PROF_TAG_FIELD_NAME:
				sObject = content.substring(0, content.indexOf(PROF_DOT_SEPARATOR));

				if (layoutMetadata.hasProcessedObject(sObject)/* && visible*/) {
					SObjectItem sObjectItem = profile.getSObjectByName(sObject);
					
					if (sObjectItem == null) {
						sObjectItem = new SObjectItem(sObject);
						profile.setSObject(sObjectItem);
					}
					
					String fieldName = content.substring(content.indexOf(PROF_DOT_SEPARATOR) + 1);
					
					//fieldItems = sObjectItem.getFields();
					fieldItem = sObjectItem.getFieldByName(fieldName);
					if (fieldItem == null) {
						fieldItem = new FieldItem(fieldName);
						sObjectItem.setField(fieldItem);
					}

					//shouldBeRemoved = false;
				}
				break;
			case FIELD_VAL_EDITABLE:
				//fieldItem.setReadonly(Boolean.parseBoolean(content));
				readOnly = !Boolean.parseBoolean(content);
				break;
			case FIELD_VAL_READABLE:
				visible = Boolean.parseBoolean(content);
				/*readable = Boolean.parseBoolean(content);
				if (!readable) {
					shouldBeRemoved = true;
				}*/
				break;
			case PROF_TAG_LAYOUT_NAME:
				foundLayoutName = content;
				break;
			case PROF_TAG_RT_NAME:
				foundRtName = content;
				break;
			case PROF_TAG_ASSIGNMENT:
				sObject = foundLayoutName.substring(0, foundLayoutName.indexOf(PROF_MINUS_SEPARATOR));

				foundRtName = foundRtName != null ? foundRtName.substring(foundRtName.indexOf(PROF_DOT_SEPARATOR) + 1) : foundRtName;

				if (layoutMetadata.hasProcessedObject(sObject)) {
					layoutItem.setName(foundLayoutName);
					layoutItem.setType(sObject);
					layoutItem.setSubtype(foundRtName);

					if ((rtName != null && rtName.equals(foundRtName)) || (rtName == null)) {
						profile.setLayout(layoutItem);
					}
				}
				break;
			}
		}

		@Override
		public void characters(char[] ch, int start, int length) throws SAXException {
			content = String.copyValueOf(ch, start, length).trim();
		}
		/*public static final String PROF_TAG_ITEM = "fieldPermissions";
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
						// fieldItems.add(fieldItem);
						fieldItems.remove(fieldItem);
					} else {
						fieldItem.setReadonly(readOnly);
					}
				}
				break;
			// For all other end tags the FieldItem has to be updated.
			case PROF_TAG_FIELD_NAME:
				sObject = content.substring(0, content.indexOf(PROF_DOT_SEPARATOR));

				if (layoutMetadata.hasProcessedObject(sObject) && readable) {
					SObjectItem sObjectItem = layoutMetadata.getSObjectByName(sObject);

					String fieldName = content.substring(content.indexOf(PROF_DOT_SEPARATOR) + 1);
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

				foundRtName = foundRtName != null ? foundRtName.substring(foundRtName.indexOf(PROF_DOT_SEPARATOR) + 1) : foundRtName;

				if (layoutMetadata.hasProcessedObject(sObject)) {
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
		}*/
	}
}
