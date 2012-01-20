package org.royaldev.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.PConfManager;
import org.royaldev.royalcommands.RoyalCommands;

public class Reply implements CommandExecutor {

    RoyalCommands plugin;

    public Reply(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label,
                             String[] args) {
        if (cmd.getName().equalsIgnoreCase("reply")) {
            if (!plugin.isAuthorized(cs, "rcmds.reply")) {
                cs.sendMessage(ChatColor.RED
                        + "You don't have permission for that!");
                plugin.log.warning("[RoyalCommands] " + cs.getName()
                        + " was denied access to the command!");
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            if (Message.replydb.containsKey(cs)) {
                CommandSender t = Message.replydb.get(cs);
                if (!Message.replydb.containsKey(t)) {
                    Message.replydb.put(t, cs);
                } else if (Message.replydb.containsKey(t)) {
                    if (Message.replydb.get(t) != cs) {
                        Message.replydb.remove(t);
                        Message.replydb.put(t, cs);
                    }
                }
                String m = plugin.getFinalArg(args, 0).trim();
                cs.sendMessage(ChatColor.GRAY + "[" + ChatColor.BLUE + "You" + ChatColor.GRAY + " -> " + ChatColor.BLUE + t.getName() + ChatColor.GRAY + "] " + m);
                t.sendMessage(ChatColor.GRAY + "[" + ChatColor.BLUE + cs.getName() + ChatColor.GRAY + " -> " + ChatColor.BLUE + "You" + ChatColor.GRAY + "] " + m);
                Player[] ps = plugin.getServer().getOnlinePlayers();
                for (Player p1 : ps) {
                    if (PConfManager.getPValBoolean(p1, "spy")) {
                        if (t == p1 || cs == p1) {
                            continue;
                        }
                        p1.sendMessage(ChatColor.GRAY + "[" + ChatColor.BLUE + cs.getName() + ChatColor.GRAY + " -> " + ChatColor.BLUE + t.getName() + ChatColor.GRAY + "] " + m);
                    }
                }
                return true;
            } else {
                cs.sendMessage(ChatColor.RED
                        + "You have no one to reply to!");
                return true;
            }
        }
        return false;
    }

}
