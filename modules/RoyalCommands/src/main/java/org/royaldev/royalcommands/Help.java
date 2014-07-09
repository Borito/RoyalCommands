package org.royaldev.royalcommands;

import org.bukkit.command.Command;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

public class Help {

    private final RoyalCommands plugin;
    private final TreeMap<String, List<PluginCommand>> commands = new TreeMap<>();
    private String customHelp = "###";

    Help(RoyalCommands instance) {
        this.plugin = instance;
    }

    public TreeMap<String, List<PluginCommand>> getCommands() {
        return this.commands;
    }

    public String getCustomHelp() {
        return this.customHelp;
    }

    public void reloadHelp() {
        this.commands.clear();
        final SimpleCommandMap commandMap;
        try {
            Object result = RUtils.getPrivateField(this.plugin.getServer().getPluginManager(), "commandMap");
            commandMap = (SimpleCommandMap) result;
        } catch (Exception e) {
            this.plugin.getLogger().warning("Could not get command map; help could not be started.");
            return;
        }
        for (final Command c : commandMap.getCommands()) {
            if (!(c instanceof PluginCommand)) continue;
            final PluginCommand pc = (PluginCommand) c;
            final String pluginName = pc.getPlugin().getName().toLowerCase();
            List<PluginCommand> pCommands = this.commands.get(pluginName);
            if (pCommands == null) pCommands = new ArrayList<>();
            if (!pCommands.contains(pc)) pCommands.add(pc);
            this.commands.put(pluginName, pCommands);
        }
        for (final String pluginName : this.commands.keySet()) {
            final List<PluginCommand> pCommands = this.commands.get(pluginName);
            if (pCommands == null) continue;
            Collections.sort(pCommands, new Comparator<PluginCommand>() {
                @Override
                public int compare(final PluginCommand object1, final PluginCommand object2) {
                    return object1.getName().compareTo(object2.getName());
                }
            });
            this.commands.put(pluginName, pCommands);
        }
        // custom help
        final StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(new File(this.plugin.getDataFolder(), "help.txt")));
            String input;
            while ((input = br.readLine()) != null) sb.append(input).append("\n");
        } catch (FileNotFoundException e) {
            sb.append(MessageColor.NEGATIVE).append("###\nCouldn't find a help file!");
            return;
        } catch (IOException e) {
            sb.append(MessageColor.NEGATIVE).append("###\nAn error occurred: ").append(MessageColor.NEUTRAL).append(e.getMessage());
            return;
        } finally {
            try {
                if (br != null) br.close();
            } catch (IOException ignored) {
            }
        }
        this.customHelp = sb.toString();
    }
}
