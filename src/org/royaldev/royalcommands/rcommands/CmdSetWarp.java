package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.io.File;
import java.io.IOException;

public class CmdSetWarp implements CommandExecutor {

    RoyalCommands plugin;

    public CmdSetWarp(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("setwarp")) {
            if (!plugin.isAuthorized(cs, "rcmds.setwarp")) {
                RUtils.dispNoPerms(cs);
                return true;
            }

            if (!(cs instanceof Player)) {
                cs.sendMessage(ChatColor.RED + "This command is only available to players!");
                return true;
            }

            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            Player p = (Player) cs;

            double locX = p.getLocation().getX();
            double locY = p.getLocation().getY();
            double locZ = p.getLocation().getZ();
            Float locYaw = p.getLocation().getYaw();
            Float locPitch = p.getLocation().getPitch();
            String locW = p.getWorld().getName();
            String name = args[0].toLowerCase();

            File pconfl = new File(plugin.getDataFolder() + File.separator + "warps.yml");
            if (pconfl.exists()) {
                FileConfiguration pconf = YamlConfiguration.loadConfiguration(pconfl);
                pconf.set("warps." + name + ".set", true);
                pconf.set("warps." + name + ".x", locX);
                pconf.set("warps." + name + ".y", locY);
                pconf.set("warps." + name + ".z", locZ);
                pconf.set("warps." + name + ".pitch", locPitch.toString());
                pconf.set("warps." + name + ".yaw", locYaw.toString());
                pconf.set("warps." + name + ".w", locW);
                try {
                    pconf.save(pconfl);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                p.sendMessage(ChatColor.BLUE + "Warp \"" + ChatColor.GRAY + name + ChatColor.BLUE + "\" set.");
                return true;
            }
        }
        return false;
    }

}
