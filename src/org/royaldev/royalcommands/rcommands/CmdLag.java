package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.text.DecimalFormat;

public class CmdLag implements CommandExecutor {

    RoyalCommands plugin;

    public CmdLag(RoyalCommands instance) {
        plugin = instance;
    }

    // HEAVILY influenced by commandbook's lag measuring
    // Give them most credit

    @Override
    public boolean onCommand(final CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("lag")) {
            if (!plugin.isAuthorized(cs, "rcmds.lag")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            double tps = 20;
            double runfor = 5;
            if (args.length > 0) {
                try {
                    runfor = Double.valueOf(args[0]);
                } catch (Exception e) {
                    cs.sendMessage(ChatColor.RED + "Please input a valid number.");
                    return true;
                }
            }

            final double expms = runfor * 1000;
            final double expsecs = runfor;
            final double expticks = tps * expsecs;
            final World world = plugin.getServer().getWorlds().get(0);
            final double started = System.currentTimeMillis();
            final double startedticks = world.getFullTime();

            cs.sendMessage(ChatColor.YELLOW + "This measures in-game time, so please do not change the time for " + ChatColor.GRAY + expsecs + ChatColor.YELLOW + " seconds.");

            Runnable getlag = new Runnable() {
                public void run() {
                    double ran = System.currentTimeMillis();
                    double ranticks = world.getFullTime();

                    double ranforms = ran - started;
                    double ranforsecs = ranforms / 1000;
                    double currentticks = ranticks - startedticks;

                    double error = Math.abs(((ranforms - expms) / expms) * 100);
                    double rtps = currentticks / ranforsecs;

                    if (expticks != currentticks)
                        cs.sendMessage(ChatColor.RED + "Got " + ChatColor.GRAY + currentticks + "ticks" + ChatColor.RED + " instead of " + ChatColor.GRAY + expticks + "ticks" + ChatColor.RED + "; Bukkit scheduling may be off.");
                    long rrtps = Math.round(rtps);
                    DecimalFormat df = new DecimalFormat("00.00");
                    String srtps = df.format(rtps);
                    if (rrtps == 20)
                        cs.sendMessage(ChatColor.GREEN + "Full throttle" + ChatColor.WHITE + " - " + ChatColor.GREEN + 20 + ChatColor.WHITE + "/20 TPS");
                    else if (rrtps < 5)
                        cs.sendMessage(ChatColor.DARK_RED + "Inefficient" + ChatColor.WHITE + " - " + ChatColor.DARK_RED + srtps + ChatColor.WHITE + "/20 TPS");
                    else if (rrtps < 10)
                        cs.sendMessage(ChatColor.RED + "Severe lag" + ChatColor.WHITE + " - " + ChatColor.RED + srtps + ChatColor.WHITE + "/20 TPS");
                    else if (rrtps < 15)
                        cs.sendMessage(ChatColor.RED + "Big lag" + ChatColor.WHITE + " - " + ChatColor.RED + srtps + ChatColor.WHITE + "/20 TPS");
                    else if (rrtps <= 19)
                        cs.sendMessage(ChatColor.YELLOW + "Small lag" + ChatColor.WHITE + " - " + ChatColor.YELLOW + srtps + ChatColor.WHITE + "/20 TPS");
                    else if (rrtps > 20)
                        cs.sendMessage(ChatColor.GOLD + "Overboard" + ChatColor.WHITE + " - " + ChatColor.GOLD + srtps + ChatColor.WHITE + "/20 TPS");
                    else
                        cs.sendMessage(ChatColor.GRAY + "Unknown" + ChatColor.WHITE + " - " + ChatColor.GRAY + srtps + ChatColor.WHITE + "/20 TPS");

                    cs.sendMessage(ChatColor.BLUE + "Margin of error: " + ChatColor.GRAY + df.format(error) + "%");
                }
            };

            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, getlag, (long) expticks);

            return true;
        }
        return false;
    }
}
