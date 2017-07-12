package com.chencoder.rpc.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.chencoder.rpc.common.MessageType;
import com.chencoder.rpc.common.bean.Request;
import com.chencoder.rpc.common.bean.Response;
import com.chencoder.rpc.common.bean.RpcRequest;
import com.chencoder.rpc.common.bean.RpcResponse;
import com.chencoder.rpc.common.config.ActionMethod;
import com.chencoder.rpc.core.provider.DefaultServiceProvider;
import com.google.common.base.Preconditions;

public class DefaultRpcProcessor implements RpcProcessor{
	
	private Map<String, DefaultServiceProvider> providers = new ConcurrentHashMap<>();
	
	public DefaultRpcProcessor(){
		
	}

	@Override
	public RpcResponse proccess(RpcRequest request) {
		Request req = request.getRequest();
		DefaultServiceProvider provider = providers.get(req.getServiceName());
		Preconditions.checkNotNull(provider);
		
		ActionMethod actionMethod = provider.findActionMethod(req);
		Preconditions.checkNotNull(actionMethod);
		Object result;
		try {
			result = actionMethod.call(req.getArgs());
			Response response = new Response();
			byte extend = (byte) (request.getHeader().getExtend() | MessageType.RESPONSE_MESSAGE_TYPE);
			request.getHeader().setExtend(extend);
		    response.setResult(result);
			return new RpcResponse(request.getHeader(), request.getHeader().getMessageID(), response);
		} catch(Throwable e){
			Response response = new Response();
			response.setResult(e);
			return new RpcResponse(request.getHeader(), request.getHeader().getMessageID(), response);
		}
	}
	
	public void addServiceProvider(Class<?> interfaceClass, Object impl){
		providers.put(interfaceClass.getName(), new DefaultServiceProvider(interfaceClass, impl));
	}

}
