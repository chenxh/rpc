package com.chencoder.rpc.common;

/**
 */
public enum EventType {

    NORMAL((byte) 0), HEARTBEAT((byte) (1 << 6));

    private byte value;

    EventType(byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }

    public static boolean typeofHeartBeat(byte extend) {
        if ((extend & HEARTBEAT.getValue()) == HEARTBEAT.getValue()) {
            return true;
        }
        return false;
    }

}
