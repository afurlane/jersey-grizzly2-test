package org.example;

import org.example.entities.ExampleDetailEntity;
import org.example.entities.ExampleEntity;
import org.example.infrastructure.Producer;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;

public class MyEntityResourceTest extends CdiBaseTest {

    @Override
    public void setUp() throws Exception {
        super.setUp();
        seedData();
    }

    @Test
    public void testGGetEntityByIdWhithNullParam() {
        Response responseMsg = target().path(MyEntityResource.MyEntityResourcePath).request().get();
        String prova = responseMsg.readEntity(String.class);
        assertNotNull(responseMsg);
        assertNotNull(prova);
    }

    @Test(expected = NotFoundException.class)
    public void testGGetEntityById() {
        Long id = 1L;
        Response responseMsg = target().path(MyEntityResource.MyEntityResourcePath).path(id.toString()).request().get();
        assertNotNull(responseMsg);
    }

    @Test
    public void getEntityById() {
    }

    @Test
    public void deleteEntityById() {

    }

    private void seedData()
    {
        EntityManagerFactory entityManagerFactory = container.select(EntityManagerFactory.class).get();
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        ExampleEntity exampleEntity = new ExampleEntity();
        ExampleDetailEntity exampleDetailEntity = new ExampleDetailEntity();
        exampleDetailEntity.setName("ExampleDetailEntityName");
        exampleEntity.setExampleDetailEntity(exampleDetailEntity);
        exampleEntity.setName("ExampleEntityName");
        entityManager.persist(exampleEntity);
        entityManager.close();
        entityManagerFactory.close();
    }
}