package quintiles.poc;

import javax.ws.rs.core.Application;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class LayoutMetadataResourceTest extends JerseyTest {
	@Override
    protected Application configure() {
        return new ResourceConfig(LayoutMetadataResource.class);
    }

    /**
     * Test to see that the message "Got it!" is sent in the response.
     */
    @Test
    public void testGetIt() {
        final String responseMsg = target().path("layouts").request().get(String.class);

        assertEquals("Web app is alieve", responseMsg);
    }
}
