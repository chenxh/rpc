package com.chencoder.rpc.common.config;

import java.util.List;

import com.chencoder.rpc.common.interceptor.InvokerInterceptor;

public class ClientConfig {
	
	private String serviceName;

    private int connectionTimeout;

    private String ha;

    private String loadBalance;
    
    private String registryAddress;
    
    private String remoteIp;
    
    private int remotePort;
    
    private String serializeType;
    
    private String compressType; 
    
    private long timeOut = 0;
    
    private int retry = 0;
    
    private List<InvokerInterceptor> interceptors;

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public String getHa() {
		return ha;
	}

	public void setHa(String ha) {
		this.ha = ha;
	}

	public String getLoadBalance() {
		return loadBalance;
	}

	public void setLoadBalance(String loadBalance) {
		this.loadBalance = loadBalance;
	}

	public String getRegistryAddress() {
		return registryAddress;
	}

	public void setRegistryAddress(String registryAddress) {
		this.registryAddress = registryAddress;
	}

	public String getRemoteIp() {
		return remoteIp;
	}

	public void setRemoteIp(String remoteIp) {
		this.remoteIp = remoteIp;
	}

	public int getRemotePort() {
		return remotePort;
	}

	public void setRemotePort(int remotePort) {
		this.remotePort = remotePort;
	}

	public String getCompressType() {
		return compressType;
	}

	public void setCompressType(String compressType) {
		this.compressType = compressType;
	}

	public String getSerializeType() {
		return serializeType;
	}

	public void setSerializeType(String serializeType) {
		this.serializeType = serializeType;
	}

	public List<InvokerInterceptor> getInterceptors() {
		return interceptors;
	}

	public void setInterceptors(List<InvokerInterceptor> interceptors) {
		this.interceptors = interceptors;
	}

	public long getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(long timeOut) {
		this.timeOut = timeOut;
	}

	public RpcRuntimeConfig newRuntimeConfig() {
		return new RpcRuntimeConfig(this.timeOut, this.retry) ;
	}


}
