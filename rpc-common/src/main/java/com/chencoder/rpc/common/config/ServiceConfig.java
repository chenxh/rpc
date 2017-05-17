package com.chencoder.rpc.common.config;

import java.util.Map;

import com.chencoder.rpc.common.cluster.ha.HaStrategy;
import com.chencoder.rpc.common.cluster.lb.LoadBalance;
import com.google.common.collect.Maps;

/**
 * Created by Dempe on 2016/12/7.
 */
public class ServiceConfig {

    private String serviceName;

    private int connectionTimeout;

    private HaStrategy haStrategyType;

    private LoadBalance loadBalance;

    private Map<String, MethodConfig> methodConfigMap = Maps.newConcurrentMap();

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Map<String, MethodConfig> getMethodConfigMap() {
        return methodConfigMap;
    }

    public void setMethodConfigMap(Map<String, MethodConfig> methodConfigMap) {
        this.methodConfigMap = methodConfigMap;
    }

    public MethodConfig getMethodConfig(String methodName) {
        return methodConfigMap.get(methodName);
    }

    public MethodConfig registerMethodConfig(String methodName, MethodConfig methodConfig) {
        return methodConfigMap.put(methodName, methodConfig);
    }

   /* public HaStrategyType getHaStrategyType() {
        return haStrategyType;
    }

    public void setHaStrategyType(HaStrategyType haStrategyType) {
        this.haStrategyType = haStrategyType;
    }

    public LoadBalanceType getLoadBalanceType() {
        return loadBalanceType;
    }

    public void setLoadBalanceType(LoadBalanceType loadBalanceType) {
        this.loadBalanceType = loadBalanceType;
    }*/

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public static class Builder {
        private ServiceConfig config;

        public static Builder newBuilder() {
            Builder builder = new Builder();
            builder.config = new ServiceConfig();
            return builder;
        }

        public ServiceConfig build() {
            return config;
        }

        public Builder withConnectionTimeout(int connectionTimeout) {
            config.setConnectionTimeout(connectionTimeout);
            return this;
        }
       /* public Builder withHaStrategyType(HaStrategyType type){
            config.setHaStrategyType(type);
            return this;
        }
        public Builder withLoadBalanceType(LoadBalanceType type){
            config.setLoadBalanceType(type);
            return this;
        }*/
        public Builder withMethodConfig(String methodName, MethodConfig methodConfig) {
            config.registerMethodConfig(methodName, methodConfig);
            return this;
        }

    }
}
