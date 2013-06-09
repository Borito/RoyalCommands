package org.royaldev.royalcommands.rcommands;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.configuration.PConfManager;

import java.util.Map;

public class CmdListHome implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdListHome(RoyalCommands instance) {
        plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("listhome")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.listhome")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player) && args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                cs.sendMessage(cmd.getUsage().replace("<command>", label));
                return true;
            }
            OfflinePlayer t;
            if (args.length < 1) t = (OfflinePlayer) cs;
            else {
                if (!plugin.ah.isAuthorized(cs, "rcmds.others.listhome")) {
                    cs.sendMessage(MessageColor.NEGATIVE + "You cannot list other players' homes!");
                    return true;
                }
                t = plugin.getServer().getPlayer(args[0]);
                if (t == null) t = plugin.getServer().getOfflinePlayer(args[0]);
                if (plugin.ah.isAuthorized(t, "rcmds.exempt.listhome")) {
                    cs.sendMessage(MessageColor.NEGATIVE + "You can't list that player's homes!");
                    return true;
                }
            }

            PConfManager pcm = PConfManager.getPConfManager(t);
            if (!pcm.exists()) {
                cs.sendMessage(MessageColor.NEGATIVE + "No such player!");
                return true;
            }
            ConfigurationSection cfgs = pcm.getConfigurationSection("home");
            if (cfgs == null) {
                cs.sendMessage(MessageColor.NEGATIVE + "No homes found!");
                return true;
            }
            final Map<String, Object> opts = cfgs.getValues(false);
            if (opts.keySet().isEmpty()) {
                cs.sendMessage(MessageColor.NEGATIVE + "No homes found!");
                return true;
            }
            StringBuilder homes = new StringBuilder();
            for (String home : opts.keySet()) {
                homes.append(MessageColor.NEUTRAL);
                homes.append(home);
                homes.append(MessageColor.RESET);
                homes.append(", ");
            }
            cs.sendMessage(MessageColor.POSITIVE + "Homes:");
            cs.sendMessage(homes.substring(0, homes.length() - 4));
            return true;
        }
        return false;
    }
}
