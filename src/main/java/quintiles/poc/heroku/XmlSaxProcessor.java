package quintiles.poc.heroku;

import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import quintiles.poc.container.LayoutItem;
import quintiles.poc.container.LayoutMetadata;
import quintiles.poc.handler.SaxLayoutHandler;
import quintiles.poc.handler.SaxProfileHandler;
import quintiles.poc.handler.SaxSObjectHandler;

public class XmlSaxProcessor {

	SAXParser parser = null;
	
	public XmlSaxProcessor() throws ParserConfigurationException, SAXException {
		SAXParserFactory parserFactory = SAXParserFactory.newInstance();
		this.parser = parserFactory.newSAXParser();
	}
	
	public void processMetadata(LayoutMetadata layoutMetadata) throws SAXException, IOException {
		processSObjectMetadata(layoutMetadata);
		processProfileMetadata(layoutMetadata);
		processLayoutMetadata(layoutMetadata);
	}
	
	private void processSObjectMetadata(LayoutMetadata layoutMetadata) throws SAXException, IOException {
		for (String sObjectName : layoutMetadata.getProcessedObjects()) {
			SaxSObjectHandler handler = new SaxSObjectHandler(layoutMetadata, sObjectName);
			
			String metadataFileName = sObjectName + Consts.METADATA_OBJECT_EXT;
			
			FileInputStream foundSObject = Utils.getProcessedFileInputStream(metadataFileName, Consts.WORKING_DIR);
			parser.parse(foundSObject, handler);
		}
	}
	
	private void processProfileMetadata(LayoutMetadata layoutMetadata) throws SAXException, IOException {
			SaxProfileHandler handler = new SaxProfileHandler(layoutMetadata);
			
			String metadataFileName = layoutMetadata.getProfileName() + Consts.METADATA_PROFILE_EXT;
			
			FileInputStream foundSObject = Utils.getProcessedFileInputStream(metadataFileName, Consts.WORKING_DIR);
			parser.parse(foundSObject, handler);
	}
	
	private void processLayoutMetadata(LayoutMetadata layoutMetadata) throws SAXException, IOException {
		for (LayoutItem layoutItem : layoutMetadata.getLayouts()) {
			String layoutName = layoutItem.getName();
			SaxLayoutHandler handler = new SaxLayoutHandler(layoutMetadata, layoutName);
			
			String metadataFileName = layoutItem.getName() + Consts.METADATA_LAYOUT_EXT;
			
			FileInputStream foundSObject = Utils.getProcessedFileInputStream(metadataFileName, Consts.WORKING_DIR);
			parser.parse(foundSObject, handler);
		}
	}
}
