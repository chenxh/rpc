package com.chencoder.rpc.common.bean;

public class RpcResponse extends RpcMessage{
	
	private Response response;
	
	public RpcResponse(Header header,Response response) {
		super(header);
		this.response = response;
	}

	public RpcResponse(Header header, long messageId,Response response) {
		super(header,messageId);
		this.response = response;
	}

	@Override
	public Object getContent() {
		return response;
	}

	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}

}
