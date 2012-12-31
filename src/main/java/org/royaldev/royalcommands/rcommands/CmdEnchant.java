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

    private RoyalCommands plugin;

    public CmdEnchant(RoyalCommands instance) {
        plugin = instance;
    }

    /**
     * Gets the real level of an enchantment to be applied to an item.
     * <p/>
     * Keep in mind that...
     * <ul>
     * <li>-1 is the maximum level of the enchantment</li>
     * <li>-2 is the maximum <em>short</em> value in Java</li>
     * <li>0 will return 0 for removal</li>
     * <li>Any other number will be returned without modification</li>
     * </ul>
     *
     * @param e Enchantment being added
     * @param i Level supplied (-1, -2, 0, 10, etc.)
     * @return Real level
     */
    private int getRealLevel(Enchantment e, int i) {
        switch (i) {
            case -1: // Max level
                return e.getMaxLevel();
            case -2: // Max short value
                return Short.MAX_VALUE;
            case 0:
                return 0;
            default:
                return i;
        }
    }

    private void sendEnchantmentList(CommandSender cs) {
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
            if (toAdd == null) {
                if (args[0].equalsIgnoreCase("all")) {
                    int level;
                    if (args.length < 2) level = -1;
                    else if (args.length > 1 && args[1].equalsIgnoreCase("max")) level = -2;
                    else {
                        try {
                            level = (int) Short.parseShort(args[1]);
                        } catch (NumberFormatException e) {
                            cs.sendMessage(ChatColor.RED + "The level supplied was not a number or greater than " + Short.MAX_VALUE + "!");
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
                    if (level == 0) {
                        for (Enchantment e : Enchantment.values()) {
                            if (!hand.containsEnchantment(e)) continue;
                            hand.removeEnchantment(e);
                        }
                        cs.sendMessage(ChatColor.BLUE + "Removed all enchantments from " + ChatColor.GRAY + RUtils.getItemName(hand) + ChatColor.BLUE + ".");
                    } else {
                        if (level == -2 && !plugin.isAuthorized(cs, "rcmds.enchant.levels")) {
                            cs.sendMessage(ChatColor.RED + "You cannot apply levels for enchantments higher than the maximum vanilla level!");
                            return true;
                        }
                        boolean skipped = false;
                        for (Enchantment e : Enchantment.values()) {
                            int toApply = getRealLevel(e, level);
                            if (toApply > e.getMaxLevel() && !plugin.isAuthorized(cs, "rcmds.enchant.levels")) {
                                skipped = true;
                                continue;
                            }
                            if (!e.canEnchantItem(hand) && !plugin.isAuthorized(cs, "rcmds.enchant.illegal")) {
                                skipped = true;
                                continue;
                            }
                            hand.addUnsafeEnchantment(e, toApply);
                        }
                        if (skipped)
                            cs.sendMessage(ChatColor.RED + "Some enchantments were not applied because you do not have permission for them.");
                        String atLevel;
                        if (level == -1) atLevel = "their maximum levels";
                        else if (level == -2) atLevel = "the maximum possible level";
                        else atLevel = "level " + String.valueOf(level);
                        cs.sendMessage(ChatColor.BLUE + "Added " + ChatColor.GRAY + "all" + ChatColor.BLUE + " to " + ChatColor.GRAY + RUtils.getItemName(hand) + ChatColor.BLUE + " at " + ChatColor.GRAY + atLevel + ChatColor.BLUE + ".");
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
            if (level == 0) {
                if (!hand.containsEnchantment(toAdd)) {
                    cs.sendMessage(ChatColor.RED + "That " + ChatColor.GRAY + RUtils.getItemName(hand) + ChatColor.BLUE + " does not contain " + ChatColor.GRAY + toAdd.getName().toLowerCase().replace("_", " ") + ChatColor.BLUE + ".");
                    return true;
                }
                hand.removeEnchantment(toAdd);
                cs.sendMessage(ChatColor.BLUE + "Added " + ChatColor.GRAY + toAdd.getName().toLowerCase().replace("_", " ") + ChatColor.BLUE + " from " + ChatColor.GRAY + RUtils.getItemName(hand) + ChatColor.BLUE + ".");
            } else {
                int toApply = getRealLevel(toAdd, level);
                if (toApply > toAdd.getMaxLevel() && !plugin.isAuthorized(cs, "rcmds.enchant.levels")) {
                    cs.sendMessage(ChatColor.RED + "That level is too high for " + ChatColor.GRAY + toAdd.getName().replace("_", " ").toLowerCase() + ChatColor.RED + ".");
                    return true;
                }
                if (!toAdd.canEnchantItem(hand) && !plugin.isAuthorized(cs, "rcmds.enchant.illegal")) {
                    cs.sendMessage(ChatColor.RED + "Cannot add " + ChatColor.GRAY + toAdd.getName().replace("_", " ").toLowerCase() + ChatColor.RED + " because it is not for that type of item!");
                    return true;
                }
                if (!plugin.isAuthorized(cs, "rcmds.enchant.illegal"))
                    for (Enchantment e : hand.getEnchantments().keySet()) {
                        if (toAdd.conflictsWith(e)) {
                            cs.sendMessage(ChatColor.RED + "Cannot add " + ChatColor.GRAY + toAdd.getName().replace("_", " ").toLowerCase() + ChatColor.RED + " because it conflicts with " + ChatColor.GRAY + e.getName().replace("_", " ").toLowerCase() + ChatColor.RED + ".");
                            return true;
                        }
                    }
                hand.addUnsafeEnchantment(toAdd, toApply);
                cs.sendMessage(ChatColor.BLUE + "Added " + ChatColor.GRAY + toAdd.getName().toLowerCase().replace("_", " ") + ChatColor.BLUE + " to " + ChatColor.GRAY + RUtils.getItemName(hand) + ChatColor.BLUE + " at level " + ChatColor.GRAY + toApply + ChatColor.BLUE + ".");
            }
            return true;
        }
        return false;
    }

}
