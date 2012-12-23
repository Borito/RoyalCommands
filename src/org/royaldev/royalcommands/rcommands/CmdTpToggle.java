package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.PConfManager;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdTpToggle implements CommandExecutor {

    private RoyalCommands plugin;

    public CmdTpToggle(RoyalCommands instance) {
        plugin = instance;
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("tptoggle")) {
            if (!plugin.isAuthorized(cs, "rcmds.tptoggle")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(ChatColor.RED + "This command is only available to players.");
                return true;
            }
            Player p = (Player) cs;
            PConfManager pcm = new PConfManager(p);
            if (pcm.getBoolean("allow-tp")) {
                pcm.setBoolean(false, "allow-tp");
                cs.sendMessage(ChatColor.BLUE + "Disabled teleportation.");
                return true;
            }
            pcm.setBoolean(true, "allow-tp");
            cs.sendMessage(ChatColor.BLUE + "Enabled teleportation.");
            return true;
        }
        return false;
    }

}
