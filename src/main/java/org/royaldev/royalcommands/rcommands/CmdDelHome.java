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

public class CmdDelHome implements CommandExecutor {

    private RoyalCommands plugin;

    public CmdDelHome(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("delhome")) {
            if (!plugin.isAuthorized(cs, "rcmds.delhome")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(ChatColor.RED + "Type \"" + ChatColor.GRAY + "/delhome home" + ChatColor.RED + "\" to delete your default home.");
                return true;
            }
            String name = args[0];
            if (!(cs instanceof Player) && !name.contains(":")) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            PConfManager pcm;
            if (name.contains(":") && plugin.isAuthorized(cs, "rcmds.others.delhome")) {
                if (!PConfManager.getPConfManager(name.split(":")[0]).exists()) {
                    cs.sendMessage(ChatColor.RED + "That player does not exist!");
                    return true;
                }
                OfflinePlayer op = plugin.getServer().getOfflinePlayer(name.split(":")[0]);
                if (plugin.isAuthorized(op, "rcmds.exempt.delhome")) {
                    cs.sendMessage(ChatColor.RED + "You cannot delete that player's home!");
                    return true;
                }
                String[] ss = name.split(":");
                if (ss.length < 2) {
                    cs.sendMessage(ChatColor.RED + "You must include the name of the player and home (player:home).");
                    return true;
                }
                pcm = PConfManager.getPConfManager(ss[0]);
                name = ss[1];
            } else {
                pcm = PConfManager.getPConfManager(cs.getName());
            }
            if (pcm.get("home." + name) == null) {
                cs.sendMessage(ChatColor.RED + "That home does not exist!");
                return true;
            }
            pcm.set("home." + name, null);
            cs.sendMessage(ChatColor.BLUE + "The home \"" + ChatColor.GRAY + name + ChatColor.BLUE + "\" has been deleted.");
            return true;
        }
        return false;
    }

}
