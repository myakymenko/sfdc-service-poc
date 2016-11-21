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

	@GET
	@Path("sobjectLayouts")
	@Produces(MediaType.APPLICATION_JSON)
	public String getLayouts(@QueryParam("sObjectName") String sObjectName, @QueryParam("userId") String userId, @QueryParam("rt") String rt) {
		String result = "";
		
		try {
			connection = ConnectionUtil.getSOAPConnection(Consts.URL);
			LoginResult loginResult = connection.login(Consts.USERNAME, Consts.PASSWORD);//??????
			MetadataConnection metadataConnection = ConnectionUtil.getSOAPMetadataConnection(loginResult);

			MetadataUtil.retrieveMetadata(metadataConnection);
			LayoutMetadata layoutMetadata = initMetadata(sObjectName, userId, rt);
			XmlLayoutProcessor processor = new XmlLayoutProcessor(layoutMetadata, connection);
			
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
			layoutMetadata = Utils.isBlankString(sObjectName)?
					new LayoutMetadata(profileName, Consts.METADATA_CUSTOM_OBJECT_RETRIEVE):
					new LayoutMetadata(profileName, sObjectName, rt);
		} else {
			throw new Exception(Consts.MSG_URI_EXCEPTION);
		}

		return layoutMetadata;
	}

	//separate service
	private String getUserProfile(String userId) throws ConnectionException {
		String profileName = "";
		
		QueryResult queryResults = connection.query("SELECT Profile.Name profName FROM User WHERE Id = '" + userId + "' GROUP BY Profile.Name");
		SObject[] users = queryResults.getRecords();
		if (users.length != 0) {
			SObject user = users[0];
			profileName = (String) user.getField("profName");
		}

		if (Consts.MISMATCH_PROFILE_NAMES.containsKey(profileName)) {
			profileName = Consts.MISMATCH_PROFILE_NAMES.get(profileName);
		}

		return profileName;
	}
}
