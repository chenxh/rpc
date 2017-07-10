package com.chencoder.rpc.common.config;

public class RpcRuntimeConfig {
	
	private long timeOut = 0;
	
	private int retry = 0;
	
	public RpcRuntimeConfig(){
		
	}
	public RpcRuntimeConfig(long timeOut, int retry){
		setRetry(retry);
		setTimeOut(timeOut);
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
	
	

}
