package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdExplode implements CommandExecutor {

    RoyalCommands plugin;

    public CmdExplode(RoyalCommands instance) {
        this.plugin = instance;
    }

    public void explodePlayer(Player p) {
        if (p == null) return;
        p.getLocation().getWorld().createExplosion(p.getLocation(), plugin.explodePower, plugin.explodeFire);
    }

    public void explodePlayer(Player p, float power) {
        if (p == null) return;
        p.getLocation().getWorld().createExplosion(p.getLocation(), power, plugin.explodeFire);
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("explode")) {
            if (!plugin.isAuthorized(cs, "rcmds.explode")) {
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
                p.getWorld().createExplosion(l, plugin.explodePower, plugin.explodeFire);
                return true;
            }
            if (args.length == 2) {
                if (args[1].equalsIgnoreCase("power")) {
                    if (!(cs instanceof Player)) {
                        cs.sendMessage(cmd.getDescription());
                        return false;
                    }
                    Player p = (Player) cs;
                    Float power;
                    try {
                        power = Float.parseFloat(args[0]);
                    } catch (Exception e) {
                        cs.sendMessage(ChatColor.RED + "That wasn't a valid power!");
                        return true;
                    }
                    if (power == null) {
                        cs.sendMessage(ChatColor.RED + "That wasn't a valid power!");
                        return true;
                    }
                    if (power > plugin.maxExplodePower) {
                        cs.sendMessage(ChatColor.RED + "The specified power was higher than the server limit.");
                        cs.sendMessage(ChatColor.RED + "Setting power to " + ChatColor.GRAY + plugin.maxExplodePower + ChatColor.RED + ".");
                        power = plugin.maxExplodePower;
                    }
                    Location l = RUtils.getTarget(p).getLocation();
                    p.getWorld().createExplosion(l, power, plugin.explodeFire);
                    return true;
                }
            }
            if (args.length > 0) {
                Player t = plugin.getServer().getPlayer(args[0].trim());
                if (t == null || plugin.isVanished(t)) {
                    cs.sendMessage(ChatColor.RED + "That player does not exist!");
                    return true;
                }
                if (plugin.isAuthorized(t, "rcmds.exempt.explode")) {
                    cs.sendMessage(ChatColor.RED + "You may not explode that player!");
                    return true;
                }
                if (args.length == 2) {
                    if (args[1].trim().equalsIgnoreCase("power")) {
                        if (!(cs instanceof Player)) {
                            cs.sendMessage(cmd.getDescription());
                            return false;
                        }
                        Player p = (Player) cs;
                        Float power;
                        try {
                            power = Float.parseFloat(args[0]);
                        } catch (Exception e) {
                            cs.sendMessage(ChatColor.RED + "That wasn't a valid power!");
                            return true;
                        }
                        if (power == null) {
                            cs.sendMessage(ChatColor.RED + "That wasn't a valid power!");
                            return true;
                        }
                        Location l = RUtils.getTarget(p).getLocation();
                        p.getWorld().createExplosion(l, power, plugin.explodeFire);
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
                cs.sendMessage(ChatColor.BLUE + "You have exploded " + ChatColor.GRAY + t.getName() + ChatColor.BLUE + "!");
                return true;
            }
        }
        return false;
    }
}
