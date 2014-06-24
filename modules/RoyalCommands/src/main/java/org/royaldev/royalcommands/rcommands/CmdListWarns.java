package org.royaldev.royalcommands.rcommands;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.AuthorizationHandler.PermType;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.configuration.PConfManager;

import java.util.List;

@ReflectCommand
public class CmdListWarns implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdListWarns(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("listwarns")) {
            if (!this.plugin.ah.isAuthorized(cs, cmd)) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player) && args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            if (args.length > 1 && !this.plugin.ah.isAuthorized(cs, cmd, PermType.OTHERS)) {
                RUtils.dispNoPerms(cs, "You're not allowed to view other players' warnings.");
                return true;
            }
            OfflinePlayer op = plugin.getServer().getOfflinePlayer((args.length > 0) ? args[0] : cs.getName());
            PConfManager pcm = PConfManager.getPConfManager(op);
            if (!pcm.exists()) {
                cs.sendMessage(MessageColor.NEGATIVE + "That player does not exist!");
                return true;
            }
            List<String> warns = pcm.getStringList("warns");
            if (warns == null || warns.isEmpty()) {
                cs.sendMessage(MessageColor.NEUTRAL + op.getName() + MessageColor.POSITIVE + " has no warnings!");
                return true;
            }
            for (int i = 0; i < warns.size(); i++)
                cs.sendMessage(MessageColor.NEUTRAL + "" + (i + 1) + ". " + warns.get(i).split("\\u00b5")[0]);
            return true;
        }
        return false;
    }
}
