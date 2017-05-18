package com.chencoder.rpc.core.transport.client;

import com.chencoder.rpc.common.Constants;
import com.chencoder.rpc.common.EventType;
import com.chencoder.rpc.common.bean.Header;
import com.chencoder.rpc.common.bean.Message;
import com.chencoder.rpc.common.bean.Response;
import com.chencoder.rpc.core.transport.ResponseFuture;
import com.google.common.collect.Maps;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;


public class Connection implements Closeable {
    private final static Logger LOGGER = LoggerFactory.getLogger(Connection.class);
    public final static Map<Long, ResponseFuture<Response>> callbackMap = Maps.newConcurrentMap();
    private ChannelFuture future;
    private AtomicBoolean isConnected = new AtomicBoolean();
    private final static AtomicLong id = new AtomicLong(0);

    public Connection() {
        this.isConnected.set(false);
        this.future = null;
    }

    public ChannelFuture getFuture() {
        return future;
    }

    public void setFuture(ChannelFuture future) {
        this.future = future;
    }

    public boolean isConnected() {
        return isConnected.get();
    }

    public void setIsConnected(boolean isConnected) {
        this.isConnected.set(isConnected);
    }

    public ResponseFuture<Response> request(Message message, long timeOut) {
        if (!isConnected()) {
            throw new RuntimeException("client is not connected");
        }
        message.getHeader().setMessageID(id.incrementAndGet());
        ResponseFuture responseFuture = new ResponseFuture(System.currentTimeMillis(), timeOut, message, new Promise());
        registerCallbackMap(message.getHeader().getMessageID(), responseFuture);
        try {
            future.channel().writeAndFlush(message);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            removeCallbackMap(message.getHeader().getMessageID());
        }
        return responseFuture;
    }

    public boolean ping() {
        Header heartBeatHeader = Header.HeaderMaker.newMaker().make();
        heartBeatHeader.setExtend(EventType.HEARTBEAT.getValue());
        Message message = new Message(heartBeatHeader, null);
        ResponseFuture<Response> request = request(message, Constants.DEFAULT_TIMEOUT);
        try {
            return request.getPromise().await().getCode() == Constants.DEF_PING_CODE;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return false;
        }
    }

    public ResponseFuture registerCallbackMap(Long messageId, ResponseFuture<Response> responseFuture) {
        return callbackMap.put(messageId, responseFuture);
    }

    public ResponseFuture removeCallbackMap(Long messageId) {
        return callbackMap.remove(messageId);
    }

    @Override
    public void close() throws IOException {
        future.channel().close();
    }
}
