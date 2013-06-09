package org.royaldev.royalcommands.rcommands;

import org.royaldev.royalcommands.MessageColor;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.Config;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.io.File;
import java.util.Random;

public class CmdWorldManager implements CommandExecutor {

    private RoyalCommands plugin;

    public CmdWorldManager(RoyalCommands instance) {
        plugin = instance;
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("worldmanager")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.worldmanager")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                cs.sendMessage(cmd.getUsage().replace("<command>", label));
                return true;
            }
            if (!Config.useWorldManager) {
                cs.sendMessage(MessageColor.NEGATIVE + "WorldManager is disabled!");
                return true;
            }
            String command = args[0].toLowerCase();
            if (command.equals("create")) {
                if (args.length < 4) {
                    cs.sendMessage(MessageColor.NEGATIVE + "Not enough arguments! Try " + MessageColor.NEUTRAL + "/" + label + " help" + MessageColor.NEGATIVE + " for help.");
                    return true;
                }
                String name = args[1];
                for (World w : plugin.getServer().getWorlds()) {
                    if (w.getName().equals(name)) {
                        cs.sendMessage(MessageColor.NEGATIVE + "A world with that name already exists!");
                        return true;
                    }
                }
                WorldType type = WorldType.getByName(args[2].toUpperCase());
                if (type == null) {
                    cs.sendMessage(MessageColor.NEGATIVE + "Invalid world type!");
                    String types = "";
                    for (WorldType t : WorldType.values())
                        types = (types.equals("")) ? types.concat(MessageColor.NEUTRAL + t.getName() + MessageColor.RESET) : types.concat(", " + MessageColor.NEUTRAL + t.getName() + MessageColor.RESET);
                    cs.sendMessage(types);
                    return true;
                }
                World.Environment we;
                try {
                    we = World.Environment.valueOf(args[3].toUpperCase());
                } catch (Exception e) {
                    cs.sendMessage(MessageColor.NEGATIVE + "Invalid environment!");
                    String types = "";
                    for (World.Environment t : World.Environment.values())
                        types = (types.equals("")) ? types.concat(MessageColor.NEUTRAL + t.name() + MessageColor.RESET) : types.concat(", " + MessageColor.NEUTRAL + t.name() + MessageColor.RESET);
                    cs.sendMessage(types);
                    return true;
                }
                cs.sendMessage(MessageColor.POSITIVE + "Creating world...");
                WorldCreator wc = new WorldCreator(name);
                wc = wc.type(type);
                wc = wc.environment(we);
                if (args.length > 4) {
                    long seed;
                    try {
                        seed = Long.valueOf(args[4]);
                    } catch (Exception e) {
                        seed = args[4].hashCode();
                    }
                    wc = wc.seed(seed);
                } else wc = wc.seed(new Random().nextLong());
                if (args.length > 5) {
                    wc = wc.generator(args[5]);
                    RoyalCommands.wm.getConfig().set("worlds." + name + ".generator", args[5]);
                }
                World w = wc.createWorld();
                w.save();
                cs.sendMessage(MessageColor.POSITIVE + "World " + MessageColor.NEUTRAL + w.getName() + MessageColor.POSITIVE + " created successfully.");
                return true;
            } else if (command.equals("unload")) {
                if (args.length < 2) {
                    cs.sendMessage(MessageColor.NEGATIVE + "Not enough arguments! Try " + MessageColor.NEUTRAL + "/" + label + " help" + MessageColor.NEGATIVE + " for help.");
                    return true;
                }
                World w = plugin.getServer().getWorld(args[1]);
                if (w == null) {
                    cs.sendMessage(MessageColor.NEGATIVE + "No such world!");
                    return true;
                }
                cs.sendMessage(MessageColor.POSITIVE + "Unloading world...");
                if (args.length > 2 && Boolean.getBoolean(args[2].toLowerCase()))
                    for (Player p : w.getPlayers())
                        p.kickPlayer("Your world is being unloaded!");
                boolean success = plugin.getServer().unloadWorld(w, true);
                if (success)
                    cs.sendMessage(MessageColor.POSITIVE + "World unloaded successfully!");
                else cs.sendMessage(MessageColor.NEGATIVE + "Could not unload that world.");
                return true;
            } else if (command.equals("delete")) {
                if (args.length < 2) {
                    cs.sendMessage(MessageColor.NEGATIVE + "Not enough arguments! Try " + MessageColor.NEUTRAL + "/" + label + " help" + MessageColor.NEGATIVE + " for help.");
                    return true;
                }
                World w = plugin.getServer().getWorld(args[1]);
                if (w == null) {
                    cs.sendMessage(MessageColor.NEGATIVE + "No such world!");
                    return true;
                }
                cs.sendMessage(MessageColor.POSITIVE + "Unloading world...");
                if (args.length > 2 && args[2].equalsIgnoreCase("true"))
                    for (Player p : w.getPlayers())
                        p.kickPlayer("Your world is being unloaded!");
                boolean success = plugin.getServer().unloadWorld(w, true);
                if (success)
                    cs.sendMessage(MessageColor.POSITIVE + "World unloaded successfully!");
                else {
                    cs.sendMessage(MessageColor.NEGATIVE + "Could not unload that world.");
                    return true;
                }
                cs.sendMessage(MessageColor.POSITIVE + "Deleting world...");
                success = RUtils.deleteDirectory(w.getWorldFolder());
                if (success)
                    cs.sendMessage(MessageColor.POSITIVE + "Successfully deleted world!");
                else cs.sendMessage(MessageColor.NEGATIVE + "Could not delete world.");
                return true;
            } else if (command.equals("list")) {
                cs.sendMessage(MessageColor.POSITIVE + "Worlds:");
                String worlds = "";
                for (World w : plugin.getServer().getWorlds())
                    worlds = (worlds.equals("")) ? worlds.concat(MessageColor.NEUTRAL + RUtils.getMVWorldName(w)) : worlds.concat(MessageColor.RESET + ", " + MessageColor.NEUTRAL + RUtils.getMVWorldName(w));
                cs.sendMessage(worlds);
                return true;
            } else if (command.equals("help")) {
                cs.sendMessage(MessageColor.POSITIVE + "RoyalCommands WorldManager Help");
                cs.sendMessage(MessageColor.POSITIVE + "===============================");
                cs.sendMessage("* " + MessageColor.NEUTRAL + "/" + label + " create [name] [type] [environment] (seed) (generator)" + MessageColor.POSITIVE + " - Creates a new world.");
                cs.sendMessage("* " + MessageColor.NEUTRAL + "/" + label + " load [name]" + MessageColor.POSITIVE + " - Loads a world.");
                cs.sendMessage("* " + MessageColor.NEUTRAL + "/" + label + " unload [name] (true)" + MessageColor.POSITIVE + " - Unloads a world. If true is specified, will kick all players on the world.");
                cs.sendMessage("* " + MessageColor.NEUTRAL + "/" + label + " delete [name] (true)" + MessageColor.POSITIVE + " - Unloads and deletes a world. If true is specified, will kick all players on the world.");
                cs.sendMessage("* " + MessageColor.NEUTRAL + "/" + label + " info" + MessageColor.POSITIVE + " - Displays available world types and environments; if you are a player, displays information about your world.");
                cs.sendMessage("* " + MessageColor.NEUTRAL + "/" + label + " help" + MessageColor.POSITIVE + " - Displays this help.");
                cs.sendMessage("* " + MessageColor.NEUTRAL + "/" + label + " list" + MessageColor.POSITIVE + " - Lists all the loaded worlds.");
                return true;
            } else if (command.equals("load")) {
                if (args.length < 2) {
                    cs.sendMessage(MessageColor.NEGATIVE + "Not enough arguments! Try " + MessageColor.NEUTRAL + "/" + label + " help" + MessageColor.NEGATIVE + " for help.");
                    return true;
                }
                String name = args[1];
                boolean contains = false;
                File[] fs = plugin.getServer().getWorldContainer().listFiles();
                if (fs == null) {
                    cs.sendMessage(MessageColor.NEGATIVE + "The world directory is invalid!");
                    return true;
                }
                for (File f : fs) if (f.getName().equals(name)) contains = true;
                if (!contains) {
                    cs.sendMessage(MessageColor.NEGATIVE + "No such world!");
                    return true;
                }
                World w;
                try {
                    w = RoyalCommands.wm.loadWorld(name);
                } catch (IllegalArgumentException e) {
                    cs.sendMessage(MessageColor.NEGATIVE + "No such world!");
                    return true;
                } catch (NullPointerException e) {
                    cs.sendMessage(MessageColor.NEGATIVE + "Could not read world folders!");
                    return true;
                }
                cs.sendMessage(MessageColor.POSITIVE + "Loaded world " + MessageColor.NEUTRAL + w.getName() + MessageColor.POSITIVE + ".");
                return true;
            } else if (command.equals("info")) {
                cs.sendMessage(MessageColor.POSITIVE + "RoyalCommands WorldManager Info");
                cs.sendMessage(MessageColor.POSITIVE + "===============================");
                cs.sendMessage(MessageColor.POSITIVE + "Available world types:");
                String types = "";
                for (WorldType t : WorldType.values())
                    types = (types.equals("")) ? types.concat(MessageColor.NEUTRAL + t.getName() + MessageColor.RESET) : types.concat(", " + MessageColor.NEUTRAL + t.getName() + MessageColor.RESET);
                cs.sendMessage("  " + types);
                types = "";
                cs.sendMessage(MessageColor.POSITIVE + "Available world environments:");
                for (World.Environment t : World.Environment.values())
                    types = (types.equals("")) ? types.concat(MessageColor.NEUTRAL + t.name() + MessageColor.RESET) : types.concat(", " + MessageColor.NEUTRAL + t.name() + MessageColor.RESET);
                cs.sendMessage("  " + types);
                if (!(cs instanceof Player)) return true;
                Player p = (Player) cs;
                World w = p.getWorld();
                cs.sendMessage(MessageColor.POSITIVE + "Information on this world:");
                cs.sendMessage(MessageColor.POSITIVE + "Name: " + MessageColor.NEUTRAL + w.getName());
                cs.sendMessage(MessageColor.POSITIVE + "Environment: " + MessageColor.NEUTRAL + w.getEnvironment().name());
                return true;
            } else if (command.equals("tp") || command.equals("teleport")) {
                if (!(cs instanceof Player)) {
                    cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
                    return true;
                }
                if (args.length < 2) {
                    cs.sendMessage(MessageColor.NEGATIVE + "Not enough arguments! Try " + MessageColor.NEUTRAL + "/" + label + " help" + MessageColor.NEGATIVE + " for help.");
                    return true;
                }
                Player p = (Player) cs;
                String world = args[1];
                World w = plugin.getServer().getWorld(world);
                if (w == null) {
                    cs.sendMessage(MessageColor.NEGATIVE + "That world does not exist!");
                    return true;
                }
                p.sendMessage(MessageColor.POSITIVE + "Teleporting you to world " + MessageColor.NEUTRAL + RUtils.getMVWorldName(w) + MessageColor.POSITIVE + ".");
                String error = RUtils.teleport(p, CmdSpawn.getWorldSpawn(w));
                if (!error.isEmpty()) {
                    p.sendMessage(MessageColor.NEGATIVE + error);
                    return true;
                }
                return true;
            } else if (command.equals("who")) {
                for (World w : plugin.getServer().getWorlds()) {
                    StringBuilder sb = new StringBuilder(RUtils.getMVWorldName(w));
                    sb.append(": ");
                    if (w.getPlayers().isEmpty()) {
                        if (Config.wmShowEmptyWorlds)
                            cs.sendMessage(MessageColor.NEGATIVE + "No players in " + MessageColor.NEUTRAL + RUtils.getMVWorldName(w) + MessageColor.NEGATIVE + ".");
                        continue;
                    }
                    for (Player p : w.getPlayers()) {
                        sb.append(MessageColor.NEUTRAL);
                        sb.append(p.getName());
                        sb.append(MessageColor.RESET);
                        sb.append(", ");
                    }
                    cs.sendMessage(sb.substring(0, sb.length() - 4));
                }
                return true;
            } else {
                cs.sendMessage(MessageColor.NEGATIVE + "Invalid subcommand!");
                return true;
            }

        }
        return false;
    }

}
