package tk.royalcraf.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import tk.royalcraf.royalcommands.RoyalCommands;

public class Weather implements CommandExecutor {

	RoyalCommands plugin;

	public Weather(RoyalCommands plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("weather")) {
			if (!plugin.isAuthorized(cs, "rcmds.weather")) {
				cs.sendMessage(ChatColor.RED
						+ "You don't have permission for that!");
				plugin.log.warning("[RoyalCommands] " + cs.getName()
						+ " was denied access to the command!");
			} else {
				if (args.length < 1) {
					return false;
				} else {
					if (!(cs instanceof Player)) {
					} else if (args.length < 2) {
						Player p = (Player) cs;
						String conds = args[0];
						World world = p.getWorld();
						if (conds.toLowerCase().trim().startsWith("sun")) {
							world.setStorm(false);
							world.setThundering(false);
							cs.sendMessage(ChatColor.BLUE + "Set weather to "
									+ ChatColor.GRAY + "sun" + ChatColor.BLUE
									+ " in " + ChatColor.GRAY + world.getName()
									+ ChatColor.BLUE + ".");
							return true;
						} else if (conds.toLowerCase().startsWith("rain")) {
							world.setStorm(true);
							world.setThundering(false);
							cs.sendMessage(ChatColor.BLUE + "Set weather to "
									+ ChatColor.GRAY + "rain" + ChatColor.BLUE
									+ " in " + ChatColor.GRAY + world.getName()
									+ ChatColor.BLUE + ".");
							return true;
						} else if (conds.toLowerCase().startsWith("storm")) {
							world.setStorm(true);
							world.setThundering(true);
							cs.sendMessage(ChatColor.BLUE + "Set weather to "
									+ ChatColor.GRAY + "storm" + ChatColor.BLUE
									+ " in " + ChatColor.GRAY + world.getName()
									+ ChatColor.BLUE + ".");
							return true;
						}
					} else if (args.length < 3) {
						Player p = (Player) cs;
						String conds = args[0];
						String slength = args[1];
						int length = 0;
						try {
							length = Integer.parseInt(slength);
						} catch (Exception e) {
							p.sendMessage(ChatColor.RED
									+ "The time specified was invalid!");
							return true;
						}
						if (length < 1) {
							p.sendMessage(ChatColor.RED
									+ "The time specified was invalid!");
							return true;
						}
						World world = p.getWorld();
						if (conds.toLowerCase().trim().startsWith("sun")) {
							world.setStorm(false);
							world.setThundering(false);
							world.setWeatherDuration(length * 20);
							cs.sendMessage(ChatColor.BLUE + "Set weather to "
									+ ChatColor.GRAY + "sun" + ChatColor.BLUE
									+ " in " + ChatColor.GRAY + world.getName()
									+ ChatColor.BLUE + " for " + ChatColor.GRAY
									+ length + ChatColor.BLUE + " seconds.");
							return true;
						} else if (conds.toLowerCase().startsWith("rain")) {
							world.setStorm(true);
							world.setThundering(false);
							world.setWeatherDuration(length * 20);
							cs.sendMessage(ChatColor.BLUE + "Set weather to "
									+ ChatColor.GRAY + "rain" + ChatColor.BLUE
									+ " in " + ChatColor.GRAY + world.getName()
									+ ChatColor.BLUE + " for " + ChatColor.GRAY
									+ length + ChatColor.BLUE + " seconds.");
							return true;
						} else if (conds.toLowerCase().startsWith("storm")) {
							world.setStorm(true);
							world.setThundering(true);
							world.setWeatherDuration(length * 20);
							cs.sendMessage(ChatColor.BLUE + "Set weather to "
									+ ChatColor.GRAY + "storm" + ChatColor.BLUE
									+ " in " + ChatColor.GRAY + world.getName()
									+ ChatColor.BLUE + " for " + ChatColor.GRAY
									+ length + ChatColor.BLUE + " seconds.");
							return true;
						}
					}
				}
			}
		}
		return false;
	}
}
