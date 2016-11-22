package quintiles.poc.handler.layout;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import quintiles.poc.api.IHandler;
import quintiles.poc.api.IInputStreamRetriever;
import quintiles.poc.api.IMetadata;
import quintiles.poc.container.FieldItem;
import quintiles.poc.container.LayoutItem;
import quintiles.poc.container.LayoutMetadata;
import quintiles.poc.container.ProfileItem;
import quintiles.poc.container.SObjectItem;
import quintiles.poc.container.SectionItem;
import quintiles.poc.heroku.Utils;

public class FieldAccessibilityHandler implements IHandler {

	private IHandler handler;
	private IMetadata metadata;
	private IInputStreamRetriever retriever;

	public FieldAccessibilityHandler(IMetadata metadata) throws ParserConfigurationException, SAXException {
		this.metadata = metadata;
	}

	public FieldAccessibilityHandler(IHandler handler) {
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
		ProfileItem profile = layoutMetadata.getProfile();

		for (LayoutItem layout : layoutMetadata.getLayouts()) {
			String sObjectName = layout.getType();
			String layoutName = layout.getName();

			LayoutItem profileLayout = profile.getLayoutByName(layoutName);
			SObjectItem metadataSObject = layoutMetadata.getSObjectByName(sObjectName);
			SObjectItem profileSObject = profile.getSObjectByName(sObjectName);

			LayoutItem processedLayout = new LayoutItem(layoutName);
			processedLayout.setType(sObjectName);
			processedLayout.setSubtype(profileLayout.getSubtype());

			for (SectionItem sectionItem : layout.getSections()) {
				SectionItem processedSectionItem = new SectionItem(sectionItem.getName());
				for (FieldItem sectionField : sectionItem.getFields()) {
					String sectionFieldName = sectionField.getName();
					FieldItem profileField = profileSObject.getFieldByName(sectionFieldName);
					FieldItem metadataField = metadataSObject.getFieldByName(sectionFieldName);

					if ((profileField != null && profileField.isVisible()) || profileField == null) {
						FieldItem processedFieldItem = new FieldItem(sectionFieldName);
						processedFieldItem.setRequired(sectionField.isRequired());
						processedFieldItem.setRelatedObject(sectionField.getRelatedObject());

						if (metadataField != null) {
							String labelValue = Utils.isBlankString(metadataField.getLabel()) ? metadataField.getName() : metadataField.getLabel();
							
							processedFieldItem.setLabel(labelValue);
							processedFieldItem.setOptions(metadataField.getOptions());
							processedFieldItem.setRelatedObject(metadataField.getRelatedObject());
							processedFieldItem.setType(metadataField.getType());
						}

						boolean readOnly = profileField != null && profileField.isReadonly() ? profileField.isReadonly() : sectionField.isReadonly();

						processedFieldItem.setReadonly(readOnly);

						processedSectionItem.setField(processedFieldItem);
					}
				}
				processedLayout.setSection(processedSectionItem);
			}
			layoutMetadata.setProcessedLayout(processedLayout);
		}
	}
}
