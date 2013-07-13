package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdClearInventory implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdClearInventory(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("clearinventory")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.clearinventory")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                if (!(cs instanceof Player)) {
                    cs.sendMessage(cmd.getDescription());
                    cs.sendMessage(cmd.getUsage().replace("<command>", label));
                    return true;
                }
                Player p = (Player) cs;
                p.getInventory().clear();
                cs.sendMessage(MessageColor.POSITIVE + "You have cleared your inventory.");
                return true;
            }
        }
        if (args.length == 1) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.others.clearinventory")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            Player target = plugin.getServer().getPlayer(args[0]);
            if (target == null || plugin.isVanished(target, cs)) {
                cs.sendMessage(MessageColor.NEGATIVE + "That player is not online!");
                return true;
            }
            if (plugin.ah.isAuthorized(target, "rcmds.exempt.clearinventory")) {
                cs.sendMessage(MessageColor.NEGATIVE + "You cannot alter that player's inventory!");
                return true;
            }
            cs.sendMessage(MessageColor.POSITIVE + "You have cleared the inventory of " + MessageColor.NEUTRAL + target.getName() + MessageColor.POSITIVE + ".");
            target.sendMessage(MessageColor.NEGATIVE + "Your inventory has been cleared.");
            target.getInventory().clear();
            return true;
        }
        return false;
    }

}
