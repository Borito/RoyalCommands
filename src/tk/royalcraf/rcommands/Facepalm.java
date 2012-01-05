package tk.royalcraf.rcommands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import tk.royalcraf.royalcommands.RoyalCommands;

public class Facepalm implements CommandExecutor {

	RoyalCommands plugin;

	public Facepalm(RoyalCommands plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("facepalm")) {
			if (!plugin.isAuthorized(cs, "rcmds.facepalm")) {
				cs.sendMessage(ChatColor.RED
						+ "You don't have permission for that!");
				plugin.log.warning("[RoyalCommands] " + cs.getName()
						+ " was denied access to the command!");
				return true;
			} else {
				Player victim = null;
				if (args.length < 1) {
					Bukkit.getServer().broadcastMessage(
							ChatColor.YELLOW + cs.getName() + ChatColor.AQUA
									+ " has facepalmed.");
					return true;
				} else {
					if (plugin.isOnline(args[0]) == false) {
						cs.sendMessage(ChatColor.RED
								+ "That player is not online!");
						return true;
					} else {
						victim = (Player) plugin.getServer().getPlayer(args[0]);
						if (plugin
								.isAuthorized(victim, "rcmds.exempt.facepalm")) {
							cs.sendMessage(ChatColor.RED
									+ "You cannot facepalm at that player!");
							return true;
						} else {
							Bukkit.getServer().broadcastMessage(
									ChatColor.YELLOW + cs.getName()
											+ ChatColor.AQUA
											+ " has facepalmed at "
											+ ChatColor.YELLOW
											+ victim.getName() + ChatColor.AQUA
											+ ".");
							return true;
						}
					}
				}
			}
		}
		return false;
	}

}
