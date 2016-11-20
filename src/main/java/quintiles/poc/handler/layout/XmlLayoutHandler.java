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
import quintiles.poc.container.SectionItem;
import quintiles.poc.heroku.Consts;

public class XmlLayoutHandler implements IHandler {

	private IInputStreamRetriever retriever;
	private IHandler handler;
	private IMetadata metadata;

	public XmlLayoutHandler(IMetadata metadata, IInputStreamRetriever retriever) throws ParserConfigurationException, SAXException {
		this.retriever = retriever;
		this.metadata = metadata;
	}
	
	public XmlLayoutHandler(IHandler handler) {
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
		for (LayoutItem layoutItem : layoutMetadata.getProfile().getLayouts()) {
			String layoutName = layoutItem.getName();
			SaxLayoutHandler handler = new SaxLayoutHandler(layoutMetadata, layoutName);
			
			String metadataFileName = layoutItem.getName() + Consts.METADATA_LAYOUT_EXT;
			
			retriever.setSourceItem(metadataFileName);
			InputStream foundSObject = retriever.getInputStream();
			parser.parse(foundSObject, handler);
		}
		
	}
	
	private class SaxLayoutHandler extends DefaultHandler {
		public static final String LAYOUT_TAG_SECTION = "layoutSections";
		public static final String LAYOUT_TAG_ITEM = "layoutItems";
		public static final String LAYOUT_TAG_NAME = "field";
		public static final String LAYOUT_TAG_SECTION_LABEL = "label";
		public static final String LAYOUT_TAG_BEHAVIOR = "behavior";
		public static final String LAYOUT_VAL_READONLY = "Readonly";
		public static final String LAYOUT_VAL_REQUIRED = "Required";
		
		public static final String PROF_MINUS_SEPARATOR = "-";
			
		private LayoutItem layoutItem = null;
		private SectionItem sectionItem = null;
		
		private FieldItem fieldItem = null;
		private String content = null;
		private String behavior = null;
		private String section = null;
		//private SObjectItem sObjectItem = null; 
		
		
		public SaxLayoutHandler(LayoutMetadata layoutMetadata, String processedLayoutName) {
			/*ProfileItem profile = layoutMetadata.getProfile();
			this.layoutItem = profile.getLayoutByName(processedLayoutName);
			layoutMetadata.setLayout(layoutItem);
			this.sObjectItem = profile.getSObjectByName(layoutItem.getType());*/
			String sObject = processedLayoutName.substring(0, processedLayoutName.indexOf(PROF_MINUS_SEPARATOR));

			if (layoutMetadata.hasProcessedObject(sObject)) {
				layoutItem = new LayoutItem(processedLayoutName);
				layoutItem.setType(sObject);
			}
			layoutMetadata.setLayout(layoutItem);
			
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
				//fieldItem = sObjectItem.getFieldByName(content);
				fieldItem = new FieldItem(content);
				break;
			case LAYOUT_TAG_BEHAVIOR:
				behavior = content;
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
		/*public static final String LAYOUT_TAG_SECTION = "layoutSections";
		public static final String LAYOUT_TAG_ITEM = "layoutItems";
		public static final String LAYOUT_TAG_NAME = "field";
		public static final String LAYOUT_TAG_SECTION_LABEL = "label";
		public static final String LAYOUT_TAG_BEHAVIOR = "behavior";
		public static final String LAYOUT_VAL_READONLY = "Readonly";
		public static final String LAYOUT_VAL_REQUIRED = "Required";
			
		private LayoutItem layoutItem = null;
		private SectionItem sectionItem = null;
		
		private FieldItem fieldItem = null;
		private String content = null;
		private String behavior = null;
		private String section = null;
		private SObjectItem sObjectItem = null; 
		
		
		public SaxLayoutHandler(LayoutMetadata layoutMetadata, String processedLayoutName) {
			this.layoutItem = layoutMetadata.getLayoutByName(processedLayoutName);
			this.sObjectItem = layoutMetadata.getSObjectByName(layoutItem.getType());
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
		}*/
	}
}
