package com.chencoder.rpc.common.config;

public class ServerConfig {
	
	private Integer soBacklog;
	private Boolean soKeepAlive;
	private Boolean tcpNoDelay;
	
	private Integer port;
	
	private ServiceConfig serviceConfig;
	
	public Integer getSoBacklog() {
		return soBacklog;
	}
	public void setSoBacklog(Integer soBacklog) {
		this.soBacklog = soBacklog;
	}
	public Boolean getSoKeepAlive() {
		return soKeepAlive;
	}
	public void setSoKeepAlive(Boolean soKeepAlive) {
		this.soKeepAlive = soKeepAlive;
	}
	public Boolean getTcpNoDelay() {
		return tcpNoDelay;
	}
	public void setTcpNoDelay(Boolean tcpNoDelay) {
		this.tcpNoDelay = tcpNoDelay;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public ServiceConfig getServiceConfig() {
		return serviceConfig;
	}
	public void setServiceConfig(ServiceConfig serviceConfig) {
		this.serviceConfig = serviceConfig;
	}

}
