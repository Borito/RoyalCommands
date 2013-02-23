package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdPing implements CommandExecutor {

    private RoyalCommands plugin;

    public CmdPing(RoyalCommands instance) {
        this.plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("ping")) {
            if (!plugin.isAuthorized(cs, "rcmds.ping")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            cs.sendMessage(ChatColor.BLUE + "Pong!");
            return true;
            /*
            if (!(cs instanceof Player) && args.length < 1) {
                cs.sendMessage("Pong!");
                return true;
            }
            if (args.length > 0) {
                if (!plugin.isAuthorized(cs, "rcmds.others.ping")) {
                    RUtils.dispNoPerms(cs);
                    return true;
                }
                Player p = plugin.getServer().getPlayer(args[0]);
                if (p == null || plugin.isVanished(p, cs)) {
                    cs.sendMessage(ChatColor.RED + "That player does not exist!");
                    return true;
                }
                int ping = ((CraftPlayer) p).getHandle().ping;
                String possessive = (p.getName().endsWith("s")) ? "'" : "'s";
                cs.sendMessage(ChatColor.GRAY + p.getName() + possessive + ChatColor.BLUE + " ping: " + ChatColor.GRAY + ping + "ms");
                return true;
            }
            Player p = (Player) cs;
            int ping = ((CraftPlayer) p).getHandle().ping;
            p.sendMessage(ChatColor.BLUE + "Your ping: " + ChatColor.GRAY + ping + "ms");
            return true;
            */
        }
        return false;
    }
}
