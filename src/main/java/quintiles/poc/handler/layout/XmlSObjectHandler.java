package quintiles.poc.handler.layout;

import java.io.InputStream;
import java.util.ArrayList;

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
import quintiles.poc.container.LayoutMetadata;
import quintiles.poc.container.OptionItem;
import quintiles.poc.container.SObjectItem;
import quintiles.poc.heroku.Consts;

public class XmlSObjectHandler implements IHandler {
	
	private IInputStreamRetriever retriever;
	private IHandler handler;
	private IMetadata metadata;

	public XmlSObjectHandler(IMetadata metadata, IInputStreamRetriever retriever) throws ParserConfigurationException, SAXException {
		this.retriever = retriever;
		this.metadata = metadata;
	}
	
	public XmlSObjectHandler(IHandler handler) {
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
		for (String sObjectName : layoutMetadata.getProcessedObjects()) {
			SaxSObjectHandler handler = new SaxSObjectHandler(layoutMetadata, sObjectName);
			
			String metadataFileName = sObjectName + Consts.METADATA_OBJECT_EXT;
			
			retriever.setSourceItem(metadataFileName);
			InputStream foundSObject = retriever.getInputStream();
			parser.parse(foundSObject, handler);
		}
	}
	
	private class SaxSObjectHandler extends DefaultHandler {
		public static final String OBJ_TAG_FIELDS = "fields";
		public static final String OBJ_TAG_FULLNAME = "fullName";
		public static final String OBJ_TAG_LABEL = "label";
		public static final String OBJ_TAG_REFERENCE = "referenceTo";
		public static final String OBJ_TAG_OPTIONS = "valueSet";
		public static final String OBJ_TAG_OPTION_VALUE = "value";

		public final ArrayList<String> SKIP_TAGS = new ArrayList<String>(){
			{
				add("listViews");
				add("recordTypes");
				add("businessProcesses");
			}
		};
		
		private ArrayList<FieldItem> fieldItemList = null;
		private FieldItem fieldItem = null;
		private ArrayList<OptionItem> optionItems = null;
		private OptionItem optionItem = null;
		private String content = null;
		private boolean ignoreTags = false;
		private boolean isOptionsSet = false;

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
			case OBJ_TAG_OPTIONS:
				if (fieldItem != null) {
					isOptionsSet = true;
					
					
				}
				break;
			case OBJ_TAG_OPTION_VALUE:
				optionItem = new OptionItem();
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
				if (!ignoreTags) {
					if (isOptionsSet) {
						optionItem.setCode(content);
					} else {
						fieldItem.setName(content);
					}
				}
				break;
			case OBJ_TAG_LABEL:
				if (!ignoreTags)
					fieldItem.setLabel(content);
				break;
			case OBJ_TAG_REFERENCE:
				if (!ignoreTags)
					fieldItem.setRelatedObject(content);
				break;
			case OBJ_TAG_OPTION_VALUE:
				if (isOptionsSet) {
					fieldItem.setOption(optionItem);
				}
				break;
			case OBJ_TAG_OPTIONS:
				isOptionsSet = false;
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
	
}
