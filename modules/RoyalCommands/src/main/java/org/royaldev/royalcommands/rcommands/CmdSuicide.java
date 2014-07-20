package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RoyalCommands;

@ReflectCommand
public class CmdSuicide extends BaseCommand {

    public CmdSuicide(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player)) {
            cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
            return true;
        }
        Player p = (Player) cs;
        p.setLastDamageCause(new EntityDamageByEntityEvent(p, p, DamageCause.SUICIDE, 0D));
        p.setHealth(0);
        this.plugin.getServer().broadcastMessage(MessageColor.NEGATIVE + "The player " + MessageColor.NEUTRAL + p.getDisplayName() + MessageColor.NEGATIVE + " committed suicide.");
        return true;
    }
}
