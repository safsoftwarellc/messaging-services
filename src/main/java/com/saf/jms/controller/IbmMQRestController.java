package com.saf.jms.controller;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.saf.jms.mq.IbmMqInfo;
import com.saf.jms.mq.IbmMqService;

@RestController
public class IbmMQRestController extends ControllerBase {

	@Autowired
	IbmMqService ibmMqService;
	
	
	@RequestMapping(path = "/rest/mq/postMessageTestMessage", 
			method = RequestMethod.POST, 
			consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Object> postMessageTestMessage(@RequestParam("messageText") String messageText,
			@RequestParam(name="keyStoreFile", required = false) MultipartFile keyStoreFile,
			@RequestParam(name="keyStorePwd", required = false) String keyStorePwd,
			@RequestParam(name="trustStoreFile", required = false) MultipartFile trustStoreFile,
			@RequestParam(name="trustStorePwd", required = false) String trustStorePwd,
			@RequestParam("queueName") String queueName,
			@RequestParam("messageProperties") String messageProperties,
			@RequestParam("hostName") String hostName,
			@RequestParam("portNumber") String portNumber,
			@RequestParam("queueManager") String queueManager,
			@RequestParam("channel") String channel,
			@RequestParam("sslCipherSuite") String sslCipherSuite,
			@RequestParam("useIBMCipherMappings") String useIBMCipherMappings){
		
		return new ResponseEntity<Object>("Posted Successfull!", HttpStatus.OK);
	}
	
	@RequestMapping(path = "/rest/mq/postMessageTest", 
			method = RequestMethod.POST, 
			consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Object> postMessageTest(@RequestParam("messageFile") MultipartFile messageFile,
			@RequestParam(name="keyStoreFile", required = false) MultipartFile keyStoreFile,
			@RequestParam(name="keyStorePwd", required = false) String keyStorePwd,
			@RequestParam(name="trustStoreFile", required = false) MultipartFile trustStoreFile,
			@RequestParam(name="trustStorePwd", required = false) String trustStorePwd,
			@RequestParam("queueName") String queueName,
			@RequestParam("messageProperties") String messageProperties,
			@RequestParam("hostName") String hostName,
			@RequestParam("portNumber") String portNumber,
			@RequestParam("queueManager") String queueManager,
			@RequestParam("channel") String channel,
			@RequestParam("sslCipherSuite") String sslCipherSuite,
			@RequestParam("useIBMCipherMappings") String useIBMCipherMappings){
		
		return new ResponseEntity<Object>("Posted Successfull!", HttpStatus.OK);
	}
	
	@RequestMapping(path = "/rest/mq/postMessage", 
			method = RequestMethod.POST, 
			consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Object> postMessage(@RequestParam("messageFile") MultipartFile messageFile,
											@RequestParam(name="keyStoreFile", required = false) MultipartFile keyStoreFile,
											@RequestParam(name="keyStorePwd", required = false) String keyStorePwd,
											@RequestParam(name="trustStoreFile", required = false) MultipartFile trustStoreFile,
											@RequestParam(name="trustStorePwd", required = false) String trustStorePwd,
											@RequestParam("queueName") String queueName,
											@RequestParam("messageProperties") String messageProperties,
											@RequestParam("hostName") String hostName,
											@RequestParam("portNumber") String portNumber,
											@RequestParam("queueManager") String queueManager,
											@RequestParam("channel") String channel,
											@RequestParam("sslCipherSuite") String sslCipherSuite,
											@RequestParam("useIBMCipherMappings") String useIBMCipherMappings){
		
		boolean isMessagePosted = false;
		String ksFilePath = null;
		String tsFilePath =  null;
		if(keyStoreFile!=null) {
			ksFilePath = System.getProperty("java.io.tmpdir")+getRandomNumber()+"_"+keyStoreFile.getOriginalFilename();
			System.out.println("KeyStore File is - "+ksFilePath);
			try {
				
				FileUtils.writeByteArrayToFile(new File(ksFilePath), keyStoreFile.getBytes());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		if(trustStoreFile!=null) {
			tsFilePath = System.getProperty("java.io.tmpdir")+getRandomNumber()+"_"+trustStoreFile.getOriginalFilename();
			System.out.println("TrustStore File is - "+tsFilePath);
			try {
				FileUtils.writeByteArrayToFile(new File(tsFilePath), trustStoreFile.getBytes());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

		String fileData = null;
		IbmMqInfo ibmMqInfo = new IbmMqInfo(hostName, portNumber, queueManager, channel, sslCipherSuite, useIBMCipherMappings);
		try {
			fileData = new String(messageFile.getBytes());
			ibmMqService.init(ibmMqInfo, ksFilePath, keyStorePwd, tsFilePath, trustStorePwd);
			isMessagePosted = ibmMqService.sendTextMessage(queueName, fileData, messageProperties);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(ksFilePath!=null) {
			try {
				FileUtils.delete(new File(ksFilePath));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(tsFilePath!=null) {
			try {
				FileUtils.delete(new File(tsFilePath));
			} catch (IOException e) {
				e.printStackTrace();
			}
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
