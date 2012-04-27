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

public class CmdSetHome implements CommandExecutor {

    RoyalCommands plugin;

    public CmdSetHome(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("sethome")) {
            if (!plugin.isAuthorized(cs, "rcmds.sethome")) {
                RUtils.dispNoPerms(cs);
                return true;
            }

            if (args.length > 0) {
                if (!plugin.isAuthorized(cs, "rcmds.sethome.multi")) {
                    cs.sendMessage(ChatColor.RED + "You don't have permission for multiple homes!");
                    plugin.log.warning("[RoyalCommands] " + cs.getName() + " was denied access to the command!");
                    return true;
                }
            }

            if (!(cs instanceof Player)) {
                cs.sendMessage(ChatColor.RED + "This command is only available to players!");
                return true;
            }
            Player p = (Player) cs;

            double locX = p.getLocation().getX();
            double locY = p.getLocation().getY();
            double locZ = p.getLocation().getZ();
            Float locYaw = p.getLocation().getYaw();
            Float locPitch = p.getLocation().getPitch();
            String locW = p.getWorld().getName();
            String name = "";
            if (args.length > 0) name = args[0];

            if (name.contains(":")) {
                cs.sendMessage(ChatColor.RED + "The name of your home cannot contain \":\"!");
                return true;
            }

            File pconfl = new File(plugin.getDataFolder() + File.separator + "userdata" + File.separator + cs.getName().toLowerCase() + ".yml");
            if (pconfl.exists()) {
                FileConfiguration pconf = YamlConfiguration.loadConfiguration(pconfl);
                if (args.length > 0) {
                    pconf.set("home." + name + ".set", true);
                    pconf.set("home." + name + ".x", locX);
                    pconf.set("home." + name + ".y", locY);
                    pconf.set("home." + name + ".z", locZ);
                    pconf.set("home." + name + ".pitch", locPitch.toString());
                    pconf.set("home." + name + ".yaw", locYaw.toString());
                    pconf.set("home." + name + ".w", locW);
                } else {
                    pconf.set("home.home.set", true);
                    pconf.set("home.home.x", locX);
                    pconf.set("home.home.y", locY);
                    pconf.set("home.home.z", locZ);
                    pconf.set("home.home.pitch", locPitch.toString());
                    pconf.set("home.home.yaw", locYaw.toString());
                    pconf.set("home.home.w", locW);
                }
                try {
                    pconf.save(pconfl);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (args.length > 0) {
                    p.sendMessage(ChatColor.BLUE + "Home \"" + ChatColor.GRAY + name + ChatColor.BLUE + "\" set.");
                } else {
                    p.sendMessage(ChatColor.BLUE + "Home set.");
                }
                return true;
            }
        }
        return false;
    }

}
