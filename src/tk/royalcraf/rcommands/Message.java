package tk.royalcraf.rcommands;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import tk.royalcraf.royalcommands.RoyalCommands;

public class Message implements CommandExecutor {

	RoyalCommands plugin;

	public Message(RoyalCommands plugin) {
		this.plugin = plugin;
	}

	public static HashMap<Player, CommandSender> replydb = new HashMap<Player, CommandSender>();

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("message")) {
			if (!plugin.isAuthorized(cs, "rcmds.message")) {
				cs.sendMessage(ChatColor.RED
						+ "You don't have permission for that!");
				plugin.log.warning("[RoyalCommands] " + cs.getName()
						+ " was denied access to the command!");
				return true;
			} else {
				if (args.length < 2) {
					return true;
				} else {
					if (!(cs instanceof Player)) {
						cs.sendMessage(ChatColor.RED
								+ "This command is only available to players!");
						return true;
					}
					Player t = plugin.getServer().getPlayer(args[0]);
					if (t == null) {
						cs.sendMessage(ChatColor.RED
								+ "That player is not online!");
						return true;
					} else {
						if (!replydb.containsKey(t)) {
							replydb.put(t, cs);
						} else if (replydb.containsKey(t)) {
							if (replydb.get(t) != cs) {
								replydb.remove(t);
								replydb.put(t, cs);
							}
						}
						t.sendMessage(ChatColor.GRAY + "[" + ChatColor.BLUE
								+ cs.getName() + ChatColor.GRAY + " -> "
								+ ChatColor.BLUE + "You" + ChatColor.GRAY
								+ "] " + plugin.getFinalArg(args, 1));
						cs.sendMessage(ChatColor.GRAY + "[" + ChatColor.BLUE
								+ "You" + ChatColor.GRAY + " -> "
								+ ChatColor.BLUE + t.getName() + ChatColor.GRAY
								+ "] " + plugin.getFinalArg(args, 1));
						return true;
					}
				}
			}
		}
		return false;
	}

}
