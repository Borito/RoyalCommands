package org.royaldev.royalcommands.rcommands;

import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

@SuppressWarnings("unchecked")
@ReflectCommand
public class CmdBackpack implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdBackpack(RoyalCommands instance) {
        plugin = instance;
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("backpack")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.backpack")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
                return true;
            }
            Player p = (Player) cs;
            if (args.length > 0) {
                if (!plugin.ah.isAuthorized(p, "rcmds.others.backpack")) {
                    RUtils.dispNoPerms(cs);
                    return true;
                }
                if (args.length < 2) {
                    cs.sendMessage(MessageColor.NEGATIVE + "No world specified.");
                    return true;
                }
                World w = plugin.getServer().getWorld(args[1]);
                if (w == null) {
                    cs.sendMessage(MessageColor.NEGATIVE + "No such world!");
                    return true;
                }
                OfflinePlayer t = plugin.getServer().getPlayer(args[0]);
                if (t == null) t = plugin.getServer().getOfflinePlayer(args[0]);
                if (plugin.ah.isAuthorized(t, "rcmds.exempt.backpack")) {
                    RUtils.dispNoPerms(cs, MessageColor.NEGATIVE + "You cannot access that player's backpack!");
                    return true;
                }
                Inventory i = RUtils.getBackpack(t.getName(), w);
                if (i == null) {
                    cs.sendMessage(MessageColor.NEGATIVE + "That player does not exist!");
                    return true;
                }
                p.openInventory(i);
                return true;
            }
            Inventory i = RUtils.getBackpack(p);
            p.openInventory(i);
            return true;
        }
        return false;
    }

}
