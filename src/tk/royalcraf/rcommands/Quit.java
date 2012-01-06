package tk.royalcraf.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import tk.royalcraf.royalcommands.RoyalCommands;

public class Quit implements CommandExecutor {

	RoyalCommands plugin;

	public Quit(RoyalCommands plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("quit")) {
			if (!plugin.isAuthorized(cs, "rcmds.quit")) {
				cs.sendMessage(ChatColor.RED
						+ "You don't have permission for that!");
				plugin.log.warning("[RoyalCommands] " + cs.getName()
						+ " was denied access to the command!");
				return true;
			}
			((Player) cs).kickPlayer("You have left the game.");
			plugin.getServer().broadcastMessage(
					ChatColor.YELLOW + cs.getName() + " has left the game.");
			return true;
		}
		return false;
	}

}
