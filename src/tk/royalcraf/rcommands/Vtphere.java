package tk.royalcraf.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import tk.royalcraf.royalcommands.RoyalCommands;

public class Vtphere implements CommandExecutor {

	RoyalCommands plugin;

	public Vtphere(RoyalCommands plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("vtphere")) {
			if (!plugin.isAuthorized(cs, "rcmds.vtphere")) {
				cs.sendMessage(ChatColor.RED
						+ "You don't have permission for that!");
				return true;
			} else {
				if (args.length < 1) {
					return false;
				}
				Player victim = plugin.getServer().getPlayer(args[0]);
				if (victim != null) {
					if (cs instanceof Player) {
						Player player = (Player) cs;
						victim.teleport(player.getLocation());
						cs.sendMessage(ChatColor.BLUE + "Teleporting player "
								+ ChatColor.GRAY + victim.getName()
								+ ChatColor.BLUE + "to you.");
						return true;
					} else {
						cs.sendMessage(ChatColor.RED
								+ "This command cannot be used in console.");
						return true;
					}
				} else {
					cs.sendMessage(ChatColor.RED
							+ "That player does not exist!");
					return true;
				}
			}
		}
		return false;
	}

}
