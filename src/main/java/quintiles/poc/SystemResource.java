package quintiles.poc;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import quintiles.poc.schedule.LayoutMetadataTask;

@Path("sync")
public class SystemResource {
	
	private Logger log = LoggerFactory.getLogger(SystemResource.class);
	
	@POST
	public Response resetLayouts() {
		Response response = null;
		try {
			LayoutMetadataTask scheduler = LayoutMetadataTask.getInstance();
			scheduler.runTask();
			response = Response.ok().build();
		} catch (Exception e) {
			response = Response.serverError().build();
			log.error(e.getMessage());
		}
		return response;
	}
}
