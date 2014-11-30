package org.royaldev.royalcommands.shaded.mkremins.fanciful;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.craftbukkit.libs.com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * Internal class: Represents a component of a JSON-serializable {@link FancyMessage}.
 */
final class MessagePart implements JsonRepresentedObject, ConfigurationSerializable, Cloneable {

    static final BiMap<ChatColor, String> stylesToNames;

    static {
        ImmutableBiMap.Builder<ChatColor, String> builder = ImmutableBiMap.builder();
        for (final ChatColor style : ChatColor.values()) {
            if (!style.isFormat()) {
                continue;
            }

            String styleName;
            switch (style) {
                case MAGIC:
                    styleName = "obfuscated";
                    break;
                case UNDERLINE:
                    styleName = "underlined";
                    break;
                default:
                    styleName = style.name().toLowerCase();
                    break;
            }

            builder.put(style, styleName);
        }
        stylesToNames = builder.build();
    }

    ChatColor color = ChatColor.WHITE;
    ArrayList<ChatColor> styles = new ArrayList<ChatColor>();
    String clickActionName = null, clickActionData = null,
        hoverActionName = null;
    JsonRepresentedObject hoverActionData = null;
    TextualComponent text = null;

    MessagePart(final TextualComponent text) {
        this.text = text;
    }

    MessagePart() {
        this.text = null;
    }

    @SuppressWarnings("unchecked")
    public static MessagePart deserialize(Map<String, Object> serialized) {
        MessagePart part = new MessagePart((TextualComponent) serialized.get("text"));
        part.styles = (ArrayList<ChatColor>) serialized.get("styles");
        part.color = ChatColor.getByChar(serialized.get("color").toString());
        part.hoverActionName = serialized.get("hoverActionName").toString();
        part.hoverActionData = (JsonRepresentedObject) serialized.get("hoverActionData");
        part.clickActionName = serialized.get("clickActionName").toString();
        part.clickActionData = serialized.get("clickActionData").toString();
        return part;
    }

    static {
        ConfigurationSerialization.registerClass(MessagePart.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public MessagePart clone() throws CloneNotSupportedException {
        MessagePart obj = (MessagePart) super.clone();
        obj.styles = (ArrayList<ChatColor>) styles.clone();
        if (hoverActionData instanceof JsonString) {
            obj.hoverActionData = new JsonString(((JsonString) hoverActionData).getValue());
        } else if (hoverActionData instanceof FancyMessage) {
            obj.hoverActionData = ((FancyMessage) hoverActionData).clone();
        }
        return obj;

    }

    boolean hasText() {
        return text != null;
    }

    public Map<String, Object> serialize() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("text", text);
        map.put("styles", styles);
        map.put("color", color.getChar());
        map.put("hoverActionName", hoverActionName);
        map.put("hoverActionData", hoverActionData);
        map.put("clickActionName", clickActionName);
        map.put("clickActionData", clickActionData);
        return map;
    }

    public void writeJson(JsonWriter json) {
        try {
            json.beginObject();
            text.writeJson(json);
            json.name("color").value(color.name().toLowerCase());
            for (final ChatColor style : styles) {
                json.name(stylesToNames.get(style)).value(true);
            }
            if (clickActionName != null && clickActionData != null) {
                json.name("clickEvent")
                    .beginObject()
                    .name("action").value(clickActionName)
                    .name("value").value(clickActionData)
                    .endObject();
            }
            if (hoverActionName != null && hoverActionData != null) {
                json.name("hoverEvent")
                    .beginObject()
                    .name("action").value(hoverActionName)
                    .name("value");
                hoverActionData.writeJson(json);
                json.endObject();
            }
            json.endObject();
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.WARNING, "A problem occured during writing of JSON string", e);
        }
    }

}
