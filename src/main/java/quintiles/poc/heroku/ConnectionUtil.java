package quintiles.poc.heroku;

import com.sforce.soap.metadata.MetadataConnection;
import com.sforce.soap.partner.LoginResult;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

/**
 * Login utility.
 */
public class ConnectionUtil {

	public static PartnerConnection getSOAPConnection(String loginUrl) throws ConnectionException {
		ConnectorConfig config = new ConnectorConfig();
		config.setAuthEndpoint(loginUrl);
		config.setUsername(Consts.USERNAME);
		config.setPassword(Consts.PASSWORD);
		config.setServiceEndpoint(loginUrl);
		
		PartnerConnection connection = new PartnerConnection(config);
		
		return connection;
	}
	
	public static MetadataConnection getSOAPMetadataConnection(LoginResult loginResult) throws ConnectionException {
		ConnectorConfig config = new ConnectorConfig();
		config.setServiceEndpoint(loginResult.getMetadataServerUrl());
		config.setSessionId(loginResult.getSessionId());
		
		MetadataConnection connection = new MetadataConnection(config);
		
		return connection;
	}
}