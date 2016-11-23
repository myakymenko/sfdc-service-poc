package quintiles.poc.handler.layout;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import quintiles.poc.api.IInputStreamRetriever;
import quintiles.poc.api.IMetadata;
import quintiles.poc.container.FieldItem;
import quintiles.poc.container.LayoutMetadata;
import quintiles.poc.container.OptionItem;
import quintiles.poc.container.SObjectItem;
import quintiles.poc.handler.AbstractHandler;
import quintiles.poc.util.Consts;

public class XmlSObjectHandler extends AbstractHandler {

	public XmlSObjectHandler(IMetadata metadata, IInputStreamRetriever retriever) {
		super(metadata, retriever);
	}

	public XmlSObjectHandler(AbstractHandler handler) {
		super(handler);
	}

	@Override
	protected void executeHandlerAction() throws Exception {
		SAXParserFactory parserFactory = SAXParserFactory.newInstance();
		SAXParser parser = parserFactory.newSAXParser();

		LayoutMetadata layoutMetadata = (LayoutMetadata) metadata;
		for (String sObjectName : layoutMetadata.getAvailableObjects()) {
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
		public static final String OBJ_TAG_TYPE = "type";
		public static final String OBJ_TAG_OPTIONS = "valueSet";
		public static final String OBJ_TAG_OPTION_VALUE = "value";
		public final ArrayList<String> SKIP_TAGS = new ArrayList<String>() {
			{
				add("listViews");
				add("recordTypes");
				add("businessProcesses");
				add("actionOverrides");
			}
		};

		private ArrayList<FieldItem> fieldItemList = null;
		private FieldItem fieldItem = null;
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
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			switch (qName) {
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
			case OBJ_TAG_FIELDS:
				fieldItemList.add(fieldItem);
				break;
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
			case OBJ_TAG_TYPE:
				if (!ignoreTags) {
					fieldItem.setType(content);
				}
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