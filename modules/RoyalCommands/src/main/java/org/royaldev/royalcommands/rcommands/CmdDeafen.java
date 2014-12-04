package org.royaldev.royalcommands.rcommands;

import org.apache.commons.lang.BooleanUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.AuthorizationHandler.PermType;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.configuration.PConfManager;

@ReflectCommand
public class CmdDeafen extends BaseCommand {

    public CmdDeafen(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] args) {
        if (args.length < 1 && !(cs instanceof Player)) {
            cs.sendMessage(cmd.getDescription());
            return false;
        }
        String name = (args.length < 1) ? cs.getName() : args[0];
        if (!name.equalsIgnoreCase(cs.getName()) && !this.ah.isAuthorized(cs, cmd, PermType.OTHERS)) {
            cs.sendMessage(MessageColor.NEGATIVE + "You are not allowed to deafen other players!");
            return true;
        }
        Player t = this.plugin.getServer().getPlayer(name);
        if (t == null || this.plugin.isVanished(t, cs)) {
            cs.sendMessage(MessageColor.NEGATIVE + "That player does not exist!");
            return true;
        }
        if (this.ah.isAuthorized(t, cmd, PermType.EXEMPT) && !t.getName().equals(cs.getName())) {
            cs.sendMessage(MessageColor.NEGATIVE + "You are not allowed to deafen that player!");
            return true;
        }
        PConfManager pcm = PConfManager.getPConfManager(t);
        final boolean isDeaf = pcm.getBoolean("deaf");
        pcm.set("deaf", !isDeaf);
        cs.sendMessage(MessageColor.POSITIVE + "Toggled deaf " + MessageColor.NEUTRAL + BooleanUtils.toStringOnOff(!isDeaf) + MessageColor.POSITIVE + " for " + MessageColor.NEUTRAL + t.getName() + MessageColor.POSITIVE + ".");
        return true;
    }
}
