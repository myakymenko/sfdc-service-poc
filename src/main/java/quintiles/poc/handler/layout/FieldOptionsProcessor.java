package quintiles.poc.handler.layout;

import java.util.ArrayList;

import quintiles.poc.api.IInputStreamRetriever;
import quintiles.poc.api.IMetadata;
import quintiles.poc.container.FieldItem;
import quintiles.poc.container.LayoutMetadata;
import quintiles.poc.container.OptionItem;
import quintiles.poc.container.SObjectItem;
import quintiles.poc.handler.AbstractHandler;

public class FieldOptionsProcessor extends AbstractHandler {

	public FieldOptionsProcessor(IMetadata metadata, IInputStreamRetriever retriever) {
		super(metadata, retriever);
	}

	public FieldOptionsProcessor(AbstractHandler handler) {
		super(handler);
	}

	@Override
	protected void executeHandlerAction() throws Exception {
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