package tk.royalcraf.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import tk.royalcraf.royalcommands.PConfManager;
import tk.royalcraf.royalcommands.RoyalCommands;

public class Banreason implements CommandExecutor {

	RoyalCommands plugin;

	public Banreason(RoyalCommands plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("banreason")) {
			if (!plugin.isAuthorized(cs, "rcmds.banreason")) {
				cs.sendMessage(ChatColor.RED
						+ "You don't have permission for that!");
				plugin.log.warning("[RoyalCommands] " + cs.getName()
						+ " was denied access to the command!");
				return true;
			}
			if (args.length < 1) {
				return false;
			}
			OfflinePlayer t = plugin.getServer().getOfflinePlayer(
					args[0].trim());
			if (!PConfManager.getPConfExists(t)) {
				cs.sendMessage(ChatColor.RED + "That player does not exist!");
				return true;
			}
			if (!t.isBanned()) {
				cs.sendMessage(ChatColor.RED + "That player is not banned!");
				return true;
			}
			String banreason = PConfManager.getPValString(t, "banreason");
			cs.sendMessage(ChatColor.BLUE + "The player " + ChatColor.GRAY
					+ t.getName() + ChatColor.BLUE + " was banned for: "
					+ ChatColor.GRAY + banreason + ChatColor.BLUE + ".");
			return true;
		}
		return false;
	}
}