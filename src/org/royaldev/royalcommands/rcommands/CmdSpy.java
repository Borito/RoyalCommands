package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.PConfManager;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdSpy implements CommandExecutor {

    RoyalCommands plugin;

    public CmdSpy(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("spy")) {
            if (!plugin.isAuthorized(cs, "rcmds.spy")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(ChatColor.RED + "This command is only available to players!");
                return true;
            }
            Player p = (Player) cs;
            if (PConfManager.getPVal(p, "spy") == null || !PConfManager.getPValBoolean(p, "spy")) {
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
