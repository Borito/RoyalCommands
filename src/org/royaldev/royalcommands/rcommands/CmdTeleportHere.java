package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdTeleportHere implements CommandExecutor {

    RoyalCommands plugin;

    public CmdTeleportHere(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("teleporthere")) {
            if (!plugin.isAuthorized(cs, "rcmds.teleporthere")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(ChatColor.RED + "This command is only available to players!");
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            Player t = plugin.getServer().getPlayer(args[0].trim());
            if (t == null || plugin.isVanished(t)) {
                cs.sendMessage(ChatColor.RED + "That player does not exist!");
                return true;
            }
            if (!RUtils.isTeleportAllowed(t) && !plugin.isAuthorized(cs, "rcmds.tpoverride")) {
                cs.sendMessage(ChatColor.RED + "That player has teleportation off!");
                return true;
            }
            Player p = (Player) cs;
            p.sendMessage(ChatColor.BLUE + "Teleporting " + ChatColor.GRAY + t.getName() + ChatColor.BLUE + " to you.");
            t.teleport(p.getLocation());
            return true;
        }
        return false;
    }

}
