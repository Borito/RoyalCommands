package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

@ReflectCommand
public class CmdSuicide implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdSuicide(RoyalCommands instance) {
        plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("suicide")) {
            if (!this.plugin.ah.isAuthorized(cs, cmd)) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
                return true;
            }
            Player p = (Player) cs;
            p.setLastDamageCause(new EntityDamageByEntityEvent(p, p, EntityDamageEvent.DamageCause.SUICIDE, 0D));
            p.setHealth(0);
            plugin.getServer().broadcastMessage(MessageColor.NEGATIVE + "The player " + MessageColor.NEUTRAL + p.getDisplayName() + MessageColor.NEGATIVE + " committed suicide.");
            return true;
        }
        return false;
    }
}
