package org.example.servicesclient;

import https.www_ebi_ac_uk.webservices.chebi.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SOAPClientTestTest {

    @Mock
    ChebiWebServiceService chebiWebServiceService;

    @Mock
    ChebiWebServicePortType chebiWebServicePortType;

    @Mock
    Entity entity;

    @Mock
    ChebiWebServiceFault chebiWebServiceFault;

    @InjectMocks
    SOAPClientTest soapClientTest;

    private String param = "MockTest";
    private Entity entity2;

    @Test
    public void testCall() throws ChebiWebServiceFault_Exception {
        doReturn(chebiWebServicePortType).when(chebiWebServiceService).getChebiWebServicePort();
        when(chebiWebServicePortType.getCompleteEntity(param)).thenReturn(entity);
        entity2 = chebiWebServicePortType.getCompleteEntity(param);
        assertNotNull(entity2);
        assertEquals(entity, entity2);

        given(chebiWebServicePortType.getCompleteEntity(param)).willThrow(new ChebiWebServiceFault_Exception(param, chebiWebServiceFault));
        Throwable thrown = catchThrowable(() -> { chebiWebServicePortType.getCompleteEntity(param); });
        then(thrown).isInstanceOf(ChebiWebServiceFault_Exception.class).hasMessageContaining(param);
    }
}