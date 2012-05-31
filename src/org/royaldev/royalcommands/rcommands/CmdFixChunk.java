package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdFixChunk implements CommandExecutor {

    RoyalCommands plugin;

    public CmdFixChunk(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("fixchunk")) {
            if (!plugin.isAuthorized(cs, "rcmds.fixchunk")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(ChatColor.RED + "This command is only available to players!");
                return true;
            }
            Player p = (Player) cs;
            Chunk c = p.getLocation().getChunk();
            boolean worked = p.getWorld().refreshChunk(c.getX(), c.getZ());
            c.unload();
            c.load();
            if (worked) cs.sendMessage(ChatColor.BLUE + "The chunk you're standing in has been reloaded!");
            else cs.sendMessage(ChatColor.RED + "The chunk could not be reloaded.");
            return true;
        }
        return false;
    }

}
