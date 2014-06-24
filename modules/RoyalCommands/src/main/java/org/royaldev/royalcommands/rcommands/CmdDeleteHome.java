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

@ReflectCommand
public class CmdDeleteHome implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdDeleteHome(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("deletehome")) {
            if (!this.plugin.ah.isAuthorized(cs, cmd)) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(MessageColor.NEGATIVE + "Type \"" + MessageColor.NEUTRAL + "/deletehome home" + MessageColor.NEGATIVE + "\" to delete your default home.");
                return true;
            }
            String name = args[0];
            if (!(cs instanceof Player) && !name.contains(":")) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            PConfManager pcm;
            if (name.contains(":") && this.plugin.ah.isAuthorized(cs, cmd, PermType.OTHERS)) {
                if (!PConfManager.getPConfManager(plugin.getServer().getOfflinePlayer(name.split(":")[0])).exists()) {
                    cs.sendMessage(MessageColor.NEGATIVE + "That player does not exist!");
                    return true;
                }
                OfflinePlayer op = plugin.getServer().getOfflinePlayer(name.split(":")[0]);
                if (this.plugin.ah.isAuthorized(op, cmd, PermType.EXEMPT)) {
                    cs.sendMessage(MessageColor.NEGATIVE + "You cannot delete that player's home!");
                    return true;
                }
                String[] ss = name.split(":");
                if (ss.length < 2) {
                    cs.sendMessage(MessageColor.NEGATIVE + "You must include the name of the player and home (player:home).");
                    return true;
                }
                pcm = PConfManager.getPConfManager(plugin.getServer().getOfflinePlayer(ss[0]));
                name = ss[1];
            } else pcm = PConfManager.getPConfManager(((OfflinePlayer) cs).getUniqueId());
            if (pcm.get("home." + name) == null) {
                cs.sendMessage(MessageColor.NEGATIVE + "That home does not exist!");
                return true;
            }
            pcm.set("home." + name, null);
            cs.sendMessage(MessageColor.POSITIVE + "The home \"" + MessageColor.NEUTRAL + name + MessageColor.POSITIVE + "\" has been deleted.");
            return true;
        }
        return false;
    }

}
