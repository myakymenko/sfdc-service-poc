package quintiles.poc.heroku;

public class ConstsPOC {
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
	public static final String MANIFEST_FILE = "/classes/package.xml";
}
