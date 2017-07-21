package com.chencoder.rpc.common.serialize;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

public class ProtostuffSerialization implements Serialization{
	
	private static Map<Class<?>, Schema<?>> cachedSchema = new ConcurrentHashMap<>();

    private static Objenesis objenesis = new ObjenesisStd(true);

	@Override
	public <T> byte[] serialize(T obj) throws IOException {
		Class cls = obj.getClass();
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try {
            Schema<T> schema = getSchema(cls);
            return ProtostuffIOUtil.toByteArray(obj, schema, buffer);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        } finally {
            buffer.clear();
        }
		
	}

	private <T> Schema<T> getSchema(Class<T> cls) {
		 Schema<T> schema = (Schema<T>) cachedSchema.get(cls);
	        if (schema == null) {
	            schema = RuntimeSchema.createFrom(cls);
	            if (schema != null) {
	                cachedSchema.put(cls, schema);
	            }
	        }
	        return schema;
	}

	@Override
	public <T> T deserialize(byte[] bytes, Class<T> clz) throws IOException, ClassNotFoundException {
		try {
            T message = (T) objenesis.newInstance(clz);
            Schema<T> schema = getSchema(clz);
            ProtostuffIOUtil.mergeFrom(bytes, message, schema);
            return message;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
	}

}
