package tk.royalcraf.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import tk.royalcraf.royalcommands.RoyalCommands;

public class Sci implements CommandExecutor {

	RoyalCommands plugin;

	public Sci(RoyalCommands plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("sci")) {

			Player victim = null;

			if (!plugin.isAuthorized(cs, "rcmds.sci")) {
				cs.sendMessage(ChatColor.RED
						+ "You don't have permission for that!");
				plugin.log.warning("[RoyalCommands] " + cs.getName()
						+ " was denied access to the command!");
				return true;
			}
			int errord = 0;
			if (args.length < 2) {
				cs.sendMessage(cmd.getDescription());
				return false;
			}
			if (errord == 0) {
				int removeID = 0;
				victim = (Player) plugin.getServer().getPlayer(args[0]);
				if (plugin.isVanished(victim)) {
					cs.sendMessage(ChatColor.RED
							+ "That player does not exist!");
					return true;
				}
				if (victim == null) {
					cs.sendMessage(ChatColor.RED
							+ "You must input an online player.");
					errord = 1;
				}
				try {
					removeID = Integer.parseInt(args[1]);
				} catch (NumberFormatException e) {
					cs.sendMessage(ChatColor.RED
							+ "You must input a numerical ID to remove.");
					return false;
				}
				if (removeID <= 0 || removeID > 2266) {
					cs.sendMessage(ChatColor.RED
							+ "You must specify a valid item ID.");
					return true;
				}
				if (removeID < 2255 && removeID > 382) {
					cs.sendMessage(ChatColor.RED
							+ "You must specify a valid item ID.");
					return true;
				}
				if (plugin.isAuthorized(victim, "rcmds.exempt.sci")) {
					cs.sendMessage(ChatColor.RED
							+ "You cannot alter that player's inventory.");
					return true;
				}
				victim.getInventory().remove(removeID);
				victim.sendMessage(ChatColor.RED
						+ "You have just had all of your item ID "
						+ ChatColor.BLUE + removeID + ChatColor.RED
						+ " removed by " + ChatColor.RED + cs.getName()
						+ ChatColor.BLUE + "!");
				cs.sendMessage(ChatColor.BLUE
						+ "You have just removed all of the item ID "
						+ ChatColor.RED + removeID + ChatColor.BLUE + " from "
						+ ChatColor.RED + victim.getName() + ChatColor.BLUE
						+ "'s inventory.");
				return true;
			}
		}
		return false;
	}

}
