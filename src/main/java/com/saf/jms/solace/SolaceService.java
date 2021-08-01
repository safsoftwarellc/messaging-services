package com.saf.jms.solace;

import java.util.Hashtable;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.solacesystems.jms.SolDestination;
import com.solacesystems.jms.SupportedProperty;
import com.solacesystems.jms.message.SolTextMessage;

@Service
public class SolaceService {
	
	public boolean postMessageToQueue(SolaceQueueInfo solaceQueueInfo, 
			String fileData, String jmsCorrelationID, String messageProperties) {
		
		boolean messagePosted = false;
		
		Session session = null;
		MessageProducer messageProducer = null;
		
		Hashtable<String, Object> environment = getSolaceQueueEnvironmentInfo(solaceQueueInfo);
		
		
		try {
			InitialContext initialContext = new InitialContext(environment);
			
			ConnectionFactory connectionFactory = (ConnectionFactory) initialContext.lookup(solaceQueueInfo.getConnectionFactory());
			Connection connection = connectionFactory.createConnection();
			
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			
			SolDestination solDestination = (SolDestination) initialContext.lookup(solaceQueueInfo.getSolDestination());
			
			messageProducer = session.createProducer(solDestination);
			
			SolTextMessage message = getSolaceTextMessageObject(fileData, jmsCorrelationID, messageProperties);
			messageProducer.send(message);
			messagePosted = true;
		
		} catch (NamingException | JMSException e) {
			e.printStackTrace();
		}finally {
			if(messageProducer!=null) {
				try {
					messageProducer.close();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
			if(session!=null) {
				try {
					session.close();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		}
		
	
		
		return messagePosted;
	}
	
	
	private Hashtable<String, Object> getSolaceQueueEnvironmentInfo(SolaceQueueInfo solaceQueueInfo) {
		Hashtable<String, Object> environment = new Hashtable<String, Object>();
		environment.put(InitialContext.INITIAL_CONTEXT_FACTORY, solaceQueueInfo.getInitialContextFactory());
		environment.put(InitialContext.PROVIDER_URL, solaceQueueInfo.getProviderUrl());
		environment.put(InitialContext.SECURITY_PRINCIPAL, solaceQueueInfo.getSecurityPrincipal());
		environment.put(InitialContext.SECURITY_CREDENTIALS, solaceQueueInfo.getSecurityCredentials());
		
		environment.put(SupportedProperty.SOLACE_JMS_VPN, solaceQueueInfo.getSolaceJmsVpn());
		environment.put(SupportedProperty.SOLACE_JMS_SSL_VALIDATE_CERTIFICATE, solaceQueueInfo.getSolaceJmsSslValidateCertificate());
		environment.put(SupportedProperty.SOLACE_JMS_RESPECT_TIME_TO_LIVE, solaceQueueInfo.getSolaceJmsRespectTimeToLIve());
		
		return environment;
	}
	
	private SolTextMessage getSolaceTextMessageObject(String fileData, String jmsCorrelationID, String messageProperties) throws JMSException {
		SolTextMessage message = new SolTextMessage();
		if(StringUtils.isNotBlank(jmsCorrelationID)) {
			message.setJMSCorrelationID(jmsCorrelationID);
		}
		if(StringUtils.isNotBlank(messageProperties)) {
			JSONObject propertiesObject = new JSONObject(messageProperties);
			for(String key: propertiesObject.keySet()) {
				try {
					message.setObjectProperty(key, propertiesObject.get(key));
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		message.setText(fileData);
		
		return message;
	}

}
