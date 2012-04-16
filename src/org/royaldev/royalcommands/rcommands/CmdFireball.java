package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdFireball implements CommandExecutor {

    RoyalCommands plugin;

    public CmdFireball(RoyalCommands instance) {
        plugin = instance;
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("fireball")) {
            if (!plugin.isAuthorized(cs, "rcmds.fireball")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(ChatColor.RED + "This command is only available to players!");
                return true;
            }
            Player p = (Player) cs;
            //Fireball fb = p.launchProjectile(Fireball.class);
            Vector dir = p.getEyeLocation().getDirection().multiply(2);
            Fireball fb = p.getWorld().spawn(p.getEyeLocation().add(dir.getX(), dir.getY(), dir.getZ()), Fireball.class);
            fb.setDirection(dir);
            //fb.teleport(p.getEyeLocation().add(dir.getX(), dir.getY(), dir.getZ()));
            fb.setIsIncendiary(true);
            return true;
        }
        return false;
    }

}
