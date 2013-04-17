package org.royaldev.royalcommands.rcommands;

import org.royaldev.royalcommands.MessageColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.configuration.PConfManager;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.util.Map;

public class CmdListHome implements CommandExecutor {

    private RoyalCommands plugin;

    public CmdListHome(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("listhome")) {
            if (!plugin.isAuthorized(cs, "rcmds.listhome")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player) && args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            OfflinePlayer t;
            if (args.length < 1) t = (OfflinePlayer) cs;
            else {
                if (!plugin.isAuthorized(cs, "rcmds.others.listhome")) {
                    cs.sendMessage(MessageColor.NEGATIVE + "You cannot list other players' homes!");
                    return true;
                }
                t = plugin.getServer().getPlayer(args[0]);
                if (t == null) t = plugin.getServer().getOfflinePlayer(args[0]);
                if (plugin.isAuthorized(t, "rcmds.exempt.listhome")) {
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
            String homes = opts.keySet().toString();
            homes = homes.substring(1, homes.length() - 1);
            cs.sendMessage(MessageColor.POSITIVE + "Homes:");
            cs.sendMessage(homes);
            return true;
        }
        return false;
    }
}
