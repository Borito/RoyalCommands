package org.royaldev.royalcommands.rcommands;

import org.royaldev.royalcommands.MessageColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdTeleportHere implements CommandExecutor {

    private RoyalCommands plugin;

    public CmdTeleportHere(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("teleporthere")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.teleporthere")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                cs.sendMessage(cmd.getUsage().replace("<command>", label));
                return true;
            }
            Player t = plugin.getServer().getPlayer(args[0]);
            if (t == null || plugin.isVanished(t, cs)) {
                cs.sendMessage(MessageColor.NEGATIVE + "That player does not exist!");
                return true;
            }
            if (!RUtils.isTeleportAllowed(t) && !plugin.ah.isAuthorized(cs, "rcmds.tpoverride")) {
                cs.sendMessage(MessageColor.NEGATIVE + "That player has teleportation off!");
                return true;
            }
            Player p = (Player) cs;
            p.sendMessage(MessageColor.POSITIVE + "Teleporting " + MessageColor.NEUTRAL + t.getName() + MessageColor.POSITIVE + " to you.");
            String error = RUtils.teleport(t, p);
            if (!error.isEmpty()) {
                p.sendMessage(MessageColor.NEGATIVE + error);
                return true;
            }
            return true;
        }
        return false;
    }

}
