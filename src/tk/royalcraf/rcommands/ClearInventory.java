package tk.royalcraf.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import tk.royalcraf.royalcommands.RoyalCommands;

public class ClearInventory implements CommandExecutor {

	RoyalCommands plugin;

	public ClearInventory(RoyalCommands plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("clearinventory")) {
			if (!plugin.isAuthorized(cs, "rcmds.clearinventory")) {
				cs.sendMessage(ChatColor.RED
						+ "You don't have permission for that!");
				return true;
			} else {
				if (args.length < 1) {
					if (!(cs instanceof Player)) {
						cs.sendMessage(ChatColor.RED
								+ "This command is only available to players!");
					} else {
						Player p = (Player) cs;
						p.getInventory().clear();
						cs.sendMessage(ChatColor.BLUE
								+ "You have cleared your inventory.");
						return true;
					}
				} else if (args.length == 1) {
					if (!plugin.isAuthorized(cs, "rcmds.clearinventory.others")) {
						cs.sendMessage(ChatColor.RED
								+ "You don't have permission for that!");
						plugin.log.warning("[RoyalCommands] " + cs.getName()
								+ " was denied access to the command!");
						return true;
					} else {
						Player target = plugin.getServer().getPlayer(args[0]);
						if (target == null) {
							cs.sendMessage(ChatColor.RED
									+ "That player is not online!");
							return true;
						} else {
							if (plugin.isAuthorized(target,
									"rcmds.exempt.clearinventory")) {
								cs.sendMessage(ChatColor.RED
										+ "You cannot alter that player's inventory!");
								return true;
							} else {
								cs.sendMessage(ChatColor.BLUE
										+ "You have cleared the inventory of "
										+ ChatColor.GRAY + target.getName()
										+ ChatColor.BLUE + ".");
								target.sendMessage(ChatColor.RED
										+ "Your inventory has been cleared.");
								target.getInventory().clear();
							}
						}
					}
				}
			}
		}
		return false;
	}

}
