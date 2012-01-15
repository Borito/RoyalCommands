package org.royaldev.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RoyalCommands;

public class Gamemode implements CommandExecutor {

    RoyalCommands plugin;

    public Gamemode(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label,
                             String[] args) {
        if (cmd.getName().equalsIgnoreCase("gamemode")) {
            if (!plugin.isAuthorized(cs, "rcmds.gamemode")) {
                cs.sendMessage(ChatColor.RED
                        + "You don't have permission for that!");
                plugin.log.warning("[RoyalCommands] " + cs.getName()
                        + " was denied access to the command!");
                return true;
            }
            if (!(cs instanceof Player) && args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            if (args.length < 1) {
                Player p = (Player) cs;
                if (p.getGameMode().equals(GameMode.CREATIVE)) {
                    p.setGameMode(GameMode.SURVIVAL);
                    p.sendMessage(ChatColor.BLUE
                            + "Your game mode has been set to " + ChatColor.GRAY
                            + "survival" + ChatColor.BLUE + ".");
                    return true;
                } else if (p.getGameMode().equals(GameMode.SURVIVAL)) {
                    p.setGameMode(GameMode.CREATIVE);
                    p.sendMessage(ChatColor.BLUE
                            + "Your game mode has been set to " + ChatColor.GRAY
                            + "creative" + ChatColor.BLUE + ".");
                    return true;
                }
            }
            if (args.length > 0) {
                Player t = plugin.getServer().getPlayer(args[0].trim());
                if (t == null || plugin.isVanished(t)) {
                    cs.sendMessage(ChatColor.RED + "That player does not exist!");
                    return true;
                }
                if (plugin.isAuthorized(t, "rcmds.exempt.gamemode")) {
                    cs.sendMessage(ChatColor.RED + "You cannot change that player's gamemode.");
                    return true;
                }
                if (t.getGameMode().equals(GameMode.CREATIVE)) {
                    t.setGameMode(GameMode.SURVIVAL);
                    cs.sendMessage(ChatColor.BLUE + "You have changed " + t.getName() + "\'s" + ChatColor.BLUE + " game mode to " + ChatColor.GRAY + "survival" + ChatColor.BLUE + ".");
                    t.sendMessage(ChatColor.BLUE + "Your game mode has been changed to " + ChatColor.GRAY + "survival" + ChatColor.BLUE + ".");
                    return true;
                }
                if (t.getGameMode().equals(GameMode.SURVIVAL)) {
                    t.setGameMode(GameMode.CREATIVE);
                    cs.sendMessage(ChatColor.BLUE + "You have changed " + t.getName() + "\'s" + ChatColor.BLUE + " game mode to " + ChatColor.GRAY + "creative" + ChatColor.BLUE + ".");
                    t.sendMessage(ChatColor.BLUE + "Your game mode has been changed to " + ChatColor.GRAY + "creative" + ChatColor.BLUE + ".");
                    return true;
                }
            }
        }

        return false;
    }

}
