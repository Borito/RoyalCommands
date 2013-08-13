package org.royaldev.royalcommands.rcommands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.Config;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdExplode implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdExplode(RoyalCommands instance) {
        plugin = instance;
    }

    public void explodePlayer(Player p) {
        if (p == null) return;
        p.getLocation().getWorld().createExplosion(p.getLocation(), Config.explodePower, Config.explodeFire);
    }

    public void explodePlayer(Player p, float power) {
        if (p == null) return;
        p.getLocation().getWorld().createExplosion(p.getLocation(), power, Config.explodeFire);
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("explode")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.explode")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1 && !(cs instanceof Player)) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            if (args.length < 1) {
                Player p = (Player) cs;
                Location l = RUtils.getTarget(p).getLocation();
                p.getWorld().createExplosion(l, Config.explodePower, Config.explodeFire);
                return true;
            }
            if (args.length == 2) {
                if (args[1].equalsIgnoreCase("power")) {
                    if (!(cs instanceof Player)) {
                        cs.sendMessage(cmd.getDescription());
                        return false;
                    }
                    Player p = (Player) cs;
                    float power;
                    try {
                        power = Float.parseFloat(args[0]);
                    } catch (Exception e) {
                        cs.sendMessage(MessageColor.NEGATIVE + "That wasn't a valid power!");
                        return true;
                    }
                    if (power > Config.maxExplodePower) {
                        cs.sendMessage(MessageColor.NEGATIVE + "The specified power was higher than the server limit.");
                        cs.sendMessage(MessageColor.NEGATIVE + "Setting power to " + MessageColor.NEUTRAL + Config.maxExplodePower + MessageColor.NEGATIVE + ".");
                        power = Config.maxExplodePower;
                    }
                    Location l = RUtils.getTarget(p).getLocation();
                    p.getWorld().createExplosion(l, power, Config.explodeFire);
                    return true;
                }
            }
            if (args.length > 0) {
                Player t = plugin.getServer().getPlayer(args[0]);
                if (t == null || plugin.isVanished(t, cs)) {
                    cs.sendMessage(MessageColor.NEGATIVE + "That player does not exist!");
                    return true;
                }
                if (plugin.ah.isAuthorized(t, "rcmds.exempt.explode")) {
                    cs.sendMessage(MessageColor.NEGATIVE + "You may not explode that player!");
                    return true;
                }
                if (args.length == 2) {
                    if (args[1].equalsIgnoreCase("power")) {
                        if (!(cs instanceof Player)) {
                            cs.sendMessage(cmd.getDescription());
                            return false;
                        }
                        Player p = (Player) cs;
                        float power;
                        try {
                            power = Float.parseFloat(args[0]);
                        } catch (Exception e) {
                            cs.sendMessage(MessageColor.NEGATIVE + "That wasn't a valid power!");
                            return true;
                        }
                        Location l = RUtils.getTarget(p).getLocation();
                        p.getWorld().createExplosion(l, power, Config.explodeFire);
                        return true;
                    }
                    Float power = null;
                    try {
                        power = Float.parseFloat(args[1]);
                    } catch (Exception e) {
                        explodePlayer(t);
                    } finally {
                        if (power != null) explodePlayer(t, power);
                        else explodePlayer(t);
                    }
                } else {
                    explodePlayer(t);
                }
                cs.sendMessage(MessageColor.POSITIVE + "You have exploded " + MessageColor.NEUTRAL + t.getName() + MessageColor.POSITIVE + ".");
                return true;
            }
        }
        return false;
    }
}
