package org.example;

import org.junit.Test;

import javax.persistence.metamodel.ListAttribute;
import javax.ws.rs.core.Response;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MyResourceTest extends CdiBaseTest {

    /**
     * Test to see that the message "Got it!" is sent in the response.
     */
    @Test
    public void testGetIt() {
        String responseMsg = target().path(MyResource.MyResourcePath).request().get(String.class);
        assertEquals("Got it!", responseMsg);
    }

    @Test
    public void getItWithQuery() {
        List<String> responseMsg = target().path(MyResource.MyResourcePath)
                .path(MyResource.MyResourceTryQuery).request().get(List.class);
        assertNotNull(responseMsg);
    }
}
