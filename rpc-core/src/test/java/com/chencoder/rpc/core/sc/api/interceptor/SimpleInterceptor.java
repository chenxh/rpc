package com.chencoder.rpc.core.sc.api.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chencoder.rpc.common.bean.RpcRequest;
import com.chencoder.rpc.common.interceptor.AbstractInvokerInterceptor;

public class SimpleInterceptor extends AbstractInvokerInterceptor{
	
	private static final Logger logger = LoggerFactory.getLogger(SimpleInterceptor.class);

	@Override
	public boolean beforeInvoke(RpcRequest req) {
		//logger.debug("before request:");
		System.out.println("before request:");
		return true;
	}

	@Override
	public Object processInvoke(RpcRequest req) {
		//System.out.println("proccess request:" + req);
		return null;
	}

	@Override
	public boolean afterInvoke(RpcRequest req) {
		//System.out.println("after request:" + req);
		return true;
	}

}
