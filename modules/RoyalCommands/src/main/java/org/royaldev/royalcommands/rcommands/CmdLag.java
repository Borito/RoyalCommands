package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RoyalCommands;

import java.text.DecimalFormat;

@ReflectCommand
public class CmdLag extends BaseCommand {

    public CmdLag(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    // Give them most credit@Override
    public boolean runCommand(final CommandSender cs, Command cmd, String label, String[] args) {
        double tps = 20D;
        double runfor = 5D;
        if (args.length > 0) {
            try {
                runfor = Double.valueOf(args[0]);
            } catch (Exception e) {
                cs.sendMessage(MessageColor.NEGATIVE + "Please input a valid number.");
                return true;
            }
        }
        final double expms = runfor * 1000D;
        final double expsecs = runfor;
        final double expticks = tps * expsecs;
        final World world = this.plugin.getServer().getWorlds().get(0);
        final double started = System.currentTimeMillis();
        final double startedticks = world.getFullTime();
        cs.sendMessage(MessageColor.POSITIVE + "This measures in-game time, so please do not change the time for " + MessageColor.NEUTRAL + expsecs + MessageColor.POSITIVE + " seconds.");
        Runnable getlag = new Runnable() {
            public void run() {
                double ran = System.currentTimeMillis();
                double ranticks = world.getFullTime();
                double ranforms = ran - started;
                double ranforsecs = ranforms / 1000D;
                double currentticks = ranticks - startedticks;
                double error = Math.abs(((ranforms - expms) / expms) * 100D);
                double rtps = currentticks / ranforsecs;
                if (expticks != currentticks)
                    cs.sendMessage(MessageColor.NEGATIVE + "Got " + MessageColor.NEUTRAL + currentticks + "ticks" + MessageColor.NEGATIVE + " instead of " + MessageColor.NEUTRAL + expticks + "ticks" + MessageColor.NEGATIVE + "; Bukkit scheduling may be off.");
                long rrtps = Math.round(rtps);
                DecimalFormat df = new DecimalFormat("00.00");
                String srtps = df.format(rtps);
                if (rrtps == 20L)
                    cs.sendMessage(ChatColor.GREEN + "Full throttle" + MessageColor.RESET + " - " + ChatColor.GREEN + 20 + MessageColor.RESET + "/20 TPS");
                else if (rrtps < 5L)
                    cs.sendMessage(ChatColor.DARK_RED + "Inefficient" + MessageColor.RESET + " - " + ChatColor.DARK_RED + srtps + MessageColor.RESET + "/20 TPS");
                else if (rrtps < 10L)
                    cs.sendMessage(MessageColor.NEGATIVE + "Severe lag" + MessageColor.RESET + " - " + MessageColor.NEGATIVE + srtps + MessageColor.RESET + "/20 TPS");
                else if (rrtps < 15L)
                    cs.sendMessage(MessageColor.NEGATIVE + "Big lag" + MessageColor.RESET + " - " + MessageColor.NEGATIVE + srtps + MessageColor.RESET + "/20 TPS");
                else if (rrtps <= 19L)
                    cs.sendMessage(ChatColor.YELLOW + "Small lag" + MessageColor.RESET + " - " + ChatColor.YELLOW + srtps + MessageColor.RESET + "/20 TPS");
                else if (rrtps > 20L)
                    cs.sendMessage(ChatColor.GOLD + "Overboard" + MessageColor.RESET + " - " + ChatColor.GOLD + srtps + MessageColor.RESET + "/20 TPS");
                else
                    cs.sendMessage(MessageColor.NEUTRAL + "Unknown" + MessageColor.RESET + " - " + MessageColor.NEUTRAL + srtps + MessageColor.RESET + "/20 TPS");

                cs.sendMessage(MessageColor.POSITIVE + "Margin of error: " + MessageColor.NEUTRAL + df.format(error) + "%");
            }
        };
        this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, getlag, (long) expticks);
        return true;
    }
}
