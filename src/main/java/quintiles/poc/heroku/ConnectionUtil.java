package quintiles.poc.heroku;

import com.sforce.soap.metadata.MetadataConnection;
import com.sforce.soap.partner.LoginResult;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

import quintiles.poc.util.Settings;

/**
 * Login utility.
 */
public class ConnectionUtil {

	public static PartnerConnection getSOAPConnection(String loginUrl) throws ConnectionException {
		Settings settings = Settings.getInstance();
		
		String userName = settings.get(Consts.ENV_SFDC_USER);
		String password =  settings.get(Consts.ENV_SFDC_PASSWORD);
		
		ConnectorConfig config = new ConnectorConfig();
		config.setAuthEndpoint(loginUrl);
		config.setUsername(userName);
		config.setPassword(password);
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