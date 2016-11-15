package quintiles.poc.heroku;

public class MainPOC {
/*
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

	public static String pracessData() throws ConnectionException, FileNotFoundException, ParserConfigurationException, SAXException, IOException {
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
	
	private static ProfileDescribe getProfileDescribe(File profile, String objectName, String recordTypeName) throws ParserConfigurationException, SAXException, FileNotFoundException, IOException {
		SAXParserFactory parserFactor = SAXParserFactory.newInstance();
		SAXParser parser = parserFactor.newSAXParser();

		ProfileHandler handler = new ProfileHandler(objectName, recordTypeName);
		parser.parse(new FileInputStream(profile), handler);
		
		return handler.getProfileDescribe(); 
	}
	
	private static LayoutDescribe getLayoutDescribe(File layout) throws ParserConfigurationException, SAXException, IOException {
		SAXParserFactory parserFactor = SAXParserFactory.newInstance();
		SAXParser parser = parserFactor.newSAXParser();

		LayoutHandler handler = new LayoutHandler();
		parser.parse(new FileInputStream(layout), handler);
		
		return handler.getLayoutDescribe();
	}
	
	private static SobjectDescribe getSobjectDescribe(File sobject) throws ParserConfigurationException, SAXException, IOException {
		SAXParserFactory parserFactor = SAXParserFactory.newInstance();
		SAXParser parser = parserFactor.newSAXParser();
		
		SobjectHandler handler = new SobjectHandler();
		parser.parse(new FileInputStream(sobject), handler);
		
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
	}*/

}
