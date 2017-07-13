package com.chencoder.rpc.common.compress;

import java.io.IOException;

public interface Compress {

    byte[] compress(byte[] array) throws IOException;


    byte[] unCompress(byte[] array) throws IOException;
}
