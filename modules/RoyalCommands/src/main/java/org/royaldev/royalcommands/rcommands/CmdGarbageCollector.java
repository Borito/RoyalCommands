package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RoyalCommands;

@ReflectCommand
public class CmdGarbageCollector extends BaseCommand {

    public CmdGarbageCollector(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(CommandSender cs, Command cmd, String label, String[] args) {
        Runtime r = Runtime.getRuntime();
        long oldMem = r.freeMemory() / 1048576L;
        cs.sendMessage(MessageColor.POSITIVE + "Running Java garbage collector...");
        r.gc();
        int processors = r.availableProcessors();
        long maxMem = r.maxMemory() / 1048576L;
        long newMem = r.freeMemory() / 1048576L;
        if (r.maxMemory() < 0L) {
            cs.sendMessage(MessageColor.NEGATIVE + "You may be using CACAO Java, which means that these values may be negative.");
            cs.sendMessage(MessageColor.NEGATIVE + "Please switch to another JVM.");
        }
        cs.sendMessage(MessageColor.POSITIVE + "Used memory before: " + MessageColor.NEUTRAL + (maxMem - oldMem) + " MB");
        cs.sendMessage(MessageColor.POSITIVE + "Current memory: " + MessageColor.NEUTRAL + (maxMem - newMem) + " MB" + MessageColor.POSITIVE + "/" + MessageColor.NEUTRAL + maxMem + " MB");
        cs.sendMessage(MessageColor.POSITIVE + "Memory freed: " + MessageColor.NEUTRAL + (newMem - oldMem) + " MB");
        cs.sendMessage(MessageColor.POSITIVE + "Processors available to Java: " + MessageColor.NEUTRAL + processors);
        return true;
    }
}
