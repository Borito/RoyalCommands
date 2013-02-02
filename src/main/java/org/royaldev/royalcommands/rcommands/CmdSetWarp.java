package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.ConfManager;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdSetWarp implements CommandExecutor {

    private RoyalCommands plugin;

    public CmdSetWarp(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("setwarp")) {
            if (!plugin.isAuthorized(cs, "rcmds.setwarp")) {
                RUtils.dispNoPerms(cs);
                return true;
            }

            if (!(cs instanceof Player)) {
                cs.sendMessage(ChatColor.RED + "This command is only available to players!");
                return true;
            }

            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            Player p = (Player) cs;

            double locX = p.getLocation().getX();
            double locY = p.getLocation().getY();
            double locZ = p.getLocation().getZ();
            Float locYaw = p.getLocation().getYaw();
            Float locPitch = p.getLocation().getPitch();
            String locW = p.getWorld().getName();
            String name = args[0].toLowerCase();

            ConfManager warps = new ConfManager("warps.yml");
            if (!warps.exists()) warps.createFile();
            warps.set("warps." + name + ".set", true);
            warps.set("warps." + name + ".x", locX);
            warps.set("warps." + name + ".y", locY);
            warps.set("warps." + name + ".z", locZ);
            warps.set("warps." + name + ".pitch", locPitch);
            warps.set("warps." + name + ".yaw", locYaw);
            warps.set("warps." + name + ".w", locW);
            p.sendMessage(ChatColor.BLUE + "Warp \"" + ChatColor.GRAY + name + ChatColor.BLUE + "\" set.");
            return true;
        }
        return false;
    }

}
