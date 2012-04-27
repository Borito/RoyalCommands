package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdSpeak implements CommandExecutor {

    RoyalCommands plugin;

    public CmdSpeak(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("speak")) {
            if (!plugin.isAuthorized(cs, "rcmds.speak")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 2) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }

            Player victim;

            victim = plugin.getServer().getPlayer(args[0]);

            if (victim == null || plugin.isVanished(victim)) {
                cs.sendMessage(ChatColor.RED + "That player does not exist!");
                return true;
            }
            if (args[1].startsWith("/")) {
                cs.sendMessage(ChatColor.RED + "You may not send commands!");
                return true;
            }
            if (plugin.isAuthorized(victim, "rcmds.exempt.speak")) {
                cs.sendMessage(ChatColor.RED
                        + "You may not make that player speak.");
                return true;
            }
            victim.chat(plugin.getFinalArg(args, 1));
            plugin.log.info(cs.getName() + " has spoofed a message from " + victim.getName() + "!");
            return true;
        }
        return false;
    }

}
