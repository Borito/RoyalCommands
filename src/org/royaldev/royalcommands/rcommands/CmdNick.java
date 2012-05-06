package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.PConfManager;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

@SuppressWarnings("unused")
public class CmdNick implements CommandExecutor {

    RoyalCommands plugin;

    public CmdNick(RoyalCommands instance) {
        this.plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("nick")) {
            if (!plugin.isAuthorized(cs, "rcmds.nick")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                if (!(cs instanceof Player)) {
                    cs.sendMessage(cmd.getDescription());
                    return false;
                }
                Player p = (Player) cs;
                String newname = p.getName();
                p.setDisplayName(newname);
                PConfManager.setPValString(p, newname, "dispname");
                p.sendMessage(ChatColor.BLUE + "Your name has been reset to " + ChatColor.GRAY + newname + ChatColor.BLUE + ".");
                return true;
            }
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("help") || args[0].equals("?")) {
                    cs.sendMessage(cmd.getDescription());
                    return false;
                }
                if (!(cs instanceof Player)) {
                    cs.sendMessage(cmd.getDescription());
                    return false;
                }
                Player p = (Player) cs;
                String newname = plugin.nickPrefix + args[0];
                if (newname.equalsIgnoreCase("none")) {
                    p.setDisplayName(p.getName());
                    p.setPlayerListName(p.getName());
                    PConfManager.setPValString(p, p.getName(), "dispname");
                    p.sendMessage(ChatColor.BLUE + "Removed your nickname.");
                    return true;
                }
                p.setDisplayName(newname);
                p.setPlayerListName(newname);
                PConfManager.setPValString(p, newname, "dispname");
                p.sendMessage(ChatColor.BLUE + "You have set your nickname to " + ChatColor.GRAY + newname + ChatColor.BLUE + ".");
                return true;
            }

            if (args.length > 1) {
                Player t = plugin.getServer().getPlayer(args[0]);
                if ((cs instanceof Player) && t.equals(cs)) {
                    Player p = (Player) cs;
                    String newname = plugin.nickPrefix + args[0];
                    if (newname.equalsIgnoreCase("none")) {
                        p.setDisplayName(p.getName());
                        p.setPlayerListName(p.getName());
                        PConfManager.setPValString(p, p.getName(), "dispname");
                        p.sendMessage(ChatColor.BLUE + "Removed your nickname.");
                        return true;
                    }
                    p.setDisplayName(newname);
                    p.setPlayerListName(newname);
                    PConfManager.setPValString(p, newname, "dispname");
                    p.sendMessage(ChatColor.BLUE + "You have set your nickname to " + ChatColor.GRAY + newname + ChatColor.BLUE + ".");
                    return true;
                }
                if (!plugin.isAuthorized(cs, "rcmds.others.nick")) {
                    RUtils.dispNoPerms(cs);
                    return true;
                }
                if (t == null || plugin.isVanished(t)) {
                    cs.sendMessage(ChatColor.RED + "That player does not exist!");
                    return true;
                }
                if (plugin.isAuthorized(t, "rcmds.exempt.nick")) {
                    cs.sendMessage(ChatColor.RED + "You cannot change that player's nick!");
                    return true;
                }
                String newname = plugin.nickPrefix + args[0];
                t.setDisplayName(newname);
                t.setPlayerListName(newname);
                PConfManager.setPValString(t, newname, "dispname");
                t.sendMessage(ChatColor.BLUE + "Your display name has been changed to " + ChatColor.GRAY + newname + ChatColor.BLUE + ".");
                cs.sendMessage(ChatColor.BLUE + "You have changed " + ChatColor.GRAY + t.getName() + "\'s" + ChatColor.BLUE + " name to " + ChatColor.GRAY + newname + ChatColor.BLUE + ".");
                return true;
            }
        }
        return false;
    }
}