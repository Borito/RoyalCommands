package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.util.HashMap;

public class CmdDump implements CommandExecutor {

    RoyalCommands plugin;

    public CmdDump(RoyalCommands instance) {
        plugin = instance;
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("dump")) {
            if (!plugin.isAuthorized(cs, "rcmds.dump")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length > 0 && args[0].equals("?")) {
                cs.sendMessage(ChatColor.GRAY + "/" + cmd.getName() + ChatColor.BLUE + " - " + cmd.getDescription());
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(ChatColor.RED + "This command is only available to players!");
                return true;
            }
            Player p = (Player) cs;
            Block bl = RUtils.getTarget(p);
            Location l = bl.getLocation();
            l.setY(l.getY() + 1);
            Block b = l.getBlock();
            if (b.getTypeId() != 0) {
                cs.sendMessage(ChatColor.RED + "Please make sure the block above is air.");
                return true;
            }
            b.setType(Material.CHEST);
            Chest c = (Chest) b.getState();
            Inventory ci = c.getInventory();
            PlayerInventory pi = p.getInventory();
            ItemStack[] pc = p.getInventory().getContents();
            for (ItemStack aPc : pc) {
                if (aPc == null) continue;
                HashMap<Integer, ItemStack> left = ci.addItem(aPc.clone());
                pi.removeItem(aPc.clone());
                if (left.isEmpty()) continue;
                for (ItemStack item : left.values()) {
                    if (item == null) continue;
                    pi.addItem(item);
                }
            }
            cs.sendMessage(ChatColor.BLUE + "Items stored.");
            return true;
        }
        return false;
    }

}
