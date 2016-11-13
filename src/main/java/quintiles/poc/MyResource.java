package quintiles.poc;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sforce.soap.enterprise.EnterpriseConnection;
import com.sforce.soap.enterprise.LoginResult;
import com.sforce.soap.metadata.MetadataConnection;
import com.sforce.ws.ConnectionException;

import quintiles.poc.heroku.ConnectionUtil;
import quintiles.poc.heroku.ConstsPOC;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("myresource")
public class MyResource {
	private EnterpriseConnection connection;
	private MetadataConnection metadataConnection;
	private LoginResult loginResult;
    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() {
        return "Hello, Heroku!";
    }
    
    @GET
    @Path("login")
    @Produces(MediaType.TEXT_PLAIN)
    public String setUpConnetions() throws ConnectionException {
		connection = ConnectionUtil.getSOAPConnection(ConstsPOC.URL);
		loginResult = connection.login(ConstsPOC.USERNAME, ConstsPOC.PASSWORD);
		metadataConnection = ConnectionUtil.getSOAPMetadataConnection(loginResult);
		return "===== Connections established";
	}
}
