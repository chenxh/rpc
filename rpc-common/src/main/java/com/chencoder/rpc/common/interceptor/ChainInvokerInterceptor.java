package com.chencoder.rpc.common.interceptor;

import java.util.Iterator;
import java.util.List;

import com.chencoder.rpc.common.bean.RpcRequest;
import com.google.common.collect.Lists;

/**
 */
public class ChainInvokerInterceptor implements RpcInvokerInterceptor {

    private List<RpcInvokerInterceptor> interceptors = Lists.newArrayList();

    public ChainInvokerInterceptor(List<RpcInvokerInterceptor> interceptors2) {
    	this.interceptors = interceptors2;
    }

	@Override
    public boolean beforeInvoke(RpcRequest request) {
        Iterator<RpcInvokerInterceptor> iterator = interceptors.iterator();
        boolean noInterrupt = true;
        while (iterator.hasNext() && noInterrupt) {
            noInterrupt = iterator.next().beforeInvoke(request);
        }
        return noInterrupt;
    }

    @Override
    public Object processInvoke(RpcRequest request) {
        Object result = null;
        for (RpcInvokerInterceptor interceptor : interceptors) {
            Object ret = interceptor.processInvoke(request);
            if (ret != null) {
                result = ret;
            }
        }
        return result;
    }

    @Override
    public boolean afterInvoke(RpcRequest request) {
        Iterator<RpcInvokerInterceptor> iterator = interceptors.iterator();
        boolean noInterrupt = true;
        while (iterator.hasNext() && noInterrupt) {
            noInterrupt = iterator.next().afterInvoke(request);
        }
        return noInterrupt;
    }

    public boolean addInvokerInterceptor(RpcInvokerInterceptor invokerInterceptor) {
        return interceptors.add(invokerInterceptor);
    }

    public void addInvokerInterceptor(int index, RpcInvokerInterceptor invokerInterceptor) {
        interceptors.add(index, invokerInterceptor);
    }
}
