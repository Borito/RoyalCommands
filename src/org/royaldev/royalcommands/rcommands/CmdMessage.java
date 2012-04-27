package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.PConfManager;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.util.HashMap;

public class CmdMessage implements CommandExecutor {

    RoyalCommands plugin;

    public CmdMessage(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    public static HashMap<CommandSender, CommandSender> replydb = new HashMap<CommandSender, CommandSender>();

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label,
                             String[] args) {
        if (cmd.getName().equalsIgnoreCase("message")) {
            if (!plugin.isAuthorized(cs, "rcmds.message")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 2) {
                return false;
            }
            Player t = plugin.getServer().getPlayer(args[0]);
            String m = plugin.getFinalArg(args, 1).trim();
            if (t == null || t.getName().trim().equals("")) {
                cs.sendMessage(ChatColor.RED + "That player is not online!");
                return true;
            }
            if (plugin.isVanished(t) && (cs instanceof Player)) {
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
        }
        return false;
    }

}
