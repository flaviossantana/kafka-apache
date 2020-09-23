package br.com.kafka.serialization.adapter;

import br.com.kafka.dto.Message;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class MessageAdapter implements JsonSerializer<Message> {
    @Override
    public JsonElement serialize(Message message, Type type, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        json.addProperty("type", message.getPayload().getClass().getName());
        json.add("payload", context.serialize(message.getPayload()));
        json.add("correlation", context.serialize(message.getCorrelation()));
        return json;
    }
}
