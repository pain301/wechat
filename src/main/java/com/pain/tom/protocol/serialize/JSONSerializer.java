package com.pain.tom.protocol.serialize;

import com.alibaba.fastjson.JSON;

public class JSONSerializer implements Serializer {


    public byte getSerializeAlgorithm() {
        return 0;
    }

    public byte[] serialize(Object o) {
        return JSON.toJSONBytes(o);
    }

    public <T> T deserialize(Class<T> clazz, byte[] bytes) {
        return JSON.parseObject(bytes, clazz);
    }
}
