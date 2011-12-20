package tk.royalcraf.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import tk.royalcraf.royalcommands.RoyalCommands;

public class Starve implements CommandExecutor {

	RoyalCommands plugin;

	public Starve(RoyalCommands plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("starve")) {
			if (!plugin.isAuthorized(cs, "rcmds.starve")) {
				cs.sendMessage(ChatColor.RED
						+ "You don't have permission for that!");
				plugin.log.warning("[RoyalCommands] " + cs.getName()
						+ " was denied access to the command!");
				return true;
			} else {

				if (args.length < 2) {
					return false;
				}
				if (plugin.isOnline(args[0]) == false) {
					cs.sendMessage(ChatColor.RED + "That person is not online!");
					return true;
				} else {
					Player victim = null;
					int toStarve = 0;
					try {
						toStarve = Integer.parseInt(args[1]);
					} catch (NumberFormatException e) {
						cs.sendMessage(ChatColor.RED
								+ "The damage must be a number between 1 and 20!");
						return false;
					}
					if (toStarve > 20 || toStarve <= 0) {
						cs.sendMessage(ChatColor.RED
								+ "The damage you entered is not within 1 and 20!");
						return true;
					} else {
						victim = (Player) plugin.getServer().getPlayer(args[0]);
						if (plugin.isAuthorized(victim, "rcmds.exempt.starve")) {
							cs.sendMessage(ChatColor.RED
									+ "You may not starve that player.");
							return true;
						} else {
							int starveLevel = victim.getFoodLevel() - toStarve;
							victim.setFoodLevel(starveLevel);
							victim.sendMessage(ChatColor.RED
									+ "You have just been starved by "
									+ ChatColor.BLUE + cs.getName()
									+ ChatColor.RED + "!");
							cs.sendMessage(ChatColor.BLUE + "You just starved "
									+ ChatColor.RED + victim.getName()
									+ ChatColor.BLUE + "!");
							return true;
						}
					}
				}
			}
		}
		return false;
	}

}
