package org.example;

import org.junit.Test;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;

public class MyEntityResourceTest extends CdiBaseTest {

    @Test
    public void testGGetEntityByIdWhithNullParam() {
        Response responseMsg = target().path(MyEntityResource.MyEntityResourcePath).request().get();
        String prova = responseMsg.readEntity(String.class);
        assertNotNull(responseMsg);
        assertNotNull(prova);
    }

    @Test(expected = NotFoundException.class)
    public void testGGetEntityById() {
        UUID id = UUID.randomUUID();
        Response responseMsg = target().path(MyEntityResource.MyEntityResourcePath).path(id.toString()).request().get();
        assertNotNull(responseMsg);
    }

    @Test
    public void getEntityById() {
    }

    @Test
    public void deleteEntityById() {
    }
}