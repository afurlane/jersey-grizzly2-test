package org.example;

import org.apache.logging.log4j.Logger;
import org.example.entities.ExampleEntity;
import org.example.models.ExampleModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.Response;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MyEntityResourceTest {
    private final long waitThreadTimeout = 5_000;

    @Mock
    EntityManagerFactory entityManagerFactory;
    @Mock
    EntityManager entityManager;
    @Mock
    CriteriaBuilder criteriaBuilder;
    @Mock
    ModelMapper modelMapper;
    @Mock
    CriteriaQuery<ExampleEntity> entityCriteriaQuery;
    @Mock
    Root<ExampleEntity> exampleEntityRoot;
    @Mock
    TypedQuery<ExampleEntity> exampleEntityTypedQuery;
    @Mock
    ExampleEntity exampleEntity;
    @Mock
    ExampleModel exampleModel;
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    AsyncResponse asyncResponse;
    @Captor
    ArgumentCaptor<Response> captor;
    @Mock
    Logger logger;

    @InjectMocks
    MyEntityResource myEntityResource;

    // Just to show mockito features but using @Mock annotation this is almost useless.
    @Test
    public void TestGetEntityById()
    {
        doReturn(entityManager).when(entityManagerFactory).createEntityManager();
        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(ExampleEntity.class)).thenReturn(entityCriteriaQuery);
        when(entityCriteriaQuery.from(ExampleEntity.class)).thenReturn(exampleEntityRoot);
        when(entityManager.createQuery( entityCriteriaQuery)).thenReturn(exampleEntityTypedQuery);
        when(exampleEntityTypedQuery.setFirstResult(0)).thenReturn(exampleEntityTypedQuery);
        when(exampleEntityTypedQuery.setMaxResults(1)).thenReturn(exampleEntityTypedQuery);
        when(exampleEntityTypedQuery.getSingleResult()).thenReturn(exampleEntity);
        // First call; return example model
        when(modelMapper.map(exampleEntity, ExampleModel.class)).thenReturn(exampleModel);
        myEntityResource.GetEntityById(asyncResponse);

        // If you use ArgumentCaptor you should be sure that the other thread has completed.
        Sleep(waitThreadTimeout);
        verify(asyncResponse, atLeastOnce()).resume(captor.capture());
        Response res = captor.getValue();
        assertNotNull(res);
        assertEquals(res.getEntity(), exampleModel);
        assertEquals(res.getStatus(), Response.Status.OK.getStatusCode());

        // second call, return null.
        when(exampleEntityTypedQuery.getSingleResult()).thenReturn(null);
        myEntityResource.GetEntityById(asyncResponse);
        Sleep(waitThreadTimeout);
        verify(asyncResponse, atLeastOnce()).resume(captor.capture());
        res = captor.getValue();

        assertNotNull(res);
        assertEquals(res.getStatus(), Response.Status.NOT_FOUND.getStatusCode());
    }

    private void Sleep(long millis)
    {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
