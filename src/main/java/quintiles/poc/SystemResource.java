package quintiles.poc;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import quintiles.poc.schedule.LayoutMetadataTask;
import quintiles.poc.util.Settings;

@Path("sync")
public class SystemResource {
	
	private Logger log = LoggerFactory.getLogger(Settings.class);
	
	@POST
	public Response resetLayouts() {
		Response response = null;
		try {
			LayoutMetadataTask scheduler = LayoutMetadataTask.getInstance();
			scheduler.runTask();
			response = Response.ok().build();
		} catch (Exception e) {
			response = Response.serverError().build();
		}
		return response;
	}
}
