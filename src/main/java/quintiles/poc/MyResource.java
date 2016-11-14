package quintiles.poc;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.sforce.soap.enterprise.EnterpriseConnection;
import com.sforce.soap.enterprise.LoginResult;
import com.sforce.soap.metadata.MetadataConnection;
import com.sforce.ws.ConnectionException;

import quintiles.poc.heroku.ConnectionUtil;
import quintiles.poc.heroku.ConstsPOC;
import quintiles.poc.heroku.MetadataUtil;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("layouts")
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
    public String setUpConnetions() {
    	String result = "It works";
    	try {
			connection = ConnectionUtil.getSOAPConnection(ConstsPOC.URL);
			loginResult = connection.login(ConstsPOC.USERNAME, ConstsPOC.PASSWORD);
			metadataConnection = ConnectionUtil.getSOAPMetadataConnection(loginResult);
			
			MetadataUtil.retrieveMetadata(metadataConnection);
    	} catch (Exception e) {
    		result = e.getCause().getMessage();
    	}
		return result;
	}
    
    @GET
    @Path("sobjectLayout")
    @Produces(MediaType.APPLICATION_JSON)
    public String getLayout(@QueryParam("sObjectName") String sObjectName,
            				@QueryParam("userId") String userId) {
    	String result =  "";
    	
    	if ("Account".equals(sObjectName)) {
	    	if ("0050Y0000010Z9mQAE".equals(userId)) {
	    		result = "[{\"sectionName\":\"Address Information\",\"fields\":[{\"readonly\":false,\"name\":\"BillingAddress\",\"section\":\"Address Information\",\"label\":null,\"required\":false},{\"readonly\":false,\"name\":\"ShippingAddress\",\"section\":\"Address Information\",\"label\":null,\"required\":false}]},{\"sectionName\":\"Account Information\",\"fields\":[{\"readonly\":false,\"name\":\"ParentId\",\"section\":\"Account Information\",\"label\":null,\"required\":false},{\"readonly\":false,\"name\":\"AccountNumber\",\"section\":\"Account Information\",\"label\":null,\"required\":false},{\"readonly\":false,\"name\":\"Site\",\"section\":\"Account Information\",\"label\":null,\"required\":false},{\"readonly\":false,\"name\":\"Type\",\"section\":\"Account Information\",\"label\":null,\"required\":false},{\"readonly\":true,\"name\":\"Industry\",\"section\":\"Account Information\",\"label\":null,\"required\":false},{\"readonly\":false,\"name\":\"AnnualRevenue\",\"section\":\"Account Information\",\"label\":null,\"required\":false},{\"readonly\":false,\"name\":\"Test_1\",\"section\":\"Account Information\",\"label\":\"Test_1__c\",\"required\":false},{\"readonly\":true,\"name\":\"TestReqdonly\",\"section\":\"Account Information\",\"label\":\"TestReqdonly__c\",\"required\":false},{\"readonly\":false,\"name\":\"TestRequired\",\"section\":\"Account Information\",\"label\":\"TestRequired__c\",\"required\":true},{\"readonly\":false,\"name\":\"Rating\",\"section\":\"Account Information\",\"label\":null,\"required\":false},{\"readonly\":false,\"name\":\"Phone\",\"section\":\"Account Information\",\"label\":null,\"required\":false},{\"readonly\":false,\"name\":\"Fax\",\"section\":\"Account Information\",\"label\":null,\"required\":false},{\"readonly\":false,\"name\":\"Website\",\"section\":\"Account Information\",\"label\":null,\"required\":false},{\"readonly\":false,\"name\":\"TickerSymbol\",\"section\":\"Account Information\",\"label\":null,\"required\":false},{\"readonly\":false,\"name\":\"Ownership\",\"section\":\"Account Information\",\"label\":null,\"required\":false},{\"readonly\":false,\"name\":\"NumberOfEmployees\",\"section\":\"Account Information\",\"label\":null,\"required\":false},{\"readonly\":false,\"name\":\"Sic\",\"section\":\"Account Information\",\"label\":null,\"required\":false}]},{\"sectionName\":\"Description Information\",\"fields\":[{\"readonly\":false,\"name\":\"Description\",\"section\":\"Description Information\",\"label\":null,\"required\":false}]}]";
	    	} else if ("0050Y0000010Z7qQAE".equals(userId)) {
	    		result = "[{\"sectionName\":\"Address Information\",\"fields\":[{\"readonly\":false,\"name\":\"BillingAddress\",\"section\":\"Address Information\",\"label\":null,\"required\":false},{\"readonly\":false,\"name\":\"ShippingAddress\",\"section\":\"Address Information\",\"label\":null,\"required\":false}]},{\"sectionName\":\"Account Information\",\"fields\":[{\"readonly\":false,\"name\":\"ParentId\",\"section\":\"Account Information\",\"label\":null,\"required\":false},{\"readonly\":false,\"name\":\"AccountNumber\",\"section\":\"Account Information\",\"label\":null,\"required\":false},{\"readonly\":false,\"name\":\"Site\",\"section\":\"Account Information\",\"label\":null,\"required\":false},{\"readonly\":false,\"name\":\"Type\",\"section\":\"Account Information\",\"label\":null,\"required\":false},{\"readonly\":true,\"name\":\"Industry\",\"section\":\"Account Information\",\"label\":null,\"required\":false},{\"readonly\":false,\"name\":\"AnnualRevenue\",\"section\":\"Account Information\",\"label\":null,\"required\":false},{\"readonly\":false,\"name\":\"Test_2\",\"section\":\"Account Information\",\"label\":\"Test_2__c\",\"required\":false},{\"readonly\":true,\"name\":\"TestReqdonly\",\"section\":\"Account Information\",\"label\":\"TestReqdonly__c\",\"required\":false},{\"readonly\":false,\"name\":\"TestRequired\",\"section\":\"Account Information\",\"label\":\"TestRequired__c\",\"required\":true},{\"readonly\":false,\"name\":\"Rating\",\"section\":\"Account Information\",\"label\":null,\"required\":false},{\"readonly\":false,\"name\":\"Phone\",\"section\":\"Account Information\",\"label\":null,\"required\":false},{\"readonly\":false,\"name\":\"Fax\",\"section\":\"Account Information\",\"label\":null,\"required\":false},{\"readonly\":false,\"name\":\"Website\",\"section\":\"Account Information\",\"label\":null,\"required\":false},{\"readonly\":false,\"name\":\"TickerSymbol\",\"section\":\"Account Information\",\"label\":null,\"required\":false},{\"readonly\":false,\"name\":\"Ownership\",\"section\":\"Account Information\",\"label\":null,\"required\":false},{\"readonly\":false,\"name\":\"NumberOfEmployees\",\"section\":\"Account Information\",\"label\":null,\"required\":false},{\"readonly\":false,\"name\":\"Sic\",\"section\":\"Account Information\",\"label\":null,\"required\":false}]},{\"sectionName\":\"Description Information\",\"fields\":[{\"readonly\":false,\"name\":\"Description\",\"section\":\"Description Information\",\"label\":null,\"required\":false}]}]";
	    	} else if ("0050Y0000010Y5BQAU".equals(userId)) {
	    		result = "[{\"sectionName\":\"Address Information\",\"fields\":[{\"readonly\":false,\"name\":\"BillingAddress\",\"section\":\"Address Information\",\"label\":null,\"required\":false},{\"readonly\":false,\"name\":\"ShippingAddress\",\"section\":\"Address Information\",\"label\":null,\"required\":false}]},{\"sectionName\":\"Account Information\",\"fields\":[{\"readonly\":false,\"name\":\"ParentId\",\"section\":\"Account Information\",\"label\":null,\"required\":false},{\"readonly\":false,\"name\":\"AccountNumber\",\"section\":\"Account Information\",\"label\":null,\"required\":false},{\"readonly\":false,\"name\":\"Site\",\"section\":\"Account Information\",\"label\":null,\"required\":false},{\"readonly\":false,\"name\":\"Type\",\"section\":\"Account Information\",\"label\":null,\"required\":false},{\"readonly\":true,\"name\":\"Industry\",\"section\":\"Account Information\",\"label\":null,\"required\":false},{\"readonly\":false,\"name\":\"AnnualRevenue\",\"section\":\"Account Information\",\"label\":null,\"required\":false},{\"readonly\":false,\"name\":\"Test_3\",\"section\":\"Account Information\",\"label\":\"Test_3__c\",\"required\":false},{\"readonly\":true,\"name\":\"TestReqdonly\",\"section\":\"Account Information\",\"label\":\"TestReqdonly__c\",\"required\":false},{\"readonly\":false,\"name\":\"TestRequired\",\"section\":\"Account Information\",\"label\":\"TestRequired__c\",\"required\":true},{\"readonly\":false,\"name\":\"Rating\",\"section\":\"Account Information\",\"label\":null,\"required\":false},{\"readonly\":false,\"name\":\"Phone\",\"section\":\"Account Information\",\"label\":null,\"required\":false},{\"readonly\":false,\"name\":\"Fax\",\"section\":\"Account Information\",\"label\":null,\"required\":false},{\"readonly\":false,\"name\":\"Website\",\"section\":\"Account Information\",\"label\":null,\"required\":false},{\"readonly\":false,\"name\":\"TickerSymbol\",\"section\":\"Account Information\",\"label\":null,\"required\":false},{\"readonly\":false,\"name\":\"Ownership\",\"section\":\"Account Information\",\"label\":null,\"required\":false},{\"readonly\":false,\"name\":\"NumberOfEmployees\",\"section\":\"Account Information\",\"label\":null,\"required\":false},{\"readonly\":false,\"name\":\"Sic\",\"section\":\"Account Information\",\"label\":null,\"required\":false}]},{\"sectionName\":\"Description Information\",\"fields\":[{\"readonly\":false,\"name\":\"Description\",\"section\":\"Description Information\",\"label\":null,\"required\":false}]}]";
	    	} else {
	    		result = "{\"error\":\"User with Id = '" + userId + "'was not found\"}";
	    	}
    	} else {
    		result = "{\"error\":\"'" + sObjectName + "' SObject was not found\"}";
    	}
		return result;
	}
    
    @GET
    @Path("sobjectLayouts")
    @Produces(MediaType.APPLICATION_JSON)
    public String getLayouts(@QueryParam("sObjectName") String sObjectName,
            				@QueryParam("userId") String userId) {
    	String result =  "";
    	
    	if ("Account".equals(sObjectName)) {
	    	if ("0050Y0000010Z9mQAE".equals(userId)) {
	    		result = "[[{\"sectionName\":\"Address Information\",\"fields\":[{\"readonly\":false,\"name\":\"BillingAddress\",\"section\":\"Address Information\",\"label\":null,\"required\":false},{\"readonly\":false,\"name\":\"ShippingAddress\",\"section\":\"Address Information\",\"label\":null,\"required\":false}]},{\"sectionName\":\"Account Information\",\"fields\":[{\"readonly\":false,\"name\":\"Test_1\",\"section\":\"Account Information\",\"label\":\"Test_1__c\",\"required\":false}]},{\"sectionName\":\"Description Information\",\"fields\":[{\"readonly\":false,\"name\":\"Description\",\"section\":\"Description Information\",\"label\":null,\"required\":false}]}],[{\"sectionName\":\"Address Information\",\"fields\":[{\"readonly\":false,\"name\":\"BillingAddress\",\"section\":\"Address Information\",\"label\":null,\"required\":false},{\"readonly\":false,\"name\":\"ShippingAddress\",\"section\":\"Address Information\",\"label\":null,\"required\":false}]},{\"sectionName\":\"Account Information\",\"fields\":[{\"readonly\":false,\"name\":\"Test_2\",\"section\":\"Account Information\",\"label\":\"Test_2__c\",\"required\":false}]},{\"sectionName\":\"Description Information\",\"fields\":[{\"readonly\":false,\"name\":\"Description\",\"section\":\"Description Information\",\"label\":null,\"required\":false}]}],[{\"sectionName\":\"Address Information\",\"fields\":[{\"readonly\":false,\"name\":\"BillingAddress\",\"section\":\"Address Information\",\"label\":null,\"required\":false},{\"readonly\":false,\"name\":\"ShippingAddress\",\"section\":\"Address Information\",\"label\":null,\"required\":false}]},{\"sectionName\":\"Account Information\",\"fields\":[{\"readonly\":false,\"name\":\"Test_3\",\"section\":\"Account Information\",\"label\":\"Test_3__c\",\"required\":false}]},{\"sectionName\":\"Description Information\",\"fields\":[{\"readonly\":false,\"name\":\"Description\",\"section\":\"Description Information\",\"label\":null,\"required\":false}]}]]";
	    	} else {
	    		result = "{\"error\":\"User with Id = '" + userId + "'was not found\"}";
	    	}
    	} else {
    		result = "{\"error\":\"'" + sObjectName + "' SObject was not found\"}";
    	}
		return result;
	}
}
