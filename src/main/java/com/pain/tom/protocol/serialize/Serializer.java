package com.pain.tom.protocol.serialize;

public interface Serializer {

    Serializer DEFAULT = new JSONSerializer();

    byte getSerializeAlgorithm();

    byte[] serialize(Object o);

    <T> T deserialize(Class<T> clazz, byte[] bytes);
}
