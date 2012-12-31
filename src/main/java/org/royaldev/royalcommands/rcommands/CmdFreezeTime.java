package org.royaldev.royalcommands.rcommands;

import org.apache.commons.lang.BooleanUtils;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.ConfManager;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdFreezeTime implements CommandExecutor {

    private RoyalCommands plugin;

    public CmdFreezeTime(RoyalCommands instance) {
        plugin = instance;
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("freezetime")) {
            if (!plugin.isAuthorized(cs, "rcmds.freezetime")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            World w = plugin.getServer().getWorld(args[0]);
            if (w == null) {
                cs.sendMessage(ChatColor.RED + "No such world!");
                return true;
            }
            ConfManager cm = RoyalCommands.wm.getConfig();
            Boolean isFrozen = cm.getBoolean("worlds." + w.getName() + ".freezetime");
            if (isFrozen == null) isFrozen = false;
            cm.setBoolean(!isFrozen, "worlds." + w.getName() + ".freezetime");
            cm.setLong(w.getTime(), "worlds." + w.getName() + ".frozenat");
            cs.sendMessage(ChatColor.BLUE + "Turned freezetime on " + ChatColor.GRAY + RUtils.getMVWorldName(w) + ChatColor.BLUE + " to " + ChatColor.GRAY + BooleanUtils.toStringOnOff(!isFrozen) + ChatColor.BLUE + ".");
            return true;
        }
        return false;
    }

}
