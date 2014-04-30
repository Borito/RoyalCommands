package org.royaldev.royalcommands.rcommands;

import org.apache.commons.lang.BooleanUtils;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.configuration.PConfManager;

import java.text.DecimalFormat;
import java.util.Date;

@ReflectCommand
public class CmdWhois implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdWhois(RoyalCommands instance) {
        plugin = instance;
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("whois")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.whois")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            OfflinePlayer t = plugin.getServer().getPlayer(args[0]);
            if (t == null) t = plugin.getServer().getOfflinePlayer(args[0]);
            if (!t.isOnline() && !t.hasPlayedBefore()) {
                cs.sendMessage(MessageColor.NEGATIVE + "That player has never played before!");
                return true;
            }
            PConfManager pcm = PConfManager.getPConfManager(t);
            DecimalFormat df = new DecimalFormat("#.##");
            String ip = pcm.getString("ip");
            String name = pcm.getString("name");
            String dispname = pcm.getString("dispname");
            cs.sendMessage(MessageColor.POSITIVE + "=====================");
            cs.sendMessage(MessageColor.POSITIVE + ((t.isOnline()) ? "Whois" : "Whowas") + " for " + MessageColor.NEUTRAL + name);
            cs.sendMessage(MessageColor.POSITIVE + "Nickname: " + MessageColor.NEUTRAL + dispname);
            cs.sendMessage(MessageColor.POSITIVE + "IP: " + MessageColor.NEUTRAL + ip);
            cs.sendMessage(MessageColor.POSITIVE + "Is VIP: " + MessageColor.NEUTRAL + BooleanUtils.toStringYesNo(pcm.getBoolean("vip")));
            cs.sendMessage(MessageColor.POSITIVE + "Is muted: " + MessageColor.NEUTRAL + BooleanUtils.toStringYesNo(pcm.getBoolean("muted")));
            cs.sendMessage(MessageColor.POSITIVE + "Is frozen: " + MessageColor.NEUTRAL + BooleanUtils.toStringYesNo(pcm.getBoolean("frozen")));
            cs.sendMessage(MessageColor.POSITIVE + "Is jailed: " + MessageColor.NEUTRAL + BooleanUtils.toStringYesNo(pcm.getBoolean("jailed")));
            long timestamp = RUtils.getTimeStamp(t, "seen");
            String lastseen = (timestamp < 0) ? "unknown" : RUtils.formatDateDiff(timestamp);
            cs.sendMessage(MessageColor.POSITIVE + "Last seen:" + MessageColor.NEUTRAL + ((t.isOnline()) ? " now" : lastseen));
            cs.sendMessage(MessageColor.POSITIVE + "First played:" + MessageColor.NEUTRAL + RUtils.formatDateDiff(t.getFirstPlayed()));
            if (t.isOnline()) {
                Player p = (Player) t;
                cs.sendMessage(MessageColor.POSITIVE + "Gamemode: " + MessageColor.NEUTRAL + p.getGameMode().name().toLowerCase());
                cs.sendMessage(MessageColor.POSITIVE + "Can fly: " + MessageColor.NEUTRAL + BooleanUtils.toStringYesNo(p.getAllowFlight()));
                cs.sendMessage(MessageColor.POSITIVE + "Health/Hunger/Saturation: " + MessageColor.NEUTRAL + p.getHealth() / 2 + MessageColor.POSITIVE + "/" + MessageColor.NEUTRAL + p.getFoodLevel() / 2 + MessageColor.POSITIVE + "/" + MessageColor.NEUTRAL + p.getSaturation() / 2);
                cs.sendMessage(MessageColor.POSITIVE + "Total Exp/Exp %/Level: " + MessageColor.NEUTRAL + p.getTotalExperience() + MessageColor.POSITIVE + "/" + MessageColor.NEUTRAL + df.format(p.getExp() * 100) + "%" + MessageColor.POSITIVE + "/" + MessageColor.NEUTRAL + p.getLevel());
                cs.sendMessage(MessageColor.POSITIVE + "Item in hand: " + MessageColor.NEUTRAL + RUtils.getItemName(p.getItemInHand()));
                cs.sendMessage(MessageColor.POSITIVE + "Alive for:" + MessageColor.NEUTRAL + RUtils.formatDateDiff(new Date().getTime() - p.getTicksLived() * 50));
                Location l = p.getLocation();
                cs.sendMessage(MessageColor.POSITIVE + "Last position: " + "(" + MessageColor.NEUTRAL + l.getX() + MessageColor.POSITIVE + ", " + MessageColor.NEUTRAL + l.getY() + MessageColor.POSITIVE + ", " + MessageColor.NEUTRAL + l.getZ() + ")");
                cs.sendMessage(MessageColor.POSITIVE + "Last world: " + MessageColor.NEUTRAL + RUtils.getMVWorldName(l.getWorld()) + MessageColor.POSITIVE + " (" + MessageColor.NEUTRAL + l.getWorld().getName() + MessageColor.POSITIVE + ")");
            } else {
                String lP = "lastposition.";
                World w = (pcm.isSet(lP + "world")) ? plugin.getServer().getWorld(pcm.getString(lP + "world")) : null;
                if (w != null) {
                    Location l = new Location(w, pcm.getDouble(lP + "x"), pcm.getDouble(lP + "y"), pcm.getDouble(lP + "z"));
                    cs.sendMessage(MessageColor.POSITIVE + "Last position: " + "(" + MessageColor.NEUTRAL + l.getX() + MessageColor.POSITIVE + ", " + MessageColor.NEUTRAL + l.getY() + MessageColor.POSITIVE + ", " + MessageColor.NEUTRAL + l.getZ() + ")");
                    cs.sendMessage(MessageColor.POSITIVE + "Last world: " + MessageColor.NEUTRAL + RUtils.getMVWorldName(l.getWorld()) + MessageColor.POSITIVE + " (" + MessageColor.NEUTRAL + l.getWorld().getName() + MessageColor.POSITIVE + ")");
                }
            }
            cs.sendMessage(MessageColor.POSITIVE + "=====================");
            return true;
        }
        return false;
    }

}
