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
            warps.set(true, "warps." + name + ".set");
            warps.set(locX, "warps." + name + ".x");
            warps.set(locY, "warps." + name + ".y");
            warps.set(locZ, "warps." + name + ".z");
            warps.set(locPitch.toString(), "warps." + name + ".pitch");
            warps.set(locYaw.toString(), "warps." + name + ".yaw");
            warps.set(locW, "warps." + name + ".w");
            p.sendMessage(ChatColor.BLUE + "Warp \"" + ChatColor.GRAY + name + ChatColor.BLUE + "\" set.");
            return true;
        }
        return false;
    }

}
