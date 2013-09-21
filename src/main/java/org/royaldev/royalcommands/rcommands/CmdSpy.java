package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.configuration.PConfManager;

@ReflectCommand
public class CmdSpy implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdSpy(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("spy")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.spy")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
                return true;
            }
            Player p = (Player) cs;
            PConfManager pcm = PConfManager.getPConfManager(p);
            if (pcm.get("spy") == null || !pcm.getBoolean("spy")) {
                pcm.set("spy", true);
                cs.sendMessage(MessageColor.POSITIVE + "Spy mode enabled.");
                return true;
            }
            if (pcm.getBoolean("spy")) {
                pcm.set("spy", false);
                cs.sendMessage(MessageColor.POSITIVE + "Spy mode disabled.");
                return true;
            }
        }
        return false;
    }

}
