package br.com.kafka.serialization.adapter;

import br.com.kafka.dto.CorrelationId;
import br.com.kafka.dto.Message;
import com.google.gson.*;

import java.lang.reflect.Type;

public class MessageAdapter implements JsonSerializer<Message>, JsonDeserializer<Message> {
    @Override
    public JsonElement serialize(Message message, Type type, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        json.addProperty("type", message.getPayload().getClass().getName());
        json.add("payload", context.serialize(message.getPayload()));
        json.add("correlation", context.serialize(message.getCorrelation()));
        return json;
    }

    @Override
    public Message deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        try {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String typePayload = jsonObject.get("type").getAsString();
            CorrelationId correlation = context.deserialize(jsonObject.get("correlation"), CorrelationId.class);
            Object payload = context.deserialize(jsonObject.get("payload"), Class.forName(typePayload));
            return new Message(correlation, payload);
        } catch (ClassNotFoundException e) {
            throw new JsonParseException(e);
        }
    }
}
