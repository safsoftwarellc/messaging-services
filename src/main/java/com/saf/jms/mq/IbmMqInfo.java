package com.saf.jms.mq;

public class IbmMqInfo {
	
	private String hostName;
	private String portNumber;
	private String queueManager;
	private String channel;
	private String sslCipherSuite;
	private String useIBMCipherMappings;


	public IbmMqInfo() {
		
	}	
	
	public IbmMqInfo(String hostName, String portNumber, String queueManager, String channel, String sslCipherSuite, String useIBMCipherMappings) {
		super();
		this.hostName = hostName;
		this.portNumber = portNumber;
		this.queueManager = queueManager;
		this.channel = channel;
		this.sslCipherSuite = sslCipherSuite;
		this.useIBMCipherMappings = useIBMCipherMappings;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getPortNumber() {
		return portNumber;
	}
	public void setPortNumber(String portNumber) {
		this.portNumber = portNumber;
	}
	public String getQueueManager() {
		return queueManager;
	}
	public void setQueueManager(String queueManager) {
		this.queueManager = queueManager;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getSslCipherSuite() {
		return sslCipherSuite;
	}
	public void setSslCipherSuite(String sslCipherSuite) {
		this.sslCipherSuite = sslCipherSuite;
	}
	public String getUseIBMCipherMappings() {
		return useIBMCipherMappings;
	}
	public void setUseIBMCipherMappings(String useIBMCipherMappings) {
		this.useIBMCipherMappings = useIBMCipherMappings;
	}

	

}
