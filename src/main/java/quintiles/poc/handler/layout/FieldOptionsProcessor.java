package quintiles.poc.handler.layout;

import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import quintiles.poc.api.IHandler;
import quintiles.poc.api.IInputStreamRetriever;
import quintiles.poc.api.IMetadata;
import quintiles.poc.container.FieldItem;
import quintiles.poc.container.LayoutMetadata;
import quintiles.poc.container.OptionItem;
import quintiles.poc.container.SObjectItem;

public class FieldOptionsProcessor implements IHandler {

	private IHandler handler;
	private IMetadata metadata;
	private IInputStreamRetriever retriever;

	public FieldOptionsProcessor(IMetadata metadata) throws ParserConfigurationException, SAXException {
		this.metadata = metadata;
	}

	public FieldOptionsProcessor(IHandler handler) {
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
		LayoutMetadata layoutMetadata = (LayoutMetadata) metadata;

		for (SObjectItem sobject : layoutMetadata.getSObjects()) {
			for (FieldItem field : sobject.getFields()) {
				ArrayList<OptionItem> options = field.getOptions();
				if (options != null) {
					processOptions(options);
				}
			}
		}
	}

	// TODO Label value will be get from external system.
	private void processOptions(ArrayList<OptionItem> options) {
		for (OptionItem optionItem : options) {
			optionItem.setLabel(optionItem.getCode());
		}
	}

}
