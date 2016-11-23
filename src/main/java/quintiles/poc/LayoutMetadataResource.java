package quintiles.poc;

import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.ws.ConnectionException;

import quintiles.poc.container.LayoutItem;
import quintiles.poc.container.LayoutMetadata;
import quintiles.poc.schedule.LayoutMetadataTask;
import quintiles.poc.service.ConnectionService;
import quintiles.poc.service.SfdcQueryService;
import quintiles.poc.util.Consts;
import quintiles.poc.util.Settings;
import quintiles.poc.util.Utils;

@Path("layouts")
public class LayoutMetadataResource {
	
	private Logger log = LoggerFactory.getLogger(LayoutMetadataResource.class);
	
	@GET
	@Path("sobjectLayouts")
	@Produces(MediaType.APPLICATION_JSON)
	public String getLayouts(@QueryParam(Consts.QUERY_PARAM_SOBJECT) String sObjectName, @QueryParam(Consts.QUERY_PARAM_USER_ID) String userId, @QueryParam(Consts.QUERY_PARAM_RECORD_TYPE) String rt) {
		String result = "";
		PartnerConnection connection = null;
		
		try {
			validateRequiredParam(userId);
			
			connection = setUpConection();
			
			SfdcQueryService queryService = new SfdcQueryService(connection);
			String userProfile = queryService.getUserProfile(userId);
			
			validateRequiredParam(userProfile);
			
			LayoutMetadata layoutMetadata = LayoutMetadataTask.getLayoutMetadata();
			
			ArrayList<LayoutItem> processedLayouts = layoutMetadata.getProcessedLayouts();
			
			ArrayList<LayoutItem> resultList = new ArrayList<>();
			
			for (LayoutItem layoutItem : processedLayouts) {
				String profile = layoutItem.getProfileName();
				String sObject = layoutItem.getType();
				String recordType = layoutItem.getSubtype();
				
				if ((!Utils.isBlankString(userProfile) && !Utils.isBlankString(sObjectName) && !Utils.isBlankString(rt)) &&
						(userProfile.equals(profile) && sObjectName.equals(sObject) && rt.equals(recordType))) {
					resultList.add(layoutItem);
					break;
				} else if ((!Utils.isBlankString(userProfile) && !Utils.isBlankString(sObjectName) && Utils.isBlankString(rt)) &&
						(userProfile.equals(profile) && sObjectName.equals(sObject))) {
					resultList.add(layoutItem);
				} else if ((!Utils.isBlankString(userProfile) && Utils.isBlankString(sObjectName) && Utils.isBlankString(rt)) &&
						(userProfile.equals(profile))) {
					resultList.add(layoutItem);
				}
			}
			
			result = getJSON(resultList);
		} catch (Exception e) {
			log.error(e.getMessage());
			result = e.getCause().getMessage();
		} finally {
			try {
				if (connection != null) {
					connection.logout();
					log.info("Connection logout");
				}
			} catch (ConnectionException e) {
				log.error(e.getMessage());
			}
		}

		return result;
	}
	
	private void validateRequiredParam(String param) throws Exception {
		if (Utils.isBlankString(param)) {
			throw new Exception(Consts.MSG_URI_EXCEPTION);
		}
	}
	
	private PartnerConnection setUpConection() throws ConnectionException {
		Settings settings = Settings.getInstance();

		String userName = settings.get(Consts.ENV_SFDC_USER);
		String password = settings.get(Consts.ENV_SFDC_PASSWORD);
		String soapUrl = settings.get(Consts.ENV_SOAP_URL);
		ConnectionService connectionService = new ConnectionService();

		PartnerConnection connection = connectionService.getSOAPConnection(soapUrl, userName, password);
		
		return connection;
	}

	private String getJSON(ArrayList<LayoutItem> layouts) {
		String jsonString = "";
		if (layouts == null || layouts.isEmpty())
			return jsonString;
		
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		
		if (layouts.size() > 1) {
			jsonString = gson.toJson(layouts);
		} else {
			jsonString = gson.toJson(layouts.get(0));
		}

		return jsonString;
	}
}
