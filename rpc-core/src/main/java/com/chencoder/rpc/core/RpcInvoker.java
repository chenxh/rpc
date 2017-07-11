package com.chencoder.rpc.core;

import com.chencoder.rpc.common.bean.RpcMessage;

public interface RpcInvoker {
	
	Object invoke(RpcMessage message);
	
}
