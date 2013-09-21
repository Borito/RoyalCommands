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
public class CmdMuteAll implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdMuteAll(RoyalCommands instance) {
        plugin = instance;
    }

    private boolean allMuted = false;

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("muteall")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.muteall")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            Player[] ps = plugin.getServer().getOnlinePlayers();
            for (Player p : ps) {
                if (plugin.isVanished(p, cs) || plugin.ah.isAuthorized(p, "rcmds.exempt.mute"))
                    continue;
                if (cs instanceof Player) {
                    if (p == cs) continue;
                }
                PConfManager pcm = PConfManager.getPConfManager(p);
                if (!allMuted) {
                    pcm.set("muted", true);
                    p.sendMessage(MessageColor.NEGATIVE + "You have been muted!");
                } else {
                    pcm.set("muted", false);
                    p.sendMessage(MessageColor.POSITIVE + "You have been unmuted!");
                }
            }
            if (!allMuted) {
                cs.sendMessage(MessageColor.POSITIVE + "You have muted all players.");
                allMuted = true;
            } else {
                cs.sendMessage(MessageColor.POSITIVE + "You have unmuted all players.");
                allMuted = false;
            }
            return true;
        }
        return false;
    }
}
