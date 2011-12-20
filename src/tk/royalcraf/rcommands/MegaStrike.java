package tk.royalcraf.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;

import tk.royalcraf.royalcommands.RoyalCommands;

public class MegaStrike implements CommandExecutor {

	RoyalCommands plugin;

	public MegaStrike(RoyalCommands plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("megastrike")) {
			if (!plugin.isAuthorized(cs, "rcmds.megastrike")) {
				cs.sendMessage(ChatColor.RED
						+ "You don't have permission for that!");
				return true;
			} else {
				Player p = null;
				if (cs instanceof Player) {
					p = (Player) cs;
				}
				BlockIterator b = new BlockIterator(p, 100);
				for (int i = 0; i < 5; i++) {
					p.getWorld().strikeLightning(b.next().getLocation());
				}
				return true;
			}
		}
		return false;
	}

}
