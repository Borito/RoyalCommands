package org.royaldev.royalcommands.serializable;

import net.minecraft.server.v1_4_5.NBTTagCompound;

import java.io.Serializable;

public class SerializableNBTTagCompound implements Serializable {

    static final long serialVersionUID = 42L;

    private String name = "";
    private SerializableNBTTagList tagList = null;
    private String title = "";
    private String author = "";
    private String dispName = null;
    private SerializableNBTTagList dispLore = null;

    public SerializableNBTTagCompound(final NBTTagCompound nbttc) {
        if (nbttc == null) return;
        name = nbttc.getName();
        title = nbttc.getString("title");
        author = nbttc.getString("author");
        tagList = new SerializableNBTTagList(nbttc.getList("pages"));
        NBTTagCompound display = nbttc.getCompound("display");
        if (display == null) return;
        dispName = display.getString("Name");
        if (display.getList("Lore") == null) return;
        dispLore = new SerializableNBTTagList(display.getList("Lore"));
    }

    public NBTTagCompound getNBTTagCompound() {
        NBTTagCompound nbttc = new NBTTagCompound(name);
        if (tagList != null) nbttc.set("pages", tagList.getNBTTagList());
        if (title != null) nbttc.setString("title", title);
        if (title != null) nbttc.setString("author", author);
        if (dispName != null && !dispName.isEmpty()) {
            if (nbttc.getCompound("display") == null) nbttc.setCompound("display", new NBTTagCompound());
            NBTTagCompound display = nbttc.getCompound("display");
            display.setString("Name", dispName);
            nbttc.setCompound("display", display);
        }
        if (dispLore != null) {
            if (nbttc.getCompound("display") == null) nbttc.setCompound("display", new NBTTagCompound());
            NBTTagCompound display = nbttc.getCompound("display");
            display.set("Lore", dispLore.getNBTTagList());
            nbttc.setCompound("display", display);
        }
        return nbttc;
    }

}
