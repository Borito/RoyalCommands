package tk.royalcraf.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import tk.royalcraf.royalcommands.RoyalCommands;

public class Time implements CommandExecutor {

	RoyalCommands plugin;

	public Time(RoyalCommands plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("time")) {
			if (!plugin.isAuthorized(cs, "rcmds.time")) {
				cs.sendMessage(ChatColor.RED
						+ "You don't have permission for that!");
				plugin.log.warning("[RoyalCommands] " + cs.getName()
						+ " was denied access to the command!");
				return true;
			}
			if (!(cs instanceof Player)) {
				cs.sendMessage(ChatColor.RED
						+ "This command is only available to players.");
				return true;
			}
			if (args.length < 1) {
				return false;
			}
			Player p = (Player) cs;
			Integer time = null;
			World world = null;
			try {
				time = Integer.parseInt(args[0]);
			} catch (Exception e) {
				cs.sendMessage(ChatColor.RED + "The number entered is invalid!");
				return true;
			}
			try {
				world = plugin.getServer().getWorld(args[1]);
				world.setTime(time);
				p.sendMessage(ChatColor.BLUE + "Set time in " + ChatColor.GRAY
						+ world.getName() + ChatColor.BLUE + " to "
						+ ChatColor.GRAY + time + ChatColor.BLUE + " ticks.");
				return true;
			} catch (Exception e) {
				world = p.getWorld();
				world.setTime(time);
				p.sendMessage(ChatColor.BLUE + "Set time in " + ChatColor.GRAY
						+ world.getName() + ChatColor.BLUE + " to "
						+ ChatColor.GRAY + time + ChatColor.BLUE + " ticks.");
				return true;
			}
		}
		return false;
	}

}
