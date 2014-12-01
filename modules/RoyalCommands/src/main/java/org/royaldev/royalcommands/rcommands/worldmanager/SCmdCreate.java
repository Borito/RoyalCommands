package org.royaldev.royalcommands.rcommands.worldmanager;

import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.Config;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.rcommands.CmdWorldManager;
import org.royaldev.royalcommands.rcommands.SubCommand;

import java.util.ArrayList;
import java.util.List;

public class SCmdCreate extends SubCommand<CmdWorldManager> {

    private final Flag<String> nameFlag = new Flag<>(String.class, "name", "n");
    private final Flag<String> typeFlag = new Flag<>(String.class, "type", "t");
    private final Flag<String> envFlag = new Flag<>(String.class, "environment", "env", "e");
    private final Flag<String> seedFlag = new Flag<>(String.class, "seed", "s");
    private final Flag<String> genFlag = new Flag<>(String.class, "generator", "gen", "g");

    public SCmdCreate(final RoyalCommands instance, final CmdWorldManager parent) {
        super(instance, parent, "create", true, "Creates a new world.", "<command> -[name,n] [name] -[type,t] [type] -[environment,env,e] [environment] -(seed,s) (seed) -(generator,gen,g) (generator)", new String[0], new Short[0]);
        this.setAlwaysUse(CompletionType.LIST);
    }

    @Override
    public List<String> customList(CommandSender cs, Command cmd, String label, String[] args, String arg) {
        final List<String> completions = new ArrayList<>();
        if (args.length < 2) return completions;
        return completions; // TODO: do later
    }

    @Override
    public boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] eargs, final CommandArguments ca) {
        if (!Config.useWorldManager) {
            cs.sendMessage(MessageColor.NEGATIVE + "WorldManager is disabled!");
            return true;
        }
        if (!ca.hasContentFlag(this.nameFlag) || !ca.hasContentFlag(this.typeFlag) || !ca.hasContentFlag(this.envFlag)) {
            cs.sendMessage(MessageColor.NEGATIVE + "Not enough arguments! Try " + MessageColor.NEUTRAL + "/" + label + " help" + MessageColor.NEGATIVE + " for help.");
            return true;
        }
        final String name = ca.getFlag(this.nameFlag).getValue();
        final WorldType type = WorldType.getByName(ca.getFlag(this.typeFlag).getValue());
        final Environment we;
        try {
            we = Environment.valueOf(ca.getFlag(this.envFlag).getValue().toUpperCase());
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
        if (ca.hasContentFlag(this.seedFlag)) {
            final String seedString = ca.getFlag(this.seedFlag).getValue();
            long seed;
            try {
                seed = Long.valueOf(seedString);
            } catch (Exception e) {
                seed = seedString.hashCode();
            }
            wc = wc.seed(seed);
        } else wc = wc.seed(this.getParent().getRandom().nextLong());
        if (ca.hasContentFlag(this.genFlag)) {
            final String generator = ca.getFlag(this.genFlag).getValue();
            wc = wc.generator(generator);
            RoyalCommands.wm.getConfig().set("worlds." + name + ".generator", generator);
        }
        World w = wc.createWorld();
        w.save();
        cs.sendMessage(MessageColor.POSITIVE + "World " + MessageColor.NEUTRAL + w.getName() + MessageColor.POSITIVE + " created successfully.");
        return true;
    }
}
