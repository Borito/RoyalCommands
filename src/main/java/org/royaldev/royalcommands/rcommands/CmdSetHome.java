package org.royaldev.royalcommands.rcommands;

import org.royaldev.royalcommands.MessageColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.Config;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.configuration.PConfManager;

public class CmdSetHome implements CommandExecutor {

    private RoyalCommands plugin;

    public CmdSetHome(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    private Integer getHomeLimit(Player p) {
        String name = p.getName();
        String group;
        try {
            group = RoyalCommands.permission.getPrimaryGroup(p);
        } catch (Exception e) {
            group = "";
        }
        if (group == null) group = "";
        ConfigurationSection players = Config.homeLimits.getConfigurationSection("players");
        ConfigurationSection groups = Config.homeLimits.getConfigurationSection("groups");
        Integer limit;
        if (players != null && players.contains(name)) limit = players.getInt(name);
        else if (groups != null && groups.contains(group)) limit = groups.getInt(group);
        else limit = null;
        return limit;
    }

    private int getCurrentHomes(Player p) {
        ConfigurationSection pconf = PConfManager.getPConfManager(p).getConfigurationSection("home");
        if (pconf == null) return 0;
        return pconf.getValues(false).keySet().size();
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("sethome")) {
            if (!plugin.isAuthorized(cs, "rcmds.sethome")) {
                RUtils.dispNoPerms(cs);
                return true;
            }

            if (args.length > 0 && !plugin.isAuthorized(cs, "rcmds.sethome.multi")) {
                RUtils.dispNoPerms(cs, MessageColor.NEGATIVE + "You don't have permission for multiple homes!");
                return true;
            }

            if (!(cs instanceof Player)) {
                cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
                return true;
            }
            Player p = (Player) cs;
            PConfManager pcm = PConfManager.getPConfManager(p);
            double locX = p.getLocation().getX();
            double locY = p.getLocation().getY();
            double locZ = p.getLocation().getZ();
            Float locYaw = p.getLocation().getYaw();
            Float locPitch = p.getLocation().getPitch();
            String locW = p.getWorld().getName();
            String name = "";
            if (args.length > 0) name = args[0];

            if (name.contains(":")) {
                cs.sendMessage(MessageColor.NEGATIVE + "The name of your home cannot contain \":\"!");
                return true;
            }


            Integer limit = getHomeLimit(p);
            int curHomes = getCurrentHomes(p);
            if (limit != null && pcm.get("home." + name) != null) {
                if (limit == 0) {
                    RUtils.dispNoPerms(cs, MessageColor.NEGATIVE + "Your home limit is set to " + MessageColor.NEUTRAL + "0" + MessageColor.NEGATIVE + "!");
                    cs.sendMessage(MessageColor.NEGATIVE + "You can't set any homes!");
                    return true;
                } else if (curHomes >= limit && limit > -1) {
                    RUtils.dispNoPerms(cs, MessageColor.NEGATIVE + "You've reached your max number of homes! (" + MessageColor.NEUTRAL + limit + MessageColor.NEGATIVE + ")");
                    return true;
                }
            }
            if (!name.equals("")) {
                pcm.set("home." + name + ".set", true);
                pcm.set("home." + name + ".x", locX);
                pcm.set("home." + name + ".y", locY);
                pcm.set("home." + name + ".z", locZ);
                pcm.set("home." + name + ".pitch", locPitch.toString());
                pcm.set("home." + name + ".yaw", locYaw.toString());
                pcm.set("home." + name + ".w", locW);
            } else {
                pcm.set("home.home.set", true);
                pcm.set("home.home.x", locX);
                pcm.set("home.home.y", locY);
                pcm.set("home.home.z", locZ);
                pcm.set("home.home.pitch", locPitch.toString());
                pcm.set("home.home.yaw", locYaw.toString());
                pcm.set("home.home.w", locW);
            }
            if (args.length > 0) {
                p.sendMessage(MessageColor.POSITIVE + "Home \"" + MessageColor.NEUTRAL + name + MessageColor.POSITIVE + "\" set.");
            } else {
                p.sendMessage(MessageColor.POSITIVE + "Home set.");
            }
            return true;
        }
        return false;
    }

}
