package org.example.servicesclient;

import https.www_ebi_ac_uk.webservices.chebi.ChebiWebServiceFault_Exception;
import https.www_ebi_ac_uk.webservices.chebi.ChebiWebServicePortType;
import https.www_ebi_ac_uk.webservices.chebi.ChebiWebServiceService;

public class SOAPClientTest {
    public void TestCall() throws ChebiWebServiceFault_Exception {
        ChebiWebServiceService service = new ChebiWebServiceService();
        ChebiWebServicePortType port = service.getChebiWebServicePort();
        System.out.println(port.getCompleteEntity("Dude"));
    }
}
