package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class Level implements CommandExecutor {

    RoyalCommands plugin;

    public Level(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label,
                             String[] args) {
        if (cmd.getName().equalsIgnoreCase("level")) {
            if (!(cs instanceof Player)) {
                cs.sendMessage(ChatColor.RED
                        + "This command can only be used by players!");
            }
            if (!plugin.isAuthorized(cs, "rcmds.level")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(ChatColor.RED + "This command is only available to players!");
                return true;
            }
            Player player = (Player) cs;
            player.setLevel(player.getLevel() + 1);
            cs.sendMessage(ChatColor.BLUE + "XP level raised by one!");
            return true;
        }
        return false;
    }
}