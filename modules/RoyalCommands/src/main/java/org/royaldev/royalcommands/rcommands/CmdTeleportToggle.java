package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.configuration.PConfManager;

@ReflectCommand
public class CmdTeleportToggle extends BaseCommand {

    public CmdTeleportToggle(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(CommandSender cs, Command cmd, String label, String[] args) {
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
}
