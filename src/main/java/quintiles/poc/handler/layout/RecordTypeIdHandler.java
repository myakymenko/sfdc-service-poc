package quintiles.poc.handler.layout;

import java.util.ArrayList;
import java.util.HashMap;

import com.sforce.soap.partner.PartnerConnection;
import com.sforce.soap.partner.sobject.SObject;

import quintiles.poc.api.IMetadata;
import quintiles.poc.container.LayoutItem;
import quintiles.poc.container.LayoutMetadata;
import quintiles.poc.handler.AbstractHandler;
import quintiles.poc.service.SfdcQueryService;
import quintiles.poc.util.Consts;
import quintiles.poc.util.Utils;

public class RecordTypeIdHandler extends AbstractHandler {

	private PartnerConnection connection;

	public RecordTypeIdHandler(IMetadata metadata, PartnerConnection connection) {
		super(metadata);
		this.connection = connection;
	}

	public RecordTypeIdHandler(AbstractHandler handler, PartnerConnection connection) {
		super(handler);
		this.connection = connection;
	}

	@Override
	protected void executeHandlerAction() throws Exception {
		LayoutMetadata layoutMetadata = (LayoutMetadata) metadata;

		ArrayList<LayoutItem> layouts = layoutMetadata.getProcessedLayouts();

		ArrayList<String> rtNames = new ArrayList<>();
		for (LayoutItem layoutItem : layouts) {
			String rtName = layoutItem.getSubtype();
			if (!Utils.isBlankString(rtName)) {
				rtNames.add(rtName);
			}
		}

		SfdcQueryService queryService = new SfdcQueryService(connection);
		SObject[] foundSObjects = queryService.getRecordTypes(rtNames, layoutMetadata.getAvailableObjects());

		HashMap<String, String> layoutToRt = new HashMap<>();

		for (int i = 0; i < foundSObjects.length; i++) {
			SObject recordType = foundSObjects[i];

			String key = (String) recordType.getField(Consts.SOQL_RT_SOBJECT) + (String) recordType.getField(Consts.SOQL_RT_DEV_NAME);
			String value = recordType.getId();
			layoutToRt.put(key, value);
		}

		for (LayoutItem layoutItem : layouts) {
			String key = layoutItem.getType() + layoutItem.getSubtype();

			layoutItem.setRecordTypeId(layoutToRt.get(key));
		}
	}
}
