package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.PConfManager;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class CmdJail implements CommandExecutor {

    RoyalCommands plugin;

    public CmdJail(RoyalCommands instance) {
        this.plugin = instance;
    }

    public HashMap<Player, Location> jaildb = new HashMap<Player, Location>();

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("jail")) {
            if (!plugin.isAuthorized(cs, "rcmds.jail")) {
                RUtils.dispNoPerms(cs);
                return true;
            }

            if (args.length < 1) {
                File pconfl2 = new File(plugin.getDataFolder() + File.separator + "jails.yml");
                if (pconfl2.exists()) {
                    FileConfiguration pconf1 = YamlConfiguration.loadConfiguration(pconfl2);
                    if (pconf1.get("jails") == null) {
                        cs.sendMessage(ChatColor.RED + "There are no jails!");
                        return true;
                    }
                    final Map<String, Object> opts = pconf1.getConfigurationSection("jails").getValues(false);
                    if (opts.keySet().isEmpty()) {
                        cs.sendMessage(ChatColor.RED + "There are no jails!");
                        return true;
                    }
                    String jails = opts.keySet().toString();
                    jails = jails.substring(1, jails.length() - 1);
                    cs.sendMessage(ChatColor.BLUE + "Jails:");
                    cs.sendMessage(jails);
                    return true;
                }
            }

            Player t = plugin.getServer().getPlayer(args[0].trim());
            if (t == null || plugin.isVanished(t)) {
                cs.sendMessage(ChatColor.RED + "That player does not exist!");
                return true;
            }
            if (plugin.isAuthorized(t, "rcmds.exempt.jail")) {
                cs.sendMessage(ChatColor.RED + "You cannot jail that player.");
                return true;
            }
            if (args.length < 2) {
                if (PConfManager.getPValBoolean(t, "jailed")) {
                    PConfManager.setPValBoolean(t, false, "jailed");
                    cs.sendMessage(ChatColor.BLUE + "You have released " + ChatColor.GRAY + t.getName() + ChatColor.BLUE + ".");
                    t.sendMessage(ChatColor.BLUE + "You have been released.");
                    if (jaildb.get(t).getWorld() == null) {
                        t.sendMessage(ChatColor.RED + "Your previous location no longer exists. Sending you to spawn.");
                        t.teleport(t.getWorld().getSpawnLocation());
                        return true;
                    }
                    t.teleport(jaildb.get(t));
                    return true;
                }
                cs.sendMessage(cmd.getDescription());
                return false;
            }

            boolean jailSet;
            Double jailX;
            Double jailY;
            Double jailZ;
            Float jailYaw;
            Float jailPitch;
            World jailW;

            File pconfl = new File(plugin.getDataFolder() + File.separator + "jails.yml");
            if (pconfl.exists()) {
                FileConfiguration pconf = YamlConfiguration.loadConfiguration(pconfl);
                if (args.length < 1) {
                    if (pconf.get("jails") == null) {
                        cs.sendMessage(ChatColor.RED + "There are no jails!");
                        return true;
                    }
                    final Map<String, Object> opts = pconf.getConfigurationSection("jails").getValues(false);
                    if (opts.keySet().isEmpty()) {
                        cs.sendMessage(ChatColor.RED + "There are no jails!");
                        return true;
                    }
                    String jails = opts.keySet().toString();
                    jails = jails.substring(1, jails.length() - 1);
                    cs.sendMessage(ChatColor.BLUE + "Jails:");
                    cs.sendMessage(jails);
                    return true;
                }
                jailSet = pconf.getBoolean("jails." + args[1] + ".set");
                if (jailSet) {
                    jailX = pconf.getDouble("jails." + args[1] + ".x");
                    jailY = pconf.getDouble("jails." + args[1] + ".y");
                    jailZ = pconf.getDouble("jails." + args[1] + ".z");
                    jailYaw = Float.parseFloat(pconf.getString("jails." + args[1] + ".yaw"));
                    jailPitch = Float.parseFloat(pconf.getString("jails." + args[1] + ".pitch"));
                    jailW = plugin.getServer().getWorld(pconf.getString("jails." + args[1] + ".w"));
                } else {
                    cs.sendMessage(ChatColor.RED + "That jail does not exist.");
                    return true;
                }
            } else {
                cs.sendMessage(ChatColor.RED + "No jails set!");
                return true;
            }
            Location jailLoc = new Location(jailW, jailX, jailY, jailZ, jailYaw, jailPitch);
            if (PConfManager.getPValBoolean(t, "jailed")) {
                PConfManager.setPValBoolean(t, false, "jailed");
                cs.sendMessage(ChatColor.BLUE + "You have released " + ChatColor.GRAY + t.getName() + ChatColor.BLUE + ".");
                t.sendMessage(ChatColor.BLUE + "You have been released.");
                if (jaildb.get(t).getWorld() == null) {
                    t.sendMessage(ChatColor.RED + "Your previous location no longer exists. Sending you to spawn.");
                    t.teleport(t.getWorld().getSpawnLocation());
                    return true;
                }
                t.teleport(jaildb.get(t));
                return true;
            } else {
                if (jailW == null) {
                    cs.sendMessage(ChatColor.RED + "World doesn't exist!");
                }
                cs.sendMessage(ChatColor.BLUE + "You have jailed " + ChatColor.GRAY + t.getName() + ChatColor.BLUE + ".");
                t.sendMessage(ChatColor.RED + "You have been jailed.");
                jaildb.put(t, t.getLocation());
                t.teleport(jailLoc);
                PConfManager.setPValBoolean(t, true, "jailed");
                return true;
            }
        }
        return false;
    }

}
