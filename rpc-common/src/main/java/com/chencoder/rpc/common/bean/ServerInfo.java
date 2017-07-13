package com.chencoder.rpc.common.bean;

import org.apache.curator.x.discovery.ServiceInstance;

import java.util.concurrent.atomic.AtomicInteger;

public class ServerInfo {

    private String host;

    private int port;

    private AtomicInteger activeCount = new AtomicInteger(0);

    public ServerInfo(ServiceInstance<MetaInfo> instance) {
        this.host = instance.getAddress();
        this.port = instance.getPort();
    }
    
    public ServerInfo(String host, int port){
    	setHost(host);
    	setPort(port);
    }

    public ServerInfo() {
    	
	}

	public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }


    public int activeCountIncrementAndGet() {
        return activeCount.incrementAndGet();
    }

    public int activeCountGet() {
        return activeCount.incrementAndGet();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((host == null) ? 0 : host.hashCode());
        result = prime * result + port;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ServerInfo other = (ServerInfo) obj;
        if (host == null) {
            if (other.host != null)
                return false;
        } else if (!host.equals(other.host))
            return false;
        if (port != other.port)
            return false;
        return true;
    }
}
