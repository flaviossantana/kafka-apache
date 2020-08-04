package br.com.kafka.serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class GsonDeserializer<T> implements Deserializer<T> {

    public static final String TYPE_CONFIG = "br.com.kafka.serialization.type.config";
    private final Gson gson = new GsonBuilder().create();
    private Class<T> type;

    @Override
    public void configure(Map configs, boolean isKey) {
        try {
            String className = String.valueOf(configs.get(TYPE_CONFIG));
            this.type = (Class<T>) Class.forName(className);
        } catch (ClassNotFoundException e) {
           throw new RuntimeException("Dont instance class for name.");
        }
    }

    @Override
    public T deserialize(String s, byte[] bytes) {
        return gson.fromJson(new String(bytes), this.type);
    }

}
