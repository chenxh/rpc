package com.chencoder.rpc.core.sc.api.interceptor;

import com.chencoder.rpc.common.bean.RpcRequest;
import com.chencoder.rpc.common.interceptor.AbstractInvokerInterceptor;

public class SimpleInterceptor extends AbstractInvokerInterceptor{

	@Override
	public boolean beforeInvoke(RpcRequest req) {
		System.out.println("before request:" + req);
		return true;
	}

	@Override
	public Object processInvoke(RpcRequest req) {
		System.out.println("proccess request:" + req);
		return null;
	}

	@Override
	public boolean afterInvoke(RpcRequest req) {
		System.out.println("after request:" + req);
		return true;
	}

}
