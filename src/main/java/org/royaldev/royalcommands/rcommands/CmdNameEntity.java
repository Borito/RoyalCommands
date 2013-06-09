package org.royaldev.royalcommands.rcommands;

import org.royaldev.royalcommands.MessageColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.util.HashMap;
import java.util.Map;

public class CmdNameEntity implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdNameEntity(RoyalCommands instance) {
        plugin = instance;
    }

    private final static Map<String, String> names = new HashMap<String, String>();

    /**
     * Checks to see if the player has a name to give to an entity.
     *
     * @param s Name of player to check
     * @return true if naming, false if not
     */
    public static boolean isNaming(String s) {
        synchronized (names) {
            return names.containsKey(s);
        }
    }

    /**
     * Checks to see if the player has a name to give to an entity.
     *
     * @param cs CommandSender to check
     * @return true if naming, false if not
     */
    public static boolean isNaming(CommandSender cs) {
        return isNaming(cs.getName());
    }

    /**
     * Gets the name a player is renaming something to. Will return null if isNaming() is false.
     *
     * @param cs CommandSender to get name for
     * @return Name or null
     */
    public static String getNamingName(CommandSender cs) {
        return getNamingName(cs.getName());
    }

    /**
     * Gets the name a player is renaming something to. Will return null if isNaming() is false.
     *
     * @param s Name of player to get name for
     * @return Name or null
     */
    public static String getNamingName(String s) {
        synchronized (names) {
            return names.get(s);
        }
    }

    public static void cancelNaming(CommandSender cs) {
        cancelNaming(cs.getName());
    }

    public static void cancelNaming(String s) {
        synchronized (names) {
            names.remove(s);
        }
    }

    private static void setNamingName(CommandSender cs, String namingName) {
        setNamingName(cs.getName(), namingName);
    }

    private static void setNamingName(String playerName, String namingName) {
        synchronized (names) {
            names.put(playerName, namingName);
        }
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("nameentity")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.nameentity")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                cs.sendMessage(cmd.getUsage().replace("<command>", label));
                return true;
            }
            String newName = RUtils.colorize(RoyalCommands.getFinalArg(args, 0));
            if (newName.equalsIgnoreCase("reset") || newName.equalsIgnoreCase("clear") || newName.equals("remove")) {
                setNamingName(cs, "");
                cs.sendMessage(MessageColor.POSITIVE + "Right click on the entity you want to remove a custom name from.");
            }
            if (newName.length() > 32) {
                cs.sendMessage(MessageColor.NEGATIVE + "The name must be 32 characters or less!");
                return true;
            }
            setNamingName(cs, newName);
            cs.sendMessage(MessageColor.POSITIVE + "Right click on the entity you want to rename " + MessageColor.NEUTRAL + newName + MessageColor.POSITIVE + ".");
            return true;
        }
        return false;
    }

}
