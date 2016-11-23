package quintiles.poc.util;

import java.io.File;
import java.util.HashMap;

public class Consts {
	
	public static final String ENV_API_VERSION = "SFDC_SOAP_API_VERSION";
	public static final String ENV_SOAP_URL = "SFDC_SOAP_URL";
	public static final String ENV_SFDC_USER = "SFDC_USERNAME";
	public static final String ENV_SFDC_PASSWORD = "SFDC_PASSWORD";
	public static final String ENV_PROFILES = "SFDC_PROFILES";
	public static final String ENV_LAYOUTS = "SFDC_LAYOUTS";
	public static final String ENV_SOBJECTS = "SFDC_SOBJECTS";
	public static final String ENV_RETRIEVE_LAYOUT_INTERVAL = "TASK_RETRIEVE_LAYOUT_INTERVAL";
	
	public static final String ENV_VAL_SEPARATOR = ",";
	
	public static final String SOQL_RT_DEV_NAME = "DeveloperName";
	public static final String SOQL_RT_SOBJECT = "SobjectType";
	public static final String SOQL_PROFILE_NAME_ALIAS = "profileName";
	
	public static final String QUERY_PARAM_USER_ID = "userId";
	public static final String QUERY_PARAM_SOBJECT = "sObjectName";
	public static final String QUERY_PARAM_RECORD_TYPE = "rt";
	
    // one second in milliseconds
	public static final long ONE_SECOND_MS = 1000;

    // maximum number of attempts to deploy the zip file
	public static final int MAX_NUM_POLL_REQUESTS = 50;
	
	//public static final String WORKING_DIR = "/tmp/";
	public static final String WORKING_DIR = "D:\\work\\MYenv\\workingDir\\";
	
	public static final String ZIP_FILE = "metadata.zip";
	public static final String LAYUOTS_SUB_DIR = WORKING_DIR + "layoutsMetadata" + File.separator;

	public static final String METADATA_PROFILE = "Profile";
	public static final String METADATA_CUSTOM_OBJECT = "CustomObject";
	public static final String METADATA_LAYOUT = "Layout";
	
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
