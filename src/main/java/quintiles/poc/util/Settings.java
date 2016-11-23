package quintiles.poc.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Helper class for getting environment variables. It reads the .env resource file with the settings.
 * Firstly gets variable via the {@link java.lang.System#getenv(String)} method and if it is {@code null}
 * it gets the value from the .env file.</p>
 * <p>The .env file should be in the classpath. The suggested place for this file is {@code src/main/resources}.</p>
 *
 * @author Yzaretskyy
 */
public class Settings {
    private Logger log = LoggerFactory.getLogger(Settings.class);

    private static Settings instance;
    private Map<String, String> settings = new HashMap<>();

    private Settings() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(".env")));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] keyValue = line.split("=");
                if(keyValue.length > 1) {
                    settings.put(keyValue[0], keyValue[1]);
                }
            }
        } catch (IOException e) {
            log.error("Some problem occurred while trying to read .env file.");
        }
    }

    public static Settings getInstance() {
        if (instance == null) {
            synchronized(Settings.class) {
                if (instance == null) {
                    instance = new Settings();
                }
            }
        }
        return instance;
    }

    /**
     * Gets the value of the environment variable via System.getenv() method.
     * If it is {@code null} the value from the {@code .env} file will be returned.
     * @param name the name of the environment variable
     * @return the value of the environment variable via System.getenv() method or the value from the {@code .env} file if it is {@code null}
     */
    public String get(String name) {
        String result = System.getenv(name);
        if (result == null) {
            //log.warn(String.format("The environment variable with the '%s' name isn't found! Please check whether it is defined in the Heroku.", name));
        }
        return result == null ? settings.get(name) : result;
    }

    public int getInt(String name) {
        return Integer.valueOf(get(name));
    }
}
