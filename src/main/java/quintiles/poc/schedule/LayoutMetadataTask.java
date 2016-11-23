package quintiles.poc.schedule;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sforce.soap.metadata.MetadataConnection;
import com.sforce.soap.partner.LoginResult;
import com.sforce.soap.partner.PartnerConnection;
import com.sforce.ws.ConnectionException;

import quintiles.poc.container.LayoutMetadata;
import quintiles.poc.service.ConnectionService;
import quintiles.poc.service.MetadataProcessingService;
import quintiles.poc.service.MetadataRetrieveService;
import quintiles.poc.util.Consts;
import quintiles.poc.util.Settings;

public class LayoutMetadataTask {

	private Logger log = LoggerFactory.getLogger(Settings.class);

	private static LayoutMetadata layoutMetadata = null;

	private static LayoutMetadataTask instance;
	private Timer timer;

	private LayoutMetadataTask() {
		LayoutMetadataProcessTask task = new LayoutMetadataProcessTask();

		timer = new Timer();
		int interval = Integer.valueOf(Settings.getInstance().get(Consts.ENV_RETRIEVE_LAYOUT_INTERVAL));
		
		timer.schedule(task, 0, interval);
	}

	public static LayoutMetadataTask getInstance() {
		if (instance == null) {
			synchronized (LayoutMetadataTask.class) {
				if (instance == null) {
					instance = new LayoutMetadataTask();
				}
			}
		}
		return instance;
	}

	public static LayoutMetadata getLayoutMetadata() {
		return layoutMetadata;
	}

	public void runTask() throws Exception {
		PartnerConnection connection = null;
		try {
			Settings settings = Settings.getInstance();

			String userName = settings.get(Consts.ENV_SFDC_USER);
			String password = settings.get(Consts.ENV_SFDC_PASSWORD);
			String soapUrl = settings.get(Consts.ENV_SOAP_URL);
			ConnectionService connectionService = new ConnectionService();

			connection = connectionService.getSOAPConnection(soapUrl, userName, password);

			LoginResult loginResult = connection.login(userName, password);

			MetadataConnection metadataConnection = connectionService.getSOAPMetadataConnection(loginResult);

			MetadataRetrieveService metadataService = new MetadataRetrieveService(metadataConnection);
			metadataService.retrieveLayoutRelatedMetadata();

			this.layoutMetadata = processRetrievedMetadata(connection);
			log.info("Layout metadata retrieve FINISHED");
		} finally {
			try {
				if (connection != null) {
					connection.logout();
					log.info("Connection logout");
				}
			} catch (ConnectionException e) {
				log.error(e.getMessage());
			}
		}
	}

	private LayoutMetadata processRetrievedMetadata(PartnerConnection connection) throws Exception {
		Settings settings = Settings.getInstance();

		String[] sobjects = settings.get(Consts.ENV_SOBJECTS).split(Consts.ENV_VAL_SEPARATOR);
		String[] profiles = settings.get(Consts.ENV_PROFILES).split(Consts.ENV_VAL_SEPARATOR);
		String[] layouts = settings.get(Consts.ENV_LAYOUTS).split(Consts.ENV_VAL_SEPARATOR);

		LayoutMetadata layoutMetadata = new LayoutMetadata(profiles, sobjects, layouts);

		MetadataProcessingService processingService = new MetadataProcessingService();
		processingService.processLayoutMetadata(layoutMetadata, connection);

		return layoutMetadata;
	}

	private class LayoutMetadataProcessTask extends TimerTask {

		@Override
		public void run() {
			log.info("Layout metadata retrieve has been started");
			long scheduledTime = System.currentTimeMillis() + Integer.valueOf(Settings.getInstance().get(Consts.ENV_RETRIEVE_LAYOUT_INTERVAL));
			
			log.info("Next retrieve will be started at " + new Date(scheduledTime));
			try {
				LayoutMetadataTask.instance.runTask();
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}
	}
}
