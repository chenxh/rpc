package com.chencoder.rpc.core.pool;

import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;

import com.chencoder.rpc.common.bean.ServerInfo;
import com.chencoder.rpc.core.transport.netty.NettyClient;

/**
 */
public class KeyedNettyClientPool extends GenericKeyedObjectPool<ServerInfo, NettyClient> {
    public KeyedNettyClientPool(KeyedPooledObjectFactory<ServerInfo, NettyClient> factory) {
        super(factory);
    }

    public KeyedNettyClientPool(KeyedPooledObjectFactory<ServerInfo, NettyClient> factory, GenericKeyedObjectPoolConfig config) {
        super(factory, config);
    }
}
