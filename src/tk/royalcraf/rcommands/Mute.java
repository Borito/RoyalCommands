package tk.royalcraf.rcommands;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import tk.royalcraf.royalcommands.RoyalCommands;

public class Mute implements CommandExecutor {

	RoyalCommands plugin;

	public Mute(RoyalCommands plugin) {
		this.plugin = plugin;
	}

	public static HashMap<String, Integer> mutedb = new HashMap<String, Integer>();

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("mute")) {
			if (!plugin.isAuthorized(cs, "rcmds.mute")) {
				cs.sendMessage(ChatColor.RED
						+ "You don't have permission for that!");
				plugin.log.warning("[RoyalCommands] " + cs.getName()
						+ " was denied access to the command!");
				return true;
			}
			if (args.length < 1) {
				return false;
			} else if (args.length < 2) {
				Player t = plugin.getServer().getPlayer(args[0]);
				if (t == null) {
					cs.sendMessage(ChatColor.RED
							+ "That player does not exist!");
					return true;
				}
				if (plugin.isAuthorized(t, "rcmds.exempt.mute")) {
					cs.sendMessage(ChatColor.RED
							+ "You cannot mute that player!");
					return true;
				}
				if (mutedb.containsKey(t.getName())) {
					mutedb.remove(t.getName());
					t.sendMessage(ChatColor.BLUE + "You have been unmuted by "
							+ ChatColor.GRAY + cs.getName() + ChatColor.BLUE
							+ ".");
					cs.sendMessage(ChatColor.BLUE + "You have unmuted "
							+ ChatColor.GRAY + t.getName() + ChatColor.BLUE
							+ ".");
					return true;
				} else {
					mutedb.put(t.getName(), 0);
					t.sendMessage(ChatColor.RED + "You have been muted by "
							+ ChatColor.GRAY + cs.getName() + ChatColor.RED
							+ ".");
					cs.sendMessage(ChatColor.BLUE + "You have muted "
							+ ChatColor.GRAY + t.getName() + ChatColor.BLUE
							+ ".");
					return true;
				}
			} /*
			 * else if (args.length < 3) { Player t =
			 * plugin.getServer().getPlayer(args[0]); int time = 0; try { time =
			 * Integer.parseInt(args[1]); } catch (Exception e) {
			 * cs.sendMessage(ChatColor.RED +
			 * "The time specified was invalid!"); return true; } if (t == null)
			 * { cs.sendMessage(ChatColor.RED + "That player does not exist!");
			 * return true; } if (mutedb.containsKey(t)) { mutedb.remove(t);
			 * t.sendMessage(ChatColor.BLUE + "You have been unmuted by " +
			 * ChatColor.GRAY + cs.getName() + ChatColor.BLUE + ".");
			 * cs.sendMessage(ChatColor.BLUE + "You have unmuted " +
			 * ChatColor.GRAY + t.getName() + ChatColor.BLUE + "."); return
			 * true; } else { mutedb.put(t, time); t.sendMessage(ChatColor.RED +
			 * "You have been muted by " + ChatColor.GRAY + cs.getName() +
			 * ChatColor.RED + "."); cs.sendMessage(ChatColor.BLUE +
			 * "You have muted " + ChatColor.GRAY + t.getName() + ChatColor.BLUE
			 * + "."); return true; } }
			 */
		}
		return false;
	}
}
