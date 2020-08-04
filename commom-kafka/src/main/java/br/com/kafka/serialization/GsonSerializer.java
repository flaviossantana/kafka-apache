package br.com.kafka.serialization;

import com.google.gson.Gson;
import org.apache.kafka.common.serialization.Serializer;

public class GsonSerializer<T> implements Serializer<T> {

    @Override
    public byte[] serialize(String s, T obj) {
        return new Gson().toJson(obj).getBytes();
    }
}
