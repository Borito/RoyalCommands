package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.PConfManager;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdClearWarns implements CommandExecutor {

    private RoyalCommands plugin;

    public CmdClearWarns(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("clearwarns")) {
            if (!plugin.isAuthorized(cs, "rcmds.clearwarns")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            OfflinePlayer op = plugin.getServer().getOfflinePlayer(args[0]);
            PConfManager pcm = plugin.getUserdata(op);
            if (!pcm.exists()) {
                cs.sendMessage(ChatColor.RED + "That player does not exist!");
                return true;
            }
            if (pcm.get("warns") == null || pcm.getStringList("warns").isEmpty()) {
                cs.sendMessage(ChatColor.RED + "There are no warnings for " + ChatColor.GRAY + op.getName() + ChatColor.RED + "!");
                return true;
            }
            pcm.set("warns", null);
            cs.sendMessage(ChatColor.BLUE + "You've cleared the warnings of " + ChatColor.GRAY + op.getName() + ChatColor.BLUE + ".");
            return true;
        }
        return false;
    }

}
