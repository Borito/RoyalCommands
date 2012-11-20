package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.util.Map;

public class CmdGetID implements CommandExecutor {

    RoyalCommands plugin;

    public CmdGetID(RoyalCommands instance) {
        this.plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("getid")) {
            if (!plugin.isAuthorized(cs, "rcmds.getid")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(ChatColor.RED + "This command is only available to players!");
                return true;
            }
            Player p = (Player) cs;
            ItemStack hand = p.getItemInHand();
            int id = hand.getTypeId();
            short damage = hand.getDurability();
            byte data = hand.getData().getData();
            String name = RUtils.getItemName(hand);
            Map<Enchantment, Integer> enchants = hand.getEnchantments();
            cs.sendMessage(ChatColor.GRAY + name + ChatColor.BLUE + ": " + ChatColor.GRAY + id + ChatColor.BLUE + " (damage: " + ChatColor.GRAY + damage + ChatColor.BLUE + ", materialdata: " + ChatColor.GRAY + data + ChatColor.BLUE + ")");
            if (!enchants.isEmpty()) {
                cs.sendMessage(ChatColor.BLUE + "Enchantments:");
                for (Enchantment e : enchants.keySet()) {
                    int lvl = enchants.get(e);
                    cs.sendMessage(" " + ChatColor.GRAY + e.getName().toLowerCase() + " " + lvl);
                }
            }
            return true;
        }
        return false;
    }
}
