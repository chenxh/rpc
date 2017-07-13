package com.chencoder.rpc.common.interceptor;

import com.chencoder.rpc.common.bean.RpcRequest;

/**
 */
public class AbstractInvokerInterceptor implements RpcInvokerInterceptor {

	@Override
	public boolean beforeInvoke(RpcRequest req) {
		return true;
	}

	@Override
	public Object processInvoke(RpcRequest req) {
		return null;
	}

	@Override
	public boolean afterInvoke(RpcRequest req) {
		return true;
	}
	
}
