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

public class CmdGive implements CommandExecutor {

    RoyalCommands plugin;

    public CmdGive(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    public static boolean validItem(String itemname) {
        ItemStack stack = RUtils.getItem(itemname, null);
        return stack != null;
    }

    public static boolean giveItemStandalone(Player target, RoyalCommands plugin, String itemname, int amount) {
        if (target == null) return false;
        if (amount < 0) {
            target.sendMessage(ChatColor.RED + "The amount must be positive!");
            return false;
        }
        ItemStack stack = RUtils.getItem(itemname, amount);
        if (stack == null) {
            target.sendMessage(ChatColor.RED + "Invalid item name!");
            return false;
        }
        Integer itemid = stack.getTypeId();
        if (itemid == 0) {
            target.sendMessage(ChatColor.RED + "You cannot spawn air!");
            return false;
        }
        target.sendMessage(ChatColor.BLUE + "Giving " + ChatColor.GRAY + amount + ChatColor.BLUE + " of " + ChatColor.GRAY + Material.getMaterial(itemid).toString().toLowerCase().replace("_", " ") + ChatColor.BLUE + " to " + ChatColor.GRAY + target.getName() + ChatColor.BLUE + ".");
        HashMap<Integer, ItemStack> left = target.getInventory().addItem(stack);
        if (!left.isEmpty() && plugin.dropExtras)
            for (ItemStack item : left.values()) target.getWorld().dropItemNaturally(target.getLocation(), item);
        return true;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("give")) {
            if (!plugin.isAuthorized(cs, "rcmds.give")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 2) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            Player target = plugin.getServer().getPlayer(args[0]);
            if (target == null) {
                cs.sendMessage(ChatColor.RED + "That player is not online!");
                return true;
            }
            int amount = RoyalCommands.defaultStack;
            if (args.length == 3) {
                try {
                    amount = Integer.parseInt(args[2]);
                } catch (Exception e) {
                    cs.sendMessage(ChatColor.RED + "The amount was not a number!");
                    return true;
                }
            }
            if (amount < 1) {
                cs.sendMessage(ChatColor.RED + "Invalid amount! You must specify a positive amount.");
                return true;
            }
            String name = args[1];
            ItemStack toInv = RUtils.getItem(name, amount);
            if (toInv == null) {
                cs.sendMessage(ChatColor.RED + "Invalid item name!");
                return true;
            }
            Integer itemid = toInv.getTypeId();
            if (itemid == 0) {
                cs.sendMessage(ChatColor.RED + "You cannot spawn air!");
                return true;
            }
            if (plugin.blockedItems.contains(itemid.toString()) && !plugin.isAuthorized(cs, "rcmds.allowed.item") && !plugin.isAuthorized(cs, "rcmds.allowed.item." + itemid)) {
                cs.sendMessage(ChatColor.RED + "You are not allowed to spawn that item!");
                plugin.log.warning("[RoyalCommands] " + cs.getName() + " was denied access to the command!");
                return true;
            }
            HashMap<Integer, ItemStack> left = target.getInventory().addItem(toInv);
            if (!left.isEmpty() && plugin.dropExtras)
                for (ItemStack item : left.values()) target.getWorld().dropItemNaturally(target.getLocation(), item);
            cs.sendMessage(ChatColor.BLUE + "Giving " + ChatColor.GRAY + amount + ChatColor.BLUE + " of " + ChatColor.GRAY + Material.getMaterial(itemid).toString().toLowerCase().replace("_", " ") + ChatColor.BLUE + " to " + ChatColor.GRAY + target.getName() + ChatColor.BLUE + ".");
            target.sendMessage(ChatColor.BLUE + "You have been given " + ChatColor.GRAY + amount + ChatColor.BLUE + " of " + ChatColor.GRAY + Material.getMaterial(itemid).toString().toLowerCase().replace("_", " ") + ChatColor.BLUE + ".");
            return true;
        }
        return false;
    }
}
