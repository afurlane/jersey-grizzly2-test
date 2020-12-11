package org.example;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MyResourceTest extends CdiBaseTest {

    /**
     * Test to see that the message "Got it!" is sent in the response.
     */
    @Test
    public void testGetIt() {
        String responseMsg = target().path(MyResource.MyResourcePath).request().get(String.class);
        assertEquals("Got it!", responseMsg);
    }
}
