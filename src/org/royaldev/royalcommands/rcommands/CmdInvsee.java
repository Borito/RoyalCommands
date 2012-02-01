package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.util.HashMap;

public class CmdInvsee implements CommandExecutor {

    RoyalCommands plugin;

    public CmdInvsee(RoyalCommands instance) {
        this.plugin = instance;
    }

    public HashMap<Player, ItemStack[]> invseedb = new HashMap<Player, ItemStack[]>();

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("invsee")) {
            if (!plugin.isAuthorized(cs, "rcmds.invsee")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(ChatColor.RED + "This command is only available to players!");
                return true;
            }
            Player p = (Player) cs;
            if (args.length < 1) {
                if (invseedb.containsKey(p)) {
                    ItemStack[] pi = invseedb.get(p);
                    invseedb.remove(p);
                    p.getInventory().setContents(pi);
                    p.sendMessage(ChatColor.BLUE + "Your inventory was restored.");
                    return true;
                }
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            Player t = plugin.getServer().getPlayer(args[0].trim());
            if (t == null || plugin.isVanished(t)) {
                cs.sendMessage(ChatColor.RED + "That player does not exist!");
                return true;
            }
            if (invseedb.containsKey(p)) {
                ItemStack[] pi = invseedb.get(p);
                invseedb.remove(p);
                p.getInventory().setContents(pi);
                p.sendMessage(ChatColor.BLUE + "Your inventory was restored.");
                return true;
            }
            invseedb.put(p, p.getInventory().getContents());
            ItemStack[] ti = t.getInventory().getContents();
            p.getInventory().clear();
            p.getInventory().setContents(ti);
            p.sendMessage(ChatColor.BLUE + "Copied inventory of " + ChatColor.GRAY + t.getName() + ChatColor.BLUE + ".");
            return true;
        }
        return false;
    }
}
