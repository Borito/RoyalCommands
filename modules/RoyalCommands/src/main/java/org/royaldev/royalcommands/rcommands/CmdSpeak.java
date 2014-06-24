package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.AuthorizationHandler.PermType;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

@ReflectCommand
public class CmdSpeak implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdSpeak(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("speak")) {
            if (!this.plugin.ah.isAuthorized(cs, cmd)) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 2) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }

            Player t;

            t = plugin.getServer().getPlayer(args[0]);

            if (t == null || plugin.isVanished(t, cs)) {
                cs.sendMessage(MessageColor.NEGATIVE + "That player does not exist!");
                return true;
            }
            if (args[1].startsWith("/")) {
                cs.sendMessage(MessageColor.NEGATIVE + "You may not send commands!");
                return true;
            }
            if (this.plugin.ah.isAuthorized(t, cmd, PermType.EXEMPT)) {
                cs.sendMessage(MessageColor.NEGATIVE + "You may not make that player speak.");
                return true;
            }
            t.chat(RoyalCommands.getFinalArg(args, 1));
            plugin.log.info(cs.getName() + " has spoofed a message from " + t.getName() + "!");
            return true;
        }
        return false;
    }

}
