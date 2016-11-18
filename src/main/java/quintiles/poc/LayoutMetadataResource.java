package quintiles.poc;

import java.util.ArrayList;
import java.util.HashMap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.sforce.soap.enterprise.EnterpriseConnection;
import com.sforce.soap.enterprise.LoginResult;
import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.sobject.Profile;
import com.sforce.soap.enterprise.sobject.RecordType;
import com.sforce.soap.enterprise.sobject.SObject;
import com.sforce.soap.enterprise.sobject.User;
import com.sforce.soap.metadata.MetadataConnection;
import com.sforce.ws.ConnectionException;

import quintiles.poc.container.LayoutItem;
import quintiles.poc.container.LayoutMetadata;
import quintiles.poc.heroku.ConnectionUtil;
import quintiles.poc.heroku.Consts;
import quintiles.poc.heroku.MetadataUtil;
import quintiles.poc.heroku.Utils;
import quintiles.poc.heroku.XmlSaxProcessor;

@Path("layouts")
public class LayoutMetadataResource {
	private EnterpriseConnection connection;
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
			XmlSaxProcessor processor = new XmlSaxProcessor();
			processor.processMetadata(layoutMetadata);
			processRecordTypes(layoutMetadata);
			result = getJSON(layoutMetadata.getLayouts());
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

	private String processRecordTypes(LayoutMetadata layoutResp) throws ConnectionException {
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
		SObject[] profile = queryResults.getRecords();

		HashMap<String, String> layoutToRt = new HashMap<>();

		for (int i = 0; i < profile.length; i++) {
			RecordType recordType = (RecordType) profile[i];

			String key = recordType.getSobjectType() + recordType.getDeveloperName();
			String value = recordType.getId();
			layoutToRt.put(key, value);
		}

		for (LayoutItem layoutItem : layouts) {
			String key = layoutItem.getType() + layoutItem.getSubtype();

			layoutItem.setRecordTypeId(layoutToRt.get(key));
		}

		return "";
	}

	private String getJSON(ArrayList<LayoutItem> layouts) {
		String jsonString = "";
		if (layouts == null || layouts.isEmpty())
			return jsonString;

		if (layouts.size() > 1) {
			jsonString = new Gson().toJson(layouts);
		} else {
			jsonString = new Gson().toJson(layouts.get(0));
		}

		return jsonString;
	}

	private String getUserProfile(String userId) throws ConnectionException {
		QueryResult queryResults = connection.query("SELECT Id, ProfileId, Profile.Name FROM User WHERE Id = '" + userId + "'");
		User user = (User) queryResults.getRecords()[0];
		Profile profile = user.getProfile();

		String profileName = profile.getName();

		if (Consts.MISMATCH_PROFILE_NAMES.containsKey(profileName)) {
			profileName = Consts.MISMATCH_PROFILE_NAMES.get(profileName);
		}

		return profileName;
	}
}
