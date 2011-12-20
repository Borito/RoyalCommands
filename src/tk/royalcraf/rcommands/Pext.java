package tk.royalcraf.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import tk.royalcraf.royalcommands.RoyalCommands;

public class Pext implements CommandExecutor {

	RoyalCommands plugin;

	public Pext(RoyalCommands plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("pext")) {
			if (!plugin.isAuthorized(cs, "rcmds.pext")) {
				cs.sendMessage(ChatColor.RED
						+ "You don't have permission for that!");
				return true;
			} else {
				if (args.length < 1) {
					Player p = null;
					if (cs instanceof Player) {
						p = (Player) cs;
					} else {
						cs.sendMessage(ChatColor.RED
								+ "You must be a player to use this command!");
						return true;
					}
					if (p != null) {
						cs.sendMessage(ChatColor.BLUE
								+ "You have been extinguished.");
						p.setFireTicks(0);
						return true;
					}
				} else {
					if (!plugin.isAuthorized(cs, "rcmds.pext.others")) {
						Player target = plugin.getServer().getPlayer(args[0]);
						if (target != null) {
							cs.sendMessage(ChatColor.BLUE
									+ "You have extinguished " + ChatColor.GRAY
									+ target.getName() + ChatColor.BLUE + ".");
							target.sendMessage(ChatColor.BLUE
									+ "You have been extinguished by "
									+ ChatColor.GRAY + cs.getName()
									+ ChatColor.BLUE + ".");
						} else {
							cs.sendMessage(ChatColor.RED
									+ "That player does not exist!");
							return true;
						}
					}
				}
			}
		}
		return false;
	}

}
