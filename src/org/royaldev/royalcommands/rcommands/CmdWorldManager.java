package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class CmdWorldManager implements CommandExecutor {

    RoyalCommands plugin;

    public CmdWorldManager(RoyalCommands instance) {
        plugin = instance;
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("worldmanager")) {
            if (!plugin.isAuthorized(cs, "rcmds.worldmanager")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            String command = args[0].toLowerCase();
            if (command.equals("create")) {
                if (args.length < 4) {
                    cs.sendMessage(ChatColor.RED + "Not enough arguments! Try " + ChatColor.GRAY + "/" + label + " help" + ChatColor.RED + " for help.");
                    return true;
                }
                String name = args[1];
                for (World w : plugin.getServer().getWorlds()) {
                    if (w.getName().equals(name)) {
                        cs.sendMessage(ChatColor.RED + "A world with that name already exists!");
                        return true;
                    }
                }
                WorldType type = WorldType.getByName(args[2].toUpperCase());
                if (type == null) {
                    cs.sendMessage(ChatColor.RED + "Invalid world type!");
                    String types = "";
                    for (WorldType t : WorldType.values())
                        types = (types.equals("")) ? types.concat(ChatColor.GRAY + t.getName() + ChatColor.WHITE) : types.concat(", " + ChatColor.GRAY + t.getName() + ChatColor.WHITE);
                    cs.sendMessage(types);
                    return true;
                }
                World.Environment we;
                try {
                    we = World.Environment.valueOf(args[3].toUpperCase());
                } catch (Exception e) {
                    cs.sendMessage(ChatColor.RED + "Invalid environment!");
                    String types = "";
                    for (World.Environment t : World.Environment.values())
                        types = (types.equals("")) ? types.concat(ChatColor.GRAY + t.name() + ChatColor.WHITE) : types.concat(", " + ChatColor.GRAY + t.name() + ChatColor.WHITE);
                    cs.sendMessage(types);
                    return true;
                }
                cs.sendMessage(ChatColor.BLUE + "Creating world...");
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
                if (args.length > 5) wc = wc.generator(args[5]);
                World w = wc.createWorld();
                w.save();
                RoyalCommands.wm.addToLoadedWorlds(w);
                cs.sendMessage(ChatColor.BLUE + "World " + ChatColor.GRAY + w.getName() + ChatColor.BLUE + " created successfully.");
                return true;
            } else if (command.equals("unload")) {
                if (args.length < 2) {
                    cs.sendMessage(ChatColor.RED + "Not enough arguments! Try " + ChatColor.GRAY + "/" + label + " help" + ChatColor.RED + " for help.");
                    return true;
                }
                World w = plugin.getServer().getWorld(args[1]);
                if (w == null) {
                    cs.sendMessage(ChatColor.RED + "No such world!");
                    return true;
                }
                cs.sendMessage(ChatColor.BLUE + "Unloading world...");
                if (args.length > 2 && args[2].equalsIgnoreCase("true"))
                    for (Player p : w.getPlayers())
                        p.kickPlayer("Your world is being unloaded!");
                boolean success = RoyalCommands.wm.unloadWorld(w);
                if (success)
                    cs.sendMessage(ChatColor.BLUE + "World unloaded successfully!");
                else cs.sendMessage(ChatColor.RED + "Could not unload that world.");
                return true;
            } else if (command.equals("delete")) {
                if (args.length < 2) {
                    cs.sendMessage(ChatColor.RED + "Not enough arguments! Try " + ChatColor.GRAY + "/" + label + " help" + ChatColor.RED + " for help.");
                    return true;
                }
                World w = plugin.getServer().getWorld(args[1]);
                if (w == null) {
                    cs.sendMessage(ChatColor.RED + "No such world!");
                    return true;
                }
                cs.sendMessage(ChatColor.BLUE + "Unloading world...");
                if (args.length > 2 && Boolean.getBoolean(args[2].toLowerCase()))
                    for (Player p : w.getPlayers())
                        p.kickPlayer("Your world is being unloaded!");
                boolean success = RoyalCommands.wm.unloadWorld(w);
                if (success)
                    cs.sendMessage(ChatColor.BLUE + "World unloaded successfully!");
                else {
                    cs.sendMessage(ChatColor.RED + "Could not unload that world.");
                    return true;
                }
                cs.sendMessage(ChatColor.BLUE + "Deleting world...");
                try {
                    FileUtils.deleteDirectory(w.getWorldFolder());
                    success = true;
                } catch (IOException e) {
                    success = false;
                }
                if (success) {
                    cs.sendMessage(ChatColor.BLUE + "Successfully deleted world!");
                    RoyalCommands.wm.removeFromLoadedWorlds(w);
                } else cs.sendMessage(ChatColor.RED + "Could not delete world.");
                return true;
            } else if (command.equals("list")) {
                cs.sendMessage(ChatColor.BLUE + "Worlds:");
                String worlds = "";
                for (World w : plugin.getServer().getWorlds())
                    worlds = (worlds.equals("")) ? worlds.concat(ChatColor.GRAY + w.getName()) : worlds.concat(ChatColor.WHITE + ", " + ChatColor.GRAY + w.getName());
                cs.sendMessage(worlds);
                return true;
            } else if (command.equals("help")) {
                cs.sendMessage(ChatColor.BLUE + "RoyalCommands WorldManager Help");
                cs.sendMessage(ChatColor.BLUE + "===============================");
                cs.sendMessage("* " + ChatColor.GRAY + "/" + label + " create [name] [type] [environment] (seed) (generator)" + ChatColor.BLUE + " - Creates a new world.");
                cs.sendMessage("* " + ChatColor.GRAY + "/" + label + " load [name]" + ChatColor.BLUE + " - Loads a world.");
                cs.sendMessage("* " + ChatColor.GRAY + "/" + label + " unload [name] (true)" + ChatColor.BLUE + " - Unloads a world. If true is specified, will kick all players on the world.");
                cs.sendMessage("* " + ChatColor.GRAY + "/" + label + " delete [name] (true)" + ChatColor.BLUE + " - Unloads and deletes a world. If true is specified, will kick all players on the world.");
                cs.sendMessage("* " + ChatColor.GRAY + "/" + label + " info" + ChatColor.BLUE + " - Displays available world types and environments; if you are a player, displays information about your world.");
                cs.sendMessage("* " + ChatColor.GRAY + "/" + label + " help" + ChatColor.BLUE + " - Displays this help.");
                cs.sendMessage("* " + ChatColor.GRAY + "/" + label + " list" + ChatColor.BLUE + " - Lists all the loaded worlds.");
                cs.sendMessage("* " + ChatColor.GRAY + "/" + label + " tp [name]" + ChatColor.BLUE + " - Teleports to the specified world.");
                cs.sendMessage("* " + ChatColor.GRAY + "/" + label + " who" + ChatColor.BLUE + " - Lists the players in each world.");
                return true;
            } else if (command.equals("load")) {
                if (args.length < 2) {
                    cs.sendMessage(ChatColor.RED + "Not enough arguments! Try " + ChatColor.GRAY + "/" + label + " help" + ChatColor.RED + " for help.");
                    return true;
                }
                String name = args[1];
                boolean contains = false;
                for (File f : plugin.getServer().getWorldContainer().listFiles()) {
                    if (f.getName().equals(name)) contains = true;
                }
                if (!contains) {
                    cs.sendMessage(ChatColor.RED + "No such world!");
                    return true;
                }
                World w;
                try {
                    w = RoyalCommands.wm.loadWorld(name);
                } catch (IllegalArgumentException e) {
                    cs.sendMessage(ChatColor.RED + "No such world!");
                    return true;
                } catch (NullPointerException e) {
                    cs.sendMessage(ChatColor.RED + "Could not read world folders!");
                    return true;
                }
                cs.sendMessage(ChatColor.BLUE + "Loaded world " + ChatColor.GRAY + w.getName() + ChatColor.BLUE + ".");
                return true;
            } else if (command.equals("info")) {
                cs.sendMessage(ChatColor.BLUE + "RoyalCommands WorldManager Info");
                cs.sendMessage(ChatColor.BLUE + "===============================");
                cs.sendMessage(ChatColor.BLUE + "Available world types:");
                String types = "";
                for (WorldType t : WorldType.values())
                    types = (types.equals("")) ? types.concat(ChatColor.GRAY + t.getName() + ChatColor.WHITE) : types.concat(", " + ChatColor.GRAY + t.getName() + ChatColor.WHITE);
                cs.sendMessage("  " + types);
                types = "";
                cs.sendMessage(ChatColor.BLUE + "Available world environments:");
                for (World.Environment t : World.Environment.values())
                    types = (types.equals("")) ? types.concat(ChatColor.GRAY + t.name() + ChatColor.WHITE) : types.concat(", " + ChatColor.GRAY + t.name() + ChatColor.WHITE);
                cs.sendMessage("  " + types);
                if (!(cs instanceof Player)) return true;
                Player p = (Player) cs;
                World w = p.getWorld();
                cs.sendMessage(ChatColor.BLUE + "Information on this world:");
                cs.sendMessage(ChatColor.BLUE + "Name: " + ChatColor.GRAY + w.getName());
                cs.sendMessage(ChatColor.BLUE + "Environment: " + ChatColor.GRAY + w.getEnvironment().name());
                return true;
            } else if (command.equals("tp") || command.equals("teleport")) {
                if (!(cs instanceof Player)) {
                    cs.sendMessage(ChatColor.RED + "This command is only available to players!");
                    return true;
                }
                if (args.length < 2) {
                    cs.sendMessage(ChatColor.RED + "Not enough arguments! Try " + ChatColor.GRAY + "/" + label + " help" + ChatColor.RED + " for help.");
                    return true;
                }
                Player p = (Player) cs;
                String world = args[1];
                World w = plugin.getServer().getWorld(world);
                if (w == null) {
                    cs.sendMessage(ChatColor.RED + "That world does not exist!");
                    return true;
                }
                p.sendMessage(ChatColor.BLUE + "Teleporting you to world " + ChatColor.GRAY + RUtils.getMVWorldName(w) + ChatColor.BLUE + ".");
                String error = RUtils.teleport(p, CmdSpawn.getWorldSpawn(w));
                if (!error.isEmpty()) {
                    p.sendMessage(ChatColor.RED + error);
                    return true;
                }
                return true;
            } else if (command.equals("who")) {
                for (World w : plugin.getServer().getWorlds()) {
                    StringBuilder sb = new StringBuilder(RUtils.getMVWorldName(w));
                    sb.append(": ");
                    if (w.getPlayers().isEmpty()) {
                        cs.sendMessage(ChatColor.RED + "No players in " + ChatColor.GRAY + RUtils.getMVWorldName(w) + ChatColor.RED + ".");
                        continue;
                    }
                    for (Player p : w.getPlayers()) {
                        if (plugin.isVanished(p, cs)) continue;
                        sb.append(ChatColor.GRAY);
                        sb.append(p.getName());
                        sb.append(ChatColor.RESET);
                        sb.append(", ");
                    }
                    cs.sendMessage(sb.substring(0, sb.length() - 4)); // "\u00a7r, " = 4
                }
                return true;
            } else {
                cs.sendMessage(ChatColor.RED + "Invalid subcommand!");
                return true;
            }
        }
        return false;
    }

}
