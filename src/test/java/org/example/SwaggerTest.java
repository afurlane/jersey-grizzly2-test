package org.example;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SwaggerTest extends CdiBaseTest {

    @Test
    public void TestSwagger ()
    {
        String responseMsg = target().path("openapi.json").request().get(String.class);
        assertEquals("Got it!", responseMsg);
    }
}
