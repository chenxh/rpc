package com.chencoder.rpc.common.compress;


import org.xerial.snappy.Snappy;

import java.io.IOException;

public class SnappyCompress implements Compress {

    public byte[] compress(byte[] array) throws IOException {
        if (array == null) {
            return null;
        }
        return Snappy.compress(array);
    }

    public byte[] unCompress(byte[] array) throws IOException {
        if (array == null) {
            return null;
        }
        return Snappy.uncompress(array);
    }
}
