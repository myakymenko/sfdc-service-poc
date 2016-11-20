package quintiles.poc;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.sforce.soap.metadata.MetadataConnection;
import com.sforce.soap.partner.LoginResult;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.soap.partner.QueryResult;
import com.sforce.soap.partner.sobject.SObject;
import com.sforce.ws.ConnectionException;

import quintiles.poc.container.LayoutMetadata;
import quintiles.poc.heroku.ConnectionUtil;
import quintiles.poc.heroku.Consts;
import quintiles.poc.heroku.MetadataUtil;
import quintiles.poc.heroku.Utils;
import quintiles.poc.heroku.XmlLayoutProcessor;

@Path("layouts")
public class LayoutMetadataResource {
	private PartnerConnection connection;
	private MetadataConnection metadataConnection;
	private LoginResult loginResult;

	/**
	 * Method handling HTTP GET requests. The returned object will be sent to
	 * the client as "text/plain" media type.
	 *
	 * @return String that will be returned as a text/plain response.
	 */
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getIt() {
		return "Web app is alieve";
	}

	@GET
	@Path("sobjectLayouts")
	@Produces(MediaType.APPLICATION_JSON)
	public String getLayouts(@QueryParam("sObjectName") String sObjectName, @QueryParam("userId") String userId, @QueryParam("rt") String rt) {

		String result = "";
		try {
			connection = ConnectionUtil.getSOAPConnection(Consts.URL);
			loginResult = connection.login(Consts.USERNAME, Consts.PASSWORD);
			metadataConnection = ConnectionUtil.getSOAPMetadataConnection(loginResult);

			MetadataUtil.retrieveMetadata(metadataConnection);
			LayoutMetadata layoutMetadata = initMetadata(sObjectName, userId, rt);
			/*XmlSaxProcessor processor = new XmlSaxProcessor();
			processor.processMetadata(layoutMetadata);*/
			XmlLayoutProcessor processor = new XmlLayoutProcessor(layoutMetadata, connection);
			//processRecordTypes(layoutMetadata);
			
			result = processor.process();
		} catch (Exception e) {
			result = e.getCause().getMessage();
		}

		return result;
	}

	private LayoutMetadata initMetadata(String sObjectName, String userId, String rt) throws Exception {
		LayoutMetadata layoutMetadata = null;
		String profileName = getUserProfile(userId);
		if (!Utils.isBlankString(profileName)) {
			if (Utils.isBlankString(sObjectName)) {
				layoutMetadata = new LayoutMetadata(profileName, Consts.METADATA_CUSTOM_OBJECT_RETRIEVE);
			} else {
				layoutMetadata = new LayoutMetadata(profileName, sObjectName, rt);
			}
		} else {
			throw new Exception(Consts.MSG_URI_EXCEPTION);
		}

		return layoutMetadata;
	}

	/*private String processRecordTypes(LayoutMetadata layoutResp) throws ConnectionException {
		ArrayList<LayoutItem> layouts = layoutResp.getLayouts();

		ArrayList<String> rtNames = new ArrayList<>();
		for (LayoutItem layoutItem : layouts) {
			String rtName = layoutItem.getSubtype();
			if (!Utils.isBlankString(rtName)) {
				rtNames.add(rtName);
			}
		}

		String recordTypes = String.join("','", rtNames);
		String sObjects = String.join("','", layoutResp.getProcessedObjects());
		String query = "SELECT Id, DeveloperName, SobjectType FROM RecordType WHERE DeveloperName IN ('" + recordTypes + "') AND SobjectType IN ('" + sObjects + "')";
		QueryResult queryResults = connection.query(query);
		SObject[] foundSObjects = queryResults.getRecords();
		
		HashMap<String, String> layoutToRt = new HashMap<>();
		
		for (int i = 0; i < foundSObjects.length; i++) {
			SObject recordType = foundSObjects[i];
			
			String key = (String)recordType.getField("SobjectType") + (String)recordType.getField("DeveloperName");
			String value = recordType.getId();
			layoutToRt.put(key, value);
		}

		for (LayoutItem layoutItem : layouts) {
			String key = layoutItem.getType() + layoutItem.getSubtype();

			layoutItem.setRecordTypeId(layoutToRt.get(key));
		}

		return "";
	}*/

	/*private String getJSON(ArrayList<LayoutItem> layouts) {
		String jsonString = "";
		if (layouts == null || layouts.isEmpty())
			return jsonString;

		if (layouts.size() > 1) {
			jsonString = new Gson().toJson(layouts);
		} else {
			jsonString = new Gson().toJson(layouts.get(0));
		}

		return jsonString;
	}*/

	private String getUserProfile(String userId) throws ConnectionException {
		String profileName = "";
		
		QueryResult queryResults = connection.query("SELECT Id, ProfileId, Profile.Name FROM User WHERE Id = '" + userId + "'");
		SObject[] users = queryResults.getRecords();
		if (users.length != 0) {
			SObject user = users[0];
			profileName = (String) user.getField("Name");
		}

		if (Consts.MISMATCH_PROFILE_NAMES.containsKey(profileName)) {
			profileName = Consts.MISMATCH_PROFILE_NAMES.get(profileName);
		}

		return profileName;
	}
}
