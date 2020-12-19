package org.example;

import org.example.entities.ExampleDetailEntity;
import org.example.entities.ExampleEntity;
import org.example.models.ExampleModel;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;

import java.util.Collections;

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
        ExampleModel exampleEntity = responseMsg.readEntity(ExampleModel.class);
        assertNotNull(responseMsg);
        assertNotNull(exampleEntity);
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
        var transaction = entityManager.getTransaction();
        transaction.begin();
        ExampleEntity exampleEntity = new ExampleEntity();
        ExampleDetailEntity exampleDetailEntity = new ExampleDetailEntity();
        exampleDetailEntity.setName("ExampleDetailEntityName");
        exampleEntity.setExampleDetailEntity(Collections.singletonList(exampleDetailEntity));
        exampleEntity.setName("ExampleEntityName");
        entityManager.persist(exampleEntity);
        transaction.commit();
        entityManager.close();
        entityManagerFactory.close();
    }
}