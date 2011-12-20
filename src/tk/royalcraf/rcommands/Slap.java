package tk.royalcraf.rcommands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import tk.royalcraf.royalcommands.RoyalCommands;

public class Slap implements CommandExecutor {

	RoyalCommands plugin;

	public Slap(RoyalCommands plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("slap")) {
			if (!plugin.isAuthorized(cs, "rcmds.slap")) {
				cs.sendMessage(ChatColor.RED
						+ "You don't have permission for that!");
				plugin.log.warning("[RoyalCommands] " + cs.getName()
						+ " was denied access to the command!");
				return true;
			} else {
				if (args.length < 1) {
					return false;
				}
				Player victim = null;
				if (plugin.isOnline(args[0]) == false) {
					cs.sendMessage(ChatColor.RED + "That person is not online!");
					return true;
				} else {
					victim = (Player) plugin.getServer().getPlayer(args[0]);
					if (plugin.isAuthorized(victim, "rcmds.exempt.slap")) {
						cs.sendMessage(ChatColor.RED
								+ "You may not slap that player.");
						return true;
					} else {
						Bukkit.getServer().broadcastMessage(
								ChatColor.GOLD + cs.getName() + ChatColor.WHITE
										+ " slaps " + ChatColor.RED
										+ victim.getName() + ChatColor.WHITE
										+ "!");
						return true;
					}
				}
			}
		}
		return false;
	}

}
