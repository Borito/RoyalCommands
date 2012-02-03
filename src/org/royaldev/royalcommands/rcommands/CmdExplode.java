package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
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
        Location l = p.getLocation();
        World w = p.getWorld();
        w.createExplosion(l, 4);
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("explode")) {
            if (!plugin.isAuthorized(cs, "rcmds.explode")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            Player t = plugin.getServer().getPlayer(args[0].trim());
            if (t == null || plugin.isVanished(t)) {
                cs.sendMessage(ChatColor.RED + "That player does not exist!");
                return true;
            }
            if (plugin.isAuthorized(t, "rcmds.exempt.explode")) {
                cs.sendMessage(ChatColor.RED + "You may not explode that player!");
                return true;
            }
            explodePlayer(t);
            cs.sendMessage(ChatColor.BLUE + "You have exploded " + ChatColor.GRAY + t.getName() + ChatColor.BLUE + "!");
            return true;
        }
        return false;
    }
}
