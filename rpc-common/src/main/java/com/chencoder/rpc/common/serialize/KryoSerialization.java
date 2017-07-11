package com.chencoder.rpc.common.serialize;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.FastInput;
import com.esotericsoftware.kryo.io.FastOutput;
import com.esotericsoftware.kryo.io.KryoObjectInput;
import com.esotericsoftware.kryo.io.KryoObjectOutput;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class KryoSerialization implements Serialization {

	public final static ThreadLocal<Kryo> kryoThreadMap = new ThreadLocal<Kryo>();

    public KryoSerialization() {
        kryoThreadMap.set(new Kryo());
    }

    @Override
    public byte[] serialize(Object data) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Kryo kryo = kryoThreadMap.get();
        KryoObjectOutput out = new KryoObjectOutput(kryo, new FastOutput(bos));
        out.writeObject(data);
        out.flush();
        return bos.toByteArray();
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clz) throws IOException, ClassNotFoundException {
        Kryo kryo = kryoThreadMap.get();
        kryo.register(clz);
        KryoObjectInput input = new KryoObjectInput(kryo, new FastInput(new ByteArrayInputStream(data)));
        return (T) input.readObject();
    }
}
