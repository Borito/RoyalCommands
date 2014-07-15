package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RoyalCommands;

import java.text.DecimalFormat;

@ReflectCommand
public class CmdMemory extends BaseCommand {

    public CmdMemory(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(CommandSender cs, Command cmd, String label, String[] args) {

        double memUsed = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1048576D;
        double memMax = Runtime.getRuntime().maxMemory() / 1048576D;
        double memFree = memMax - memUsed;
        double percfree = (100D / memMax) * memFree;
        ChatColor color;
        if (percfree >= 60D) color = ChatColor.GREEN;
        else if (percfree >= 35D) color = ChatColor.YELLOW;
        else color = ChatColor.RED;
        DecimalFormat df = new DecimalFormat("00.00");
        cs.sendMessage(color + df.format(percfree) + "% " + MessageColor.POSITIVE + "free (" + color + memUsed + MessageColor.POSITIVE + " MB/" + memMax + " MB)");
        return true;
    }
}
