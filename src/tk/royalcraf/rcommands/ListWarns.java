package tk.royalcraf.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import tk.royalcraf.royalcommands.RoyalCommands;

import java.io.File;
import java.util.Map;

public class ListWarns implements CommandExecutor {

    RoyalCommands plugin;

    public ListWarns(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label,
                             String[] args) {
        if (cmd.getName().equalsIgnoreCase("listwarns")) {
            if (!plugin.isAuthorized(cs, "rcmds.listwarns")) {
                cs.sendMessage(ChatColor.RED
                        + "You don't have permission for that!");
                plugin.log.warning("[RoyalCommands] " + cs.getName()
                        + " was denied access to the command!");
                return true;
            }
            File pconfl = new File(plugin.getDataFolder() + "/userdata/"
                    + cs.getName().toLowerCase() + ".yml");
            if (pconfl.exists()) {
                FileConfiguration pconf = YamlConfiguration
                        .loadConfiguration(pconfl);
                if (pconf.get("warns") == null) {
                    cs.sendMessage(ChatColor.RED + "You have no warnings!");
                    return true;
                }
                final Map<String, Object> opts = pconf.getConfigurationSection(
                        "warns").getValues(true);
                if (opts.values().isEmpty()) {
                    cs.sendMessage(ChatColor.RED + "You have no warnings!");
                    return true;
                }
                String homes = opts.values().toString();
                homes = homes.substring(1, homes.length() - 1);
                cs.sendMessage(ChatColor.RED + "Warnings:");
                cs.sendMessage(homes.replaceAll("(&([a-f0-9]))", "\u00A7$2"));
                return true;
            }
        }
        return false;
    }
}
