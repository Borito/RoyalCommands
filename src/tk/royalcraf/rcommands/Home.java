package tk.royalcraf.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import tk.royalcraf.royalcommands.RoyalCommands;

import java.io.File;

public class Home implements CommandExecutor {

    RoyalCommands plugin;

    public Home(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label,
                             String[] args) {
        if (cmd.getName().equalsIgnoreCase("home")) {
            if (!plugin.isAuthorized(cs, "rcmds.home")) {
                cs.sendMessage(ChatColor.RED
                        + "You don't have permission for that!");
                plugin.log.warning("[RoyalCommands] " + cs.getName()
                        + " was denied access to the command!");
                return true;
            }

            Player p;

            if (!(cs instanceof Player)) {
                cs.sendMessage(ChatColor.RED
                        + "This command is only available to players!");
                return true;
            } else {
                p = (Player) cs;
            }

            boolean homeSet;
            Double homeX = null;
            Double homeY = null;
            Double homeZ = null;
            Float homeYaw = null;
            Float homePitch = null;
            World homeW = null;

            File pconfl = new File(plugin.getDataFolder() + "/userdata/"
                    + cs.getName().toLowerCase() + ".yml");
            if (pconfl.exists()) {
                FileConfiguration pconf = YamlConfiguration
                        .loadConfiguration(pconfl);
                if (args.length > 0) {
                    homeSet = pconf.getBoolean("home." + args[0] + ".set");
                } else {
                    homeSet = pconf.getBoolean("home.home.set");
                }
                if (homeSet) {
                    if (args.length > 0) {
                        homeX = pconf.getDouble("home." + args[0] + ".x");
                        homeY = pconf.getDouble("home." + args[0] + ".y");
                        homeZ = pconf.getDouble("home." + args[0] + ".z");
                        homeYaw = Float.parseFloat(pconf.getString("home."
                                + args[0] + ".yaw"));
                        homePitch = Float.parseFloat(pconf.getString("home."
                                + args[0] + ".pitch"));
                        homeW = plugin.getServer().getWorld(
                                pconf.getString("home." + args[0] + ".w"));
                    } else {
                        homeX = pconf.getDouble("home.home.x");
                        homeY = pconf.getDouble("home.home.y");
                        homeZ = pconf.getDouble("home.home.z");
                        homeYaw = Float.parseFloat(pconf
                                .getString("home.home.yaw"));
                        homePitch = Float.parseFloat(pconf
                                .getString("home.home.pitch"));
                        homeW = plugin.getServer().getWorld(
                                pconf.getString("home.home.w"));
                    }
                } else {
                    cs.sendMessage(ChatColor.RED
                            + "You don't have that home set!");
                    return true;
                }
            }
            Location homeLoc = new Location(homeW, homeX, homeY, homeZ,
                    homeYaw, homePitch);
            if (args.length > 0) {
                p.sendMessage(ChatColor.BLUE + "Going to home \""
                        + ChatColor.GRAY + args[0] + ChatColor.BLUE + ".\"");
            } else {
                p.sendMessage(ChatColor.BLUE + "Going home.");
            }
            Back.backdb.put(p, p.getLocation());
            p.teleport(homeLoc);
            return true;

        }
        return false;
    }
}