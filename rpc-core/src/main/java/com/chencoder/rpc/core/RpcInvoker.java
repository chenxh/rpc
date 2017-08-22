package com.chencoder.rpc.core;

import java.io.Closeable;

import com.chencoder.rpc.common.bean.RpcRequest;

public interface RpcInvoker extends Closeable{

	Object invoke(RpcRequest request);
	
}
