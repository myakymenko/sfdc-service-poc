package quintiles.poc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

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

public class MainPOC {

	private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

	private static EnterpriseConnection connection;
	private static MetadataConnection metadataConnection;
	private static LoginResult loginResult;

	public static void main(String[] args) {
		try {
			setUpConnetions();

			String choice = getUsersChoice();
			while (choice != null && !choice.equals("0")) {
				if (choice.equals("1")) {
					MetadataUtil.retrieveMetadata(metadataConnection);
				} else if (choice.equals("2")) {
					pracessData();
				} else {
					break;
				}
				choice = getUsersChoice();
			}

		} catch (ConnectionException e) {
			System.out.println("----- Connection Exception");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("----- General Exception");
			e.printStackTrace();
		} finally {
			try {
				connection.logout();
				System.out.println("===== Logged out");
			} catch (ConnectionException e) {
				System.out.println("----- Logout Exception");
				e.printStackTrace();
			}
		}

	}

	private static void pracessData() throws ConnectionException, FileNotFoundException, ParserConfigurationException, SAXException, IOException {
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
	}

	private static String getUserProfile(String profileId) throws ConnectionException {
		QueryResult queryResults = connection.query("SELECT Id, Name FROM Profile WHERE Id = '" + profileId + "'");

		Profile profile = (Profile) queryResults.getRecords()[0];
		return profile.getName();
	}

	private static File searchFile(String fileName, File path) {
		File result = null;
		File[] list = path.listFiles();
		if (list != null) {
			for (File file : list) {
				if (file.isDirectory()) {
					System.out.println("Found dir : " + file.getAbsolutePath());
					result = searchFile(fileName, file);
				} else {
					if (fileName.equalsIgnoreCase(file.getName())) {
						System.out.println("Comparison file : " + fileName);
						System.out.println("Found file : " + file.getName());
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
	
	private static ProfileDescribe getProfileDescribe(File profile, String objectName, String recordTypeName) throws ParserConfigurationException, SAXException, FileNotFoundException, IOException {
		SAXParserFactory parserFactor = SAXParserFactory.newInstance();
		SAXParser parser = parserFactor.newSAXParser();

		ProfileHandler handler = new ProfileHandler(objectName, recordTypeName);
		parser.parse(new FileInputStream(profile), handler);
		/*System.out.println("**** " + handler.getProfileDescribe().getLayoutName());
		for (FieldItem emp : handler.getProfileDescribe().getFields()) {
			System.out.println(emp.getLabel() + " " + emp.getName() + " " + emp.getSection());
			System.out.println("Readonly = " + emp.isReadonly() + "; Required = " + emp.isRequired());
			
			System.out.println("-----");
		}*/
		
		return handler.getProfileDescribe(); 
	}
	
	private static LayoutDescribe getLayoutDescribe(File layout) throws ParserConfigurationException, SAXException, IOException {
		SAXParserFactory parserFactor = SAXParserFactory.newInstance();
		SAXParser parser = parserFactor.newSAXParser();

		LayoutHandler handler = new LayoutHandler();
		parser.parse(new FileInputStream(layout), handler);

		// Printing the list of FieldItems obtained from XML
		/*for (FieldItem emp : handler.getLayoutDescribe().getFields()) {
			System.out.println(emp.getLabel() + " " + emp.getName() + " " + emp.getSection());
			System.out.println("Readonly = " + emp.isReadonly() + "; Required = " + emp.isRequired());
			
			System.out.println("-----");
		}*/
		
		return handler.getLayoutDescribe();
	}
	
	private static SobjectDescribe getSobjectDescribe(File sobject) throws ParserConfigurationException, SAXException, IOException {
		SAXParserFactory parserFactor = SAXParserFactory.newInstance();
		SAXParser parser = parserFactor.newSAXParser();
		
		SobjectHandler handler = new SobjectHandler();
		parser.parse(new FileInputStream(sobject), handler);
		
		// Printing the list of FieldItems obtained from XML
		/*for (FieldItem emp : handler.getSobjectDescribe().getFields()) {
			System.out.println(emp.getLabel() + " " + emp.getName() + " " + emp.getSection());
			System.out.println("Readonly = " + emp.isReadonly() + "; Required = " + emp.isRequired());
			
			System.out.println("-----");
		}*/
		
		return handler.getSobjectDescribe();
	}

	private static void setUpConnetions() throws ConnectionException {
		connection = ConnectionUtil.getSOAPConnection(ConstsPOC.URL);
		loginResult = connection.login(ConstsPOC.USERNAME, ConstsPOC.PASSWORD);
		metadataConnection = ConnectionUtil.getSOAPMetadataConnection(loginResult);
		System.out.println("===== Connections established");
	}

	private static String getUsersChoice() throws IOException {
		System.out.println(" 1: Retrieve Metadata");
		System.out.println(" 2: TODO");
		System.out.println(" 0: Exit");
		System.out.println();
		System.out.print("Enter 1 to retrieve or 0 to exit: ");
		// wait for the user input.
		String choice = reader.readLine();
		return choice != null ? choice.trim() : "";
	}

}
