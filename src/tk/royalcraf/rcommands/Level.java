package tk.royalcraf.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import tk.royalcraf.royalcommands.RoyalCommands;

public class Level implements CommandExecutor {
	
	RoyalCommands plugin;

	public Level(RoyalCommands plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("level")) {
			Player player = null;
			if (cs instanceof Player) {
				player = (Player) cs;
			}
			if (cmd.getName().equalsIgnoreCase("level")) {
				if (player == null) {
					cs.sendMessage(ChatColor.RED
							+ "This command can only be used by players!");
				} else {
					if (!plugin.isAuthorized(cs, "rcmds.level")) {
						cs.sendMessage(ChatColor.RED
								+ "You don't have permission for that!");
						plugin.log.warning("[RoyalCommands] " + cs.getName()
								+ " was denied access to the command!");
						return true;
					} else {
						player.setLevel(player.getLevel() + 1);
						cs.sendMessage(ChatColor.BLUE
								+ "XP level raised by one! You may need to relog to see the changes.");
						return true;
					}
				}
			}
		}
		return false;
	}
}