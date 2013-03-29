package org.royaldev.royalcommands.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.kitteh.tag.PlayerReceiveNameTagEvent;
import org.royaldev.royalcommands.PConfManager;
import org.royaldev.royalcommands.RoyalCommands;

public class TagAPIListener implements Listener {

    private RoyalCommands plugin;

    public TagAPIListener(RoyalCommands instance) {
        plugin = instance;
    }

    @EventHandler
    public void onTag(PlayerReceiveNameTagEvent e) {
        if (e.isModified()) return;
        if (e.getNamedPlayer() == null) return;
        PConfManager pcm = PConfManager.getPConfManager(e.getNamedPlayer());
        String dispname = pcm.getString("dispname");
        if (dispname == null) return;
        if (dispname.equalsIgnoreCase(e.getNamedPlayer().getName())) {
            e.setTag(dispname);
            return;
        }
        if (!plugin.nickPrefix.equals("") && dispname.startsWith(plugin.nickPrefix))
            dispname = dispname.substring(plugin.nickPrefix.length());
        e.setTag(dispname);
    }

}
