package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdGamemode implements CommandExecutor {

    RoyalCommands plugin;

    public CmdGamemode(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("gamemode")) {
            if (!plugin.isAuthorized(cs, "rcmds.gamemode")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player) && args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            if (args.length < 1) {
                Player p = (Player) cs;
                GameMode toSet = (p.getGameMode().equals(GameMode.CREATIVE)) ? GameMode.SURVIVAL : GameMode.CREATIVE;
                p.setGameMode(toSet);
                p.sendMessage(ChatColor.BLUE + "Your game mode has been set to " + ChatColor.GRAY + toSet.name().toLowerCase() + ChatColor.BLUE + ".");
                return true;
            }
            if (args.length > 0) {
                Player t = plugin.getServer().getPlayer(args[0].trim());
                if (t == null || plugin.isVanished(t)) {
                    cs.sendMessage(ChatColor.RED + "That player does not exist!");
                    return true;
                }
                if (!plugin.isAuthorized(cs, "rcmds.others.gamemode")) {
                    cs.sendMessage(ChatColor.RED + "You can't change other players' gamemodes!");
                    return true;
                }
                if (plugin.isAuthorized(t, "rcmds.exempt.gamemode")) {
                    cs.sendMessage(ChatColor.RED + "You cannot change that player's gamemode.");
                    return true;
                }
                GameMode toSet = (t.getGameMode().equals(GameMode.CREATIVE)) ? GameMode.SURVIVAL : GameMode.CREATIVE;
                t.setGameMode(toSet);
                cs.sendMessage(ChatColor.BLUE + "You have changed " + ChatColor.GRAY + t.getName() + "\'s" + ChatColor.BLUE + " game mode to " + ChatColor.GRAY + toSet.name().toLowerCase() + ChatColor.BLUE + ".");
                t.sendMessage(ChatColor.BLUE + "Your game mode has been changed to " + ChatColor.GRAY + toSet.name().toLowerCase() + ChatColor.BLUE + ".");
                return true;
            }
        }
        return false;
    }
}