package org.royaldev.royalcommands.rcommands;

import org.royaldev.royalcommands.MessageColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.Config;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.configuration.PConfManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CmdWarn implements CommandExecutor {

    private RoyalCommands plugin;

    public CmdWarn(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("warn")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.warn")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                cs.sendMessage(cmd.getUsage());
                return true;
            }
            OfflinePlayer op = plugin.getServer().getPlayer(args[0]);
            if (op == null) op = plugin.getServer().getOfflinePlayer(args[0]);
            PConfManager pcm = PConfManager.getPConfManager(op);
            if (!pcm.exists()) {
                cs.sendMessage(MessageColor.NEGATIVE + "That player does not exist!");
                return true;
            }
            if (plugin.ah.isAuthorized(op, "rcmds.exempt.warn")) {
                RUtils.dispNoPerms(cs, MessageColor.NEGATIVE + "You can't warn that player!");
                return true;
            }
            List<String> warns = pcm.getStringList("warns");
            if (warns == null) warns = new ArrayList<String>();
            String reason = (args.length > 1) ? RoyalCommands.getFinalArg(args, 1) : Config.defaultWarn;
            reason = RUtils.colorize(reason);
            if (reason.contains("\u00b5")) {
                cs.sendMessage(MessageColor.NEGATIVE + "Reason cannot contain micro sign!");
                return true;
            }
            warns.add(reason + "\u00b5" + new Date().getTime());
            if (Config.warnActions != null && Config.warnActions.getKeys(true).contains(String.valueOf(warns.size())) && Config.warnActions.get(String.valueOf(warns.size())) != null) {
                try {
                    String action = Config.warnActions.getString(String.valueOf(warns.size())).substring(1).replace("{reason}", reason).replace("{player}", op.getName());
                    plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), action);
                } catch (StringIndexOutOfBoundsException ignored) {
                    // catch OOBE, debug further later (no OOBE should happen here)
                }
            }
            if (op.isOnline()) {
                Player t = (Player) op;
                t.sendMessage(MessageColor.NEGATIVE + "You have been warned for " + MessageColor.NEUTRAL + reason + MessageColor.NEGATIVE + " by " + MessageColor.NEUTRAL + cs.getName() + MessageColor.NEGATIVE + ".");
            }
            cs.sendMessage(MessageColor.POSITIVE + "You have warned " + MessageColor.NEUTRAL + op.getName() + MessageColor.POSITIVE + " for " + MessageColor.NEUTRAL + reason + MessageColor.POSITIVE + ".");
            pcm.set("warns", warns);
            return true;
        }
        return false;
    }
}
