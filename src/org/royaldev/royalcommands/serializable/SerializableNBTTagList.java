package org.royaldev.royalcommands.serializable;

import net.minecraft.server.v1_4_5.NBTBase;
import net.minecraft.server.v1_4_5.NBTTagList;
import net.minecraft.server.v1_4_5.NBTTagString;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SerializableNBTTagList implements Serializable {

    static final long serialVersionUID = 42L;

    private List<SerializableNBTTagString> data = new ArrayList<SerializableNBTTagString>();
    private String name = "";

    public SerializableNBTTagList(NBTTagList nbttl) {
        if (nbttl == null) return;
        for (int i = 0; i < nbttl.size(); i++) {
            NBTBase b = nbttl.get(i);
            if (b.getTypeId() != (byte) 8) continue;
            NBTTagString s = (NBTTagString) b;
            data.add(new SerializableNBTTagString(s));
        }
        name = nbttl.getName();
    }

    public NBTTagList getNBTTagList() {
        NBTTagList nbttl = new NBTTagList();
        nbttl.setName(name);
        for (SerializableNBTTagString snbtts : data) nbttl.add(snbtts.getNBTTagString());
        return nbttl;
    }
}
