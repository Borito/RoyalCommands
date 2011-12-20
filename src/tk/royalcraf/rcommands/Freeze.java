package tk.royalcraf.rcommands;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
				return true;
			} else {
				if (args.length < 1) {
					return false;
				}
				Player victim = plugin.getServer().getPlayer(args[0]);
				if (victim != null) {
					if (plugin.isAuthorized(victim, "rcmds.exempt.freeze")) {
						cs.sendMessage(ChatColor.RED
								+ "You can't freeze that player!");
						return true;
					}
					if (!freezedb.containsKey(victim.getName())) {
						freezedb.put(victim.getName(), true);
						cs.sendMessage(ChatColor.BLUE + "You have frozen "
								+ ChatColor.GRAY + victim.getName()
								+ ChatColor.BLUE + "!");
						victim.sendMessage(ChatColor.RED
								+ "You have been frozen!");
						return true;
					} else {
						freezedb.remove(victim.getName());
						cs.sendMessage(ChatColor.BLUE + "You have thawed "
								+ ChatColor.GRAY + victim.getName()
								+ ChatColor.BLUE + "!");
						victim.sendMessage(ChatColor.BLUE
								+ "You have been thawed!");
						return true;
					}
				} else {
					cs.sendMessage(ChatColor.RED + "That player is not online!");
					return true;
				}
			}
		}
		return false;
	}
}
