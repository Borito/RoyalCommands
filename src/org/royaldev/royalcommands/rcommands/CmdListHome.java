package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.PConfManager;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.io.File;
import java.util.Map;

public class CmdListHome implements CommandExecutor {

    RoyalCommands plugin;

    public CmdListHome(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("listhome")) {
            if (!plugin.isAuthorized(cs, "rcmds.listhome")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player) && args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            File pconfl;
            if (args.length < 1) {
                pconfl = new File(plugin.getDataFolder() + File.separator + "userdata" + File.separator + cs.getName().toLowerCase() + ".yml");
            } else {
                if (!PConfManager.getPConfExists(args[0].trim().toLowerCase())) {
                    cs.sendMessage(ChatColor.RED + "That player does not exist!");
                    return true;
                }
                OfflinePlayer op = plugin.getServer().getOfflinePlayer(args[0].trim());
                if (op.isOp() || (op.isOnline() && plugin.isAuthorized((Player) op, "rcmds.exempt.listhome"))) {
                    cs.sendMessage(ChatColor.RED + "You can't list that player's homes!");
                    return true;
                }
                pconfl = new File(plugin.getDataFolder() + File.separator + "userdata" + File.separator + args[0].trim().toLowerCase() + ".yml");
            }

            if (pconfl.exists()) {
                FileConfiguration pconf = YamlConfiguration.loadConfiguration(pconfl);
                final Map<String, Object> opts = pconf.getConfigurationSection("home").getValues(false);
                if (opts.keySet().isEmpty()) {
                    cs.sendMessage(ChatColor.RED + "No homes found!");
                    return true;
                }
                String homes = opts.keySet().toString();
                homes = homes.substring(1, homes.length() - 1);
                cs.sendMessage(ChatColor.BLUE + "Homes:");
                cs.sendMessage(homes);
                return true;
            }
        }
        return false;

    }
}