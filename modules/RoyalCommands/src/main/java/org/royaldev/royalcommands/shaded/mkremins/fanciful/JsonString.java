package org.royaldev.royalcommands.shaded.mkremins.fanciful;

import com.google.gson.stream.JsonWriter;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import javax.annotation.concurrent.Immutable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a JSON string value.
 * Writes by this object will not write name values nor begin/end objects in the JSON stream.
 * All writes merely write the represented string value.
 */
@Immutable
final class JsonString implements JsonRepresentedObject, ConfigurationSerializable {

    private String _value;

    public JsonString(String value) {
        _value = value;
    }

    public static JsonString deserialize(Map<String, Object> map) {
        return new JsonString(map.get("stringValue").toString());
    }

    public String getValue() {
        return _value;
    }

    public Map<String, Object> serialize() {
        HashMap<String, Object> theSingleValue = new HashMap<>();
        theSingleValue.put("stringValue", _value);
        return theSingleValue;
    }

    @Override
    public String toString() {
        return _value;
    }

    @Override
    public void writeJson(JsonWriter writer) throws IOException {
        writer.value(getValue());
    }
}
