package com.saf.jms.mq;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.jms.JMSException;
import javax.jms.QueueConnection;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.TextMessage;
import javax.net.SocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.ibm.mq.jms.MQQueueConnectionFactory;

@Service
public class IbmMqService {
	
	private IbmMqInfo ibmMqInfo;
	
	private SocketFactory socketFactory = null;

	public IbmMqService() {
		
	}
	
	public void init(IbmMqInfo ibmMqInfo, String ksFilePath, String ksPwd, String tsFilePath, String tsPwd) {
		this.ibmMqInfo = ibmMqInfo;

		if(ksFilePath!=null && new File(ksFilePath).exists()) {
			addSystemProperties(ksFilePath, ksPwd, tsFilePath, tsPwd);
			KeyStore keyStore = getKeystore(ksFilePath, ksPwd);
			socketFactory = getSSLSocketFactory(keyStore, ksPwd);
		}
		
		
	}
	
	public boolean sendTextMessage(String queueName, String message, String messageProperties) {
		boolean messagePosted = false;
		QueueConnection queueConnection = QueueConnection();
		
		QueueSession queueSession = null;
		QueueSender queueSender = null;
		TextMessage textMessage = null;
		
		try {
			queueConnection.start();
			queueSession = queueConnection.createQueueSession(false, 1);
			queueSender = queueSession.createSender(queueSession.createQueue(queueName));
			
			textMessage = queueSession.createTextMessage(message);
			queueSender.setDeliveryMode(1);
			
			if(StringUtils.isNotBlank(messageProperties)) {
				JSONObject propertiesObject = new JSONObject(messageProperties);
				for(String key: propertiesObject.keySet()) {
					if("MsgCorrelationID".equalsIgnoreCase(key)) {
						textMessage.setJMSCorrelationID(propertiesObject.get(key).toString());
					}else {
						textMessage.setStringProperty(key, propertiesObject.get(key).toString());
					}
				}
			}
			if(StringUtils.isBlank(textMessage.getJMSCorrelationID())) {
				textMessage.setJMSCorrelationID(getJMSCorrelationID());
			}
			queueSender.send(textMessage);
			messagePosted = true;
			System.out.println("Message ["+textMessage+"] Sent to Queue ["+queueName+"]");
		} catch (JMSException e) {
			e.printStackTrace();
		}finally {
			try {
				queueConnection.close();
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
		return messagePosted;
	}
	
	private QueueConnection QueueConnection() {
		QueueConnection queueConnection = null;
		
		MQQueueConnectionFactory mqQueueConnectionFactory = new MQQueueConnectionFactory();
		try {
			mqQueueConnectionFactory.setHostName(ibmMqInfo.getHostName());
			mqQueueConnectionFactory.setPort(Integer.parseInt(ibmMqInfo.getPortNumber()));
			mqQueueConnectionFactory.setTransportType(1);
			mqQueueConnectionFactory.setQueueManager(ibmMqInfo.getQueueManager());
			mqQueueConnectionFactory.setChannel(ibmMqInfo.getChannel());
			mqQueueConnectionFactory.setSSLCipherSuite(ibmMqInfo.getSslCipherSuite());
			if(socketFactory!=null) {
				mqQueueConnectionFactory.setSSLSocketFactory(socketFactory);
			}
			queueConnection = mqQueueConnectionFactory.createQueueConnection();
			return queueConnection;
		} catch (NumberFormatException | JMSException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	
	private void addSystemProperties(String ksFilePath, String ksPwd, String tsFilePath, String tsPwd) {
		if(ksFilePath!=null && new File(ksFilePath).exists()) {
			System.setProperty("javax.net.ssl.keyStore", ksFilePath);
			System.setProperty("javax.net.ssl.keyStorePassword", ksPwd);
		}
		if(tsFilePath!=null && new File(tsFilePath).exists()) {
			System.setProperty("javax.net.ssl.trustStore", tsFilePath);
			System.setProperty("javax.net.ssl.trustStorePassword", tsPwd);
		}
		System.setProperty("com.ibm.mq.cfg.useIBMCipherMappings", ibmMqInfo.getUseIBMCipherMappings());
		
	}
	
	private SSLSocketFactory getSSLSocketFactory(KeyStore keyStore, String ksPwd) {
		try {
			char[] ksPwdArr = toCharArray(ksPwd);
			KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			keyManagerFactory.init(keyStore, ksPwdArr);
			SSLContext sslContext = SSLContext.getInstance("SSLv3");
			sslContext.init(keyManagerFactory.getKeyManagers(), null, null);
			return sslContext.getSocketFactory();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private KeyStore getKeystore(String ksFilePath, String ksPwd) {
		
		KeyStore keyStore = null;
		InputStream is = null;
		try {
			if(ksFilePath!=null) {
				File file = new File(ksFilePath);
				if(file.exists()) {
					keyStore = KeyStore.getInstance("JKS");
					char[] ksPwdArr = toCharArray(ksPwd);
					is = new BufferedInputStream(new FileInputStream(file));
					keyStore.load(is, ksPwdArr);
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(is!=null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return keyStore;
		
	}
	
	private char[] toCharArray(String inputValue) {
		if(inputValue!=null) {
			return inputValue.toCharArray();
		}
		return null;
	}
	
	private String getJMSCorrelationID() {
		String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		String[] timestampArray = timestamp.split("");
		int totalSpaces = 24 - timestampArray.length;
		StringBuffer sb = new StringBuffer();
		sb.append("ID:");
		
		for(int i = 0; i < timestampArray.length; i++) {
			sb.append("f"+timestampArray[i]);
		}
		
		for(int i = 0; i < totalSpaces; i++) {
			sb.append("40");
		}
		
		return sb.toString();
	}
	
	
	
}
