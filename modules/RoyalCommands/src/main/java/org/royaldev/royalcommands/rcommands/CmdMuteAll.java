package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.AuthorizationHandler.PermType;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.configuration.PConfManager;

@ReflectCommand
public class CmdMuteAll extends BaseCommand {

    private boolean allMuted = false;

    public CmdMuteAll(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(CommandSender cs, Command cmd, String label, String[] args) {
        for (Player p : this.plugin.getServer().getOnlinePlayers()) {
            if (this.plugin.isVanished(p, cs) || this.ah.isAuthorized(p, cmd, PermType.EXEMPT)) continue;
            if (cs instanceof Player) {
                if (p == cs) continue;
            }
            PConfManager pcm = PConfManager.getPConfManager(p);
            if (!this.allMuted) {
                pcm.set("muted", true);
                p.sendMessage(MessageColor.NEGATIVE + "You have been muted!");
            } else {
                pcm.set("muted", false);
                p.sendMessage(MessageColor.POSITIVE + "You have been unmuted!");
            }
        }
        if (!this.allMuted) {
            cs.sendMessage(MessageColor.POSITIVE + "You have muted all players.");
            this.allMuted = true;
        } else {
            cs.sendMessage(MessageColor.POSITIVE + "You have unmuted all players.");
            this.allMuted = false;
        }
        return true;
    }
}
