package tk.royalcraf.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import tk.royalcraf.royalcommands.RoyalCommands;

public class Setlevel implements CommandExecutor {

	RoyalCommands plugin;

	public Setlevel(RoyalCommands plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("setlevel")) {
			Player player = null;
			Player victim = null;
			if (cs instanceof Player) {
				player = (Player) cs;
			}

			if (player == null) {
				cs.sendMessage(ChatColor.RED
						+ "This command can only be used by players!");
				return true;
			}
			if (!plugin.isAuthorized(cs, "rcmds.setlevel")) {
				cs.sendMessage(ChatColor.RED
						+ "You don't have permission for that!");
				plugin.log.warning("[RoyalCommands] " + cs.getName()
						+ " was denied access to the command!");
				return true;
			}
			if (args.length < 1) {
				return false;
			}
			int toLevel = 0;
			if (args.length == 2) {
				if (plugin.isOnline(args[1]) == false) {
					cs.sendMessage(ChatColor.RED
							+ "You must input a valid player!");
					return true;
				}
				victim = plugin.getServer().getPlayer(args[1].trim());
				try {
					toLevel = Integer.parseInt(args[0]);
				} catch (NumberFormatException e) {
					cs.sendMessage(ChatColor.RED
							+ "Your input was not an integer!");
					return false;
				}
				if (toLevel < 0) {
					cs.sendMessage(ChatColor.RED
							+ "You cannot input anything below 0.");
					return true;
				}
				victim.setLevel(toLevel);
				cs.sendMessage(ChatColor.BLUE + victim.getName()
						+ "'s XP level was set to " + toLevel
						+ "! They may need to relog to see the changes.");
				victim.sendMessage(ChatColor.BLUE + "Your XP level was set to "
						+ toLevel + " by " + cs.getName()
						+ "! You may need to relog to see these changes.");
				return true;
			}
			if (args.length < 2 && args.length != 0) {
				try {
					toLevel = Integer.parseInt(args[0]);
				} catch (NumberFormatException e) {
					cs.sendMessage(ChatColor.RED
							+ "Your input was not an integer!");
					return false;
				}
				if (toLevel < 0) {
					cs.sendMessage(ChatColor.RED
							+ "You cannot input anything below 0.");
					return true;
				}
				player.setLevel(toLevel);
				cs.sendMessage(ChatColor.BLUE + "Your XP level was set to "
						+ toLevel
						+ "! You may need to relog to see the changes.");
				return true;
			}
		}
		return false;
	}

}
