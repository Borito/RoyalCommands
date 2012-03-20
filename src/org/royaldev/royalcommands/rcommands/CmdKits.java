package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.util.Map;

public class CmdKits implements CommandExecutor {

    RoyalCommands plugin;

    public CmdKits(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("kits")) {
            if (!plugin.isAuthorized(cs, "rcmds.kits")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player) && args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            final Map<String, Object> opts = plugin.getConfig().getConfigurationSection("kits").getValues(false);
            if (opts.keySet().isEmpty()) {
                cs.sendMessage(ChatColor.RED + "No kits found!");
                return true;
            }
            String kits = "";
            for (String s : opts.keySet()) {
                if (plugin.kitPerms && plugin.isAuthorized(cs, "rcmds.kit." + s))
                    kits = (kits.isEmpty()) ? kits + s : kits + ", " + s;
                else if (!plugin.kitPerms) kits = (kits.isEmpty()) ? kits + s : kits + ", " + s;
            }
            cs.sendMessage(ChatColor.BLUE + "Kits:");
            if (kits.isEmpty()) return true;
            cs.sendMessage(kits);
            return true;
        }
        return false;

    }
}