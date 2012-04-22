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
                String enchants = "";
                for (Enchantment en : Enchantment.values())
                    enchants = (enchants.equals("")) ? enchants.concat(ChatColor.GRAY + en.getName() + ChatColor.WHITE) : enchants.concat(", " + ChatColor.GRAY + en.getName() + ChatColor.WHITE);
                cs.sendMessage(enchants);
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            Player p = (Player) cs;
            String enchant = args[0];
            Enchantment enchantment = Enchantment.getByName(enchant.toUpperCase());
            if (enchantment == null && !args[0].equalsIgnoreCase("all")) {
                cs.sendMessage(ChatColor.RED + "No such enchantment!");
                String enchants = "";
                for (Enchantment en : Enchantment.values())
                    enchants = (enchants.equals("")) ? enchants.concat(ChatColor.GRAY + en.getName() + ChatColor.WHITE) : enchants.concat(", " + ChatColor.GRAY + en.getName() + ChatColor.WHITE);
                cs.sendMessage(enchants);
                return true;
            }
            ItemStack hand = p.getItemInHand();
            if (hand == null || hand.getType().equals(Material.AIR)) {
                cs.sendMessage(ChatColor.RED + "You can't enchant air!");
                return true;
            }
            int level;
            if (args.length == 1) {
                if (enchantment == null) level = 127;
                else level = enchantment.getMaxLevel();
            } else {
                try {
                    level = Integer.valueOf(args[1]);
                } catch (Exception e) {
                    cs.sendMessage(ChatColor.RED + "The level was not a number!");
                    return true;
                }
            }
            if (level < 1) {
                if (args[0].equalsIgnoreCase("all"))
                    for (Enchantment ench : Enchantment.values()) hand.removeEnchantment(ench);
                else hand.removeEnchantment(enchantment);
                String enchantments = (enchantment == null) ? "all" : enchantment.getName().toLowerCase().replace("_", " ");
                cs.sendMessage(ChatColor.BLUE + "Removed " + ChatColor.GRAY + enchantments + ChatColor.BLUE + " from " + ChatColor.GRAY + hand.getType().name().toLowerCase().replace("_", " ") + ChatColor.BLUE + ".");
                return true;
            }
            if (args[0].equalsIgnoreCase("all"))
                for (Enchantment ench : Enchantment.values()) hand.addUnsafeEnchantment(ench, level);
            else hand.addUnsafeEnchantment(enchantment, level);
            String enchantments = (enchantment == null) ? "all" : enchantment.getName().toLowerCase().replace("_", " ");
            cs.sendMessage(ChatColor.BLUE + "Enchanted " + ChatColor.GRAY + hand.getType().name().toLowerCase().replace("_", " ") + ChatColor.BLUE + " with " + ChatColor.GRAY + enchantments + ChatColor.BLUE + " at level " + ChatColor.GRAY + level + ChatColor.BLUE + ".");
            return true;
        }
        return false;
    }

}
