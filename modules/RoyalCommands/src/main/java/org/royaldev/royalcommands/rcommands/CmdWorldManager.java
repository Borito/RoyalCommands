package org.royaldev.royalcommands.rcommands;

import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.Config;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.io.File;
import java.util.Random;

@ReflectCommand
public class CmdWorldManager extends CACommand {

    private final Random r = new Random();

    public CmdWorldManager(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(CommandSender cs, Command cmd, String label, String[] eargs, CommandArguments ca) {
        if (eargs.length < 1) {
            cs.sendMessage(cmd.getDescription());
            return false;
        }
        if (!Config.useWorldManager) {
            cs.sendMessage(MessageColor.NEGATIVE + "WorldManager is disabled!");
            return true;
        }
        final String command = eargs[0].toLowerCase();
        switch (command) {
            case "create": {
                if (!ca.hasFlag("n", "name") || !ca.hasFlag("t", "type") || !ca.hasFlag("e", "env", "environment")) {
                    cs.sendMessage(MessageColor.NEGATIVE + "Not enough arguments! Try " + MessageColor.NEUTRAL + "/" + label + " help" + MessageColor.NEGATIVE + " for help.");
                    return true;
                }
                final String name = ca.getFlagString("n", "name");
                final WorldType type = WorldType.getByName(ca.getFlagString("t", "type"));
                final Environment we;
                try {
                    we = Environment.valueOf(ca.getFlagString("e", "env", "environment").toUpperCase());
                } catch (IllegalArgumentException ex) {
                    cs.sendMessage(MessageColor.NEGATIVE + "Invalid environment!");
                    String types = "";
                    for (Environment t : Environment.values())
                        types = (types.equals("")) ? types.concat(MessageColor.NEUTRAL + t.name() + MessageColor.RESET) : types.concat(", " + MessageColor.NEUTRAL + t.name() + MessageColor.RESET);
                    cs.sendMessage(types);
                    return true;
                }
                for (World w : this.plugin.getServer().getWorlds()) {
                    if (w.getName().equals(name)) {
                        cs.sendMessage(MessageColor.NEGATIVE + "A world with that name already exists!");
                        return true;
                    }
                }
                if (type == null) {
                    cs.sendMessage(MessageColor.NEGATIVE + "Invalid world type!");
                    String types = "";
                    for (WorldType t : WorldType.values())
                        types = (types.equals("")) ? types.concat(MessageColor.NEUTRAL + t.getName() + MessageColor.RESET) : types.concat(", " + MessageColor.NEUTRAL + t.getName() + MessageColor.RESET);
                    cs.sendMessage(types);
                    return true;
                }
                cs.sendMessage(MessageColor.POSITIVE + "Creating world...");
                WorldCreator wc = new WorldCreator(name);
                wc = wc.type(type);
                wc = wc.environment(we);
                if (ca.hasFlag("s", "seed")) {
                    final String seedString = ca.getFlagString("s", "seed");
                    long seed;
                    try {
                        seed = Long.valueOf(seedString);
                    } catch (Exception e) {
                        seed = seedString.hashCode();
                    }
                    wc = wc.seed(seed);
                } else wc = wc.seed(r.nextLong());
                if (ca.hasFlag("g", "gen", "generator")) {
                    final String generator = ca.getFlagString("g", "gen", "generator");
                    wc = wc.generator(generator);
                    RoyalCommands.wm.getConfig().set("worlds." + name + ".generator", generator);
                }
                World w = wc.createWorld();
                w.save();
                cs.sendMessage(MessageColor.POSITIVE + "World " + MessageColor.NEUTRAL + w.getName() + MessageColor.POSITIVE + " created successfully.");
                return true;
            }
            case "unload": {
                if (!ca.hasFlag("n", "name")) {
                    cs.sendMessage(MessageColor.NEGATIVE + "Not enough arguments! Try " + MessageColor.NEUTRAL + "/" + label + " help" + MessageColor.NEGATIVE + " for help.");
                    return true;
                }
                final boolean eject = ca.hasFlag("e", "eject");
                final World w = this.plugin.getServer().getWorld(ca.getFlagString("n", "name"));
                if (w == null) {
                    cs.sendMessage(MessageColor.NEGATIVE + "No such world!");
                    return true;
                }
                cs.sendMessage(MessageColor.POSITIVE + "Unloading world...");
                if (eject) for (Player p : w.getPlayers()) p.kickPlayer("Your world is being unloaded!");
                final boolean success = this.plugin.getServer().unloadWorld(w, true);
                if (success) cs.sendMessage(MessageColor.POSITIVE + "World unloaded successfully!");
                else cs.sendMessage(MessageColor.NEGATIVE + "Could not unload that world.");
                return true;
            }
            case "delete": {
                if (!ca.hasFlag("n", "name")) {
                    cs.sendMessage(MessageColor.NEGATIVE + "Not enough arguments! Try " + MessageColor.NEUTRAL + "/" + label + " help" + MessageColor.NEGATIVE + " for help.");
                    return true;
                }
                final World w = this.plugin.getServer().getWorld(ca.getFlagString("n", "name"));
                if (w == null) {
                    cs.sendMessage(MessageColor.NEGATIVE + "No such world!");
                    return true;
                }
                cs.sendMessage(MessageColor.POSITIVE + "Unloading world...");
                final boolean eject = ca.hasFlag("e", "eject");
                if (eject) for (Player p : w.getPlayers()) p.kickPlayer("Your world is being unloaded!");
                boolean success = this.plugin.getServer().unloadWorld(w, true);
                if (success) cs.sendMessage(MessageColor.POSITIVE + "World unloaded successfully!");
                else {
                    cs.sendMessage(MessageColor.NEGATIVE + "Could not unload that world.");
                    return true;
                }
                cs.sendMessage(MessageColor.POSITIVE + "Deleting world...");
                success = RUtils.deleteDirectory(w.getWorldFolder());
                if (success) cs.sendMessage(MessageColor.POSITIVE + "Successfully deleted world!");
                else cs.sendMessage(MessageColor.NEGATIVE + "Could not delete world.");
                return true;
            }
            case "list":
                cs.sendMessage(MessageColor.POSITIVE + "Worlds:");
                String worlds = "";
                for (World w : this.plugin.getServer().getWorlds())
                    worlds = (worlds.equals("")) ? worlds.concat(MessageColor.NEUTRAL + RUtils.getMVWorldName(w)) : worlds.concat(MessageColor.RESET + ", " + MessageColor.NEUTRAL + RUtils.getMVWorldName(w));
                cs.sendMessage(worlds);
                return true;
            case "help":
                cs.sendMessage(MessageColor.POSITIVE + "RoyalCommands WorldManager Help");
                cs.sendMessage(MessageColor.POSITIVE + "===============================");
                cs.sendMessage("  " + MessageColor.NEUTRAL + "/" + label + " create -[n,name] [name] -[t,type] [type] -[e,env,environment] [environment] -(s,seed) (seed) -(g,gen,generator) (generator)" + MessageColor.POSITIVE + " - Creates a new world.");
                cs.sendMessage("  " + MessageColor.NEUTRAL + "/" + label + " load -[n,name] [name]" + MessageColor.POSITIVE + " - Loads a world.");
                cs.sendMessage("  " + MessageColor.NEUTRAL + "/" + label + " unload -[n,name] [name] -(e,eject)" + MessageColor.POSITIVE + " - Unloads a world. If true is specified, will kick all players on the world.");
                cs.sendMessage("  " + MessageColor.NEUTRAL + "/" + label + " delete -[n,name] [name] -(e,eject)" + MessageColor.POSITIVE + " - Unloads and deletes a world. If true is specified, will kick all players on the world.");
                cs.sendMessage("  " + MessageColor.NEUTRAL + "/" + label + " teleport -[n,name] [name]" + MessageColor.POSITIVE + " - Teleports to a world.");
                cs.sendMessage("  " + MessageColor.NEUTRAL + "/" + label + " who" + MessageColor.POSITIVE + " - Displays who is in all loaded worlds.");
                cs.sendMessage("  " + MessageColor.NEUTRAL + "/" + label + " info" + MessageColor.POSITIVE + " - Displays available world types and environments; if you are a player, displays information about your world.");
                cs.sendMessage("  " + MessageColor.NEUTRAL + "/" + label + " help" + MessageColor.POSITIVE + " - Displays this help.");
                cs.sendMessage("  " + MessageColor.NEUTRAL + "/" + label + " list" + MessageColor.POSITIVE + " - Lists all the loaded worlds.");
                return true;
            case "load": {
                if (!ca.hasFlag("n", "name")) {
                    cs.sendMessage(MessageColor.NEGATIVE + "Not enough arguments! Try " + MessageColor.NEUTRAL + "/" + label + " help" + MessageColor.NEGATIVE + " for help.");
                    return true;
                }
                final String name = ca.getFlagString("n", "name");
                boolean contains = false;
                File[] fs = this.plugin.getServer().getWorldContainer().listFiles();
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
            }
            case "info": {
                cs.sendMessage(MessageColor.POSITIVE + "RoyalCommands WorldManager Info");
                cs.sendMessage(MessageColor.POSITIVE + "===============================");
                cs.sendMessage(MessageColor.POSITIVE + "Available world types:");
                String types = "";
                for (WorldType t : WorldType.values())
                    types = (types.equals("")) ? types.concat(MessageColor.NEUTRAL + t.getName() + MessageColor.RESET) : types.concat(", " + MessageColor.NEUTRAL + t.getName() + MessageColor.RESET);
                cs.sendMessage("  " + types);
                types = "";
                cs.sendMessage(MessageColor.POSITIVE + "Available world environments:");
                for (Environment t : Environment.values())
                    types = (types.equals("")) ? types.concat(MessageColor.NEUTRAL + t.name() + MessageColor.RESET) : types.concat(", " + MessageColor.NEUTRAL + t.name() + MessageColor.RESET);
                cs.sendMessage("  " + types);
                if (!(cs instanceof Player)) return true;
                Player p = (Player) cs;
                World w = p.getWorld();
                cs.sendMessage(MessageColor.POSITIVE + "Information on this world:");
                cs.sendMessage(MessageColor.POSITIVE + "Name: " + MessageColor.NEUTRAL + w.getName());
                cs.sendMessage(MessageColor.POSITIVE + "Environment: " + MessageColor.NEUTRAL + w.getEnvironment().name());
                return true;
            }
            case "tp":
            case "teleport": {
                if (!(cs instanceof Player)) {
                    cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
                    return true;
                }
                if (!ca.hasFlag("n", "name")) {
                    cs.sendMessage(MessageColor.NEGATIVE + "Not enough arguments! Try " + MessageColor.NEUTRAL + "/" + label + " help" + MessageColor.NEGATIVE + " for help.");
                    return true;
                }
                Player p = (Player) cs;
                final String world = ca.getFlagString("n", "name");
                World w = this.plugin.getServer().getWorld(world);
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
            }
            case "who":
                for (World w : this.plugin.getServer().getWorlds()) {
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
            default:
                cs.sendMessage(MessageColor.NEGATIVE + "Invalid subcommand!");
                return true;
        }
    }
}
