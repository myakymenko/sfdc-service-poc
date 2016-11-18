package quintiles.poc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import com.google.gson.Gson;
import com.sforce.soap.enterprise.EnterpriseConnection;
import com.sforce.soap.enterprise.LoginResult;
import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.sobject.Profile;
import com.sforce.soap.enterprise.sobject.User;
import com.sforce.soap.metadata.MetadataConnection;
import com.sforce.ws.ConnectionException;

import quintiles.poc.container.LayoutMetadata;
import quintiles.poc.heroku.ConnectionUtil;
import quintiles.poc.heroku.ConstsPOC;
import quintiles.poc.heroku.FieldItem;
import quintiles.poc.heroku.Layout;
import quintiles.poc.heroku.LayoutDescribe;
import quintiles.poc.heroku.LayoutHandler;
import quintiles.poc.heroku.MetadataUtil;
import quintiles.poc.heroku.ProfileDescribe;
import quintiles.poc.heroku.ProfileHandler;
import quintiles.poc.heroku.SobjectDescribe;
import quintiles.poc.heroku.SobjectHandler;
import quintiles.poc.heroku.Utils;
import quintiles.poc.heroku.XmlSaxProcessor;

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
        return "Web app is alieve";
    }
    
    /*@GET
    @Path("login")
    @Produces(MediaType.TEXT_PLAIN)
    public String setUpConnetions() {
    	String result = "It works";
    	try {
			connection = ConnectionUtil.getSOAPConnection(ConstsPOC.URL);
			loginResult = connection.login(ConstsPOC.USERNAME, ConstsPOC.PASSWORD);
			metadataConnection = ConnectionUtil.getSOAPMetadataConnection(loginResult);
			
			MetadataUtil.retrieveMetadata(metadataConnection);

			result = pracessData();
    	} catch (Exception e) {
    		result = e.getCause().getMessage();
    	}
		return result;
	}*/
    
    /*@GET
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
	}*/
	@GET
    @Path("sobjectLayoutsIMP")
    @Produces(MediaType.APPLICATION_JSON)
    public String getLayoutsIMP(@QueryParam("sObjectName") String sObjectName,
            				@QueryParam("userId") String userId,
            				@QueryParam("rt") String rt) {
    	
    	String result = "It works";
    	try {
			connection = ConnectionUtil.getSOAPConnection(ConstsPOC.URL);
			loginResult = connection.login(ConstsPOC.USERNAME, ConstsPOC.PASSWORD);
			metadataConnection = ConnectionUtil.getSOAPMetadataConnection(loginResult);
			
			MetadataUtil.retrieveMetadata(metadataConnection);
			
			LayoutMetadata layoutMetadata = initMetadata(sObjectName, userId, rt);
			XmlSaxProcessor processor = new XmlSaxProcessor();
			processor.processMetadata(layoutMetadata);
			
			result = getJSON(layoutMetadata.getLayouts());
			//result = pracessData2(sObjectName, userId, rt);
    	} catch (Exception e) {
    		result = e.getCause().getMessage();
    	}
		
		return result;
	}
	
	private LayoutMetadata initMetadata(String sObjectName,String userId,String rt) throws Exception {
		LayoutMetadata layoutMetadata = null;
		String profileName = getUserProfile2(userId);
		if (!Utils.isBlankString(profileName)) {
			if (Utils.isBlankString(sObjectName)) {
				layoutMetadata = new LayoutMetadata(profileName, ConstsPOC.METADATA_CUSTOM_OBJECT_RETRIEVE);
			} else {
				layoutMetadata = new LayoutMetadata(profileName, sObjectName, rt);
			} 
		} else {
			throw new Exception(ConstsPOC.MSG_URI_EXCEPTION);
		}
		
		return layoutMetadata;
	}
	
	private String getJSON(List<Object> objects) {
		String jsonString = "";
		if (objects == null || objects.isEmpty())
			return jsonString;
		
		if (objects.size() > 1) {
			jsonString = new Gson().toJson(objects);
		} else {
			jsonString = new Gson().toJson(objects.get(0));
		}
		
		return jsonString;
	}
	
	private String getJSON(Object object) {
		String jsonString = "";
		if (object != null) {
			jsonString = getJSON(Arrays.asList(object));
		}
		return jsonString;
	}
	
	
    
    @GET
    @Path("sobjectLayouts")
    @Produces(MediaType.APPLICATION_JSON)
    public String getLayouts(@QueryParam("sObjectName") String sObjectName,
            				@QueryParam("userId") String userId,
            				@QueryParam("rt") String rt) {
    	
    	String result = "It works";
    	try {
			connection = ConnectionUtil.getSOAPConnection(ConstsPOC.URL);
			loginResult = connection.login(ConstsPOC.USERNAME, ConstsPOC.PASSWORD);
			metadataConnection = ConnectionUtil.getSOAPMetadataConnection(loginResult);
			
			MetadataUtil.retrieveMetadata(metadataConnection);

			result = pracessData2(sObjectName, userId, rt);
    	} catch (Exception e) {
    		result = e.getCause().getMessage();
    	}
		
		return result;
	}
    
    /*@GET
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
	}*/
    
    //---------------
    
    private String pracessData2(String objectName, String userId, String rt) throws ConnectionException, FileNotFoundException, ParserConfigurationException, SAXException, IOException {
		ProfileDescribe profileDescribe = null;
		ArrayList<LayoutDescribe> layoutDescribes = null;
		SobjectDescribe sobjectDescribe = null;
		/*
		GetUserInfoResult userInfo = connection.getUserInfo();
		String profileId = userInfo.getProfileId();*/

		String profileName = getUserProfile2(userId);
		if ("System Administrator".equals(profileName)) {
			profileName =  "Admin";
		}
		
		System.out.println("===== Found Profile : " + profileName);

		File foundProfile = searchFile(profileName + ".profile", new File(ConstsPOC.WORKING_DIR));
		System.out.println("===== Profile for search " + profileName + ".profile");
		System.out.println("===== Found Profile file : " + foundProfile);
		
		profileDescribe = getProfileDescribe(foundProfile, objectName, rt);
		
		ArrayList<String> profileLayouts = profileDescribe.getLayouts();
		
		
		if (!profileLayouts.isEmpty()) {
			layoutDescribes = getLayoutDescribe2(profileLayouts);
			
			File foundSobject = searchFile(objectName + ".object", new File(ConstsPOC.WORKING_DIR));
			System.out.println("===== Found Sobject file : " + foundSobject);
			
			sobjectDescribe = getSobjectDescribe(foundSobject);
		}
		ArrayList<Layout> result = new ArrayList<>();
		for (LayoutDescribe layoutDescribe : layoutDescribes) {
			HashMap<String, ArrayList<FieldItem>> layoutContent = new HashMap<String, ArrayList<FieldItem>>();
			ArrayList<FieldItem> layoutItems = layoutDescribe.getFields();
			String layoutName = layoutDescribe.getLayoutName();
			Layout layout = new Layout();
			layout.setLayoutName(layoutName);
			
			for (FieldItem fieldItem : layoutItems) {
				if (profileDescribe.containsField(fieldItem)) {
					//System.out.println(fieldItem.getName());
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
			
			layout.setFieldSections(layoutContent);
			result.add(layout);
		}
		
		String jsonString = new Gson().toJson(result);
		
		return jsonString;
	}
    
    private String getUserProfile2(String userId) throws ConnectionException {
		QueryResult queryResults = connection.query("SELECT Id, ProfileId, Profile.Name FROM User WHERE Id = '" + userId + "'");
		User user = (User) queryResults.getRecords()[0];
		Profile profile = user.getProfile();
		return profile.getName();
	}
    
    private ArrayList<LayoutDescribe> getLayoutDescribe2(ArrayList<String> layouts) throws ParserConfigurationException, SAXException, IOException {
		ArrayList<LayoutDescribe> result = new ArrayList<>();
		
		SAXParserFactory parserFactor = SAXParserFactory.newInstance();
		SAXParser parser = parserFactor.newSAXParser();

		LayoutHandler handler = new LayoutHandler();
		
		for (String layout : layouts) {
			File foundLayout = searchFile(layout + ".layout", new File(ConstsPOC.WORKING_DIR));
			parser.parse(new FileInputStream(foundLayout), handler);
			
			LayoutDescribe layoutDescribe = handler.getLayoutDescribe();
			layoutDescribe.setLayoutName(layout);
			result.add(layoutDescribe);
			
		}
		return result;
	}
    
	/*private String getUserProfile(String profileId) throws ConnectionException {
		QueryResult queryResults = connection.query("SELECT Id, Name FROM Profile WHERE Id = '" + profileId + "'");

		Profile profile = (Profile) queryResults.getRecords()[0];
		return profile.getName();
	}*/

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
	
	/*private LayoutDescribe getLayoutDescribe(File layout) throws ParserConfigurationException, SAXException, IOException {
		SAXParserFactory parserFactor = SAXParserFactory.newInstance();
		SAXParser parser = parserFactor.newSAXParser();

		LayoutHandler handler = new LayoutHandler();
		parser.parse(new FileInputStream(layout), handler);
		
		return handler.getLayoutDescribe();
	}*/
	
	private SobjectDescribe getSobjectDescribe(File sobject) throws ParserConfigurationException, SAXException, IOException {
		SAXParserFactory parserFactor = SAXParserFactory.newInstance();
		SAXParser parser = parserFactor.newSAXParser();
		
		SobjectHandler handler = new SobjectHandler();
		parser.parse(new FileInputStream(sobject), handler);
		
		return handler.getSobjectDescribe();
	}
}
