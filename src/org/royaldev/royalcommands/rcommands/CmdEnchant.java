package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdEnchant implements CommandExecutor {

    RoyalCommands plugin;

    public CmdEnchant(RoyalCommands instance) {
        plugin = instance;
    }

    public void sendEnchantmentList(CommandSender cs) {
        StringBuilder sb = new StringBuilder();
        for (Enchantment e : Enchantment.values()) {
            sb.append(ChatColor.GRAY);
            sb.append(e.getName());
            sb.append(ChatColor.RESET);
            sb.append(", ");
        }
        cs.sendMessage(sb.substring(0, sb.length() - 4)); // "&r, " = 4
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String args[]) {
        if (cmd.getName().equalsIgnoreCase("enchant")) {
            if (!plugin.isAuthorized(cs, "rcmds.enchant")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(ChatColor.RED + "This command is only available to players!");
                return true;
            }
            if (args.length < 1) {
                sendEnchantmentList(cs);
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            Player p = (Player) cs;
            ItemStack hand = p.getItemInHand();
            if (hand == null || hand.getType() == Material.AIR) {
                cs.sendMessage(ChatColor.RED + "Air cannot be enchanted!");
                return true;
            }
            Enchantment toAdd = Enchantment.getByName(args[0].toUpperCase());
            int MAX_LEVEL = -1;
            int MAX_INT = -2;
            int REMOVE = 0;
            if (toAdd == null) {
                if (args[0].equalsIgnoreCase("all")) {
                    int level;
                    if (args.length < 2) level = -1;
                    else if (args.length > 1 && args[1].equalsIgnoreCase("max")) level = -2;
                    else {
                        try {
                            level = Integer.parseInt(args[1]);
                        } catch (NumberFormatException e) {
                            cs.sendMessage(ChatColor.RED + "The level supplied was not a number or greater than " + Integer.MAX_VALUE + "!");
                            return true;
                        }
                        if (level < 0) {
                            cs.sendMessage(ChatColor.RED + "The level cannot be below zero!");
                            return true;
                        }
                        if (level > Short.MAX_VALUE) {
                            cs.sendMessage(ChatColor.RED + "The level cannot be above " + Short.MAX_VALUE + "!");
                            return true;
                        }
                    }
                    if (level == REMOVE) {
                        for (Enchantment e : Enchantment.values()) {
                            if (!hand.containsEnchantment(e)) continue;
                            hand.removeEnchantment(e);
                        }
                        cs.sendMessage(ChatColor.BLUE + "Removed all enchantments from " + ChatColor.GRAY + RUtils.getItemName(hand) + ChatColor.BLUE + ".");
                    } else if (level == MAX_LEVEL) {
                        for (Enchantment e : Enchantment.values()) hand.addUnsafeEnchantment(e, e.getMaxLevel());
                        cs.sendMessage(ChatColor.BLUE + "Added " + ChatColor.GRAY + "all" + ChatColor.BLUE + " enchantments to " + ChatColor.GRAY + RUtils.getItemName(hand) + ChatColor.BLUE + " at their maximum levels.");
                    } else if (level == MAX_INT) {
                        for (Enchantment e : Enchantment.values()) hand.addUnsafeEnchantment(e, Short.MAX_VALUE);
                        cs.sendMessage(ChatColor.BLUE + "Added " + ChatColor.GRAY + "all" + ChatColor.BLUE + " enchantments to " + ChatColor.GRAY + RUtils.getItemName(hand) + ChatColor.BLUE + " at level " + ChatColor.GRAY + Short.MAX_VALUE + ChatColor.BLUE + ".");
                    } else {
                        for (Enchantment e : Enchantment.values()) hand.addUnsafeEnchantment(e, level);
                        cs.sendMessage(ChatColor.BLUE + "Added " + ChatColor.GRAY + "all" + ChatColor.BLUE + " enchantments to " + ChatColor.GRAY + RUtils.getItemName(hand) + ChatColor.BLUE + " at level " + ChatColor.GRAY + level + ChatColor.BLUE + ".");
                    }
                    return true;
                }
                sendEnchantmentList(cs);
                cs.sendMessage(ChatColor.RED + "No such enchantment!");
                return true;
            }
            int level;
            if (args.length < 2) level = -1;
            else if (args.length > 1 && args[1].equalsIgnoreCase("max")) level = -2;
            else {
                try {
                    level = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    cs.sendMessage(ChatColor.RED + "The level supplied was not a number or greater than " + Integer.MAX_VALUE + "!");
                    return true;
                }
                if (level < 0) {
                    cs.sendMessage(ChatColor.RED + "The level cannot be below zero!");
                    return true;
                }
                if (level > Short.MAX_VALUE) {
                    cs.sendMessage(ChatColor.RED + "The level cannot be above " + Short.MAX_VALUE + "!");
                    return true;
                }
            }
            if (level == REMOVE) {
                if (!hand.containsEnchantment(toAdd)) {
                    cs.sendMessage(ChatColor.RED + "That " + ChatColor.GRAY + RUtils.getItemName(hand) + ChatColor.BLUE + " does not contain " + ChatColor.GRAY + toAdd.getName().toLowerCase().replace("_", " ") + ChatColor.BLUE + ".");
                    return true;
                }
                hand.removeEnchantment(toAdd);
                cs.sendMessage(ChatColor.BLUE + "Added " + ChatColor.GRAY + toAdd.getName().toLowerCase().replace("_", " ") + ChatColor.BLUE + " from " + ChatColor.GRAY + RUtils.getItemName(hand) + ChatColor.BLUE + ".");
            } else if (level == MAX_LEVEL) {
                hand.addUnsafeEnchantment(toAdd, toAdd.getMaxLevel());
                cs.sendMessage(ChatColor.BLUE + "Added " + ChatColor.GRAY + toAdd.getName().toLowerCase().replace("_", " ") + ChatColor.BLUE + " to " + ChatColor.GRAY + RUtils.getItemName(hand) + ChatColor.BLUE + " at level " + ChatColor.GRAY + toAdd.getMaxLevel() + ChatColor.BLUE + ".");
            } else if (level == MAX_INT) {
                hand.addUnsafeEnchantment(toAdd, Short.MAX_VALUE);
                cs.sendMessage(ChatColor.BLUE + "Added " + ChatColor.GRAY + toAdd.getName().toLowerCase().replace("_", " ") + ChatColor.BLUE + " to " + ChatColor.GRAY + RUtils.getItemName(hand) + ChatColor.BLUE + " at level " + ChatColor.GRAY + Short.MAX_VALUE + ChatColor.BLUE + ".");
            } else {
                hand.addUnsafeEnchantment(toAdd, level);
                cs.sendMessage(ChatColor.BLUE + "Added " + ChatColor.GRAY + toAdd.getName().toLowerCase().replace("_", " ") + ChatColor.BLUE + " to " + ChatColor.GRAY + RUtils.getItemName(hand) + ChatColor.BLUE + " at level " + ChatColor.GRAY + level + ChatColor.BLUE + ".");
            }
            return true;
        }
        return false;
    }

}
