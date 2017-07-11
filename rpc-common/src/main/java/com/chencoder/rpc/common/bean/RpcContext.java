package com.chencoder.rpc.common.bean;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;


/**
 * RPC context is a thread local context. 
 * Each rpc invocation bind a context instance to current thread.
 * 
 */
public final class RpcContext {
	
	
	private static final ThreadLocal<RpcContext> THREAD_LOCAL = new ThreadLocal<RpcContext>() {
		@Override
		protected RpcContext initialValue() {
			return new RpcContext();
		}
	};
	
	
	// ~ ------------------------------------------------------------------------------------------------------------
	
	
	private InetSocketAddress   serverAddress;
	private InetSocketAddress   clientAddress;
	private Map<String, String> attachments;
	private long                timout;
	private boolean             oneway;
	private boolean             async;
	private Future<?>           future;
	
	
	public RpcContext() {
		attachments = new HashMap<String, String>();
	}
	
	
	// ~ ------------------------------------------------------------------------------------------------------------
	

	/**
	 * Get rpc context.
	 * 
	 * @return context
	 */
	public static RpcContext getContext() {
	    return THREAD_LOCAL.get();
	}
	
	/**
	 * Remove rpc context.
	 */
	public static void removeContext() {
	    THREAD_LOCAL.remove();
	}
	
	
	// ~ ------------------------------------------------------------------------------------------------------------
	
	
	/**
     * Set attachment.
     * 
     * @param key
     * @param value
     * @return context
     */
    public RpcContext setAttachment(String key, String value) {
    	attachments.put(key, value);
        return this;
    }

    /**
     * Remove attachment.
     * 
     * @param key
     * @return context
     */
    public RpcContext removeAttachment(String key) {
        attachments.remove(key);
        return this;
    }
    
    /**
     * Get attachment.
     * 
     * @param key
     * @return attachment
     */
    public String getAttachment(String key) {
    	return attachments.get(key);
    }
    
    /**
     * Get future object for asynchronous invocation.
     * 
     * @return future instance.
     */
	@SuppressWarnings("unchecked")
	public <T> Future<T> getFuture() {
        return (Future<T>) future;
    }


	public InetSocketAddress getServerAddress() {
		return serverAddress;
	}


	public void setServerAddress(InetSocketAddress serverAddress) {
		this.serverAddress = serverAddress;
	}


	public InetSocketAddress getClientAddress() {
		return clientAddress;
	}


	public void setClientAddress(InetSocketAddress clientAddress) {
		this.clientAddress = clientAddress;
	}


	public long getTimout() {
		return timout;
	}


	public void setTimout(long timout) {
		this.timout = timout;
	}


	public boolean isOneway() {
		return oneway;
	}


	public void setOneway(boolean oneway) {
		this.oneway = oneway;
	}


	public boolean isAsync() {
		return async;
	}


	public void setAsync(boolean async) {
		this.async = async;
	}

}
