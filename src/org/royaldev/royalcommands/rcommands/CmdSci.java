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

public class CmdSci implements CommandExecutor {

    RoyalCommands plugin;

    public CmdSci(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("sci")) {
            if (!plugin.isAuthorized(cs, "rcmds.sci")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 2) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            if (args.length < 3) {
                Player target = plugin.getServer().getPlayer(args[0].trim());
                if (target == null || plugin.isVanished(target)) {
                    cs.sendMessage(ChatColor.RED + "That player is not online!");
                    return true;
                }
                if (plugin.isAuthorized(target, "rcmds.exempt.sci")) {
                    cs.sendMessage(ChatColor.RED + "You cannot alter that player's inventory!");
                    return true;
                }
                String called = args[1];
                String data = null;
                if (called.contains(":")) {
                    String[] calleds = called.split(":");
                    called = calleds[0].trim();
                    data = calleds[1].trim();
                }
                Integer iblock;
                try {
                    iblock = Integer.parseInt(called);
                } catch (Exception e) {
                    try {
                        iblock = Material.getMaterial(called.trim().replace(" ", "_").toUpperCase()).getId();
                    } catch (Exception e2) {
                        cs.sendMessage(ChatColor.RED + "That block does not exist!");
                        return true;
                    }
                }
                if (iblock != 0) {
                    ItemStack toInv;
                    if (data != null) {
                        if (Material.getMaterial(iblock) == null) {
                            cs.sendMessage(ChatColor.RED + "Invalid item ID!");
                            return true;
                        }
                        int data2;
                        try {
                            data2 = Integer.parseInt(data);
                        } catch (Exception e) {
                            cs.sendMessage(ChatColor.RED + "The metadata was invalid!");
                            return true;
                        }
                        if (data2 < 0) {
                            cs.sendMessage(ChatColor.RED + "The metadata was invalid!");
                            return true;
                        }
                        toInv = new ItemStack(Material.getMaterial(iblock).getId(), plugin.defaultStack, (short) data2);
                    } else {
                        toInv = new ItemStack(Material.getMaterial(iblock).getId(), plugin.defaultStack);
                    }
                    target.getInventory().removeItem(toInv);
                    cs.sendMessage(ChatColor.BLUE + "Removing " + ChatColor.GRAY + plugin.defaultStack + ChatColor.BLUE + " of " + ChatColor.GRAY + Material.getMaterial(iblock).toString().toLowerCase().replace("_", " ") + ChatColor.BLUE + " from " + ChatColor.GRAY + target.getName() + ChatColor.BLUE + ".");
                    target.sendMessage(ChatColor.BLUE + "You have had " + ChatColor.GRAY + plugin.defaultStack + ChatColor.BLUE + " of " + ChatColor.GRAY + Material.getMaterial(iblock).toString().toLowerCase().replace("_", " ") + ChatColor.BLUE + " taken.");
                    return true;
                } else {
                    cs.sendMessage(ChatColor.RED + "You cannot spawn air!");
                    return true;
                }
            } else if (args.length == 3) {
                Player target = plugin.getServer().getPlayer(args[0]);
                if (target == null) {
                    cs.sendMessage(ChatColor.RED + "That player is not online!");
                    return true;
                }
                String called = args[1];
                Integer amount;
                String data = null;
                if (called.contains(":")) {
                    String[] calleds = called.split(":");
                    called = calleds[0].trim();
                    data = calleds[1].trim();
                }
                try {
                    amount = Integer.parseInt(args[2]);
                } catch (Exception e) {
                    cs.sendMessage(ChatColor.RED + "The amount was not a number!");
                    return true;
                }
                if (amount < 1) {
                    amount = 1;
                }
                Integer iblock;
                try {
                    iblock = Integer.parseInt(called);
                } catch (Exception e) {
                    try {
                        iblock = Material.getMaterial(called.trim().replace(" ", "_").toUpperCase()).getId();
                    } catch (Exception e2) {
                        cs.sendMessage(ChatColor.RED + "That block does not exist!");
                        return true;
                    }
                }
                if (iblock == 0) {
                    cs.sendMessage(ChatColor.RED + "You cannot spawn air!");
                    return true;
                }
                if (plugin.blockedItems.contains(iblock.toString()) && !plugin.isAuthorized(cs, "rcmds.allowed.item") && !plugin.isAuthorized(cs, "rcmds.allowed.item." + iblock.toString())) {
                    cs.sendMessage(ChatColor.RED + "You are not allowed to spawn that item!");
                    plugin.log.warning("[RoyalCommands] " + cs.getName() + " was denied access to the command!");
                    return true;
                }
                ItemStack toInv;
                if (data != null) {
                    if (Material.getMaterial(iblock) == null) {
                        cs.sendMessage(ChatColor.RED + "Invalid item ID!");
                        return true;
                    }
                    int data2;
                    try {
                        data2 = Integer.parseInt(data);
                    } catch (Exception e) {
                        cs.sendMessage(ChatColor.RED + "The metadata was invalid!");
                        return true;
                    }
                    if (data2 < 0) {
                        cs.sendMessage(ChatColor.RED + "The metadata was invalid!");
                        return true;
                    } else {
                        toInv = new ItemStack(Material.getMaterial(iblock).getId(), amount, (short) data2);
                    }
                } else {
                    toInv = new ItemStack(Material.getMaterial(iblock).getId(), amount);
                }
                target.getInventory().removeItem(toInv);
                cs.sendMessage(ChatColor.BLUE + "Removing " + ChatColor.GRAY + amount + ChatColor.BLUE + " of " + ChatColor.GRAY + Material.getMaterial(iblock).toString().toLowerCase().replace("_", " ") + ChatColor.BLUE + " from " + ChatColor.GRAY + target.getName() + ChatColor.BLUE + ".");
                target.sendMessage(ChatColor.BLUE + "You have had " + ChatColor.GRAY + amount + ChatColor.BLUE + " of " + ChatColor.GRAY + Material.getMaterial(iblock).toString().toLowerCase().replace("_", " ") + ChatColor.BLUE + " taken.");
                return true;
            }
        }
        return false;
    }
}

