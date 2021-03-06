package com.saf.jms.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.saf.jms.solace.SolaceQueueInfo;
import com.saf.jms.solace.SolaceService;

@RestController
public class SolaceRestController {
	
	@Autowired
	SolaceService solaceService;
	
	
	@RequestMapping(path = "/rest/solace/postTextMessageTest", 
			method = RequestMethod.POST)
	public ResponseEntity<Object> postTextMessageTest(@RequestParam("messageText") String messageText,
							@RequestParam("jmsCorrelationID") String jmsCorrelationID,
							@RequestParam("messageProperties") String messageProperties,
							@RequestParam("initialContextFactory") String initialContextFactory,
							@RequestParam("providerUrl") String providerUrl,
							@RequestParam("securityPrincipal") String securityPrincipal,
							@RequestParam("securityCredentials") String securityCredentials,
							@RequestParam("solaceJmsVpn") String solaceJmsVpn,
							@RequestParam("solaceJmsSslValidateCertificate") String solaceJmsSslValidateCertificate,
							@RequestParam("solaceJmsRespectTimeToLIve") String solaceJmsRespectTimeToLIve,
							@RequestParam("connectionFactory") String connectionFactory,
							@RequestParam("solDestination") String solDestination) {
		
		System.out.println(messageText);
		return new ResponseEntity<Object>("Posted Successfull!", HttpStatus.OK);
	}
	
	@RequestMapping(path = "/rest/solace/postMessageTest", 
			method = RequestMethod.POST, 
			consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Object> postMessageTest(@RequestParam("messageFile") MultipartFile messageFile,
							@RequestParam("jmsCorrelationID") String jmsCorrelationID,
							@RequestParam("messageProperties") String messageProperties,
							@RequestParam("initialContextFactory") String initialContextFactory,
							@RequestParam("providerUrl") String providerUrl,
							@RequestParam("securityPrincipal") String securityPrincipal,
							@RequestParam("securityCredentials") String securityCredentials,
							@RequestParam("solaceJmsVpn") String solaceJmsVpn,
							@RequestParam("solaceJmsSslValidateCertificate") String solaceJmsSslValidateCertificate,
							@RequestParam("solaceJmsRespectTimeToLIve") String solaceJmsRespectTimeToLIve,
							@RequestParam("connectionFactory") String connectionFactory,
							@RequestParam("solDestination") String solDestination) {
		
		return new ResponseEntity<Object>("Posted Successfull!", HttpStatus.OK);
	}

	@RequestMapping(path = "/rest/solace/postTextMessage", 
			method = RequestMethod.POST)
	public ResponseEntity<Object> postTextMessage(@RequestParam("messageText") String messageText,
							@RequestParam("jmsCorrelationID") String jmsCorrelationID,
							@RequestParam("messageProperties") String messageProperties,
							@RequestParam("initialContextFactory") String initialContextFactory,
							@RequestParam("providerUrl") String providerUrl,
							@RequestParam("securityPrincipal") String securityPrincipal,
							@RequestParam("securityCredentials") String securityCredentials,
							@RequestParam("solaceJmsVpn") String solaceJmsVpn,
							@RequestParam("solaceJmsSslValidateCertificate") String solaceJmsSslValidateCertificate,
							@RequestParam("solaceJmsRespectTimeToLIve") String solaceJmsRespectTimeToLIve,
							@RequestParam("connectionFactory") String connectionFactory,
							@RequestParam("solDestination") String solDestination) {
		
		boolean isMessagePosted = false;
		
		SolaceQueueInfo solaceQueueInfo = new SolaceQueueInfo(initialContextFactory, providerUrl, securityPrincipal, securityCredentials, 
				solaceJmsVpn, solaceJmsSslValidateCertificate, solaceJmsRespectTimeToLIve, connectionFactory, solDestination);
		isMessagePosted = solaceService.postMessageToQueue(solaceQueueInfo, messageText, jmsCorrelationID, messageProperties);
		
		ResponseEntity<Object> response = null;
		if(isMessagePosted) {
			response = new ResponseEntity<Object>("Posted Successfull!", HttpStatus.OK);
		}else {
			response = new ResponseEntity<Object>("Message NOT Posted!", HttpStatus.EXPECTATION_FAILED);
		}
		
		return response;
	}
	
	@RequestMapping(path = "/rest/solace/postMessage", 
			method = RequestMethod.POST, 
			consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Object> postMessage(@RequestParam("messageFile") MultipartFile messageFile,
							@RequestParam("jmsCorrelationID") String jmsCorrelationID,
							@RequestParam("messageProperties") String messageProperties,
							@RequestParam("initialContextFactory") String initialContextFactory,
							@RequestParam("providerUrl") String providerUrl,
							@RequestParam("securityPrincipal") String securityPrincipal,
							@RequestParam("securityCredentials") String securityCredentials,
							@RequestParam("solaceJmsVpn") String solaceJmsVpn,
							@RequestParam("solaceJmsSslValidateCertificate") String solaceJmsSslValidateCertificate,
							@RequestParam("solaceJmsRespectTimeToLIve") String solaceJmsRespectTimeToLIve,
							@RequestParam("connectionFactory") String connectionFactory,
							@RequestParam("solDestination") String solDestination) {
		
		boolean isMessagePosted = false;
		
		String fileData = null;
		SolaceQueueInfo solaceQueueInfo = new SolaceQueueInfo(initialContextFactory, providerUrl, securityPrincipal, securityCredentials, 
				solaceJmsVpn, solaceJmsSslValidateCertificate, solaceJmsRespectTimeToLIve, connectionFactory, solDestination);
		try {
			fileData = new String(messageFile.getBytes());
			isMessagePosted = solaceService.postMessageToQueue(solaceQueueInfo, fileData, jmsCorrelationID, messageProperties);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ResponseEntity<Object> response = null;
		if(isMessagePosted) {
			response = new ResponseEntity<Object>("Posted Successfull!", HttpStatus.OK);
		}else {
			response = new ResponseEntity<Object>("Message NOT Posted!", HttpStatus.EXPECTATION_FAILED);
		}
		
		return response;
	}
	
}
