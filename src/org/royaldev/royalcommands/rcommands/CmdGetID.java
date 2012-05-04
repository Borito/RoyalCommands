package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdGetID implements CommandExecutor {

    RoyalCommands plugin;

    public CmdGetID(RoyalCommands instance) {
        this.plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("getid")) {
            if (!plugin.isAuthorized(cs, "rcmds.getid")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(ChatColor.RED + "This command is only available to players!");
                return true;
            }
            Player p = (Player) cs;
            ItemStack hand = p.getItemInHand();
            int id = hand.getTypeId();
            int damage = hand.getDurability();
            String name = hand.getType().name().replace("_", " ").toLowerCase();
            cs.sendMessage(ChatColor.GRAY + name + ChatColor.BLUE + ": " + ChatColor.GRAY + id + ChatColor.BLUE + " (damage: " + ChatColor.GRAY + damage + ChatColor.BLUE + ")");
            return true;
        }
        return false;
    }
}
