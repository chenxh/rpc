package com.chencoder.rpc.core;

import com.chencoder.rpc.common.bean.RpcRequest;

public interface RpcInvoker {
	
	Object invoke(RpcRequest request);
	
}
