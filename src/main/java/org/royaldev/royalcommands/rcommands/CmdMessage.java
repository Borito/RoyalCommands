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

    private RoyalCommands plugin;

    public CmdMessage(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    public final static HashMap<String, String> replydb = new HashMap<String, String>();

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("message")) {
            if (!plugin.isAuthorized(cs, "rcmds.message")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 2) {
                return false;
            }
            Player t = plugin.getServer().getPlayer(args[0]);
            String m = RoyalCommands.getFinalArg(args, 1).trim();
            if (t == null || t.getName().trim().equals("")) {
                cs.sendMessage(ChatColor.RED + "That player is not online!");
                return true;
            }
            if (plugin.isVanished(t, cs)) {
                cs.sendMessage(ChatColor.RED + "That player does not exist!");
                return true;
            }
            synchronized (replydb) {
                replydb.put(t.getName(), cs.getName());
                replydb.put(cs.getName(), t.getName());
            }

            if (m == null || m.equals("")) {
                cs.sendMessage(ChatColor.RED + "You entered no message!");
                return true;
            }
            t.sendMessage(ChatColor.GRAY + "[" + ChatColor.BLUE + cs.getName() + ChatColor.GRAY + " -> " + ChatColor.BLUE + "You" + ChatColor.GRAY + "] " + m);
            cs.sendMessage(ChatColor.GRAY + "[" + ChatColor.BLUE + "You" + ChatColor.GRAY + " -> " + ChatColor.BLUE + t.getName() + ChatColor.GRAY + "] " + m);
            Player[] ps = plugin.getServer().getOnlinePlayers();
            for (Player p1 : ps) {
                if (new PConfManager(p1).getBoolean("spy")) {
                    if (t == p1 || cs == p1) continue;
                    p1.sendMessage(ChatColor.GRAY + "[" + ChatColor.BLUE + cs.getName() + ChatColor.GRAY + " -> " + ChatColor.BLUE + t.getName() + ChatColor.GRAY + "] " + m);
                }
            }
            return true;
        }
        return false;
    }

}
