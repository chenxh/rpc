package com.chencoder.rpc.common.config;

import java.util.List;

import com.google.common.collect.Lists;

public class ServerConfig {
	
	private Integer soBacklog;
	private Boolean soKeepAlive;
	private Boolean tcpNoDelay;
	
	private Integer port;
	
	private List<ServiceConfig> serviceConfigs;
	
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
	public void addServiceConfig(ServiceConfig config) {
		if(serviceConfigs == null){
			serviceConfigs = Lists.newArrayList();
		}
		serviceConfigs.add(config);
	}

}
