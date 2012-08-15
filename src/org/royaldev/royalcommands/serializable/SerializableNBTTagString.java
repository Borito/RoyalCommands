package org.royaldev.royalcommands.serializable;

import net.minecraft.server.NBTTagString;

import java.io.Serializable;

public class SerializableNBTTagString implements Serializable {

    static final long serialVersionUID = 42L;

    private String data = "";
    private String name = "";

    public SerializableNBTTagString(NBTTagString nbtts) {
        data = nbtts.data;
        name = nbtts.getName();
    }

    public NBTTagString getNBTTagString() {
        if (data == null) data = "";
        if (name == null) name = "";
        NBTTagString nbtts = new NBTTagString(name, data);
        nbtts.setName(name);
        return nbtts;
    }

}
