package tk.royalcraf.rcommands;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import tk.royalcraf.royalcommands.RoyalCommands;

public class Freeze implements CommandExecutor {

	RoyalCommands plugin;

	public Freeze(RoyalCommands plugin) {
		this.plugin = plugin;
	}

	public static HashMap<String, Boolean> freezedb = new HashMap<String, Boolean>();

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("freeze")) {
			if (!plugin.isAuthorized(cs, "rcmds.freeze")) {
				cs.sendMessage(ChatColor.RED
						+ "You don't have permission for that!");
			} else {
				if (args.length < 1) {
					return false;
				}
				if (plugin.isAuthorized(plugin.getServer().getPlayer(args[0]),
						"rcmds.exempt.freeze")) {
					cs.sendMessage(ChatColor.RED
							+ "You can't freeze that player!");
					return true;
				}
				if (plugin.isOnline(args[0])) {
					if (!freezedb.containsKey(args[0])) {
						freezedb.put(args[0], true);
						cs.sendMessage(ChatColor.BLUE + "You have frozen "
								+ ChatColor.GRAY + args[0] + ChatColor.BLUE
								+ "!");
						plugin.getServer()
								.getPlayer(args[0])
								.sendMessage(
										ChatColor.RED + "You have been frozen!");
						return true;
					} else {
						freezedb.remove(args[0]);
						cs.sendMessage(ChatColor.BLUE + "You have thawed "
								+ ChatColor.GRAY + args[0] + ChatColor.BLUE
								+ "!");
						plugin.getServer()
								.getPlayer(args[0])
								.sendMessage(
										ChatColor.BLUE
												+ "You have been thawed!");
						return true;
					}
				}
			}
		}
		return false;
	}
}
