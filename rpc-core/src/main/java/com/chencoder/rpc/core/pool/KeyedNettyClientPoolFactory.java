package com.chencoder.rpc.core.pool;

import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chencoder.rpc.common.bean.ServerInfo;
import com.chencoder.rpc.core.transport.netty.NettyClient;

/**
 * Created by Dempe on 2016/12/20.
 */
public class KeyedNettyClientPoolFactory implements KeyedPooledObjectFactory<ServerInfo, NettyClient> {

    private final static Logger LOGGER = LoggerFactory.getLogger(KeyedNettyClientPoolFactory.class);

    @Override
    public PooledObject<NettyClient> makeObject(ServerInfo key) throws Exception {
        NettyClient client = new NettyClient(key);
        client.connect();
        return new DefaultPooledObject<>(client);
    }

    @Override
    public void destroyObject(ServerInfo key, PooledObject<NettyClient> p) throws Exception {
        p.getObject().close();
    }

    @Override
    public boolean validateObject(ServerInfo key, PooledObject<NettyClient> p) {
        return p.getObject().isActive();
    }

    @Override
    public void activateObject(ServerInfo key, PooledObject<NettyClient> p) throws Exception {

    }

    @Override
    public void passivateObject(ServerInfo key, PooledObject<NettyClient> p) throws Exception {

    }
}
