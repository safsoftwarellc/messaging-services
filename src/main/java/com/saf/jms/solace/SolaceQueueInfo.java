package com.saf.jms.solace;

public class SolaceQueueInfo {
	
	private String initialContextFactory;
	private String providerUrl;
	private String securityPrincipal;
	private String securityCredentials;
	private String solaceJmsVpn;
	private String solaceJmsSslValidateCertificate;
	private String solaceJmsRespectTimeToLIve;
	private String connectionFactory;
	private String solDestination;
	
	
	public SolaceQueueInfo() {
		
	}
	
	
	public SolaceQueueInfo(String initialContextFactory, String providerUrl, String securityPrincipal,
			String securityCredentials, String solaceJmsVpn, String solaceJmsSslValidateCertificate,
			String solaceJmsRespectTimeToLIve, String connectionFactory, String solDestination) {
		super();
		this.initialContextFactory = initialContextFactory;
		this.providerUrl = providerUrl;
		this.securityPrincipal = securityPrincipal;
		this.securityCredentials = securityCredentials;
		this.solaceJmsVpn = solaceJmsVpn;
		this.solaceJmsSslValidateCertificate = solaceJmsSslValidateCertificate;
		this.solaceJmsRespectTimeToLIve = solaceJmsRespectTimeToLIve;
		this.connectionFactory = connectionFactory;
		this.solDestination = solDestination;
	}
	public String getInitialContextFactory() {
		return initialContextFactory;
	}
	public void setInitialContextFactory(String initialContextFactory) {
		this.initialContextFactory = initialContextFactory;
	}
	public String getProviderUrl() {
		return providerUrl;
	}
	public void setProviderUrl(String providerUrl) {
		this.providerUrl = providerUrl;
	}
	public String getSecurityPrincipal() {
		return securityPrincipal;
	}
	public void setSecurityPrincipal(String securityPrincipal) {
		this.securityPrincipal = securityPrincipal;
	}
	public String getSecurityCredentials() {
		return securityCredentials;
	}
	public void setSecurityCredentials(String securityCredentials) {
		this.securityCredentials = securityCredentials;
	}
	public String getSolaceJmsVpn() {
		return solaceJmsVpn;
	}
	public void setSolaceJmsVpn(String solaceJmsVpn) {
		this.solaceJmsVpn = solaceJmsVpn;
	}
	public String getSolaceJmsSslValidateCertificate() {
		return solaceJmsSslValidateCertificate;
	}
	public void setSolaceJmsSslValidateCertificate(String solaceJmsSslValidateCertificate) {
		this.solaceJmsSslValidateCertificate = solaceJmsSslValidateCertificate;
	}
	public String getSolaceJmsRespectTimeToLIve() {
		return solaceJmsRespectTimeToLIve;
	}
	public void setSolaceJmsRespectTimeToLIve(String solaceJmsRespectTimeToLIve) {
		this.solaceJmsRespectTimeToLIve = solaceJmsRespectTimeToLIve;
	}
	public String getConnectionFactory() {
		return connectionFactory;
	}
	public void setConnectionFactory(String connectionFactory) {
		this.connectionFactory = connectionFactory;
	}
	public String getSolDestination() {
		return solDestination;
	}
	public void setSolDestination(String solDestination) {
		this.solDestination = solDestination;
	}
	
	
	

}
