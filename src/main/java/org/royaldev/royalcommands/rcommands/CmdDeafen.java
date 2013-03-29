package org.royaldev.royalcommands.rcommands;

import org.apache.commons.lang.BooleanUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.PConfManager;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdDeafen implements CommandExecutor {

    private RoyalCommands plugin;

    public CmdDeafen(RoyalCommands instance) {
        plugin = instance;
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("deafen")) {
            if (!plugin.isAuthorized(cs, "rcmds.deafen")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1 && !(cs instanceof Player)) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            String name = (args.length < 1) ? cs.getName() : args[0];
            Player t = plugin.getServer().getPlayer(name);
            if (t == null || plugin.isVanished(t, cs)) {
                cs.sendMessage(ChatColor.RED + "That player does not exist!");
                return true;
            }
            if (plugin.isAuthorized(t, "rcmds.exempt.deafen") && !t.getName().equals(cs.getName())) {
                cs.sendMessage(ChatColor.RED + "You are not allowed to deafen that player!");
                return true;
            }
            PConfManager pcm = PConfManager.getPConfManager(t);
            Boolean isDeaf = pcm.getBoolean("deaf");
            if (isDeaf == null) isDeaf = false;
            pcm.set("deaf", !isDeaf);
            cs.sendMessage(ChatColor.BLUE + "Toggled deaf " + ChatColor.GRAY + BooleanUtils.toStringOnOff(!isDeaf) + ChatColor.BLUE + " for " + ChatColor.GRAY + t.getName() + ChatColor.BLUE + ".");
            return true;
        }
        return false;
    }

}
