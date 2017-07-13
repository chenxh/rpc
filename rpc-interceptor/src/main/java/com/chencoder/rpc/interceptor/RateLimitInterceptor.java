package com.chencoder.rpc.interceptor;

import com.chencoder.rpc.common.bean.RpcRequest;
import com.chencoder.rpc.common.interceptor.AbstractInvokerInterceptor;
import com.google.common.util.concurrent.RateLimiter;

public class RateLimitInterceptor extends AbstractInvokerInterceptor{
	
	final RateLimiter rateLimiter;

	
	public RateLimitInterceptor(int taskNum){
		rateLimiter = RateLimiter.create(taskNum);
	}

	@Override
	public boolean afterInvoke(RpcRequest req) {
		rateLimiter.acquire();
		return true;
	}

}
