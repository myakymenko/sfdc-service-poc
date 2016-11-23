package quintiles.poc.service;

import java.util.ArrayList;

import com.sforce.soap.partner.PartnerConnection;
import com.sforce.soap.partner.QueryResult;
import com.sforce.soap.partner.sobject.SObject;
import com.sforce.ws.ConnectionException;

import quintiles.poc.util.Consts;

public class SfdcQueryService {
	
	private PartnerConnection connection;

	public SfdcQueryService(PartnerConnection connection) {
		this.connection = connection;
	}

	public String getUserProfile(String userId) throws ConnectionException {
		String profileName = "";
		
		QueryResult queryResults = connection.query("SELECT Profile.Name " + Consts.SOQL_PROFILE_NAME_ALIAS + " FROM User WHERE Id = '" + userId + "' GROUP BY Profile.Name");
		SObject[] users = queryResults.getRecords();
		if (users.length != 0) {
			SObject user = users[0];
			profileName = (String) user.getField(Consts.SOQL_PROFILE_NAME_ALIAS);
		}

		if (Consts.MISMATCH_PROFILE_NAMES.containsKey(profileName)) {
			profileName = Consts.MISMATCH_PROFILE_NAMES.get(profileName);
		}

		return profileName;
	}
	
	public SObject[] getRecordTypes(ArrayList<String> rtNames, ArrayList<String> sObjects) throws ConnectionException {
		
		String recordTypes = String.join("','", rtNames);
		String sObjectTypes = String.join("','", sObjects);
		
		String query = "SELECT Id, " + Consts.SOQL_RT_DEV_NAME + ", " + Consts.SOQL_RT_SOBJECT + " FROM RecordType WHERE DeveloperName IN ('" + recordTypes + "') AND SobjectType IN ('" + sObjectTypes + "')";
		QueryResult queryResults = connection.query(query);
		
		SObject[] foundSObjects = queryResults.getRecords();

		return foundSObjects;
	}
	
}
