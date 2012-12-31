package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.PConfManager;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdSetUserdata implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdSetUserdata(RoyalCommands instance) {
        plugin = instance;
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("setuserdata")) {
            if (!plugin.isAuthorized(cs, "rcmds.setuserdata")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 3) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            String name = args[0];
            String node = args[1];
            String value = RoyalCommands.getFinalArg(args, 2);
            PConfManager pcm = new PConfManager(name);
            if (!pcm.exists() || !plugin.getServer().getOfflinePlayer(name).hasPlayedBefore()) {
                cs.sendMessage(ChatColor.RED + "No such player!");
                return true;
            }
            pcm.set(value, node);
            cs.sendMessage(ChatColor.BLUE + "Set " + ChatColor.GRAY + node + ChatColor.BLUE + " to " + ChatColor.GRAY + value + ChatColor.BLUE + " for the userdata of " + ChatColor.GRAY + name + ChatColor.BLUE + ".");
            return true;
        }
        return false;
    }

}
