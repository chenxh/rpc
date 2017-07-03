package com.chencoder.rpc.common.bean;

import java.util.concurrent.atomic.AtomicLong;

/**
 */
public class Message<T> {

    private Header header;

    private T content;
    
    private final static AtomicLong id = new AtomicLong(0);

    public Message() {
    }

    public Message(Header header, T content) {
        this(header,content, id.getAndIncrement());
    }
    
    public Message(Header header, T content, long messageId) {
        this.content = content;
        this.header = header;
        this.header.setMessageID(messageId);
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }
}
