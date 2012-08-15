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
            if (args.length < 2) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            OfflinePlayer t = plugin.getServer().getPlayer(args[0]);
            if (t == null) t = plugin.getServer().getOfflinePlayer(args[0]);
            PConfManager pcm = new PConfManager(t);
            if (!pcm.exists()) {
                cs.sendMessage(ChatColor.RED + "That player doesn't exist!");
                return true;
            }
            if (!RUtils.canActAgainst(cs, t.getName(), "nick")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args[1].equalsIgnoreCase("off")) {
                pcm.setString(t.getName(), "dispname");
                if (t.isOnline()) {
                    Player p = (Player) t;
                    p.setDisplayName(t.getName());
                    if (t.getName().length() <= 16) p.setPlayerListName(t.getName());
                    if (!(cs instanceof Player) || !cs.equals(p))
                        p.sendMessage(ChatColor.BLUE + "Your nickname was reset by " + ChatColor.GRAY + cs.getName() + ChatColor.BLUE + ".");
                }
                cs.sendMessage(ChatColor.BLUE + "You reset the nickname of " + ChatColor.GRAY + t.getName() + ChatColor.BLUE + ".");
                return true;
            }
            String newName = plugin.nickPrefix + args[1];
            if (plugin.isAuthorized(cs, "rcmds.nick.color"))
                newName = RUtils.colorize(newName);
            pcm.setString(newName, "dispname");
            if (t.isOnline()) {
                Player p = (Player) t;
                p.setDisplayName(newName);
                if (newName.length() <= 16) p.setPlayerListName(newName);
                if (!(cs instanceof Player) || !cs.equals(p))
                    p.sendMessage(ChatColor.BLUE + "Your nickname was changed to " + ChatColor.GRAY + newName + ChatColor.BLUE + " by " + ChatColor.GRAY + cs.getName() + ChatColor.BLUE + ".");
            }
            cs.sendMessage(ChatColor.BLUE + "Changed the nick of " + ChatColor.GRAY + t.getName() + ChatColor.BLUE + " to " + ChatColor.GRAY + newName + ChatColor.BLUE + ".");
            return true;
        }
        return false;
    }
}
