package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.serializable.SerializableInventory;

import java.io.File;
import java.util.HashMap;

@SuppressWarnings("unchecked")
public class CmdBackpack implements CommandExecutor {

    private RoyalCommands plugin;

    public CmdBackpack(RoyalCommands instance) {
        plugin = instance;
        HashMap<String, SerializableInventory> invs;
        File f = new File(instance.getDataFolder() + File.separator + "backpacks.sav");
        if (!f.exists()) return;
        try {
            invs = (HashMap<String, SerializableInventory>) RUtils.loadHash(f.getPath());
        } catch (Exception e) {
            f.delete();
            return;
        }
        if (!invs.keySet().isEmpty()) {
            String name = (String) invs.keySet().toArray()[0];
            Object o = invs.get(name);
            if (o instanceof SerializableInventory) convertOldBackpacks(invs);
            f.delete();
        }
    }

    private void convertOldBackpacks(HashMap<String, SerializableInventory> invs) {
        plugin.log.info("[RoyalCommands] Converting old backpacks to new system.");
        for (String name : invs.keySet()) {
            SerializableInventory si = invs.get(name);
            if (si == null) continue;
            Inventory newBackpack = RUtils.getBackpack(name);
            if (newBackpack == null) newBackpack = RUtils.getEmptyBackpack();
            newBackpack.setContents(si.getContents());
            RUtils.saveBackpack(name, newBackpack);
        }
        plugin.log.info("[RoyalCommands] Backpack conversion complete.");
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("backpack")) {
            if (!plugin.isAuthorized(cs, "rcmds.backpack")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(ChatColor.RED + "This command is only available to players!");
                return true;
            }
            Player p = (Player) cs;
            if (args.length > 0) {
                if (!plugin.isAuthorized(p, "rcmds.others.backpack")) {
                    RUtils.dispNoPerms(cs);
                    return true;
                }
                OfflinePlayer t = plugin.getServer().getPlayer(args[0]);
                if (t == null) t = plugin.getServer().getOfflinePlayer(args[0]);
                if (plugin.isAuthorized(t, "rcmds.exempt.backpack")) {
                    RUtils.dispNoPerms(cs, ChatColor.RED + "You cannot access that player's backpack!");
                    return true;
                }
                Inventory i = RUtils.getBackpack(t.getName());
                if (i == null) {
                    cs.sendMessage(ChatColor.RED + "That player does not exist!");
                    return true;
                }
                p.openInventory(i);
                return true;
            }
            Inventory i = RUtils.getBackpack(p);
            p.openInventory(i);
            return true;
        }
        return false;
    }

}
