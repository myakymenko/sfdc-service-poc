package quintiles.poc.service;

import com.sforce.soap.metadata.MetadataConnection;
import com.sforce.soap.partner.LoginResult;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.ws.ConnectionException;
import com.sforce.ws.ConnectorConfig;

public class ConnectionService {
	
	public PartnerConnection getSOAPConnection(String loginUrl, String userName, String password) throws ConnectionException {
		ConnectorConfig config = new ConnectorConfig();
		config.setAuthEndpoint(loginUrl);
		config.setUsername(userName);
		config.setPassword(password);
		config.setServiceEndpoint(loginUrl);
		
		PartnerConnection connection = new PartnerConnection(config);
		
		return connection;
	}
	
	public MetadataConnection getSOAPMetadataConnection(LoginResult loginResult) throws ConnectionException {
		ConnectorConfig config = new ConnectorConfig();
		config.setServiceEndpoint(loginResult.getMetadataServerUrl());
		config.setSessionId(loginResult.getSessionId());
		
		MetadataConnection connection = new MetadataConnection(config);
		
		return connection;
	}
}
