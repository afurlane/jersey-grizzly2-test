package org.example.servicesclient;

import https.www_ebi_ac_uk.webservices.chebi.ChebiWebServiceFault_Exception;
import https.www_ebi_ac_uk.webservices.chebi.ChebiWebServicePortType;
import https.www_ebi_ac_uk.webservices.chebi.ChebiWebServiceService;

import javax.inject.Inject;

public class SOAPClientTest {

    @Inject
    private ChebiWebServiceService service;

    public void TestCall() throws ChebiWebServiceFault_Exception {
        ChebiWebServicePortType port = service.getChebiWebServicePort();
        System.out.println(port.getCompleteEntity("Dude"));
    }
}
