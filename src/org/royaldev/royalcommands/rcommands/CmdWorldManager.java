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
                if (args.length > 2 && Boolean.getBoolean(args[2].toLowerCase()))
                    for (Player p : w.getPlayers()) p.kickPlayer("Your world is being unloaded!");
                boolean success = plugin.getServer().unloadWorld(w, true);
                if (success) cs.sendMessage(ChatColor.BLUE + "World unloaded successfully!");
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
                    for (Player p : w.getPlayers()) p.kickPlayer("Your world is being unloaded!");
                boolean success = plugin.getServer().unloadWorld(w, true);
                if (success) cs.sendMessage(ChatColor.BLUE + "World unloaded successfully!");
                else {
                    cs.sendMessage(ChatColor.RED + "Could not unload that world.");
                    return true;
                }
                cs.sendMessage(ChatColor.BLUE + "Deleting world...");
                success = w.getWorldFolder().delete();
                if (success) cs.sendMessage(ChatColor.BLUE + "World deleted successfully!");
                else cs.sendMessage(ChatColor.RED + "Could not delete world.");
                return true;
            } else if (command.equals("help")) {
                cs.sendMessage(ChatColor.GREEN + "RoyalCommands WorldManager Help");
                cs.sendMessage(ChatColor.GREEN + "===============================");
                cs.sendMessage(RUtils.wrapText("* " + ChatColor.GRAY + "/" + label + " create [name] [type] [environment] (seed) (generator)" + ChatColor.GRAY + " - " + ChatColor.WHITE + "Creates a new world.", 64));
                cs.sendMessage(RUtils.wrapText("* " + ChatColor.GRAY + "/" + label + " unload [name] (true)" + ChatColor.GRAY + " - " + ChatColor.WHITE + "Unloads a world. If true is specified, will kick all players on the world.", 64));
                cs.sendMessage(RUtils.wrapText("* " + ChatColor.GRAY + "/" + label + " delete [name] (true)" + ChatColor.GRAY + " - " + ChatColor.WHITE + "Unloads and deletes a world. If true is specified, will kick all players on the world.", 64));
                cs.sendMessage(RUtils.wrapText("* " + ChatColor.GRAY + "/" + label + " info" + ChatColor.GRAY + " - " + ChatColor.WHITE + "Displays available world types and environments; if you are a player, displays information about your world.", 64));
                cs.sendMessage(RUtils.wrapText("* " + ChatColor.GRAY + "/" + label + " help" + ChatColor.GRAY + " - " + ChatColor.WHITE + "Displays this help.", 64));
                return true;

            } else if (command.equals("info")) {
                cs.sendMessage(ChatColor.GREEN + "RoyalCommands WorldManager Info");
                cs.sendMessage(ChatColor.GREEN + "===============================");
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
                cs.sendMessage(ChatColor.GREEN + "Information on this world:");
                cs.sendMessage(ChatColor.BLUE + "Name: " + ChatColor.GRAY + w.getName());
                cs.sendMessage(ChatColor.BLUE + "Environment: " + ChatColor.GRAY + w.getEnvironment().name());
                return true;
            } else {
                cs.sendMessage(ChatColor.RED + "Invalid subcommand!");
                return true;
            }

        }
        return false;
    }

}
