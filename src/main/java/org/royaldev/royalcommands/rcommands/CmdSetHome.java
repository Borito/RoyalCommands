package org.royaldev.royalcommands.rcommands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.configuration.PConfManager;

@ReflectCommand
public class CmdSetHome implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdSetHome(RoyalCommands instance) {
        plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("sethome")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.sethome")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length > 0 && !plugin.ah.isAuthorized(cs, "rcmds.sethome.multi")) {
                RUtils.dispNoPerms(cs, MessageColor.NEGATIVE + "You don't have permission for multiple homes!");
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
                return true;
            }
            Player p = (Player) cs;
            final Location l = p.getLocation();
            PConfManager pcm = PConfManager.getPConfManager(p);
            String name = "";
            if (args.length > 0) name = args[0];
            if (name.contains(":")) {
                cs.sendMessage(MessageColor.NEGATIVE + "The name of your home cannot contain \":\"!");
                return true;
            }
            int limit = RUtils.getHomeLimit(p);
            int curHomes = RUtils.getCurrentHomes(p);
            if (limit == 0) {
                RUtils.dispNoPerms(cs, MessageColor.NEGATIVE + "Your home limit is set to " + MessageColor.NEUTRAL + "0" + MessageColor.NEGATIVE + "!");
                cs.sendMessage(MessageColor.NEGATIVE + "You can't set any homes!");
                return true;
            } else if (curHomes >= limit && limit > -1) {
                RUtils.dispNoPerms(cs, MessageColor.NEGATIVE + "You've reached your max number of homes! (" + MessageColor.NEUTRAL + limit + MessageColor.NEGATIVE + ")");
                return true;
            }
            String homePath = (name.equals("")) ? "home.home" : "home." + name;
            pcm.setLocation(homePath, l);
            if (args.length > 0) {
                p.sendMessage(MessageColor.POSITIVE + "Home " + MessageColor.NEUTRAL + name + MessageColor.POSITIVE + " set.");
            } else {
                p.sendMessage(MessageColor.POSITIVE + "Home set.");
            }
            return true;
        }
        return false;
    }

}
