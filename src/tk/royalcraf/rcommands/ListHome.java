package tk.royalcraf.rcommands;

import java.io.File;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import tk.royalcraf.royalcommands.RoyalCommands;

public class ListHome implements CommandExecutor {

	RoyalCommands plugin;

	public ListHome(RoyalCommands plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("listhome")) {
			File pconfl = new File(plugin.getDataFolder() + "/userdata/"
					+ cs.getName().toLowerCase() + ".yml");
			if (pconfl.exists()) {
				FileConfiguration pconf = YamlConfiguration
						.loadConfiguration(pconfl);
				final Map<String, Object> opts = pconf.getConfigurationSection(
						"home").getValues(false);
				if (opts.keySet().isEmpty()) {
					cs.sendMessage(ChatColor.RED + "You have no homes!");
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