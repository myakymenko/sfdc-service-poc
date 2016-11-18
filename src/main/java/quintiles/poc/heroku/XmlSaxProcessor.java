package quintiles.poc.heroku;

import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import com.google.gson.Gson;

import quintiles.poc.heroku.ConstsPOC;
import quintiles.poc.heroku.Utils;
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
		System.out.println("Start processing");
		processSObjectMetadata(layoutMetadata);
		System.out.println("Sobject");
		processProfileMetadata(layoutMetadata);
		System.out.println("Profile");
		processLayoutMetadata(layoutMetadata);
		System.out.println("layout");
	}
	
	private void processSObjectMetadata(LayoutMetadata layoutMetadata) throws SAXException, IOException {
		for (String sObjectName : layoutMetadata.getProcessedObjects()) {
			SaxSObjectHandler handler = new SaxSObjectHandler(layoutMetadata, sObjectName);
			
			String metadataFileName = sObjectName + ConstsPOC.METADATA_OBJECT_EXT;
			
			FileInputStream foundSObject = Utils.getProcessedFileInputStream(metadataFileName, ConstsPOC.WORKING_DIR);
			parser.parse(foundSObject, handler);
		}
	}
	
	private void processProfileMetadata(LayoutMetadata layoutMetadata) throws SAXException, IOException {
			SaxProfileHandler handler = new SaxProfileHandler(layoutMetadata);
			
			String metadataFileName = layoutMetadata.getProfileName() + ConstsPOC.METADATA_PROFILE_EXT;
			
			FileInputStream foundSObject = Utils.getProcessedFileInputStream(metadataFileName, ConstsPOC.WORKING_DIR);
			parser.parse(foundSObject, handler);
	}
	
	private void processLayoutMetadata(LayoutMetadata layoutMetadata) throws SAXException, IOException {
		for (LayoutItem layoutItem : layoutMetadata.getLayouts()) {
			String layoutName = layoutItem.getName();
			SaxLayoutHandler handler = new SaxLayoutHandler(layoutMetadata, layoutName);
			
			String metadataFileName = layoutItem.getName() + ConstsPOC.METADATA_LAYOUT_EXT;
			
			FileInputStream foundSObject = Utils.getProcessedFileInputStream(metadataFileName, ConstsPOC.WORKING_DIR);
			parser.parse(foundSObject, handler);
		}
	}
}
