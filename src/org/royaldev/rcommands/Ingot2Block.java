package org.royaldev.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class Ingot2Block implements CommandExecutor {

    RoyalCommands plugin;

    public Ingot2Block(RoyalCommands instance) {
        this.plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("ingot2block")) {
            if (!plugin.isAuthorized(cs, "rcmds.ingot2block")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(ChatColor.RED + "This command is only available to players!");
                return true;
            }
            Player p = (Player) cs;
            ItemStack hand = p.getItemInHand();
            if (hand == null || hand.getType() == Material.AIR) {
                cs.sendMessage(ChatColor.RED + "Your hand is empty!");
                return true;
            }
            if (hand.getType() == Material.IRON_INGOT) {
                int remainder = hand.getAmount() % 9;
                int amount = hand.getAmount() - remainder;
                int blocks = amount / 9;
                ItemStack block = new ItemStack(Material.IRON_BLOCK, blocks);
                ItemStack ingots = new ItemStack(Material.IRON_INGOT, amount);
                p.getInventory().removeItem(ingots);
                p.getInventory().addItem(block);
                cs.sendMessage(ChatColor.BLUE + "Made " + ChatColor.GRAY + blocks + " block(s) " + ChatColor.BLUE + "and had " + ChatColor.GRAY + remainder + " material(s) " + ChatColor.BLUE + "left over.");
            } else if (hand.getType() == Material.GOLD_INGOT) {
                int remainder = hand.getAmount() % 9;
                int amount = hand.getAmount() - remainder;
                int blocks = amount / 9;
                ItemStack block = new ItemStack(Material.GOLD_BLOCK, blocks);
                ItemStack ingots = new ItemStack(Material.GOLD_INGOT, amount);
                p.getInventory().removeItem(ingots);
                p.getInventory().addItem(block);
                cs.sendMessage(ChatColor.BLUE + "Made " + ChatColor.GRAY + blocks + " block(s) " + ChatColor.BLUE + "and had " + ChatColor.GRAY + remainder + " material(s) " + ChatColor.BLUE + "left over.");
            } else if (hand.getType() == Material.DIAMOND) {
                int remainder = hand.getAmount() % 9;
                int amount = hand.getAmount() - remainder;
                int blocks = amount / 9;
                ItemStack block = new ItemStack(Material.DIAMOND_BLOCK, blocks);
                ItemStack ingots = new ItemStack(Material.DIAMOND, amount);
                p.getInventory().removeItem(ingots);
                p.getInventory().addItem(block);
                cs.sendMessage(ChatColor.BLUE + "Made " + ChatColor.GRAY + blocks + " block(s) " + ChatColor.BLUE + "and had " + ChatColor.GRAY + remainder + " material(s) " + ChatColor.BLUE + "left over.");
            } else if (hand.getType() == Material.INK_SACK && hand.getDurability() == 4) {
                int remainder = hand.getAmount() % 9;
                int amount = hand.getAmount() - remainder;
                int blocks = amount / 9;
                ItemStack block = new ItemStack(Material.LAPIS_BLOCK, blocks);
                ItemStack ingots = new ItemStack(Material.INK_SACK, amount, (short) 4);
                p.getInventory().removeItem(ingots);
                p.getInventory().addItem(block);
                cs.sendMessage(ChatColor.BLUE + "Made " + ChatColor.GRAY + blocks + " block(s) " + ChatColor.BLUE + "and had " + ChatColor.GRAY + remainder + " material(s) " + ChatColor.BLUE + "left over.");
            } else {
                cs.sendMessage(ChatColor.RED + "That cannot be made into blocks!");
                return true;
            }
            return true;
        }
        return false;
    }
}
