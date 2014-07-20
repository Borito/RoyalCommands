package org.royaldev.royalcommands.rcommands;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.AuthorizationHandler.PermType;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.configuration.PConfManager;

@ReflectCommand
public class CmdDeleteHome extends CACommand {

    public CmdDeleteHome(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(CommandSender cs, Command cmd, String label, String[] eargs, CommandArguments ca) {
        if (eargs.length < 1) {
            cs.sendMessage(MessageColor.NEGATIVE + "Type \"" + MessageColor.NEUTRAL + "/" + label + " home" + MessageColor.NEGATIVE + "\" to delete your default home.");
            return true;
        }
        String name = eargs[0];
        if (!(cs instanceof Player) && !name.contains(":")) {
            cs.sendMessage(cmd.getDescription());
            return false;
        }
        PConfManager pcm;
        if (name.contains(":") && this.ah.isAuthorized(cs, cmd, PermType.OTHERS)) {
            if (!PConfManager.getPConfManager(RUtils.getOfflinePlayer(name.split(":")[0])).exists()) {
                cs.sendMessage(MessageColor.NEGATIVE + "That player does not exist!");
                return true;
            }
            OfflinePlayer op = RUtils.getOfflinePlayer(name.split(":")[0]);
            if (this.ah.isAuthorized(op, cmd, PermType.EXEMPT)) {
                cs.sendMessage(MessageColor.NEGATIVE + "You cannot delete that player's home!");
                return true;
            }
            String[] ss = name.split(":");
            if (ss.length < 2) {
                cs.sendMessage(MessageColor.NEGATIVE + "You must include the name of the player and home (player:home).");
                return true;
            }
            pcm = PConfManager.getPConfManager(RUtils.getOfflinePlayer(ss[0]));
            name = ss[1];
        } else if (ca.hasContentFlag("p", "player") && this.ah.isAuthorized(cs, cmd, PermType.OTHERS)) {
            final OfflinePlayer op = RUtils.getOfflinePlayer(ca.getFlagString("p", "player"));
            if (this.ah.isAuthorized(op, cmd, PermType.EXEMPT)) {
                cs.sendMessage(MessageColor.NEGATIVE + "You cannot delete that player's home!");
                return true;
            }
            pcm = PConfManager.getPConfManager(op);
        } else pcm = PConfManager.getPConfManager(((OfflinePlayer) cs));
        if (pcm.get("home." + name) == null) {
            cs.sendMessage(MessageColor.NEGATIVE + "That home does not exist!");
            return true;
        }
        pcm.set("home." + name, null);
        cs.sendMessage(MessageColor.POSITIVE + "The home \"" + MessageColor.NEUTRAL + name + MessageColor.POSITIVE + "\" has been deleted.");
        return true;
    }
}
