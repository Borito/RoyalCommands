package tk.royalcraf.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import tk.royalcraf.royalcommands.RoyalCommands;

public class Fakeop implements CommandExecutor {

	RoyalCommands plugin;

	public Fakeop(RoyalCommands plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("fakeop")) {
			if (!plugin.isAuthorized(cs, "rcmds.fakeop")) {
				if (args.length < 1) {
					return false;
				} else {
					Player victim = plugin.getServer().getPlayer(args[0]);
					if (plugin.isOnline(victim.getName())) {
						victim.sendMessage(ChatColor.YELLOW + "You are now op!");
						cs.sendMessage(ChatColor.BLUE + victim.getName()
								+ " has been sent a fake op notice.");
						return true;
					} else {
						cs.sendMessage(ChatColor.RED
								+ "That player is not online!");
						return true;
					}
				}
			}
		}
		return false;
	}
}
