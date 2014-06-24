package org.royaldev.royalcommands.rcommands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.AuthorizationHandler.PermType;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.configuration.PConfManager;

@ReflectCommand
public class CmdHome implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdHome(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("home")) {
            if (!this.plugin.ah.isAuthorized(cs, cmd)) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
                return true;
            }
            final Player p = (Player) cs;
            String homeName = (args.length < 1) ? "home" : args[0];
            String homeOwner = cs.getName();
            if (homeName.contains(":")) {
                String[] split = homeName.split(":");
                if (split.length < 2) {
                    cs.sendMessage(MessageColor.NEGATIVE + "Home name format invalid!");
                    return true;
                }
                if (!this.plugin.ah.isAuthorized(cs, cmd, PermType.OTHERS) && !cs.getName().equalsIgnoreCase(split[0])) {
                    cs.sendMessage(MessageColor.NEGATIVE + "You are not allowed to use other players' homes.");
                    return true;
                }
                homeOwner = split[0];
                homeName = split[1];
            }
            final PConfManager pcm = PConfManager.getPConfManager(plugin.getServer().getOfflinePlayer(homeOwner));
            if (!pcm.exists()) {
                cs.sendMessage(MessageColor.NEGATIVE + "No such player exists!");
                return true;
            }
            String homePath = "home." + homeName;
            if (!pcm.contains(homePath)) {
                cs.sendMessage(MessageColor.NEGATIVE + "The home " + MessageColor.NEUTRAL + homeName + MessageColor.NEGATIVE + " for " + MessageColor.NEUTRAL + homeOwner + MessageColor.NEGATIVE + " does not exist.");
                return true;
            }
            Location l;
            try {
                l = pcm.getLocation(homePath);
            } catch (Exception e) {
                cs.sendMessage(MessageColor.NEGATIVE + "There was an error loading that home!");
                cs.sendMessage(MessageColor.NEGATIVE + e.getClass().getSimpleName() + MessageColor.NEUTRAL + ": " + e.getMessage());
                return true;
            }
            String error = RUtils.teleport(p, l);
            if (!error.isEmpty()) cs.sendMessage(MessageColor.NEGATIVE + error);
            else
                cs.sendMessage(MessageColor.POSITIVE + "Teleported to home " + MessageColor.NEUTRAL + homeName + MessageColor.POSITIVE + " for " + MessageColor.NEUTRAL + homeOwner + MessageColor.POSITIVE + ".");
            return true;
        }
        return false;
    }
}
