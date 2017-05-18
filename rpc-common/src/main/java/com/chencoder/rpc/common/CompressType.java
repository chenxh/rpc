package com.chencoder.rpc.common;

import org.apache.commons.lang3.StringUtils;

import com.chencoder.rpc.common.compress.Compress;
import com.chencoder.rpc.common.compress.GZipCompress;
import com.chencoder.rpc.common.compress.NoCompress;
import com.chencoder.rpc.common.compress.SnappyCompress;

public enum CompressType {

    None((byte) 0), GZIP((byte) (1 << 3)), Snappy((byte) (1 << 4));

    private byte value;

    CompressType(byte value) {
        this.value = value;
    }

    public static CompressType getCompressTypeByName(String name) {
        if (StringUtils.equals(GZIP.name(), name)) {
            return GZIP;
        } else if (StringUtils.equals(Snappy.name(), name)) {
            return Snappy;
        } else if (StringUtils.equals(None.name(), name)) {
            return None;
        }
        return null;
    }

    public static Compress getCompressTypeByValueByExtend(byte extend) {
        switch (extend & 24) {
            case 0x0:
                return new NoCompress();
            case 1 << 3:
                return new GZipCompress();
            case 1 << 4:
                return new SnappyCompress();
            default:
                return new NoCompress();
        }
    }

    public byte getValue() {
        return value;
    }

    public final static CompressType DEFAULT_COMPRESS_TYPE = CompressType.None;

}
