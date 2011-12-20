package tk.royalcraf.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import tk.royalcraf.royalcommands.RoyalCommands;

public class Banned implements CommandExecutor {

	RoyalCommands plugin;

	public Banned(RoyalCommands plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("banned")) {
			if (!plugin.isAuthorized(cs, "rcmds.banned")) {
				cs.sendMessage(ChatColor.RED
						+ "You don't have permission for that!");
				plugin.log.warning("[RoyalCommands] " + cs.getName()
						+ " was denied access to the command!");
				return true;
			} else {
				if (args.length < 1) {
					return false;
				}
				OfflinePlayer dude = null;
				dude = (OfflinePlayer) plugin.getServer().getOfflinePlayer(
						args[0]);
				boolean banned = dude.isBanned();
				if (banned == false) {
					cs.sendMessage(ChatColor.GREEN + dude.getName()
							+ ChatColor.WHITE + " is not banned.");
					return true;
				} else {
					cs.sendMessage(ChatColor.RED + dude.getName()
							+ ChatColor.WHITE + " is banned.");
					return true;
				}
			}
		}
		return false;
	}

}
