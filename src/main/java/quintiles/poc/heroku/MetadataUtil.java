package quintiles.poc.heroku;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.sforce.soap.metadata.AsyncResult;
import com.sforce.soap.metadata.MetadataConnection;
import com.sforce.soap.metadata.PackageTypeMembers;
import com.sforce.soap.metadata.RetrieveMessage;
import com.sforce.soap.metadata.RetrieveRequest;
import com.sforce.soap.metadata.RetrieveResult;
import com.sforce.soap.metadata.RetrieveStatus;

import quintiles.poc.util.Settings;

public class MetadataUtil {
	
	public static void retrieveMetadata(MetadataConnection metadataConnection) throws Exception {
        RetrieveResult result = sendMetadataRetrieveRequest(metadataConnection);
        if (result.getStatus() == RetrieveStatus.Failed) {
            throw new Exception(result.getErrorStatusCode() + " msg: " +
                    result.getErrorMessage());
        } else if (result.getStatus() == RetrieveStatus.Succeeded) {  
	        StringBuilder stringBuilder = new StringBuilder();
		        //METOD log warnings
	        	if (result.getMessages() != null) {
		            for (RetrieveMessage rm : result.getMessages()) {
		                stringBuilder.append(rm.getFileName() + " - " + rm.getProblem() + "\n");
		            }
		        }
		        if (stringBuilder.length() > 0) {
		            System.out.println("Retrieve warnings:\n" + stringBuilder);
		        }
	
	        System.out.println("Writing results to zip file");
	        
	        //TODO move to background processing and to separate method
	        File resultsFile = new File(Consts.ZIP_FILE);//??????
	        FileOutputStream os = new FileOutputStream(resultsFile);
	
	        try {
	            os.write(result.getZipFile());
	            System.out.println("Starting unzip");
	            Utils.unzipFunction(Consts.WORKING_DIR, Consts.ZIP_FILE);
	            System.out.println("Finish unzip");
	        } finally {
	            os.close();
	        }
        }
    }
	
	private static RetrieveResult sendMetadataRetrieveRequest(MetadataConnection metadataConnection) throws Exception {
		RetrieveRequest retrieveRequest = new RetrieveRequest();
        // The version in package.xml overrides the version in RetrieveRequest
		Double apiVersion = Double.valueOf(Settings.getInstance().get(Consts.ENV_API_VERSION));
        retrieveRequest.setApiVersion(apiVersion);
        setPackageRequest(retrieveRequest);
        //TODO asynch make sense only in the background processing
		AsyncResult asyncResult = metadataConnection.retrieve(retrieveRequest);
		// Wait for the retrieve to complete
		int poll = 0;
		long waitTimeMilliSecs = Consts.ONE_SECOND_MS;
		String asyncResultId = asyncResult.getId();
		RetrieveResult result = null;
		do {
			Thread.sleep(waitTimeMilliSecs);
			// Double the wait time for the next iteration
			waitTimeMilliSecs *= 2;
			if (poll++ > Consts.MAX_NUM_POLL_REQUESTS) {
				throw new Exception("Request timed out.  If this is a large set " + 
									"of metadata components, check that the time allowed " + 
									"by MAX_NUM_POLL_REQUESTS is sufficient.");
			}
			result = metadataConnection.checkRetrieveStatus(asyncResultId, true);
			System.out.println("Retrieve Status: " + result.getStatus());
		} while (!result.isDone());

		return result;
	}

	private static void setPackageRequest(RetrieveRequest request) throws Exception {
		com.sforce.soap.metadata.Package p = getRetrieveManifest();
		request.setUnpackaged(p);
	}
	
	private static com.sforce.soap.metadata.Package getRetrieveManifest() {
		com.sforce.soap.metadata.Package packageManifest = null;
		
		Settings settings = Settings.getInstance();
		
		String apiVersion =  settings.get(Consts.ENV_API_VERSION);
		String[] layouts = settings.get(Consts.ENV_LAYOUTS).split(Consts.ENV_VAL_SEPARATOR);
		String[] profiles =  settings.get(Consts.ENV_PROFILES).split(Consts.ENV_VAL_SEPARATOR);
		String[] sobjects =  settings.get(Consts.ENV_SOBJECTS).split(Consts.ENV_VAL_SEPARATOR);
		
		HashMap<String, String[]> retrieveConfig = new HashMap<String, String[]>();
		retrieveConfig.put(Consts.METADATA_LAYOUT, layouts);
		retrieveConfig.put(Consts.METADATA_PROFILE, profiles);
		retrieveConfig.put(Consts.METADATA_CUSTOM_OBJECT, sobjects);
		
		List<PackageTypeMembers> pkgMembers = new ArrayList<>();
		//jdk 8?
		for (String key : retrieveConfig.keySet()) {
			PackageTypeMembers packageTypes = new PackageTypeMembers();
			packageTypes.setName(key);
			packageTypes.setMembers(retrieveConfig.get(key));
			
			pkgMembers.add(packageTypes);
		}
		
		packageManifest = new com.sforce.soap.metadata.Package();
		PackageTypeMembers[] packageTypesArray = new PackageTypeMembers[pkgMembers.size()];
		packageManifest.setTypes(pkgMembers.toArray(packageTypesArray));
		packageManifest.setVersion(apiVersion);
		
		return packageManifest;
	}
}
