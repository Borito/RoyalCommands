package org.royaldev.royalcommands.rcommands;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

@ReflectCommand
public class CmdWeather extends BaseCommand {

    public CmdWeather(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    public static boolean changeWeather(Player p, String conds) {
        World world = p.getWorld();
        String wName = RUtils.getMVWorldName(world);
        if (conds.toLowerCase().trim().startsWith("sun")) {
            world.setStorm(false);
            world.setThundering(false);
            p.sendMessage(MessageColor.POSITIVE + "Set weather to " + MessageColor.NEUTRAL + "sun" + MessageColor.POSITIVE + " in " + MessageColor.NEUTRAL + wName + MessageColor.POSITIVE + ".");
            return true;
        } else if (conds.toLowerCase().startsWith("rain")) {
            world.setStorm(true);
            world.setThundering(false);
            p.sendMessage(MessageColor.POSITIVE + "Set weather to " + MessageColor.NEUTRAL + "rain" + MessageColor.POSITIVE + " in " + MessageColor.NEUTRAL + wName + MessageColor.POSITIVE + ".");
            return true;
        } else if (conds.toLowerCase().startsWith("storm")) {
            world.setStorm(true);
            world.setThundering(true);
            p.sendMessage(MessageColor.POSITIVE + "Set weather to " + MessageColor.NEUTRAL + "storm" + MessageColor.POSITIVE + " in " + MessageColor.NEUTRAL + wName + MessageColor.POSITIVE + ".");
            return true;
        }
        return false;
    }

    public static boolean validWeather(String conds) {
        return conds.toLowerCase().startsWith("sun") || conds.toLowerCase().startsWith("rain") || conds.toLowerCase().startsWith("storm");
    }

    public static boolean changeWeather(Player p, String conds, int length) {
        if (length < 1) {
            p.sendMessage(MessageColor.NEGATIVE + "The time specified was invalid!");
            return false;
        }
        World world = p.getWorld();
        String wName = RUtils.getMVWorldName(world);
        if (conds.toLowerCase().trim().startsWith("sun")) {
            world.setStorm(false);
            world.setThundering(false);
            world.setWeatherDuration(length * 20);
            p.sendMessage(MessageColor.POSITIVE + "Set weather to " + MessageColor.NEUTRAL + "sun" + MessageColor.POSITIVE + " in " + MessageColor.NEUTRAL + wName + MessageColor.POSITIVE + " for " + MessageColor.NEUTRAL + length + MessageColor.POSITIVE + " seconds.");
            return true;
        } else if (conds.toLowerCase().startsWith("rain")) {
            world.setStorm(true);
            world.setThundering(false);
            world.setWeatherDuration(length * 20);
            p.sendMessage(MessageColor.POSITIVE + "Set weather to " + MessageColor.NEUTRAL + "rain" + MessageColor.POSITIVE + " in " + MessageColor.NEUTRAL + wName + MessageColor.POSITIVE + " for " + MessageColor.NEUTRAL + length + MessageColor.POSITIVE + " seconds.");
            return true;
        } else if (conds.toLowerCase().startsWith("storm")) {
            world.setStorm(true);
            world.setThundering(true);
            world.setWeatherDuration(length * 20);
            p.sendMessage(MessageColor.POSITIVE + "Set weather to " + MessageColor.NEUTRAL + "storm" + MessageColor.POSITIVE + " in " + MessageColor.NEUTRAL + wName + MessageColor.POSITIVE + " for " + MessageColor.NEUTRAL + length + MessageColor.POSITIVE + " seconds.");
            return true;
        } else {
            p.sendMessage(MessageColor.NEGATIVE + "Invalid condition!");
            return false;
        }
    }

    @Override
    public boolean runCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (args.length < 1) {
            cs.sendMessage(cmd.getDescription());
            return false;
        }

        if (!(cs instanceof Player)) {
            cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
            return true;
        }
        if (args.length == 1) {
            Player p = (Player) cs;
            changeWeather(p, args[0]);
            return true;
        } else if (args.length > 1) {
            Player p = (Player) cs;
            String conds = args[0];
            int length = RUtils.timeFormatToSeconds(args[1]);
            if (length <= 0) {
                cs.sendMessage(MessageColor.NEGATIVE + "Invalid time specified.");
                return true;
            }
            changeWeather(p, conds, length);
            return true;
        }
        return true;
    }
}
