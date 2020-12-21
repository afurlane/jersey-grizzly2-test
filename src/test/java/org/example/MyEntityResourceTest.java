package org.example;

import org.apache.logging.log4j.Logger;
import org.example.entities.ExampleEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.modelmapper.ModelMapper;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.Response;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MyEntityResourceTest {
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
    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    AsyncResponse asyncResponse;
    @Captor
    private ArgumentCaptor<Response> captor;
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
        // when(entityCriteriaQuery.select(exampleEntityRoot));
        when(entityManager.createQuery( entityCriteriaQuery)).thenReturn(exampleEntityTypedQuery);
        when(exampleEntityTypedQuery.setFirstResult(0)).thenReturn(exampleEntityTypedQuery);
        when(exampleEntityTypedQuery.setMaxResults(1)).thenReturn(exampleEntityTypedQuery);
        when(exampleEntityTypedQuery.getSingleResult()).thenReturn(exampleEntity);
        doAnswer(new Answer<Void>() {
            public Void answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                return null;
            }
        }).when(asyncResponse).setTimeoutHandler(asyncResponse1 -> asyncResponse1.resume(Response.status(Response.Status.SERVICE_UNAVAILABLE)
                .entity("Operation time out.").build()));
        when(asyncResponse.setTimeout(60, TimeUnit.SECONDS)).thenReturn(true);

        myEntityResource.GetEntityById(asyncResponse);

        verify(asyncResponse).resume(this.captor.capture());
        final Response res = this.captor.getValue();

        assertEquals(res.getEntity(), equals(exampleEntity));
        assertEquals(res.getStatus(), Response.Status.OK);
    }
}
