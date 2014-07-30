package org.royaldev.royalcommands.rcommands;

import mkremins.fanciful.FancyMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.io.File;
import java.io.FilenameFilter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ReflectCommand
public class CmdPrune extends CACommand {

    public CmdPrune(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    protected boolean runCommand(CommandSender cs, Command cmd, String label, String[] eargs, CommandArguments ca) {
        if (!ca.hasContentFlag("t", "time")) {
            cs.sendMessage(MessageColor.NEGATIVE + "You must include a time (-[t,time]) flag.");
            return true;
        }
        final Date dateStarted = new Date();
        final long since = RUtils.timeFormatToSeconds(ca.getFlagString("t", "time")) * 1000L;
        final File userdataDirectory = new File(this.plugin.getDataFolder(), "userdata");
        if (!userdataDirectory.isDirectory()) {
            cs.sendMessage(MessageColor.NEGATIVE + "The userdata location is not a directory!");
            return true;
        }
        final String[] userdataFiles = userdataDirectory.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".yml");
            }
        });
        final List<String> deletedFiles = new ArrayList<>();
        final long threshold = System.currentTimeMillis() - since;
        for (final String file : userdataFiles) {
            final File f = new File(userdataDirectory, file);
            final long lastModified = f.lastModified();
            if (lastModified == 0L) continue; // IOException
            if (lastModified <= threshold) {
                if (!f.delete()) {
                    cs.sendMessage(MessageColor.NEGATIVE + "Could not delete userdata in file " + MessageColor.NEUTRAL + f.getName() + MessageColor.NEGATIVE + " .");
                } else deletedFiles.add(f.getName());
            }
        }
        new FancyMessage("Deleted ").color(MessageColor.POSITIVE._()).then(String.valueOf(deletedFiles.size())).color(MessageColor.NEUTRAL._()).then(" file" + (deletedFiles.size() == 1 ? "" : "s") + ".").color(MessageColor.POSITIVE._()).send(cs);
        if (deletedFiles.size() < 1) return true;
        final StringBuilder sb = new StringBuilder();
        sb.append("The following userdata files (").append(deletedFiles.size()).append(")").append(" were purged by ").append(cs.getName()).append(" at ").append(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss Z").format(dateStarted)).append(".\n\n");
        for (final String fileName : deletedFiles) sb.append(fileName).append("\n");
        this.scheduleHastebin(cs, sb.toString(), MessageColor.POSITIVE + "The userdata files deleted are listed ", MessageColor.NEUTRAL + "here", MessageColor.POSITIVE + ".", "Click here to see the files deleted.");
        return true;
    }
}
