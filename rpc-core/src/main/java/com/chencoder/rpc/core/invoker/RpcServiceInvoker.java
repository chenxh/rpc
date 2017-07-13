package com.chencoder.rpc.core.invoker;

import java.lang.reflect.Method;

import com.chencoder.rpc.common.bean.RpcException;
import com.chencoder.rpc.common.bean.RpcRequest;
import com.chencoder.rpc.core.RpcInvoker;

public class RpcServiceInvoker implements RpcInvoker{
	
	private Object target;
	private Method method;
	
	public RpcServiceInvoker(Object target, Method method){
		this.target = target;
		this.method = method;
	}

	@Override
	public Object invoke(RpcRequest request) {
		try {
			return method.invoke(target, request.getRequest().getArgs());
		} catch (Exception e) {
			throw new RpcException(e);
		}
	}
	
}
	
	
