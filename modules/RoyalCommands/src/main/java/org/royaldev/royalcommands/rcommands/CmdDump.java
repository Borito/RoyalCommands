package org.royaldev.royalcommands.rcommands;

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
import org.royaldev.royalcommands.Config;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.util.HashMap;

@ReflectCommand
public class CmdDump implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdDump(RoyalCommands instance) {
        plugin = instance;
    }

    public void dumpItems(Chest c, Inventory i) {
        Inventory ci = c.getInventory();
        ItemStack[] pc = i.getContents();
        for (ItemStack aPc : pc) {
            if (aPc == null) continue;
            HashMap<Integer, ItemStack> left = ci.addItem(aPc.clone());
            i.removeItem(aPc.clone());
            if (left.isEmpty()) continue;
            for (ItemStack item : left.values()) {
                if (item == null) continue;
                i.addItem(item);
            }
        }
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("dump")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.dump")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length > 0 && args[0].equals("?")) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
                return true;
            }
            Player p = (Player) cs;
            Block bl = RUtils.getTarget(p);
            if (!Config.dumpCreateChest) {
                if (!(bl.getState() instanceof Chest)) {
                    cs.sendMessage(MessageColor.NEGATIVE + "That's not a chest!");
                    return true;
                }
                Chest c = (Chest) bl.getState();
                if (!plugin.canAccessChest(p, bl)) {
                    cs.sendMessage(MessageColor.NEGATIVE + "You cannot access that chest!");
                    return true;
                }
                dumpItems(c, p.getInventory());
                cs.sendMessage(MessageColor.POSITIVE + "Items stored.");
                return true;
            }
            Location l = bl.getLocation();
            l.setY(l.getY() + 1);
            Block b = l.getBlock();
            if (b.getType() != Material.AIR) {
                cs.sendMessage(MessageColor.NEGATIVE + "Please make sure the block above is air.");
                return true;
            }
            if (!plugin.canBuild(p, b)) {
                cs.sendMessage(MessageColor.NEGATIVE + "You're not allowed to create blocks there!");
                return true;
            }
            if (Config.dumpUseInv) {
                if (!p.getInventory().contains(Material.CHEST)) {
                    cs.sendMessage(MessageColor.NEGATIVE + "You have no chests to use!");
                    return true;
                }
                int chestAt = -1;
                for (int i = 0; i < p.getInventory().getContents().length; i++) {
                    ItemStack is = p.getInventory().getContents()[i];
                    if (is == null) continue;
                    if (is.getType() == Material.CHEST) {
                        chestAt = i;
                        break;
                    }
                }
                if (chestAt == -1) {
                    cs.sendMessage(MessageColor.NEGATIVE + "You have no chests to use!");
                }
                ItemStack chests = p.getInventory().getContents()[chestAt];
                chests.setAmount(chests.getAmount() - 1);
                p.getInventory().setItem(chestAt, chests);
            }
            b.setType(Material.CHEST);
            Chest c = (Chest) b.getState();
            dumpItems(c, p.getInventory());
            cs.sendMessage(MessageColor.POSITIVE + "Items stored.");
            return true;
        }
        return false;
    }

}
