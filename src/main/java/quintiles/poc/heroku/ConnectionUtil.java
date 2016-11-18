package quintiles.poc.heroku;

import com.sforce.soap.enterprise.EnterpriseConnection;
import com.sforce.soap.enterprise.LoginResult;
import com.sforce.soap.metadata.MetadataConnection;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

/**
 * Login utility.
 */
public class ConnectionUtil {

	public static EnterpriseConnection getSOAPConnection(String loginUrl) throws ConnectionException {
		ConnectorConfig config = new ConnectorConfig();
		config.setAuthEndpoint(loginUrl);
		config.setUsername(Consts.USERNAME);
		config.setPassword(Consts.PASSWORD);
		config.setServiceEndpoint(loginUrl);
		//config.setManualLogin(true);
		EnterpriseConnection connection = new EnterpriseConnection(config);
		/*ConnectorConfig config = new ConnectorConfig();
		config.setUsername(ConstsPOC.USERNAME);
		config.setPassword(ConstsPOC.PASSWORD);
		
		EnterpriseConnection connection = Connector.newConnection(config);*/
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