package quintiles.poc.heroku;

import java.util.HashMap;

public class Consts {
	//public static final String
	public static final double API_VERSION = 38.0;
	public static final String URL = "https://login.salesforce.com/services/Soap/c/" + API_VERSION;
	//static final String URL = "https://eu5.salesforce.com/services/Soap/c/38.0";
	/*public static final String USERNAME = "maxim.yakimenko@gmail.com.ceo";
	public static final String PASSWORD = "max701198838hKFvJJPtHG8n3fnko8ymkKZ";*/
	public static final String USERNAME = "myakymenko@ims.poc";
	public static final String PASSWORD = "max7011988oFZPKABDlsT01u8s1rWoCA0K";
	
    // one second in milliseconds
	public static final long ONE_SECOND_MS = 1000;

    // maximum number of attempts to deploy the zip file
	public static final int MAX_NUM_POLL_REQUESTS = 50;
	
	public static final String WORKING_DIR = "/tmp/";
	
	public static final String ZIP_FILE = WORKING_DIR + "components.zip";

    // manifest file that controls which components get retrieved
	//public static final String MANIFEST_FILE = WORKING_DIR + "package.xml";
	//public static final String MANIFEST_FILE = "/classes/package.xml";
	
	public static final String METADATA_PROFILE = "Profile";
	public static final String METADATA_CUSTOM_OBJECT = "CustomObject";
	public static final String METADATA_LAYOUT = "Layout";
	
	public static final String[] METADATA_PROFILE_RETRIEVE = new String[]{"*"};
	public static final String[] METADATA_LAYOUT_RETRIEVE = new String[]{"*"};
	public static final String[] METADATA_CUSTOM_OBJECT_RETRIEVE = new String[] { "Account", "Contact" , "Case" , "Event" , "Task"};
	
	public static final String METADATA_PROFILE_EXT = ".profile";
	public static final String METADATA_OBJECT_EXT = ".object";
	public static final String METADATA_LAYOUT_EXT = ".layout";
	
	public static final HashMap<String, String> MISMATCH_PROFILE_NAMES = new HashMap<String, String>(){
		{
			put("System Administrator", "Admin");
		}
	};
	
	public static final String MSG_URI_EXCEPTION = "Profile was not found. userId parameter is missed or incorrect.";
}
