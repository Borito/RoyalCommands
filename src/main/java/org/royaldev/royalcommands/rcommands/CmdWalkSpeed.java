package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdWalkSpeed implements CommandExecutor {

    private RoyalCommands plugin;

    public CmdWalkSpeed(RoyalCommands instance) {
        plugin = instance;
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("walkspeed")) {
            if (!plugin.isAuthorized(cs, "rcmds.walkspeed")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(ChatColor.RED + "This command is only available to players!");
                return true;
            }
            Player p = (Player) cs;
            float walkSpeed;
            try {
                walkSpeed = Float.valueOf(args[0]);
            } catch (NumberFormatException e) {
                cs.sendMessage(ChatColor.RED + "Please enter a valid number!");
                return true;
            }
            if (walkSpeed < -1F || walkSpeed > 1F) {
                cs.sendMessage(ChatColor.RED + "Speed must be between -1 and 1!");
                return true;
            }
            cs.sendMessage(ChatColor.BLUE + "Set your walk speed to " + ChatColor.GRAY + walkSpeed + ChatColor.BLUE + ".");
            p.setWalkSpeed(walkSpeed);
            return true;
        }
        return false;
    }

}
