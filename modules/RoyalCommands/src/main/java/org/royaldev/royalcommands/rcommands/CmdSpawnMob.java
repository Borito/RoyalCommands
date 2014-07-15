package org.royaldev.royalcommands.rcommands;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.Config;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

@ReflectCommand
public class CmdSpawnMob extends BaseCommand {

    public CmdSpawnMob(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player)) {
            cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
            return true;
        }
        Player p = (Player) cs;
        if (args.length < 1) {
            StringBuilder sb = new StringBuilder();
            for (EntityType et : EntityType.values()) {
                if (!et.isAlive()) continue;
                if (!et.isSpawnable()) continue;
                sb.append(MessageColor.RESET);
                sb.append(", ");
                sb.append(MessageColor.NEUTRAL);
                sb.append(et.toString().toLowerCase());
            }
            cs.sendMessage(sb.substring(4));
            cs.sendMessage(cmd.getDescription());
            return false;
        }
        Block bb = RUtils.getTarget(p);
        EntityType c;
        Location l = bb.getLocation();
        l.setY(l.getY() + 1);
        try {
            c = EntityType.valueOf(args[0].toUpperCase());
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            for (EntityType et : EntityType.values()) {
                if (!et.isAlive()) continue;
                if (!et.isSpawnable()) continue;
                sb.append(MessageColor.RESET);
                sb.append(", ");
                sb.append(MessageColor.NEUTRAL);
                sb.append(et.toString().toLowerCase());
            }
            cs.sendMessage(sb.substring(4));
            cs.sendMessage(MessageColor.NEGATIVE + "Invalid mob!");
            return true;
        }
        if (!this.ah.isAuthorized(cs, "rcmds.spawnmob." + c.name().toLowerCase()) && !this.ah.isAuthorized(cs, "rcmds.spawnmob.*")) {
            cs.sendMessage(MessageColor.NEGATIVE + "You cannot use mob type " + MessageColor.NEUTRAL + c.name().toLowerCase() + MessageColor.NEGATIVE + ".");
            return true;
        }
        if (args.length > 1) {
            int i;
            try {
                i = Integer.parseInt(args[1]);
            } catch (Exception e) {
                cs.sendMessage(MessageColor.NEGATIVE + "Invalid amount specified!");
                return true;
            }
            if (i < 0) {
                cs.sendMessage(MessageColor.NEGATIVE + "Invalid amount specified!");
                return true;
            }
            if (i > Config.spawnmobLimit && !this.ah.isAuthorized(cs, "rcmds.exempt.limit.spawnmob")) {
                cs.sendMessage(MessageColor.NEGATIVE + "The amount specified was larger than the allowed amount.");
                cs.sendMessage(MessageColor.NEGATIVE + "Setting amount to " + MessageColor.NEUTRAL + Config.spawnmobLimit + MessageColor.NEGATIVE + ".");
                i = Config.spawnmobLimit;
            }
            try {
                for (int a = 0; a < i; a++) p.getWorld().spawnEntity(l, c);
                cs.sendMessage(MessageColor.POSITIVE + "Spawned " + MessageColor.NEUTRAL + i + MessageColor.POSITIVE + " of " + MessageColor.NEUTRAL + c.name().toLowerCase() + MessageColor.POSITIVE + ".");
            } catch (Exception e) {
                cs.sendMessage(MessageColor.NEGATIVE + "Uh-oh! This mob is not currently working with this command.");
            }
            return true;
        }
        Entity spawned;
        try {
            spawned = p.getWorld().spawnEntity(l, c);
        } catch (Exception e) {
            cs.sendMessage(MessageColor.NEGATIVE + "Uh-oh! This mob is not currently working with this command.");
            return true;
        }
        if (spawned != null)
            cs.sendMessage(MessageColor.POSITIVE + "Spawned " + MessageColor.NEUTRAL + "1" + MessageColor.POSITIVE + " of " + MessageColor.NEUTRAL + c.name().toLowerCase() + MessageColor.POSITIVE + ".");
        else
            cs.sendMessage(MessageColor.NEGATIVE + "Could not spawn " + MessageColor.NEUTRAL + c.name().toLowerCase() + MessageColor.NEGATIVE + ".");
        return true;
    }
}
