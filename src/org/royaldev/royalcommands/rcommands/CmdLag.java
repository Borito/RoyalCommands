package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdLag implements CommandExecutor {

    RoyalCommands plugin;

    public CmdLag(RoyalCommands instance) {
        plugin = instance;
    }

    // HEAVILY influenced by commandbook's lag measuring
    // Give them most credit

    @Override
    public boolean onCommand(final CommandSender cs, Command cmd, String label, String[] args) {
        if (plugin.isAuthorized(cs, "rcmds.lag")) {
            RUtils.dispNoPerms(cs);
            return true;
        }
        int tps = 20;
        int runfor = 5;
        if (args.length > 0) {
            try {
                runfor = Integer.valueOf(args[0]);
            } catch (Exception e) {
                cs.sendMessage(ChatColor.RED + "Please input the amount of whole seconds you would like to run the test for.");
                return true;
            }
        }

        final int realms = runfor * 1000;
        final int realsecs = runfor;
        final int realticks = tps * realsecs;
        final World world = plugin.getServer().getWorlds().get(0);
        final long started = System.currentTimeMillis();
        final long startedticks = world.getFullTime();

        cs.sendMessage(ChatColor.YELLOW + "This measures in-game time, so please do not change the time for " + ChatColor.GRAY + realsecs + ChatColor.YELLOW + " seconds.");

        Runnable getlag = new Runnable() {
            public void run() {
                long ran = System.currentTimeMillis();
                long ranticks = world.getFullTime();

                long ranforms = started - ran;
                long ranforsecs = started / 1000;
                long currentticks = startedticks - ranticks;

                long error = ((realms - ranforms) / ranforms) * 100;
                long rtps = currentticks / ranforsecs;

                if (realticks != currentticks)
                    cs.sendMessage(ChatColor.RED + "Got " + ChatColor.GRAY + realticks + ChatColor.RED + " instead of " + ChatColor.GRAY + currentticks + ChatColor.RED + "; Bukkit scheduling may be off.");

                if (Math.round(rtps) == 20)
                    cs.sendMessage(ChatColor.GREEN + "Full throttle" + ChatColor.WHITE + " - " + ChatColor.GREEN + 20 + ChatColor.WHITE + "/20 TPS");
                else if (rtps < 19)
                    cs.sendMessage(ChatColor.YELLOW + "Small lag" + ChatColor.WHITE + " - " + ChatColor.YELLOW + rtps + ChatColor.WHITE + "/20 TPS");
                else if (rtps < 15)
                    cs.sendMessage(ChatColor.RED + "Big lag" + ChatColor.WHITE + " - " + ChatColor.RED + rtps + ChatColor.WHITE + "/20 TPS");
                else if (rtps < 10)
                    cs.sendMessage(ChatColor.RED + "Severe lag" + ChatColor.WHITE + " - " + ChatColor.RED + rtps + ChatColor.WHITE + "/20 TPS");
                else if (rtps < 5)
                    cs.sendMessage(ChatColor.DARK_RED + "Inefficient" + ChatColor.WHITE + " - " + ChatColor.DARK_RED + rtps + ChatColor.WHITE + "/20 TPS");
                else if (rtps > 20)
                    cs.sendMessage(ChatColor.GOLD + "Overboard" + ChatColor.WHITE + " - " + ChatColor.GOLD + rtps + ChatColor.WHITE + "/20 TPS");

                cs.sendMessage(ChatColor.BLUE + "Margin of error: " + ChatColor.GRAY + error);
            }
        };

        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, getlag, realticks);

        return true;
    }
}
