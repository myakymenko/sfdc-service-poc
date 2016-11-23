package quintiles.poc.service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sforce.soap.metadata.AsyncResult;
import com.sforce.soap.metadata.MetadataConnection;
import com.sforce.soap.metadata.PackageTypeMembers;
import com.sforce.soap.metadata.RetrieveMessage;
import com.sforce.soap.metadata.RetrieveRequest;
import com.sforce.soap.metadata.RetrieveResult;
import com.sforce.soap.metadata.RetrieveStatus;

import quintiles.poc.util.Consts;
import quintiles.poc.util.Settings;
import quintiles.poc.util.Utils;

public class MetadataRetrieveService {

	private Logger log = LoggerFactory.getLogger(MetadataRetrieveService.class);

	MetadataConnection metadataConnection;

	public MetadataRetrieveService(MetadataConnection metadataConnection) {
		this.metadataConnection = metadataConnection;
	}

	public void retrieveLayoutRelatedMetadata() throws Exception {
		com.sforce.soap.metadata.Package layoutManifest = getLayoutRetrieveManifest();

		RetrieveResult result = sendMetadataRetrieveRequest(metadataConnection, layoutManifest);
		processResultMessages(result);

		if (result.getStatus() == RetrieveStatus.Succeeded) {
			processRetrievedMetadata(Consts.LAYUOTS_SUB_DIR, result.getZipFile());
		}
	}

	private com.sforce.soap.metadata.Package getLayoutRetrieveManifest() {
		Settings settings = Settings.getInstance();
		String[] layouts = settings.get(Consts.ENV_LAYOUTS).split(Consts.ENV_VAL_SEPARATOR);
		String[] profiles = settings.get(Consts.ENV_PROFILES).split(Consts.ENV_VAL_SEPARATOR);
		String[] sobjects = settings.get(Consts.ENV_SOBJECTS).split(Consts.ENV_VAL_SEPARATOR);

		HashMap<String, String[]> retrieveConfig = new HashMap<String, String[]>();
		retrieveConfig.put(Consts.METADATA_LAYOUT, layouts);
		retrieveConfig.put(Consts.METADATA_PROFILE, profiles);
		retrieveConfig.put(Consts.METADATA_CUSTOM_OBJECT, sobjects);

		return getRetrieveManifest(retrieveConfig);
	}

	private com.sforce.soap.metadata.Package getRetrieveManifest(HashMap<String, String[]> retrieveConfig) {
		List<PackageTypeMembers> pkgMembers = new ArrayList<>();
		// jdk 8?
		
		/*retrieveConfig.forEach((key, value)->{
			PackageTypeMembers packageTypes = new PackageTypeMembers();
			packageTypes.setName(key);
			packageTypes.setMembers(value);

			pkgMembers.add(packageTypes);
		});*/
		for (String key : retrieveConfig.keySet()) {
			PackageTypeMembers packageTypes = new PackageTypeMembers();
			packageTypes.setName(key);
			packageTypes.setMembers(retrieveConfig.get(key));

			pkgMembers.add(packageTypes);
		}

		com.sforce.soap.metadata.Package packageManifest = new com.sforce.soap.metadata.Package();
		PackageTypeMembers[] packageTypesArray = new PackageTypeMembers[pkgMembers.size()];
		packageManifest.setTypes(pkgMembers.toArray(packageTypesArray));
		packageManifest.setVersion(Settings.getInstance().get(Consts.ENV_API_VERSION));

		return packageManifest;
	}

	private RetrieveResult sendMetadataRetrieveRequest(MetadataConnection metadataConnection, com.sforce.soap.metadata.Package layoutManifest) throws Exception {
		RetrieveRequest retrieveRequest = initRetieveRequest(layoutManifest);

		AsyncResult asyncResult = metadataConnection.retrieve(retrieveRequest);

		int poll = 0;
		
		String asyncResultId = asyncResult.getId();
		RetrieveResult result = null;
		do {
			Thread.sleep(Consts.ONE_SECOND_MS);

			if (poll++ > Consts.MAX_NUM_POLL_REQUESTS) {
				throw new Exception("Request timed out.  If this is a large set " + "of metadata components, check that the time allowed " + "by MAX_NUM_POLL_REQUESTS is sufficient.");
			}
			result = metadataConnection.checkRetrieveStatus(asyncResultId, true);
			log.info("Retrieve Status: " + result.getStatus());
		} while (!result.isDone());

		return result;
	}

	private RetrieveRequest initRetieveRequest(com.sforce.soap.metadata.Package retrieveManifest) throws Exception {
		RetrieveRequest retrieveRequest = new RetrieveRequest();
		retrieveRequest.setUnpackaged(retrieveManifest);

		Double apiVersion = Double.valueOf(Settings.getInstance().get(Consts.ENV_API_VERSION));
		retrieveRequest.setApiVersion(apiVersion);

		return retrieveRequest;
	}

	private void processResultMessages(RetrieveResult result) {
		if (result.getStatus() == RetrieveStatus.Failed) {
			String errorMsg = result.getErrorStatusCode() + " msg: " + result.getErrorMessage();
			log.error(errorMsg);
		} else if (result.getStatus() == RetrieveStatus.Succeeded) {
			StringBuilder stringBuilder = new StringBuilder();
			if (result.getMessages() != null) {
				for (RetrieveMessage rm : result.getMessages()) {
					stringBuilder.append(rm.getFileName() + " - " + rm.getProblem() + "\n");
				}
			}
			if (stringBuilder.length() > 0) {
				log.warn("Retrieve warnings:\n" + stringBuilder);
			}
		}
	}

	private void processRetrievedMetadata(String metadataSubDir, byte[] zipFile) throws Exception {
		Utils.createDirIfNeeded(metadataSubDir);
		File resultsFile = new File(metadataSubDir + Consts.ZIP_FILE);
		FileOutputStream os = new FileOutputStream(resultsFile);

		try {
			os.write(zipFile);
			Utils.unzipFunction(metadataSubDir, resultsFile.getAbsolutePath());
		} finally {
			os.close();
			log.info("Metadata unzipped successfully");
		}
	}

}
