package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

@ReflectCommand
public class CmdRide implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdRide(RoyalCommands instance) {
        plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("ride")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.ride")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(MessageColor.NEGATIVE + "Only players are allowed to use this command.");
                return true;
            }
            Player p = (Player) cs;
            if (args.length < 1) {
                if (p.getVehicle() == null) {
                    p.sendMessage(cmd.getDescription());
                    return false;
                }
                p.getVehicle().eject();
                p.sendMessage(MessageColor.POSITIVE + "You have ejected.");
                return true;
            }
            if (args.length > 0) {
                Player t = plugin.getServer().getPlayer(args[0]);
                if (t == null || plugin.isVanished(t, cs)) {
                    p.sendMessage(MessageColor.NEGATIVE + "That player does not exist!");
                    return true;
                }
                if (p.equals(t)) {
                    cs.sendMessage(MessageColor.NEGATIVE + "You cannot ride yourself.");
                    return true;
                }
                if (plugin.ah.isAuthorized(t, "rcmds.exempt.ride")) {
                    cs.sendMessage(MessageColor.NEGATIVE + "You cannot ride that player!");
                    return true;
                }
                t.setPassenger(p);
                return true;
            }
        }
        return false;
    }

}
