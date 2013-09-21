package org.royaldev.royalcommands.rcommands;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.royaldev.royalcommands.Config;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.WorldManager;

@ReflectCommand
public class CmdEnderChest implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdEnderChest(RoyalCommands instance) {
        plugin = instance;
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("enderchest")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.enderchest")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
                return true;
            }
            if (args.length < 2) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            if (!Config.separateInv || !Config.separateEnder) {
                cs.sendMessage(MessageColor.NEGATIVE + "Cannot open ender chests unless they are separated!");
                return true;
            }
            Player p = (Player) cs;
            final OfflinePlayer op = RUtils.getOfflinePlayer(args[0]);
            if (plugin.ah.isAuthorized(op, "rcmds.exempt.enderchest") && !cs.getName().equals(op.getName())) {
                cs.sendMessage(MessageColor.NEGATIVE + "You cannot open that player's ender chest!");
                return true;
            }
            if (plugin.getServer().getWorld(args[1]) == null) {
                cs.sendMessage(MessageColor.NEGATIVE + "Invalid world specified!");
                return true;
            }
            final Inventory i = WorldManager.il.getOfflinePlayerEnderInventory(op, args[1]);
            if (i == null) {
                cs.sendMessage(MessageColor.NEGATIVE + "No inventory found!");
                return true;
            }
            p.sendMessage(MessageColor.POSITIVE + "Opened the ender chest of " + MessageColor.NEUTRAL + op.getName() + MessageColor.POSITIVE + ".");
            p.openInventory(i);
            return true;
        }
        return false;
    }

}
