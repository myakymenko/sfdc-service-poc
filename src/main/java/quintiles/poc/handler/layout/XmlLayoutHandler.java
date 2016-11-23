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
import quintiles.poc.container.SectionItem;
import quintiles.poc.handler.AbstractHandler;
import quintiles.poc.util.Consts;

public class XmlLayoutHandler extends AbstractHandler {

	public XmlLayoutHandler(IMetadata metadata, IInputStreamRetriever retriever) {
		super(metadata, retriever);
	}

	public XmlLayoutHandler(AbstractHandler handler) {
		super(handler);
	}

	@Override
	protected void executeHandlerAction() throws Exception {
		SAXParserFactory parserFactory = SAXParserFactory.newInstance();
		SAXParser parser = parserFactory.newSAXParser();

		LayoutMetadata layoutMetadata = (LayoutMetadata) metadata;
		for (String layoutName : layoutMetadata.getAvailableLayouts()) {
			SaxLayoutHandler handler = new SaxLayoutHandler(layoutMetadata, layoutName);

			String metadataFileName = layoutName + Consts.METADATA_LAYOUT_EXT;

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

		public SaxLayoutHandler(LayoutMetadata layoutMetadata, String processedLayoutName) {
			String sObject = processedLayoutName.substring(0, processedLayoutName.indexOf(PROF_MINUS_SEPARATOR));

			if (layoutMetadata.hasProcessedObject(sObject)) {
				layoutItem = new LayoutItem(processedLayoutName);
				layoutItem.setType(sObject);
			}
			layoutMetadata.setLayout(layoutItem);
		}

		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			switch (qName) {
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
			case LAYOUT_TAG_NAME:
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
	}
}