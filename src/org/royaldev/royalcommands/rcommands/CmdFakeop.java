package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdFakeop implements CommandExecutor {

    RoyalCommands plugin;

    public CmdFakeop(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("fakeop")) {
            if (!plugin.isAuthorized(cs, "rcmds.fakeop")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            Player victim = plugin.getServer().getPlayer(args[0]);
            if (victim == null || plugin.isVanished(victim)) {
                cs.sendMessage(ChatColor.RED + "That player is not online!");
                return true;
            }
            victim.sendMessage(ChatColor.YELLOW + "You are now op!");
            cs.sendMessage(ChatColor.BLUE + victim.getName() + " has been sent a fake op notice.");
            return true;
        }
        return false;
    }
}
