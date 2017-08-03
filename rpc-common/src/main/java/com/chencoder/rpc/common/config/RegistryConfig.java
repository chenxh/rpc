package com.chencoder.rpc.common.config;

public class RegistryConfig {
	
	private String registryAddress;

	public String getRegistryAddress() {
		return registryAddress;
	}

	public void setRegistryAddress(String registryAddress) {
		this.registryAddress = registryAddress;
	}
	
	private RegistryConfig(Builder builder){
		super();
		this.registryAddress = builder.registryAddress;
	}
	
	public static class Builder{
		
		private String registryAddress;
		
		public Builder(){
			
		}
		
		public Builder registryAddress(String registryAddress){
			this.registryAddress = registryAddress;
			return this;
		}
		
		public RegistryConfig build(){
			return new RegistryConfig(this);
		}
		
	}
	

}
