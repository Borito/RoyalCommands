package tk.royalcraf.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import tk.royalcraf.royalcommands.RoyalCommands;

public class Rank implements CommandExecutor {

	RoyalCommands plugin;

	public Rank(RoyalCommands plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("rank")) {

			if (!plugin.isAuthorized(cs, "rcmds.rank")) {
				cs.sendMessage(ChatColor.RED
						+ "You don't have permission for that!");
				return true;
			} else {
				if (args.length < 1) {
					return false;
				} else {
					Player victim = plugin.getServer().getPlayer(args[0]);
					String rank = RoyalCommands.permission
							.getPrimaryGroup(victim);
					cs.sendMessage("The user " + victim.getName()
							+ " has the group " + rank + ".");
					return true;
				}
			}
		}
		return false;
	}

}
