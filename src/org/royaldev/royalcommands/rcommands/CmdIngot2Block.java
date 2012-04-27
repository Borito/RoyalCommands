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

public class CmdIngot2Block implements CommandExecutor {

    RoyalCommands plugin;

    public CmdIngot2Block(RoyalCommands instance) {
        this.plugin = instance;
    }

    public void i2b(Player p, ItemStack hand, Material ingot, Material block) {
        int remainder = hand.getAmount() % 9;
        int amount = hand.getAmount() - remainder;
        int blocks = amount / 9;
        ItemStack blocka = new ItemStack(block, blocks);
        ItemStack ingots = new ItemStack(ingot, amount);
        p.getInventory().removeItem(ingots);
        HashMap<Integer, ItemStack> left = p.getInventory().addItem(blocka);
        if (!left.isEmpty()) for (ItemStack s : left.values()) p.getWorld().dropItemNaturally(p.getLocation(), s);
        p.sendMessage(ChatColor.BLUE + "Made " + ChatColor.GRAY + blocks + " block(s) " + ChatColor.BLUE + "and had " + ChatColor.GRAY + remainder + " material(s) " + ChatColor.BLUE + "left over.");
    }

    public void i2b(Player p, ItemStack hand, Material ingot, Material block, short data) {
        int remainder = hand.getAmount() % 9;
        int amount = hand.getAmount() - remainder;
        int blocks = amount / 9;
        ItemStack blocka = new ItemStack(block, blocks);
        ItemStack ingots = new ItemStack(ingot, amount, data);
        p.getInventory().removeItem(ingots);
        HashMap<Integer, ItemStack> left = p.getInventory().addItem(blocka);
        if (!left.isEmpty()) for (ItemStack s : left.values()) p.getWorld().dropItemNaturally(p.getLocation(), s);
        p.sendMessage(ChatColor.BLUE + "Made " + ChatColor.GRAY + blocks + " block(s) " + ChatColor.BLUE + "and had " + ChatColor.GRAY + remainder + " material(s) " + ChatColor.BLUE + "left over.");
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
            if (hand.getType() == Material.IRON_INGOT) i2b(p, hand, Material.IRON_INGOT, Material.IRON_BLOCK);
            else if (hand.getType() == Material.GOLD_INGOT) i2b(p, hand, Material.GOLD_INGOT, Material.GOLD_BLOCK);
            else if (hand.getType() == Material.DIAMOND) i2b(p, hand, Material.DIAMOND, Material.DIAMOND_BLOCK);
            else if (hand.getType() == Material.INK_SACK && hand.getDurability() == 4)
                i2b(p, hand, Material.INK_SACK, Material.LAPIS_BLOCK, (short) 4);
            else if (hand.getType() == Material.GOLD_NUGGET) i2b(p, hand, Material.GOLD_NUGGET, Material.GOLD_INGOT);
            else cs.sendMessage(ChatColor.RED + "That cannot be made into blocks!");
            return true;
        }
        return false;
    }
}
