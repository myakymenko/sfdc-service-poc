package quintiles.poc.heroku;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UtilPOC {

	public static void unzipFunction(String destinationFolder, String zipFile) {
		FileInputStream fInput = null;
		ZipInputStream zipInput = null;
		ZipEntry entry = null;

		File directory = new File(destinationFolder);

		// if the output directory doesn't exist, create it
		if (!directory.exists())
			directory.mkdirs();

		// buffer for read and write data to file
		byte[] buffer = new byte[2048];

		try {
			fInput = new FileInputStream(zipFile);
			zipInput = new ZipInputStream(fInput);

			entry = zipInput.getNextEntry();

			while (entry != null) {
				String entryName = entry.getName();
				File file = new File(destinationFolder + File.separator + entryName);

				System.out.println("Unzip file " + entryName + " to " + file.getAbsolutePath());

				// create the directories of the zip directory
				if (entry.isDirectory()) {
					File newDir = new File(file.getAbsolutePath());
					if (!newDir.exists()) {
						boolean success = newDir.mkdirs();
						if (success == false) {
							System.out.println("Problem creating Folder");
						}
					}
				} else {
					System.out.println(file.getParent());
					File containingFolder = new File(file.getParent());
					if (!containingFolder.exists()) {
						boolean success = containingFolder.mkdirs();
						if (success == false) {
							System.out.println("Problem creating Folder");
						}
					}
					FileOutputStream fOutput = new FileOutputStream(file);
					int count = 0;
					while ((count = zipInput.read(buffer)) > 0) {
						// write 'count' bytes to the file output stream
						fOutput.write(buffer, 0, count);
					}
					fOutput.close();
				}
				// close ZipEntry and take the next one
				zipInput.closeEntry();
				entry = zipInput.getNextEntry();
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// close the last ZipEntry
			try {
				zipInput.closeEntry();
				zipInput.close();
				fInput.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
}
