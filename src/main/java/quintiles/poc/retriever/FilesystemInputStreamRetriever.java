package quintiles.poc.retriever;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import quintiles.poc.api.IInputStreamRetriever;
import quintiles.poc.util.Consts;

public class FilesystemInputStreamRetriever implements IInputStreamRetriever {

	private String fileName;

	public FilesystemInputStreamRetriever() {
	}

	@Override
	public void setSourceItem(String sourceItem) {
		this.fileName = sourceItem;
	}

	@Override
	public InputStream getInputStream() throws Exception {
		return getProcessedFileInputStream(fileName, Consts.WORKING_DIR);
	}
	
	private FileInputStream getProcessedFileInputStream(String fileName, String searchFileDir) throws FileNotFoundException {
		FileInputStream result = null;
		File processedFile = null;
		
		if (searchFileDir != null) {
			File serchDir = new File(searchFileDir);
			
			processedFile = searchFile(fileName, serchDir);
		} else {
			processedFile = new File(fileName);
		}
		result = new FileInputStream(processedFile);
		
		return result;
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

}
