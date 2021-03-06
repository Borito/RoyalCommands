package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class Less implements CommandExecutor {

    RoyalCommands plugin;

    public Less(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("less")) {
            if (!plugin.isAuthorized(cs, "rcmds.less")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(ChatColor.RED + "This command is only available to players!");
                return true;
            }
            Player p = (Player) cs;
            ItemStack hand = p.getItemInHand();
            if (hand.getTypeId() == 0) {
                cs.sendMessage(ChatColor.RED + "You can't spawn air!");
                return true;
            }
            hand.setAmount(1);
            cs.sendMessage(ChatColor.BLUE + "All of the item in hand, except for one, has been removed.");
            return true;
        }
        return false;
    }

}
