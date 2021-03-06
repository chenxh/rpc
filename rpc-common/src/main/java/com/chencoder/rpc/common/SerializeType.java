package com.chencoder.rpc.common;

import org.apache.commons.lang3.StringUtils;

import com.chencoder.rpc.common.serialize.FastJsonSerialization;
import com.chencoder.rpc.common.serialize.Hessian2Serialization;
import com.chencoder.rpc.common.serialize.KryoSerialization;
import com.chencoder.rpc.common.serialize.ProtostuffSerialization;
import com.chencoder.rpc.common.serialize.Serialization;

public enum SerializeType {

    Kyro((byte) 0), Fastjson((byte) 1), Hession2((byte) 2), Protostuff((byte)3);

    private byte value;

    SerializeType(byte value) {
        this.value = value;
    }

    public static SerializeType getSerializeTypeByName(String name) {
        if (StringUtils.equals(Kyro.name(), name)) {
            return Kyro;
        } else if (StringUtils.equals(Fastjson.name(), name)) {
            return Fastjson;
        } else if (StringUtils.equals(Hession2.name(), name)) {
            return Hession2;
        } else if (StringUtils.equals(Protostuff.name(), name)){
        	return Protostuff;
        }
        return Kyro;
    }

    public static Serialization getSerializationByExtend(byte value) {
        switch (value & 0x7) {
            case 0x0:
                return new KryoSerialization();
            case 0x1:
                return new FastJsonSerialization();
            case 0x2:
                return new Hessian2Serialization();
            case 0x3:
                return new ProtostuffSerialization();
            default:
                return new KryoSerialization();
        }

    }

    public byte getValue() {
        return value;
    }

    public final static SerializeType DEFAULT_SERIALIZE_TYPE = SerializeType.Kyro;
}
