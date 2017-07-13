package com.chencoder.rpc.common.interceptor;

import com.chencoder.rpc.common.bean.RpcRequest;

/**
 */
public interface RpcInvokerInterceptor {

    boolean beforeInvoke(RpcRequest req);

    Object processInvoke(RpcRequest req);

    boolean afterInvoke(RpcRequest req);
}
