package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.PConfManager;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

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
            if (!plugin.isAuthorized(cs, "rcmds.warn")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            OfflinePlayer op = plugin.getServer().getPlayer(args[0]);
            if (op == null) op = plugin.getServer().getOfflinePlayer(args[0]);
            PConfManager pcm = new PConfManager(op);
            if (!pcm.exists()) {
                cs.sendMessage(ChatColor.RED + "That player does not exist!");
                return true;
            }
            if (plugin.isAuthorized(op, "rcmds.exempt.warn")) {
                RUtils.dispNoPerms(cs, ChatColor.RED + "You can't warn that player!");
                return true;
            }
            List<String> warns = pcm.getStringList("warns");
            if (warns == null) warns = new ArrayList<String>();
            String reason = (args.length > 1) ? RoyalCommands.getFinalArg(args, 1) : plugin.defaultWarn;
            reason = RUtils.colorize(reason);
            if (reason.contains("\u00b5")) {
                cs.sendMessage(ChatColor.RED + "Reason cannot contain micro sign!");
                return true;
            }
            warns.add(reason + "\u00b5" + new Date().getTime());
            if (plugin.warnActions != null && plugin.warnActions.getKeys(true).contains(String.valueOf(warns.size())) && plugin.warnActions.get(String.valueOf(warns.size())) != null) {
                try {
                    String action = plugin.warnActions.getString(String.valueOf(warns.size())).substring(1).replace("{reason}", reason).replace("{player}", op.getName());
                    plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), action);
                } catch (StringIndexOutOfBoundsException ignored) {
                    // catch OOBE, debug further later (no OOBE should happen here)
                }
            }
            if (op.isOnline()) {
                Player t = (Player) op;
                t.sendMessage(ChatColor.RED + "You have been warned for " + ChatColor.GRAY + reason + ChatColor.RED + " by " + ChatColor.GRAY + cs.getName() + ChatColor.RED + ".");
            }
            cs.sendMessage(ChatColor.BLUE + "You have warned " + ChatColor.GRAY + op.getName() + ChatColor.BLUE + " for " + ChatColor.GRAY + reason + ChatColor.BLUE + ".");
            pcm.setStringList(warns, "warns");
            return true;
        }
        return false;
    }
}
