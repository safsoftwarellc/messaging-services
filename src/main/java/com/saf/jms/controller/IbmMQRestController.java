package com.saf.jms.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.saf.jms.mq.IbmMqInfo;
import com.saf.jms.mq.IbmMqService;

@RestController
public class IbmMQRestController {

	@Autowired
	IbmMqService ibmMqService;
	
	@RequestMapping(path = "/rest/mq/postMessage", 
			method = RequestMethod.POST, 
			consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Object> postMessage(@RequestParam("file") MultipartFile file,
			@RequestParam("queueName") String queueName,
			@RequestParam("messageProperties") String messageProperties,
			@RequestBody IbmMqInfo ibmMqInfo){
		
		boolean isMessagePosted = false;
		
		String fileData = null;
		try {
			fileData = new String(file.getBytes());
			ibmMqService.init(ibmMqInfo);
			ibmMqService.sendTextMessage(queueName, fileData, messageProperties);
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
