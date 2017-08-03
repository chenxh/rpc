package com.chencoder.rpc.common.config;

import java.util.List;

import com.chencoder.rpc.common.interceptor.RpcInvokerInterceptor;
import com.google.common.collect.Lists;

public class ServerConfig {
	
	private Integer soBacklog;
	private Boolean soKeepAlive;
	private Boolean tcpNoDelay;
	
	private Integer port;
	
	private List<RpcInvokerInterceptor> interceptors;
	
	private ServerConfig(Builder builder) {
		this.interceptors = builder.interceptors;
		this.port = builder.port;
		this.soBacklog = builder.soBacklog;
		this.soKeepAlive = builder.soKeepAlive;
		this.tcpNoDelay = builder.tcpNoDelay;
	}
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
	public List<RpcInvokerInterceptor> getInterceptors() {
		return interceptors;
	}
	public void setInterceptors(List<RpcInvokerInterceptor> interceptors) {
		this.interceptors = interceptors;
	}
	
	public static class Builder{
		
		private Integer soBacklog = 128;
		
		private Boolean soKeepAlive = true;
		
		private Boolean tcpNoDelay = true;
		
		private Integer port;
		
		private List<RpcInvokerInterceptor> interceptors = Lists.newArrayList();
		
		public Builder(Integer port){
			this.port = port;
		}
		
		public Builder tcpNoDelay(Boolean tcpNoDelay){
			this.tcpNoDelay = tcpNoDelay;
			return this;
		}
		
		public Builder soBacklog(Integer soBacklog){
			this.soBacklog = soBacklog;
			return this;
		}
		
		public Builder soKeepAlive(Boolean soKeepAlive){
			this.soKeepAlive = soKeepAlive;
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
		
		public ServerConfig build(){
			return new ServerConfig(this);
		}
		
	}

}
