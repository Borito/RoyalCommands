package org.royaldev.royalcommands.rcommands;

import org.royaldev.royalcommands.MessageColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.configuration.PConfManager;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdTpToggle implements CommandExecutor {

    private RoyalCommands plugin;

    public CmdTpToggle(RoyalCommands instance) {
        plugin = instance;
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("tptoggle")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.tptoggle")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players.");
                return true;
            }
            Player p = (Player) cs;
            PConfManager pcm = PConfManager.getPConfManager(p);
            if (pcm.getBoolean("allow-tp")) {
                pcm.set("allow-tp", false);
                cs.sendMessage(MessageColor.POSITIVE + "Disabled teleportation.");
                return true;
            }
            pcm.set("allow-tp", true);
            cs.sendMessage(MessageColor.POSITIVE + "Enabled teleportation.");
            return true;
        }
        return false;
    }

}
