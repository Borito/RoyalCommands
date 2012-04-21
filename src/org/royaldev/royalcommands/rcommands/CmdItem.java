package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.util.HashMap;

public class CmdItem implements CommandExecutor {

    RoyalCommands plugin;

    public CmdItem(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("item")) {
            if (!plugin.isAuthorized(cs, "rcmds.item")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(ChatColor.RED + "This command is only available to players!");
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            Player p = (Player) cs;
            String item = args[0];
            int amount = RoyalCommands.defaultStack;
            if (args.length == 2) {
                try {
                    amount = Integer.parseInt(args[1]);
                } catch (Exception e) {
                    cs.sendMessage(ChatColor.RED + "The amount was not a number!");
                    return true;
                }
                if (amount < 1) {
                    cs.sendMessage(ChatColor.RED + "Invalid amount! You must specify a positive amount.");
                    return true;
                }
            }
            ItemStack toInv = RUtils.getItem(item, amount);
            if (toInv == null) {
                cs.sendMessage(ChatColor.RED + "Invalid item name!");
                return true;
            }
            Integer itemid = toInv.getTypeId();
            if (itemid == 0) {
                cs.sendMessage(ChatColor.RED + "You cannot spawn air!");
                return true;
            }
            if (plugin.blockedItems.contains(itemid.toString()) && !plugin.isAuthorized(cs, "rcmds.allowed.item") && !plugin.isAuthorized(cs, "rcmds.allowed.item." + itemid.toString())) {
                cs.sendMessage(ChatColor.RED + "You are not allowed to spawn that item!");
                plugin.log.warning("[RoyalCommands] " + cs.getName() + " was denied access to the command!");
                return true;
            }
            HashMap<Integer, ItemStack> left = p.getInventory().addItem(toInv);
            if (!left.isEmpty() && plugin.dropExtras)
                for (ItemStack i : left.values()) p.getWorld().dropItemNaturally(p.getLocation(), i);
            cs.sendMessage(ChatColor.BLUE + "Giving " + ChatColor.GRAY + amount + ChatColor.BLUE + " of " + ChatColor.GRAY + Material.getMaterial(itemid).toString().toLowerCase().replace("_", " ") + ChatColor.BLUE + " to " + ChatColor.GRAY + p.getName() + ChatColor.BLUE + ".");
            return true;
        }
        return false;
    }
}
