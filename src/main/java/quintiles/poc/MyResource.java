package quintiles.poc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.json.JSONObject;
import org.xml.sax.SAXException;

import com.sforce.soap.enterprise.EnterpriseConnection;
import com.sforce.soap.enterprise.GetUserInfoResult;
import com.sforce.soap.enterprise.LoginResult;
import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.sobject.Profile;
import com.sforce.soap.metadata.MetadataConnection;
import com.sforce.ws.ConnectionException;

import quintiles.poc.heroku.ConnectionUtil;
import quintiles.poc.heroku.ConstsPOC;
import quintiles.poc.heroku.FieldItem;
import quintiles.poc.heroku.LayoutDescribe;
import quintiles.poc.heroku.LayoutHandler;
import quintiles.poc.heroku.MainPOC;
import quintiles.poc.heroku.MetadataUtil;
import quintiles.poc.heroku.ProfileDescribe;
import quintiles.poc.heroku.ProfileHandler;
import quintiles.poc.heroku.SobjectDescribe;
import quintiles.poc.heroku.SobjectHandler;

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
			System.out.println("-------------");
			System.out.println("-------------");
			System.out.println("-------------");
			System.out.println("-------------");
			System.out.println("-------------");
			result = pracessData();
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
    
    @GET
    @Path("sobjectRTLayout")
    @Produces(MediaType.APPLICATION_JSON)
    public String getLayoutByRecordType(@QueryParam("sObjectName") String sObjectName,
            				@QueryParam("userId") String userId,
            				@QueryParam("rt") String rt) {
    	String result =  "result";
    	if ("Account".equals(sObjectName)) {
	    	if ("0050Y0000010Z9mQAE".equals(userId)) {
	    		result = "[{\"sectionName\":\"Address Information\",\"fields\":[{\"readonly\":false,\"name\":\"BillingAddress\",\"section\":\"Address Information\",\"label\":null,\"required\":false},{\"readonly\":false,\"name\":\"ShippingAddress\",\"section\":\"Address Information\",\"label\":null,\"required\":false}]},{\"sectionName\":\"Account Information\",\"fields\":[{\"readonly\":false,\"name\":\"ParentId\",\"section\":\"Account Information\",\"label\":null,\"required\":false},{\"readonly\":false,\"name\":\"AccountNumber\",\"section\":\"Account Information\",\"label\":null,\"required\":false},{\"readonly\":false,\"name\":\"Site\",\"section\":\"Account Information\",\"label\":null,\"required\":false},{\"readonly\":false,\"name\":\"Type\",\"section\":\"Account Information\",\"label\":null,\"required\":false},{\"readonly\":true,\"name\":\"Industry\",\"section\":\"Account Information\",\"label\":null,\"required\":false},{\"readonly\":false,\"name\":\"AnnualRevenue\",\"section\":\"Account Information\",\"label\":null,\"required\":false},{\"readonly\":false,\"name\":\"Test_1\",\"section\":\"Account Information\",\"label\":\"Test_1__c\",\"required\":false},{\"readonly\":true,\"name\":\"TestReqdonly\",\"section\":\"Account Information\",\"label\":\"TestReqdonly__c\",\"required\":false},{\"readonly\":false,\"name\":\"TestRequired\",\"section\":\"Account Information\",\"label\":\"TestRequired__c\",\"required\":true},{\"readonly\":false,\"name\":\"Rating\",\"section\":\"Account Information\",\"label\":null,\"required\":false},{\"readonly\":false,\"name\":\"Phone\",\"section\":\"Account Information\",\"label\":null,\"required\":false},{\"readonly\":false,\"name\":\"Fax\",\"section\":\"Account Information\",\"label\":null,\"required\":false},{\"readonly\":false,\"name\":\"Website\",\"section\":\"Account Information\",\"label\":null,\"required\":false},{\"readonly\":false,\"name\":\"TickerSymbol\",\"section\":\"Account Information\",\"label\":null,\"required\":false},{\"readonly\":false,\"name\":\"Ownership\",\"section\":\"Account Information\",\"label\":null,\"required\":false},{\"readonly\":false,\"name\":\"NumberOfEmployees\",\"section\":\"Account Information\",\"label\":null,\"required\":false},{\"readonly\":false,\"name\":\"Sic\",\"section\":\"Account Information\",\"label\":null,\"required\":false}]},{\"sectionName\":\"Description Information\",\"fields\":[{\"readonly\":false,\"name\":\"Description\",\"section\":\"Description Information\",\"label\":null,\"required\":false}]}]";
	    	} else {
	    		result = "{\"error\":\"User with Id = '" + userId + "'was not found\"}";
	    	}
    	} else {
    		result = "{\"error\":\"'" + sObjectName + "' SObject was not found\"}";
    	}
		return result;
	}
    
    //---------------
    
    public String pracessData() throws ConnectionException, FileNotFoundException, ParserConfigurationException, SAXException, IOException {
		ProfileDescribe profileDescribe = null;
		LayoutDescribe layoutDescribe = null;
		SobjectDescribe sobjectDescribe = null;
		
		GetUserInfoResult userInfo = connection.getUserInfo();
		String profileId = userInfo.getProfileId();

		String profileName = getUserProfile(profileId);
		System.out.println("===== Found Profile : " + profileName);

		File foundProfile = searchFile(profileName + ".profile", new File(ConstsPOC.WORKING_DIR));
		System.out.println("===== Profile for search " + profileName + ".profile");
		System.out.println("===== Found Profile file : " + foundProfile);
		
		profileDescribe = getProfileDescribe(foundProfile, "Account", "TestAcc1");
		
		String layoutName = profileDescribe.getLayoutName();
		if (layoutName != null) {
			File foundLayout = searchFile(layoutName + ".layout", new File(ConstsPOC.WORKING_DIR));
			System.out.println("===== Layout for search " + layoutName + ".layout");
			System.out.println("===== Found Layout file : " + foundLayout);
			
			layoutDescribe = getLayoutDescribe(foundLayout);
			
			File foundSobject = searchFile("Account" + ".object", new File(ConstsPOC.WORKING_DIR));
			System.out.println("===== Found Sobject file : " + foundSobject);
			
			sobjectDescribe = getSobjectDescribe(foundSobject);
		}
		
		HashMap<String, ArrayList<FieldItem>> layoutContent = new HashMap<String, ArrayList<FieldItem>>();
		ArrayList<FieldItem> layoutItems = layoutDescribe.getFields();
		for (FieldItem fieldItem : layoutItems) {
			if (profileDescribe.containsField(fieldItem)) {
				System.out.println(fieldItem.getName());
				FieldItem objectFieldItem = sobjectDescribe.getField(fieldItem);
				fieldItem.setLabel(objectFieldItem.getLabel());
				String section = fieldItem.getSection();
				ArrayList<FieldItem> sectionFields = layoutContent.get(section);
				
				if (sectionFields == null) {
					sectionFields = new ArrayList<FieldItem>();
				}
				
				sectionFields.add(fieldItem);
				
				layoutContent.put(section, sectionFields);
			}
		}
		JSONObject jsonObject = new JSONObject(layoutContent);
		String myJson  = jsonObject.toString();
		System.out.println(myJson);
		
		return myJson;
	}

	private String getUserProfile(String profileId) throws ConnectionException {
		QueryResult queryResults = connection.query("SELECT Id, Name FROM Profile WHERE Id = '" + profileId + "'");

		Profile profile = (Profile) queryResults.getRecords()[0];
		return profile.getName();
	}

	private File searchFile(String fileName, File path) {
		File result = null;
		File[] list = path.listFiles();
		if (list != null) {
			for (File file : list) {
				if (file.isDirectory()) {
					result = searchFile(fileName, file);
				} else {
					if (fileName.equalsIgnoreCase(file.getName())) {
						result = file;
					}
				}
				if (result != null) {
					return result;
				}
			}
		}
		return result;
	}
	
	private ProfileDescribe getProfileDescribe(File profile, String objectName, String recordTypeName) throws ParserConfigurationException, SAXException, FileNotFoundException, IOException {
		SAXParserFactory parserFactor = SAXParserFactory.newInstance();
		SAXParser parser = parserFactor.newSAXParser();

		ProfileHandler handler = new ProfileHandler(objectName, recordTypeName);
		parser.parse(new FileInputStream(profile), handler);
		
		return handler.getProfileDescribe(); 
	}
	
	private LayoutDescribe getLayoutDescribe(File layout) throws ParserConfigurationException, SAXException, IOException {
		SAXParserFactory parserFactor = SAXParserFactory.newInstance();
		SAXParser parser = parserFactor.newSAXParser();

		LayoutHandler handler = new LayoutHandler();
		parser.parse(new FileInputStream(layout), handler);
		
		return handler.getLayoutDescribe();
	}
	
	private SobjectDescribe getSobjectDescribe(File sobject) throws ParserConfigurationException, SAXException, IOException {
		SAXParserFactory parserFactor = SAXParserFactory.newInstance();
		SAXParser parser = parserFactor.newSAXParser();
		
		SobjectHandler handler = new SobjectHandler();
		parser.parse(new FileInputStream(sobject), handler);
		
		return handler.getSobjectDescribe();
	}
}
