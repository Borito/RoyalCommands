package org.royaldev.royalcommands.rcommands;

import org.royaldev.royalcommands.MessageColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.Config;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.util.List;

public class CmdEntities implements CommandExecutor {

    private RoyalCommands plugin;

    public CmdEntities(RoyalCommands instance) {
        this.plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("entities")) {
            if (!plugin.isAuthorized(cs, "rcmds.entities")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
                return true;
            }
            if (args.length < 1) {
                Player p = (Player) cs;
                double radius = Config.defaultNear;
                List<Entity> ents = p.getNearbyEntities(radius, radius, radius);
                int amount = 0;
                for (Entity e : ents) {
                    if (e instanceof Player) continue;
                    double dist = p.getLocation().distanceSquared(e.getLocation());
                    p.sendMessage(MessageColor.NEUTRAL + e.getType().getName() + ": " + MessageColor.RESET + Math.sqrt(dist));
                    amount++;
                }
                if (amount == 0) {
                    p.sendMessage(MessageColor.NEGATIVE + "Nothing nearby!");
                    return true;
                }
                return true;
            }
            if (args.length > 0) {
                Player p = (Player) cs;
                Double radius;
                try {
                    radius = Double.parseDouble(args[0]);
                } catch (Exception e) {
                    cs.sendMessage(MessageColor.NEGATIVE + "That was not a valid number!");
                    return true;
                }
                if (radius == null || radius < 1) {
                    cs.sendMessage(MessageColor.NEGATIVE + "That was not a valid number!");
                    return true;
                }
                if (radius > Config.maxNear) {
                    p.sendMessage(MessageColor.NEGATIVE + "That radius was too large!");
                    return true;
                }
                List<Entity> ents = p.getNearbyEntities(radius, radius, radius);
                int amount = 0;
                for (Entity e : ents) {
                    if (e instanceof Player) continue;
                    double dist = p.getLocation().distanceSquared(e.getLocation());
                    p.sendMessage(MessageColor.NEUTRAL + e.getType().getName() + ": " + MessageColor.RESET + Math.sqrt(dist));
                    amount++;
                }
                if (amount == 0) {
                    p.sendMessage(MessageColor.NEGATIVE + "Nothing nearby!");
                    return true;
                }
                return true;
            }
        }
        return false;
    }
}
