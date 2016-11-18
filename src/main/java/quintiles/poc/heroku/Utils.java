package quintiles.poc.heroku;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Utils {
	
	public static void unzipFunction(String destinationFolder, String zipFile) throws Exception {
        createDirIfNeeded(destinationFolder);

        try (FileInputStream fInput = new FileInputStream(zipFile); ZipInputStream zipInput = new ZipInputStream(fInput)) {
            ZipEntry entry = zipInput.getNextEntry();

            while (entry != null) {
                String entryName = entry.getName();
                File file = new File(destinationFolder + File.separator + entryName);

                // create the directories of the zip directory
                if (entry.isDirectory()) {
                    createDirIfNeeded(file.getAbsolutePath());
                } else {
                    createDirIfNeeded(file.getParent());
                    try (FileOutputStream fOutput = new FileOutputStream(file)) {
                        byte[] buffer = new byte[2048];
                        int count = 0;
                        while ((count = zipInput.read(buffer)) > 0) {
                            fOutput.write(buffer, 0, count);
                        }
                    }
                }

                zipInput.closeEntry();
                entry = zipInput.getNextEntry();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createDirIfNeeded(String path) throws Exception {
        File dir = new File(path);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new Exception("Folder was not created");
        }
    }
	
	public static FileInputStream getProcessedFileInputStream(String fileName, String searchFileDir) throws FileNotFoundException {
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
	
	public static File searchFile(String fileName, File path) {
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
	
	public static boolean isBlankString(String str) {
		return str == null || str.trim().equals("");
	}
}
