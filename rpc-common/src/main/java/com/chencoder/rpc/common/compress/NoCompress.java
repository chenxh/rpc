package com.chencoder.rpc.common.compress;

import java.io.IOException;

public class NoCompress implements Compress {

    @Override
    public byte[] compress(byte[] array) throws IOException {
        return array;
    }

    @Override
    public byte[] unCompress(byte[] array) throws IOException {
        return array;
    }
}
