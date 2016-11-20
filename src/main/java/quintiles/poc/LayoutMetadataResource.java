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
