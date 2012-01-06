package tk.royalcraf.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import tk.royalcraf.royalcommands.RoyalCommands;

public class TpAccept implements CommandExecutor {

	RoyalCommands plugin;

	public TpAccept(RoyalCommands plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("tpaccept")) {
			if (!plugin.isAuthorized(cs, "rcmds.tpaccept")) {
				cs.sendMessage(ChatColor.RED
						+ "You don't have permission for that!");
				plugin.log.warning("[RoyalCommands] " + cs.getName()
						+ " was denied access to the command!");
				return true;
			}
			if (TeleportRequest.tprdb.containsKey((Player) cs)) {
				Player t = (Player) TeleportRequest.tprdb.get((Player) cs);
				cs.sendMessage(ChatColor.BLUE + "Teleport request accepted.");
				t.sendMessage(ChatColor.BLUE
						+ "Your teleport request was accepted.");
				t.teleport(((Player) cs).getLocation());
				return true;
			}
			cs.sendMessage(ChatColor.RED + "You have no requests pending.");
			return true;
		}
		return false;
	}
}
