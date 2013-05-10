package org.royaldev.royalcommands.rcommands;

import org.royaldev.royalcommands.MessageColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.royaldev.royalcommands.Config;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.exceptions.InvalidItemNameException;

import java.util.HashMap;

public class CmdGive implements CommandExecutor {

    private RoyalCommands plugin;

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
            target.sendMessage(MessageColor.NEGATIVE + "The amount must be positive!");
            return false;
        }
        ItemStack stack;
        try {
            stack = RUtils.getItemFromAlias(itemname, amount);
        } catch (InvalidItemNameException e) {
            stack = RUtils.getItem(itemname, amount);
        } catch (NullPointerException e) {
            target.sendMessage(MessageColor.NEGATIVE + "ItemNameManager was not loaded. Let an administrator know.");
            return false;
        }
        if (stack == null) {
            target.sendMessage(MessageColor.NEGATIVE + "Invalid item name!");
            return false;
        }
        Integer itemid = stack.getTypeId();
        if (itemid == 0) {
            target.sendMessage(MessageColor.NEGATIVE + "You cannot spawn air!");
            return false;
        }
        target.sendMessage(MessageColor.POSITIVE + "Giving " + MessageColor.NEUTRAL + amount + MessageColor.POSITIVE + " of " + MessageColor.NEUTRAL + RUtils.getItemName(Material.getMaterial(itemid)) + MessageColor.POSITIVE + " to " + MessageColor.NEUTRAL + target.getName() + MessageColor.POSITIVE + ".");
        HashMap<Integer, ItemStack> left = target.getInventory().addItem(stack);
        if (!left.isEmpty() && Config.dropExtras) for (ItemStack item : left.values())
            target.getWorld().dropItemNaturally(target.getLocation(), item);
        return true;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("give")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.give")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 2) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            Player target = plugin.getServer().getPlayer(args[0]);
            if (target == null) {
                cs.sendMessage(MessageColor.NEGATIVE + "That player is not online!");
                return true;
            }
            int amount = Config.defaultStack;
            if (args.length == 3) {
                try {
                    amount = Integer.parseInt(args[2]);
                } catch (Exception e) {
                    cs.sendMessage(MessageColor.NEGATIVE + "The amount was not a number!");
                    return true;
                }
            }
            if (amount < 1) {
                cs.sendMessage(MessageColor.NEGATIVE + "Invalid amount! You must specify a positive amount.");
                return true;
            }
            String name = args[1];
            ItemStack toInv;
            try {
                toInv = RUtils.getItemFromAlias(name, amount);
            } catch (InvalidItemNameException e) {
                toInv = RUtils.getItem(name, amount);
            } catch (NullPointerException e) {
                cs.sendMessage(MessageColor.NEGATIVE + "ItemNameManager was not loaded. Let an administrator know.");
                return true;
            }
            if (toInv == null) {
                cs.sendMessage(MessageColor.NEGATIVE + "Invalid item name!");
                return true;
            }
            Integer itemid = toInv.getTypeId();
            if (itemid == 0) {
                cs.sendMessage(MessageColor.NEGATIVE + "You cannot spawn air!");
                return true;
            }
            if (Config.blockedItems.contains(itemid.toString()) && !plugin.ah.isAuthorized(cs, "rcmds.allowed.item") && !plugin.ah.isAuthorized(cs, "rcmds.allowed.item." + itemid)) {
                cs.sendMessage(MessageColor.NEGATIVE + "You are not allowed to spawn that item!");
                plugin.log.warning("[RoyalCommands] " + cs.getName() + " was denied access to the command!");
                return true;
            }
            HashMap<Integer, ItemStack> left = target.getInventory().addItem(toInv);
            if (!left.isEmpty() && Config.dropExtras) for (ItemStack item : left.values())
                target.getWorld().dropItemNaturally(target.getLocation(), item);
            cs.sendMessage(MessageColor.POSITIVE + "Giving " + MessageColor.NEUTRAL + amount + MessageColor.POSITIVE + " of " + MessageColor.NEUTRAL + RUtils.getItemName(Material.getMaterial(itemid)) + MessageColor.POSITIVE + " to " + MessageColor.NEUTRAL + target.getName() + MessageColor.POSITIVE + ".");
            target.sendMessage(MessageColor.POSITIVE + "You have been given " + MessageColor.NEUTRAL + amount + MessageColor.POSITIVE + " of " + MessageColor.NEUTRAL + RUtils.getItemName(Material.getMaterial(itemid)) + MessageColor.POSITIVE + ".");
            return true;
        }
        return false;
    }
}
