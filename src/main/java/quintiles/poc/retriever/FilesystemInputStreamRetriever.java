package quintiles.poc.retriever;

import java.io.InputStream;

import quintiles.poc.api.IInputStreamRetriever;
import quintiles.poc.heroku.Consts;
import quintiles.poc.heroku.Utils;

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
		return Utils.getProcessedFileInputStream(fileName, Consts.WORKING_DIR);
	}

}
