package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.PConfManager;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdReply implements CommandExecutor {

    RoyalCommands plugin;

    public CmdReply(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("reply")) {
            if (!plugin.isAuthorized(cs, "rcmds.reply")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            if (CmdMessage.replydb.containsKey(cs)) {
                CommandSender t = CmdMessage.replydb.get(cs);
                if (!CmdMessage.replydb.containsKey(t)) {
                    CmdMessage.replydb.put(t, cs);
                } else if (CmdMessage.replydb.containsKey(t)) {
                    if (CmdMessage.replydb.get(t) != cs) {
                        CmdMessage.replydb.remove(t);
                        CmdMessage.replydb.put(t, cs);
                    }
                }
                if ((t instanceof Player) && !((Player) t).isOnline()) {
                    cs.sendMessage(ChatColor.RED + "That player is offline!");
                    return true;
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
                cs.sendMessage(ChatColor.RED + "You have no one to reply to!");
                return true;
            }
        }
        return false;
    }

}
