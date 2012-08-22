package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.serializable.SerializableCraftInventory;

import java.io.File;
import java.util.HashMap;

@SuppressWarnings("unchecked")
public class CmdBackpack implements CommandExecutor {

    RoyalCommands plugin;

    public static HashMap<String, SerializableCraftInventory> invs;

    public CmdBackpack(RoyalCommands instance) {
        plugin = instance;
        invs = (HashMap<String, SerializableCraftInventory>) RUtils.loadHash(instance.getDataFolder() + File.separator + "backpacks.sav");
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
                Player t = plugin.getServer().getPlayer(args[0]);
                if (t == null || plugin.isVanished(t, p)) {
                    cs.sendMessage(ChatColor.RED + "That player does not exist!");
                    return true;
                }
                if (plugin.isAuthorized(t, "rcmds.exempt.backpack")) {
                    RUtils.dispNoPerms(cs, ChatColor.RED + "You cannot access that player's backpack!");
                    return true;
                }
                if (!invs.containsKey(t.getName()))
                    invs.put(t.getName(), RUtils.convInvToSCI(RUtils.createInv(t, 36, "Backpack")));
                Inventory i = invs.get(t.getName()).getInventory();
                i = RUtils.setHolder(i, t);
                p.openInventory(i);
                return true;
            }
            if (!invs.containsKey(p.getName())) {
                invs.put(p.getName(), RUtils.convInvToSCI(RUtils.createInv(p, 36, "Backpack")));
            }
            Inventory i = invs.get(p.getName()).getInventory();
            i = RUtils.setHolder(i, p);
            p.openInventory(i);
            return true;
        }
        return false;
    }

}
