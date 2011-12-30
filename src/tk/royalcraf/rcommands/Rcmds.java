package tk.royalcraf.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import tk.royalcraf.royalcommands.RoyalCommands;

public class Rcmds implements CommandExecutor {

	RoyalCommands plugin;

	public Rcmds(RoyalCommands plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("rcmds")) {
			if (!plugin.isAuthorized(cs, "rcmds.rcmds")) {
				cs.sendMessage(ChatColor.RED
						+ "You don't have permission for that!");
				return true;
			}
			plugin.reloadConfig();
			plugin.showcommands = plugin.getConfig()
					.getBoolean("view_commands");
			plugin.disablegetip = plugin.getConfig()
					.getBoolean("disable_getip");
			plugin.banMessage = plugin.getConfig()
					.getString("default_ban_message")
					.replaceAll("(&([a-f0-9]))", "\u00A7$2");
			plugin.kickMessage = plugin.getConfig()
					.getString("default_kick_message")
					.replaceAll("(&([a-f0-9]))", "\u00A7$2");
			plugin.defaultStack = plugin.getConfig().getInt(
					"default_stack_size");
			plugin.defaultWarn = plugin.getConfig().getString(
					"default_warn_message");
			plugin.warnBan = plugin.getConfig().getInt("max_warns_before_ban");
			cs.sendMessage(ChatColor.GREEN + "RoyalCommands " + ChatColor.BLUE
					+ "v" + plugin.version + " reloaded.");
			return true;
		}
		return false;
	}
}
