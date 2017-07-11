package com.chencoder.rpc.common.bean;

public class RpcRequest  extends RpcMessage{
	
	private Request request;
	
	
	public RpcRequest(Header header,Request request) {
		super(header);
		this.request = request;
	}
	
	public RpcRequest(Header header, long messageId, Request request) {
		super(header, messageId);
		this.request = request;
	}

	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	@Override
	public Object getContent() {
		return request;
	}
	
	
}
