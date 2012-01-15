package org.royaldev.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.PConfManager;
import org.royaldev.royalcommands.RoyalCommands;

import java.util.HashMap;

public class Message implements CommandExecutor {

    RoyalCommands plugin;

    public Message(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    public static HashMap<Player, CommandSender> replydb = new HashMap<Player, CommandSender>();

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label,
                             String[] args) {
        if (cmd.getName().equalsIgnoreCase("message")) {
            if (!plugin.isAuthorized(cs, "rcmds.message")) {
                cs.sendMessage(ChatColor.RED
                        + "You don't have permission for that!");
                plugin.log.warning("[RoyalCommands] " + cs.getName()
                        + " was denied access to the command!");
                return true;
            }
            if (args.length < 2) {
                return false;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(ChatColor.RED
                        + "This command is only available to players!");
                return true;
            }
            Player t = plugin.getServer().getPlayer(args[0]);
            String m = plugin.getFinalArg(args, 1).trim();
            if (t == null || t.getName().trim().equals("")) {
                cs.sendMessage(ChatColor.RED + "That player is not online!");
                return true;
            }
            if (plugin.isVanished(t)) {
                cs.sendMessage(ChatColor.RED + "That player does not exist!");
                return true;
            }
            if (!replydb.containsKey(t)) {
                replydb.put(t, cs);
            } else if (replydb.containsKey(t)) {
                if (replydb.get(t) != cs) {
                    replydb.remove(t);
                    replydb.put(t, cs);
                }
            }
            if (m == null || m.equals("")) {
                cs.sendMessage(ChatColor.RED + "You entered no message!");
                return true;
            }
            t.sendMessage(ChatColor.GRAY + "[" + ChatColor.BLUE + cs.getName()
                    + ChatColor.GRAY + " -> " + ChatColor.BLUE + "You"
                    + ChatColor.GRAY + "] " + m);
            cs.sendMessage(ChatColor.GRAY + "[" + ChatColor.BLUE + "You"
                    + ChatColor.GRAY + " -> " + ChatColor.BLUE + t.getName()
                    + ChatColor.GRAY + "] " + m);
            for (int i = 0; i < plugin.getServer().getOnlinePlayers().length; i++) {
                if (PConfManager.getPValBoolean(plugin.getServer()
                        .getOnlinePlayers()[i], "spy")) {
                    if (t != plugin.getServer().getOnlinePlayers()[i]
                            || cs != plugin.getServer()
                            .getOnlinePlayers()[i]) {
                        plugin.getServer().getOnlinePlayers()[i]
                                .sendMessage(ChatColor.GRAY + "["
                                        + ChatColor.BLUE + cs.getName()
                                        + ChatColor.GRAY + " -> "
                                        + ChatColor.BLUE + t.getName()
                                        + ChatColor.GRAY + "] " + m);
                    }
                }
            }
            return true;
        }
        return false;
    }

}
