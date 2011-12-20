package tk.royalcraf.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
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
				if (args.length < 1) {
					BlockIterator b = new BlockIterator(p, 100);
					if (!b.hasNext()) {
						cs.sendMessage(ChatColor.RED
								+ "Cannot megastrike there!");
						return true;
					} else {
						Block bb = b.next();
						while (b.hasNext()) {
							if (!(b.next().getTypeId() == 0)) {
								bb = b.next();
								break;
							}
						}
						for (int i = 0; i < 15; i++) {
							p.getWorld().strikeLightning(bb.getLocation());
						}
						return true;
					}
				} else {
					if (!plugin.isAuthorized(cs, "rcmds.megastrike.others")) {
						cs.sendMessage(ChatColor.RED
								+ "You don't have permission for that!");
						return true;
					} else {
						Player target = plugin.getServer().getPlayer(args[0]);
						if (target != null) {
							cs.sendMessage(ChatColor.BLUE + "Megasmiting "
									+ ChatColor.GRAY + target.getName()
									+ ChatColor.BLUE + ".");
							target.sendMessage(ChatColor.RED
									+ "You have been megasmited by "
									+ ChatColor.GRAY + cs.getName()
									+ ChatColor.RED + ".");
							for (int i = 0; i < 15; i++) {
								p.getWorld().strikeLightning(
										target.getLocation());
							}
							return true;
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
