package org.royaldev.royalcommands.rcommands;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.configuration.PConfManager;

@ReflectCommand
public class CmdCommandSpy implements CommandExecutor {
    private final RoyalCommands plugin;

    public CmdCommandSpy(RoyalCommands instance) {
        this.plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("commandspy")) {
            if (!this.plugin.ah.isAuthorized(cs, "rcmds.commandspy")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
                return true;
            }
            final OfflinePlayer op = (args.length > 0) ? RUtils.getOfflinePlayer(args[0]) : (Player) cs;
            boolean isSamePerson = !op.getName().equalsIgnoreCase(cs.getName());
            if (!isSamePerson && !this.plugin.ah.isAuthorized(cs, "rcmds.others.commandspy")) {
                cs.sendMessage(MessageColor.NEGATIVE + "You don't have permission to toggle command spy for other players.");
                return true;
            }
            if (!isSamePerson && this.plugin.ah.isAuthorized(op, "rcmds.exempt.commandspy")) {
                cs.sendMessage(MessageColor.NEGATIVE + "You can't toggle command spy for this player.");
                return true;
            }
            PConfManager pcm = PConfManager.getPConfManager(op);
            boolean commandSpy = pcm.getBoolean("commandspy", false);
            pcm.set("commandspy", !commandSpy);
            cs.sendMessage(MessageColor.POSITIVE + "Command spy mode " + MessageColor.NEUTRAL + ((!commandSpy) ? "enabled" : "disabled") + MessageColor.POSITIVE + " for " + MessageColor.NEUTRAL + op.getName() + MessageColor.POSITIVE + ".");
            if (op.isOnline() && !op.getName().equalsIgnoreCase(cs.getName()))
                ((Player) op).sendMessage(MessageColor.POSITIVE + "Command spy mode has been " + MessageColor.NEUTRAL + ((!commandSpy) ? "enabled" : "disabled") + MessageColor.POSITIVE + " for you by " + MessageColor.NEUTRAL + cs.getName() + MessageColor.POSITIVE + ".");
            return true;
        }
        return false;
    }
}
