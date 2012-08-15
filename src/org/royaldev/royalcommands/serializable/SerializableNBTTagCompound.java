package org.royaldev.royalcommands.serializable;

import net.minecraft.server.NBTTagCompound;

import java.io.Serializable;

public class SerializableNBTTagCompound implements Serializable {

    static final long serialVersionUID = 42L;

    private String name = "";
    private SerializableNBTTagList tagList = null;
    private String title = "";
    private String author = "";

    public SerializableNBTTagCompound(final NBTTagCompound nbttc) {
        if (nbttc == null) return;
        name = nbttc.getName();
        title = nbttc.getString("title");
        author = nbttc.getString("author");
        tagList = new SerializableNBTTagList(nbttc.getList("pages"));
    }

    public NBTTagCompound getNBTTagCompound() {
        NBTTagCompound nbttc = new NBTTagCompound(name);
        if (tagList != null) nbttc.set("pages", tagList.getNBTTagList());
        if (title != null) nbttc.setString("title", title);
        if (title != null) nbttc.setString("author", author);
        return nbttc;
    }

}
