package org.royaldev.royalcommands.rcommands;

import org.apache.commons.lang.ArrayUtils;
import org.royaldev.royalcommands.MessageColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.Config;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class CmdTime implements CommandExecutor {

    static RoyalCommands plugin;

    public CmdTime(RoyalCommands instance) {
        plugin = instance;
    }

    public static void smoothTimeChange(long time, final World world) {
        if (time > 24000L) time = time % 24000L;
        if (time < 0L) time = 0L; // Clamp to 0 to prevent loop
        final long ftime = time;
        final Runnable r = new Runnable() {
            @Override
            public void run() {
                for (long i = world.getTime() + 1; i != ftime; i++) {
                    if (i == 24001L) {
                        i = 0L;
                        if (ftime == 0L) break;
                    }
                    world.setTime(i);
                }
                world.setTime(ftime);
            }
        };
        plugin.getServer().getScheduler().runTask(plugin, r);
    }

    public static Long getValidTime(String time) {
        Long vtime;
        try {
            vtime = Long.valueOf(time);
            if (vtime > 24000L) vtime = vtime % 24000L;
        } catch (Exception e) {
            if (time.equalsIgnoreCase("day")) vtime = 0L;
            else if (time.equalsIgnoreCase("midday") || time.equalsIgnoreCase("noon"))
                vtime = 6000L;
            else if (time.equalsIgnoreCase("sunset") || time.equalsIgnoreCase("sundown") || time.equalsIgnoreCase("dusk"))
                vtime = 12000L;
            else if (time.equalsIgnoreCase("night") || time.equalsIgnoreCase("dark"))
                vtime = 14000L;
            else if (time.equalsIgnoreCase("midnight")) vtime = 18000L;
            else if (time.equalsIgnoreCase("sunrise") || time.equalsIgnoreCase("sunup") || time.equalsIgnoreCase("dawn"))
                vtime = 23000L;
            else return null;
        }
        return vtime;
    }

    public static Map<String, String> getRealTime(long ticks) {
        if (ticks > 24000L) ticks = ticks % 24000L;
        if (ticks < 0L) ticks = 0L;
        DecimalFormat df = new DecimalFormat("00");
        df.setRoundingMode(RoundingMode.DOWN);
        float thour = 1000F;
        float tminute = 16.6666666666666666666666666666666666666666666666666666666666666666666666F;
        float hour = (ticks / thour) + 6F;
        if (hour >= 24F) hour = hour - 24F;
        float minute = (ticks % thour) / tminute;
        String meridian = (hour >= 12F) ? "PM" : "AM";
        float twelvehour = (hour > 12F) ? hour - 12F : hour;
        if (df.format(twelvehour).equals("00")) twelvehour = 12F;
        Map<String, String> toRet = new HashMap<String, String>();
        toRet.put("24h", df.format(hour) + ":" + df.format(minute));
        toRet.put("12h", df.format(twelvehour) + ":" + df.format(minute) + " " + meridian);
        return toRet;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("time")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.time")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                if (!(cs instanceof Player))
                    for (World w : plugin.getServer().getWorlds()) {
                        String name = RUtils.getMVWorldName(w);
                        long ticks = w.getTime();
                        Map<String, String> times = getRealTime(ticks);
                        cs.sendMessage(MessageColor.POSITIVE + "The current time in " + MessageColor.NEUTRAL + name + MessageColor.POSITIVE + " is " + MessageColor.NEUTRAL + ticks + " ticks" + MessageColor.POSITIVE + " (" + MessageColor.NEUTRAL + times.get("24h") + MessageColor.POSITIVE + "/" + MessageColor.NEUTRAL + times.get("12h") + MessageColor.POSITIVE + ").");
                    }
                else {
                    Player p = (Player) cs;
                    World w = p.getWorld();
                    long ticks = w.getTime();
                    Map<String, String> times = getRealTime(ticks);
                    cs.sendMessage(MessageColor.POSITIVE + "The current time in " + MessageColor.NEUTRAL + RUtils.getMVWorldName(w) + MessageColor.POSITIVE + " is " + MessageColor.NEUTRAL + ticks + " ticks" + MessageColor.POSITIVE + " (" + MessageColor.NEUTRAL + times.get("24h") + MessageColor.POSITIVE + "/" + MessageColor.NEUTRAL + times.get("12h") + MessageColor.POSITIVE + ").");
                }
                return true;
            }
            if (args.length > 0 && args[0].equals("?") || args[0].equalsIgnoreCase("help")) {
                cs.sendMessage(cmd.getDescription());
                cs.sendMessage(cmd.getUsage().replace("<command>", label));
                return true;
            }
            if (args.length > 0 && args[0].equalsIgnoreCase("set"))
                args = (String[]) ArrayUtils.remove(args, 0);
            String target = "";
            if (!(cs instanceof Player) && args.length < 2) target = "*";
            else if ((cs instanceof Player) && args.length < 2)
                target = ((Player) cs).getWorld().getName();
            if (args.length > 1) target = args[1];
            if (target.equalsIgnoreCase("all")) target = "*";
            if (RUtils.getWorld(target) == null && !target.equals("*")) {
                cs.sendMessage(MessageColor.NEGATIVE + "No such world!");
                return true;
            }
            World w = (!target.equals("*")) ? RUtils.getWorld(target) : null;
            Long ticks = getValidTime(args[0]);
            if (ticks == null) {
                if (RUtils.getWorld(args[0]) != null) {
                    w = RUtils.getWorld(args[0]);
                    ticks = w.getTime();
                    Map<String, String> times = getRealTime(ticks);
                    cs.sendMessage(MessageColor.POSITIVE + "The current time in " + MessageColor.NEUTRAL + RUtils.getMVWorldName(w) + MessageColor.POSITIVE + " is " + MessageColor.NEUTRAL + ticks + " ticks" + MessageColor.POSITIVE + " (" + MessageColor.NEUTRAL + times.get("24h") + MessageColor.POSITIVE + "/" + MessageColor.NEUTRAL + times.get("12h") + MessageColor.POSITIVE + ").");
                    return true;
                }
                cs.sendMessage(MessageColor.NEGATIVE + "Invalid time specified!");
                return true;
            }
            Map<String, String> times = getRealTime(ticks);
            if (w == null) {
                for (World ws : plugin.getServer().getWorlds()) {
                    if (Config.smoothTime) smoothTimeChange(ticks, ws);
                    else ws.setTime(ticks);
                    if (Config.timeBroadcast)
                        for (Player p : ws.getPlayers())
                            p.sendMessage(MessageColor.POSITIVE + "The time was changed to " + MessageColor.NEUTRAL + ticks + " ticks" + MessageColor.POSITIVE + " (" + MessageColor.NEUTRAL + times.get("24h") + MessageColor.POSITIVE + "/" + MessageColor.NEUTRAL + times.get("12h") + MessageColor.POSITIVE + ") by " + MessageColor.NEUTRAL + cs.getName() + " in " + RUtils.getMVWorldName(ws) + MessageColor.POSITIVE + ".");
                }
                cs.sendMessage(MessageColor.POSITIVE + "Set time in all worlds to " + MessageColor.NEUTRAL + ticks + " ticks" + MessageColor.POSITIVE + " (" + MessageColor.NEUTRAL + times.get("24h") + MessageColor.POSITIVE + "/" + MessageColor.NEUTRAL + times.get("12h") + MessageColor.POSITIVE + ").");
            } else {
                if (Config.smoothTime) smoothTimeChange(ticks, w);
                else w.setTime(ticks);
                cs.sendMessage(MessageColor.POSITIVE + "Set time in " + MessageColor.NEUTRAL + RUtils.getMVWorldName(w) + MessageColor.POSITIVE + " to " + MessageColor.NEUTRAL + ticks + " ticks" + MessageColor.POSITIVE + " (" + MessageColor.NEUTRAL + times.get("24h") + MessageColor.POSITIVE + "/" + MessageColor.NEUTRAL + times.get("12h") + MessageColor.POSITIVE + ").");
                if (Config.timeBroadcast) {
                    for (Player p : w.getPlayers())
                        p.sendMessage(MessageColor.POSITIVE + "The time was changed to " + MessageColor.NEUTRAL + ticks + " ticks" + MessageColor.POSITIVE + " (" + MessageColor.NEUTRAL + times.get("24h") + MessageColor.POSITIVE + "/" + MessageColor.NEUTRAL + times.get("12h") + MessageColor.POSITIVE + ") by " + MessageColor.NEUTRAL + cs.getName() + " in " + RUtils.getMVWorldName(w) + MessageColor.POSITIVE + ".");
                }
            }
            return true;

        }
        return false;
    }
}
