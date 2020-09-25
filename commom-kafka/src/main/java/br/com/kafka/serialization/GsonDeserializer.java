package br.com.kafka.serialization;

import br.com.kafka.dto.Message;
import br.com.kafka.serialization.adapter.MessageAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.common.serialization.Deserializer;

public class GsonDeserializer implements Deserializer<Message> {


    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(br.com.kafka.dto.Message.class, new MessageAdapter())
            .create();

    @Override
    public Message deserialize(String s, byte[] bytes) {
        return gson.fromJson(new String(bytes), Message.class);
    }

}
