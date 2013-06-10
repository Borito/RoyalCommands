package org.royaldev.royalcommands.rcommands;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.configuration.ConfManager;
import org.royaldev.royalcommands.configuration.PConfManager;

import java.util.HashMap;
import java.util.Map;

public class CmdJail implements CommandExecutor {

    private RoyalCommands plugin;

    public CmdJail(RoyalCommands instance) {
        this.plugin = instance;
    }

    public HashMap<Player, Location> jaildb = new HashMap<Player, Location>();

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("jail")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.jail")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            ConfManager cm = ConfManager.getConfManager("jails.yml");

            if (args.length < 1) {
                if (!cm.exists() || cm.get("jails") == null) {
                    cs.sendMessage(MessageColor.NEGATIVE + "There are no jails!");
                    return true;
                }
                final Map<String, Object> opts = cm.getConfigurationSection("jails").getValues(false);
                if (opts.keySet().isEmpty()) {
                    cs.sendMessage(MessageColor.NEGATIVE + "There are no jails!");
                    return true;
                }
                String jails = opts.keySet().toString();
                jails = jails.substring(1, jails.length() - 1);
                cs.sendMessage(MessageColor.POSITIVE + "Jails:");
                cs.sendMessage(jails);
                return true;
            }

            Player t = plugin.getServer().getPlayer(args[0]);
            if (t == null || plugin.isVanished(t, cs)) {
                cs.sendMessage(MessageColor.NEGATIVE + "That player does not exist!");
                return true;
            }
            if (plugin.ah.isAuthorized(t, "rcmds.exempt.jail")) {
                cs.sendMessage(MessageColor.NEGATIVE + "You cannot jail that player.");
                return true;
            }
            PConfManager pcm = PConfManager.getPConfManager(t);
            if (args.length < 2) {
                if (pcm.getBoolean("jailed")) {
                    pcm.set("jailed", false);
                    cs.sendMessage(MessageColor.POSITIVE + "You have released " + MessageColor.NEUTRAL + t.getName() + MessageColor.POSITIVE + ".");
                    t.sendMessage(MessageColor.POSITIVE + "You have been released.");
                    if (jaildb.get(t) == null || jaildb.get(t).getWorld() == null) {
                        t.sendMessage(MessageColor.NEGATIVE + "Your previous location no longer exists. Sending you to spawn.");
                        String error = RUtils.silentTeleport(t, CmdSpawn.getWorldSpawn(t.getWorld()));
                        if (!error.isEmpty()) {
                            cs.sendMessage(MessageColor.NEGATIVE + error);
                            return true;
                        }
                        return true;
                    }
                    String error = RUtils.silentTeleport(t, jaildb.get(t));
                    if (!error.isEmpty()) {
                        cs.sendMessage(MessageColor.NEGATIVE + error);
                        return true;
                    }
                    return true;
                }
                cs.sendMessage(cmd.getDescription());
                cs.sendMessage(cmd.getUsage().replace("<command>", label));
                return true;
            }

            boolean jailSet;
            Double jailX;
            Double jailY;
            Double jailZ;
            Float jailYaw;
            Float jailPitch;
            World jailW;

            if (!cm.exists()) {
                cs.sendMessage(MessageColor.NEGATIVE + "No jails set!");
                return true;
            }
            if (args.length < 1) {
                if (cm.get("jails") == null) {
                    cs.sendMessage(MessageColor.NEGATIVE + "There are no jails!");
                    return true;
                }
                final Map<String, Object> opts = cm.getConfigurationSection("jails").getValues(false);
                if (opts.keySet().isEmpty()) {
                    cs.sendMessage(MessageColor.NEGATIVE + "There are no jails!");
                    return true;
                }
                String jails = opts.keySet().toString();
                jails = jails.substring(1, jails.length() - 1);
                cs.sendMessage(MessageColor.POSITIVE + "Jails:");
                cs.sendMessage(jails);
                return true;
            }
            jailSet = cm.getBoolean("jails." + args[1] + ".set");
            if (jailSet) {
                jailX = cm.getDouble("jails." + args[1] + ".x");
                jailY = cm.getDouble("jails." + args[1] + ".y");
                jailZ = cm.getDouble("jails." + args[1] + ".z");
                jailYaw = Float.parseFloat(cm.getString("jails." + args[1] + ".yaw"));
                jailPitch = Float.parseFloat(cm.getString("jails." + args[1] + ".pitch"));
                jailW = plugin.getServer().getWorld(cm.getString("jails." + args[1] + ".w"));
            } else {
                cs.sendMessage(MessageColor.NEGATIVE + "That jail does not exist.");
                return true;
            }
            Location jailLoc = new Location(jailW, jailX, jailY, jailZ, jailYaw, jailPitch);
            if (pcm.getBoolean("jailed")) {
                pcm.set("jailed", false);
                cs.sendMessage(MessageColor.POSITIVE + "You have released " + MessageColor.NEUTRAL + t.getName() + MessageColor.POSITIVE + ".");
                t.sendMessage(MessageColor.POSITIVE + "You have been released.");
                if (jaildb.get(t) == null || jaildb.get(t).getWorld() == null) {
                    t.sendMessage(MessageColor.NEGATIVE + "Your previous location no longer exists. Sending you to spawn.");
                    String error = RUtils.silentTeleport(t, CmdSpawn.getWorldSpawn(t.getWorld()));
                    if (!error.isEmpty()) {
                        cs.sendMessage(MessageColor.NEGATIVE + error);
                        return true;
                    }
                    return true;
                }
                String error = RUtils.silentTeleport(t, jaildb.get(t));
                if (!error.isEmpty()) {
                    cs.sendMessage(MessageColor.NEGATIVE + error);
                    return true;
                }
                return true;
            } else {
                if (jailW == null) {
                    cs.sendMessage(MessageColor.NEGATIVE + "World doesn't exist!");
                }
                cs.sendMessage(MessageColor.POSITIVE + "You have jailed " + MessageColor.NEUTRAL + t.getName() + MessageColor.POSITIVE + ".");
                t.sendMessage(MessageColor.NEGATIVE + "You have been jailed.");
                jaildb.put(t, t.getLocation());
                String error = RUtils.silentTeleport(t, jailLoc);
                if (!error.isEmpty()) {
                    cs.sendMessage(MessageColor.NEGATIVE + error);
                    return true;
                }
                pcm.set("jailed", true);
                return true;
            }
        }
        return false;
    }

}
