package org.royaldev.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.PConfManager;
import org.royaldev.royalcommands.RoyalCommands;

public class Spy implements CommandExecutor {

    RoyalCommands plugin;

    public Spy(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label,
                             String[] args) {
        if (cmd.getName().equalsIgnoreCase("spy")) {
            if (!plugin.isAuthorized(cs, "rcmds.spy")) {
                cs.sendMessage(ChatColor.RED
                        + "You don't have permission for that!");
                plugin.log.warning("[RoyalCommands] " + cs.getName()
                        + " was denied access to the command!");
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(ChatColor.RED
                        + "This command is only available to players!");
                return true;
            }
            Player p = (Player) cs;
            if (PConfManager.getPVal(p, "spy") == null
                    || !PConfManager.getPValBoolean(p, "spy")) {
                PConfManager.setPValBoolean(p, true, "spy");
                cs.sendMessage(ChatColor.BLUE + "Spy mode enabled.");
                return true;
            }
            if (PConfManager.getPValBoolean(p, "spy")) {
                PConfManager.setPValBoolean(p, false, "spy");
                cs.sendMessage(ChatColor.BLUE + "Spy mode disabled.");
                return true;
            }
        }
        return false;
    }

}
