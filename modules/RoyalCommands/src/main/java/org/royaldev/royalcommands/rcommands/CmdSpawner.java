package org.royaldev.royalcommands.rcommands;

import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

@ReflectCommand
public class CmdSpawner extends BaseCommand {

    public CmdSpawner(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] args) {
        if (!(cs instanceof Player)) {
            cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
            return true;
        }
        if (args.length < 1) {
            cs.sendMessage(cmd.getDescription());
            return false;
        }
        Player p = (Player) cs;
        Block bb = RUtils.getTarget(p);
        if (bb == null) {
            cs.sendMessage(MessageColor.NEGATIVE + "No block found!");
            return true;
        }
        if (!(bb.getState() instanceof CreatureSpawner)) {
            cs.sendMessage(MessageColor.NEGATIVE + "That's not a mob spawner!");
            return true;
        }
        CreatureSpawner crs = (CreatureSpawner) bb.getState();
        EntityType ct;
        try {
            ct = EntityType.valueOf(args[0].toUpperCase());
        } catch (Exception e) {
            cs.sendMessage(MessageColor.NEGATIVE + "Invalid mob!");
            return true;
        }
        if (!this.ah.isAuthorized(cs, "rcmds.spawnmob." + ct.name().toLowerCase()) && !this.ah.isAuthorized(cs, "rcmds.spawnmob.*")) {
            cs.sendMessage(MessageColor.NEGATIVE + "You cannot use mob type " + MessageColor.NEUTRAL + ct.name().toLowerCase() + MessageColor.NEGATIVE + ".");
            return true;
        }
        crs.setSpawnedType(ct);
        cs.sendMessage(MessageColor.POSITIVE + "Spawner type set to " + MessageColor.NEUTRAL + crs.getCreatureTypeName().toLowerCase().replace("_", " ") + MessageColor.POSITIVE + ".");
        return true;
    }
}
