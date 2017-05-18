package com.chencoder.rpc.common.bean;

import org.codehaus.jackson.map.annotate.JsonRootName;

import com.chencoder.rpc.common.HaStrategyType;
import com.chencoder.rpc.common.LoadBalanceType;

@JsonRootName("metaInfo") 
public class MetaInfo {

   private HaStrategyType haStrategyType;

   private LoadBalanceType loadBalanceType;

   private int avgTime;

   private int total;

   private int successCount;

   private int maxTime;

   private int minTime;

   public HaStrategyType getHaStrategyType() {
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
   }

   public int getAvgTime() {
       return avgTime;
   }

   public void setAvgTime(int avgTime) {
       this.avgTime = avgTime;
   }

   public int getTotal() {
       return total;
   }

   public void setTotal(int total) {
       this.total = total;
   }

   public int getSuccessCount() {
       return successCount;
   }

   public void setSuccessCount(int successCount) {
       this.successCount = successCount;
   }

   public int getMaxTime() {
       return maxTime;
   }

   public void setMaxTime(int maxTime) {
       this.maxTime = maxTime;
   }

   public int getMinTime() {
       return minTime;
   }

   public void setMinTime(int minTime) {
       this.minTime = minTime;
   }

   @Override
   public String toString() {
       return "MetaInfo{" +
               "haStrategyType=" + haStrategyType +
               ", loadBalanceType=" + loadBalanceType +
               ", avgTime=" + avgTime +
               ", total=" + total +
               ", successCount=" + successCount +
               ", maxTime=" + maxTime +
               ", minTime=" + minTime +
               '}';
   }
}
