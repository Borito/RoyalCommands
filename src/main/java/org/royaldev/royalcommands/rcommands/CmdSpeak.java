package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdSpeak implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdSpeak(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("speak")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.speak")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 2) {
                cs.sendMessage(cmd.getDescription());
                cs.sendMessage(cmd.getUsage().replace("<command>", label));
                return true;
            }

            Player victim;

            victim = plugin.getServer().getPlayer(args[0]);

            if (victim == null || plugin.isVanished(victim, cs)) {
                cs.sendMessage(MessageColor.NEGATIVE + "That player does not exist!");
                return true;
            }
            if (args[1].startsWith("/")) {
                cs.sendMessage(MessageColor.NEGATIVE + "You may not send commands!");
                return true;
            }
            if (plugin.ah.isAuthorized(victim, "rcmds.exempt.speak")) {
                cs.sendMessage(MessageColor.NEGATIVE + "You may not make that player speak.");
                return true;
            }
            victim.chat(RoyalCommands.getFinalArg(args, 1));
            plugin.log.info(cs.getName() + " has spoofed a message from " + victim.getName() + "!");
            return true;
        }
        return false;
    }

}
