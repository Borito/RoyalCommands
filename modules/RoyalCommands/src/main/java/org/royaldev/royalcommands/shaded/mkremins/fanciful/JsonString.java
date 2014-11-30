package org.royaldev.royalcommands.shaded.mkremins.fanciful;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.craftbukkit.libs.com.google.gson.stream.JsonWriter;

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
        HashMap<String, Object> theSingleValue = new HashMap<String, Object>();
        theSingleValue.put("stringValue", _value);
        return theSingleValue;
    }

    @Override
    public String toString() {
        return _value;
    }

    public void writeJson(JsonWriter writer) throws IOException {
        writer.value(getValue());
    }
}
