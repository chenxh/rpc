package com.chencoder.rpc.common.bean;

import java.util.concurrent.atomic.AtomicLong;

/**
 */
public class RpcMessage {

    private Header header;
    
    private final static AtomicLong id = new AtomicLong(0);

    public RpcMessage() {
    }

    public RpcMessage(Header header) {
        this(header, id.getAndIncrement());
    }
    
    public RpcMessage(Header header, long messageId) {
        this.header = header;
        this.header.setMessageID(messageId);
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }
    
    public Object getContent(){
    	return null;
    }
}
