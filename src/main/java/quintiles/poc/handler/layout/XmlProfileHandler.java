package quintiles.poc.handler.layout;

import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import quintiles.poc.api.IInputStreamRetriever;
import quintiles.poc.api.IMetadata;
import quintiles.poc.container.FieldItem;
import quintiles.poc.container.LayoutItem;
import quintiles.poc.container.LayoutMetadata;
import quintiles.poc.container.ProfileItem;
import quintiles.poc.container.SObjectItem;
import quintiles.poc.handler.AbstractHandler;
import quintiles.poc.util.Consts;

public class XmlProfileHandler extends AbstractHandler {

	public XmlProfileHandler(IMetadata metadata, IInputStreamRetriever retriever) {
		super(metadata, retriever);
	}

	public XmlProfileHandler(AbstractHandler handler) {
		super(handler);
	}

	@Override
	protected void executeHandlerAction() throws Exception {
		SAXParserFactory parserFactory = SAXParserFactory.newInstance();
		SAXParser parser = parserFactory.newSAXParser();

		LayoutMetadata layoutMetadata = (LayoutMetadata) metadata;
		
		for (String processedProfile : layoutMetadata.getAvailableProfiles()) {
			SaxProfileHandler handler = new SaxProfileHandler(layoutMetadata, processedProfile);
			
			String metadataFileName = processedProfile + Consts.METADATA_PROFILE_EXT;
			
			retriever.setSourceItem(metadataFileName);
			InputStream foundSObject = retriever.getInputStream();
			parser.parse(foundSObject, handler);
		}
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
		private String foundLayoutName = null;
		private String foundRtName = null;
		private boolean visible = true;
		private boolean readOnly = true;

		public SaxProfileHandler(LayoutMetadata layoutMetadata, String profileName) {
			this.layoutMetadata = layoutMetadata;
			this.profile = new ProfileItem(profileName);
			layoutMetadata.setProfile1(profile);
		}

		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

			switch (qName) {
			case PROF_TAG_ITEM:
				fieldItem = null;
				sObject = null;
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
			case PROF_TAG_ITEM:
				if (fieldItem != null) {
					fieldItem.setReadonly(readOnly);
					fieldItem.setVisible(visible);
				}
				break;
			case PROF_TAG_FIELD_NAME:
				sObject = content.substring(0, content.indexOf(PROF_DOT_SEPARATOR));

				if (layoutMetadata.hasProcessedObject(sObject)/* && visible */) {
					SObjectItem sObjectItem = profile.getSObjectByName(sObject);

					if (sObjectItem == null) {
						sObjectItem = new SObjectItem(sObject);
						profile.setSObject(sObjectItem);
					}

					String fieldName = content.substring(content.indexOf(PROF_DOT_SEPARATOR) + 1);

					fieldItem = sObjectItem.getFieldByName(fieldName);
					if (fieldItem == null) {
						fieldItem = new FieldItem(fieldName);
						sObjectItem.setField(fieldItem);
					}
				}
				break;
			case FIELD_VAL_EDITABLE:
				readOnly = !Boolean.parseBoolean(content);
				break;
			case FIELD_VAL_READABLE:
				visible = Boolean.parseBoolean(content);
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

					profile.setLayout(layoutItem);
				}
				break;
			}
		}

		@Override
		public void characters(char[] ch, int start, int length) throws SAXException {
			content = String.copyValueOf(ch, start, length).trim();
		}
	}
}