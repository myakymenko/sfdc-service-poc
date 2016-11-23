package quintiles.poc.handler.layout;

import quintiles.poc.api.IMetadata;
import quintiles.poc.container.FieldItem;
import quintiles.poc.container.LayoutItem;
import quintiles.poc.container.LayoutMetadata;
import quintiles.poc.container.ProfileItem;
import quintiles.poc.container.SObjectItem;
import quintiles.poc.container.SectionItem;
import quintiles.poc.handler.AbstractHandler;
import quintiles.poc.util.Utils;

public class FieldAccessibilityHandler extends AbstractHandler {

	public FieldAccessibilityHandler(IMetadata metadata) {
		super(metadata);
	}

	public FieldAccessibilityHandler(AbstractHandler handler) {
		super(handler);
	}

	@Override
	protected void executeHandlerAction() throws Exception {
		LayoutMetadata layoutMetadata = (LayoutMetadata) metadata;
		
		for (LayoutItem layout : layoutMetadata.getLayouts()) {
			String sObjectName = layout.getType();
			String layoutName = layout.getName();

			SObjectItem metadataSObject = layoutMetadata.getSObjectByName(sObjectName);
			for (ProfileItem profile : layoutMetadata.getProfiles()) {

				LayoutItem profileLayout = profile.getLayoutByName(layoutName);
				SObjectItem profileSObject = profile.getSObjectByName(sObjectName);

				LayoutItem processedLayout = new LayoutItem(layoutName);
				processedLayout.setType(sObjectName);
				processedLayout.setSubtype(profileLayout.getSubtype());
				processedLayout.setProfileName(profile.getName());

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
}