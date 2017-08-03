package com.chencoder.rpc.common.config;

import java.util.List;

import org.apache.curator.shaded.com.google.common.collect.Lists;

import com.chencoder.rpc.common.interceptor.RpcInvokerInterceptor;

public class ClientConfig {
	
    private int connectionTimeout;

    private String loadBalance;
    
    private String registryAddress;
    
    private String remoteIp;
    
    private int remotePort;
    
    private String serializeType;
    
    private String compressType; 
    
    private long timeOut = 0;
    
    private int retry = 0;
    
    private List<RpcInvokerInterceptor> interceptors;

	private ClientConfig(Builder builder) {
		super();
		this.compressType = builder.compressType;
		this.connectionTimeout = builder.connectionTimeout;
		this.loadBalance = builder.loadBalance;
		this.registryAddress = builder.registryAddress;
		this.remoteIp = builder.remoteIp;
		this.remotePort = builder.remotePort;
		this.retry = builder.retry;
		this.serializeType = builder.serializeType;
		this.timeOut = builder.timeOut;
	}

	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
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

	public List<RpcInvokerInterceptor> getInterceptors() {
		return interceptors;
	}

	public void setInterceptors(List<RpcInvokerInterceptor> interceptors) {
		this.interceptors = interceptors;
	}

	public long getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(long timeOut) {
		this.timeOut = timeOut;
	}

	public int getRetry() {
		return retry;
	}

	public void setRetry(int retry) {
		this.retry = retry;
	}
	
	public static class Builder{
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
	    
	    private List<RpcInvokerInterceptor> interceptors = Lists.newArrayList();
	    
	    public  Builder(){
	    	
	    }
	    
	    public Builder withRegistry(String registryAddress){
	    	this.registryAddress = registryAddress;
	    	return this;		
	    }
	    
	    public Builder withDirect(String remoteIp, int remotePort){
	    	this.remoteIp = remoteIp;
	    	this.remotePort = remotePort;
	    	return this;		
	    }


		public Builder connectionTimeout(int connectionTimeout) {
			this.connectionTimeout = connectionTimeout;
			return this;
		}


		public Builder loadBalance(String loadBalance) {
			this.loadBalance = loadBalance;
			return this;
		}

		public Builder serializeType(String serializeType) {
			this.serializeType = serializeType;
			return this;
		}

		public Builder compressType(String compressType) {
			this.compressType = compressType;
			return this;
		}

		public Builder timeOut(long timeOut) {
			this.timeOut = timeOut;
			return this;
		}

		public Builder retryTimes(int retry) {
			this.retry = retry;
			return this;
		}
		
		public Builder addInterceptor(RpcInvokerInterceptor interceptor){
			this.interceptors.add(interceptor);
			return this;
		}
		
		public Builder addInterceptors(List<RpcInvokerInterceptor> interceptors){
			this.interceptors.addAll(interceptors);
			return this;
		}
		
		public ClientConfig build(){
			return new ClientConfig(this);
		}
		
	}

}
