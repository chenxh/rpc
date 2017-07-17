package com.chencoder.rpc.core.invoker;

import java.io.IOException;
import java.util.List;

import com.chencoder.rpc.common.bean.RpcRequest;
import com.chencoder.rpc.common.interceptor.ChainInvokerInterceptor;
import com.chencoder.rpc.common.interceptor.RpcInvokerInterceptor;
import com.chencoder.rpc.core.RpcInvoker;

public class RpcInvokerWraper implements RpcInvoker {

	private RpcInvoker clientInvoker;

	private ChainInvokerInterceptor interceptor;
	
	public RpcInvokerWraper(RpcInvoker invoker){
		this(invoker, null);
	}

	public RpcInvokerWraper(RpcInvoker clientInvoker, List<RpcInvokerInterceptor> interceptors) {
		this.clientInvoker = clientInvoker;
		if (interceptors != null && interceptors.size() > 0) {
			this.interceptor = new ChainInvokerInterceptor(interceptors);
		}
	}

	@Override
	public Object invoke(RpcRequest request) {
		if (interceptor == null) {
			return clientInvoker.invoke(request);
		}
		interceptor.beforeInvoke(request);
		Object result = interceptor.processInvoke(request);
		if (result == null) {
			result = clientInvoker.invoke(request);
		}
		interceptor.afterInvoke(request);
		return result;
	}

	@Override
	public void close() throws IOException {
		clientInvoker.close();
	}

}
