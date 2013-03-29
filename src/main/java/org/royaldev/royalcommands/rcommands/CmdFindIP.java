package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.PConfManager;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CmdFindIP implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdFindIP(RoyalCommands instance) {
        plugin = instance;
    }

    private boolean isValid(String address) {
        if (address == null) return false;
        String[] ips = address.split("\\.");
        if (ips.length != 4) return false;
        for (String s : ips) {
            int ip;
            try {
                ip = Integer.valueOf(s);
            } catch (Exception e) {
                return false;
            }
            if (ip > 255) return false;
        }
        return true;
    }

    @Override
    public boolean onCommand(final CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("findip")) {
            if (!plugin.isAuthorized(cs, "rcmds.findip")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            final String ip = args[0].replaceAll("[^\\.\\d]+", "");
            final boolean partial = !isValid(ip);
            if (partial) cs.sendMessage(ChatColor.BLUE + "Checking for a partial IP address.");
            final DecimalFormat df = new DecimalFormat("00.00");
            final OfflinePlayer[] offlinePlayers = plugin.getServer().getOfflinePlayers();
            final List<String> hasIP = new ArrayList<String>();
            final Runnable r = new Runnable() {
                @Override
                public void run() {
                    int runThrough = 0;
                    for (OfflinePlayer op : offlinePlayers) {
                        runThrough++;
                        if (runThrough % (int) Math.floor(offlinePlayers.length * (plugin.findIpPercent / 100D)) == 0) {
                            double percentage = ((double) runThrough / (double) offlinePlayers.length) * 100D;
                            cs.sendMessage(ChatColor.BLUE + "Searching. " + ChatColor.GRAY + df.format(percentage) + "%" + ChatColor.BLUE + " (" + ChatColor.GRAY + runThrough + ChatColor.BLUE + "/" + ChatColor.GRAY + offlinePlayers.length + ChatColor.BLUE + ") complete.");
                        }
                        boolean alreadyPresent = PConfManager.isManagerCreated(op);
                        PConfManager pcm = PConfManager.getPConfManager(op);
                        String pip = pcm.getString("ip");
                        if (pip == null) continue;
                        if (pip.contains(ip)) {
                            synchronized (hasIP) {
                                hasIP.add(pcm.getString("name", op.getName()) + ((partial) ? ChatColor.BLUE + ": " + ChatColor.GRAY + pip : ""));
                            }
                        }
                        if (!alreadyPresent) pcm.discard();
                    }
                    if (hasIP.isEmpty()) cs.sendMessage(ChatColor.RED + "No players with that IP address were found.");
                    else {
                        cs.sendMessage(ChatColor.BLUE + "The following players matched " + ChatColor.GRAY + ip + ChatColor.BLUE + ":");
                        for (String name : hasIP) cs.sendMessage("  " + ChatColor.GRAY + name);
                    }
                }
            };
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, r);
            return true;
        }
        return false;
    }

}
