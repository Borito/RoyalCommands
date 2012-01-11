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
			if (!plugin.isAuthorized(cs, "rcmds.setlevel")) {
				cs.sendMessage(ChatColor.RED
						+ "You don't have permission for that!");
				plugin.log.warning("[RoyalCommands] " + cs.getName()
						+ " was denied access to the command!");
				return true;
			}
			Player victim = null;

			if (args.length < 1) {
				cs.sendMessage(cmd.getDescription());
				return false;
			}
			if (args.length == 1) {
				if (!(cs instanceof Player)) {
					cs.sendMessage(ChatColor.RED
							+ "This command can only be used by players!");
					return true;
				}
			}
			Player player = (Player) cs;
			int toLevel = 0;
			if (args.length == 2) {
				victim = plugin.getServer().getPlayer(args[1].trim());
				if (victim == null) {
					cs.sendMessage(ChatColor.RED
							+ "You must input a valid player!");
					return true;
				}
				if (plugin.isVanished(victim)) {
					cs.sendMessage(ChatColor.RED
							+ "That player does not exist!");
					return true;
				}
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
				cs.sendMessage(ChatColor.GRAY + victim.getName()
						+ ChatColor.BLUE + "'s XP level was set to "
						+ ChatColor.GRAY + toLevel + ChatColor.BLUE
						+ "! They may need to relog to see the changes.");
				victim.sendMessage(ChatColor.BLUE + "Your XP level was set to "
						+ ChatColor.GRAY + toLevel + ChatColor.BLUE + " by "
						+ ChatColor.GRAY + cs.getName() + ChatColor.BLUE
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
						+ ChatColor.GRAY + toLevel + ChatColor.BLUE
						+ "! You may need to relog to see the changes.");
				return true;
			}
		}
		return false;
	}

}
