package com.chencoder.rpc.common;


public enum ProtoVersion {

    VERSION_1((byte) 1, 16), VERSION_2((byte) 2, 16);// V2为数据包压缩版本

    private byte version;
    private int headerLength;

    ProtoVersion(byte version, int headerLength) {
        this.version = version;
        this.headerLength = headerLength;
    }

    public byte getVersion() {
        return version;
    }

    public int getHeaderLength() {
        return headerLength;
    }

}
