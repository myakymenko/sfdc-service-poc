package quintiles.poc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sforce.soap.metadata.AsyncResult;
import com.sforce.soap.metadata.MetadataConnection;
import com.sforce.soap.metadata.PackageTypeMembers;
import com.sforce.soap.metadata.RetrieveMessage;
import com.sforce.soap.metadata.RetrieveRequest;
import com.sforce.soap.metadata.RetrieveResult;
import com.sforce.soap.metadata.RetrieveStatus;

public class MetadataUtil {
	
	public static void retrieveMetadata(MetadataConnection metadataConnection) throws Exception {
        RetrieveResult result = sendMetadataRetrieveRequest(metadataConnection);

        if (result.getStatus() == RetrieveStatus.Failed) {
            throw new Exception(result.getErrorStatusCode() + " msg: " +
                    result.getErrorMessage());
        } else if (result.getStatus() == RetrieveStatus.Succeeded) {  
	        // Print out any warning messages
	        StringBuilder stringBuilder = new StringBuilder();
	        if (result.getMessages() != null) {
	            for (RetrieveMessage rm : result.getMessages()) {
	                stringBuilder.append(rm.getFileName() + " - " + rm.getProblem() + "\n");
	            }
	        }
	        if (stringBuilder.length() > 0) {
	            System.out.println("Retrieve warnings:\n" + stringBuilder);
	        }
	
	        System.out.println("Writing results to zip file");
	        File resultsFile = new File(ConstsPOC.ZIP_FILE);
	        FileOutputStream os = new FileOutputStream(resultsFile);
	
	        try {
	            os.write(result.getZipFile());
	            UtilPOC.unzipFunction(ConstsPOC.WORKING_DIR, ConstsPOC.ZIP_FILE);
	        } finally {
	            os.close();
	        }
        }
    }
	private static RetrieveResult sendMetadataRetrieveRequest(MetadataConnection metadataConnection) throws Exception {
		RetrieveRequest retrieveRequest = new RetrieveRequest();
        // The version in package.xml overrides the version in RetrieveRequest
        retrieveRequest.setApiVersion(ConstsPOC.API_VERSION);
        setUnpackaged(retrieveRequest);
        
		AsyncResult asyncResult = metadataConnection.retrieve(retrieveRequest);
		// Wait for the retrieve to complete
		int poll = 0;
		long waitTimeMilliSecs = ConstsPOC.ONE_SECOND_MS;
		String asyncResultId = asyncResult.getId();
		RetrieveResult result = null;
		do {
			Thread.sleep(waitTimeMilliSecs);
			// Double the wait time for the next iteration
			waitTimeMilliSecs *= 2;
			if (poll++ > ConstsPOC.MAX_NUM_POLL_REQUESTS) {
				throw new Exception("Request timed out.  If this is a large set " + 
									"of metadata components, check that the time allowed " + 
									"by MAX_NUM_POLL_REQUESTS is sufficient.");
			}
			result = metadataConnection.checkRetrieveStatus(asyncResultId, true);
			System.out.println("Retrieve Status: " + result.getStatus());
		} while (!result.isDone());

		return result;
	}

	private static void setUnpackaged(RetrieveRequest request) throws Exception {
		// Edit the path, if necessary, if your package.xml file is located
		// elsewhere
		File unpackedManifest = new File(ConstsPOC.MANIFEST_FILE);
		System.out.println("Manifest file: " + unpackedManifest.getAbsolutePath());

		if (!unpackedManifest.exists() || !unpackedManifest.isFile()) {
			throw new Exception("Should provide a valid retrieve manifest " + 
								"for unpackaged content. Looking for " + 
								unpackedManifest.getAbsolutePath());
		}

		// Note that we use the fully quualified class name because
		// of a collision with the java.lang.Package class
		com.sforce.soap.metadata.Package p = parsePackageManifest(unpackedManifest);
		request.setUnpackaged(p);
	}

	private static com.sforce.soap.metadata.Package parsePackageManifest(File file) throws ParserConfigurationException, IOException, SAXException {
		com.sforce.soap.metadata.Package packageManifest = null;
		List<PackageTypeMembers> listPackageTypes = new ArrayList<PackageTypeMembers>();
		DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		InputStream inputStream = new FileInputStream(file);
		Element d = db.parse(inputStream).getDocumentElement();
		for (Node c = d.getFirstChild(); c != null; c = c.getNextSibling()) {
			if (c instanceof Element) {
				Element ce = (Element) c;
				NodeList nodeList = ce.getElementsByTagName("name");
				if (nodeList.getLength() == 0) {
					continue;
				}
				String name = nodeList.item(0).getTextContent();
				NodeList m = ce.getElementsByTagName("members");
				List<String> members = new ArrayList<String>();
				for (int i = 0; i < m.getLength(); i++) {
					Node mm = m.item(i);
					members.add(mm.getTextContent());
				}
				PackageTypeMembers packageTypes = new PackageTypeMembers();
				packageTypes.setName(name);
				packageTypes.setMembers(members.toArray(new String[members.size()]));
				listPackageTypes.add(packageTypes);
			}
		}
		packageManifest = new com.sforce.soap.metadata.Package();
		PackageTypeMembers[] packageTypesArray = new PackageTypeMembers[listPackageTypes.size()];
		packageManifest.setTypes(listPackageTypes.toArray(packageTypesArray));
		packageManifest.setVersion(ConstsPOC.API_VERSION + "");
		return packageManifest;
	}
}
