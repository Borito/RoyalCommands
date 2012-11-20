package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdLore implements CommandExecutor {

    private RoyalCommands plugin;

    public CmdLore(RoyalCommands instance) {
        plugin = instance;
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("lore")) {
            if (!plugin.isAuthorized(cs, "rcmds.lore")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(ChatColor.RED + "This command is only available to players!");
                return true;
            }
            Player p = (Player) cs;
            String loreText = RoyalCommands.getFinalArg(args, 0);
            ItemStack is = p.getItemInHand();
            if (loreText.equalsIgnoreCase("clear")) {
                is = RUtils.clearLore(is);
                cs.sendMessage(ChatColor.BLUE + "Reset the lore on your " + ChatColor.GRAY + RUtils.getItemName(is) + ChatColor.BLUE + ".");
                return true;
            }
            is = RUtils.addLore(is, loreText);
            p.setItemInHand(is);
            cs.sendMessage(ChatColor.BLUE + "Set the lore on your " + ChatColor.GRAY + RUtils.getItemName(is) + ChatColor.BLUE + ".");
            return true;
        }
        return false;
    }

}
