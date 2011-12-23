package tk.royalcraf.rcommands;

import java.io.File;
import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import tk.royalcraf.royalcommands.RoyalCommands;

public class SetHome implements CommandExecutor {

	RoyalCommands plugin;

	public SetHome(RoyalCommands plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("sethome")) {
			if (!plugin.isAuthorized(cs, "rcmds.sethome")) {
				cs.sendMessage(ChatColor.RED
						+ "You don't have permission for that!");
				plugin.log.warning("[RoyalCommands] " + cs.getName()
						+ " was denied access to the command!");
				return true;
			}

			if (args.length > 0) {
				if (!plugin.isAuthorized(cs, "rcmds.sethome.multi")) {
					cs.sendMessage(ChatColor.RED
							+ "You don't have permission for multiple homes!");
					plugin.log.warning("[RoyalCommands] " + cs.getName()
							+ " was denied access to the command!");
					return true;
				}
			}

			Player p = null;

			if (!(cs instanceof Player)) {
				cs.sendMessage(ChatColor.RED
						+ "This command is only available to players!");
				return true;
			} else {
				p = (Player) cs;
			}

			double locX = p.getLocation().getX();
			double locY = p.getLocation().getY();
			double locZ = p.getLocation().getZ();
			Float locYaw = p.getLocation().getYaw();
			Float locPitch = p.getLocation().getPitch();
			String locW = p.getWorld().getName();

			File pconfl = new File(plugin.getDataFolder() + "/userdata/"
					+ cs.getName().toLowerCase() + ".yml");
			if (pconfl.exists()) {
				FileConfiguration pconf = YamlConfiguration
						.loadConfiguration(pconfl);
				if (args.length > 0) {
					pconf.set("home." + args[0] + ".set", true);
					pconf.set("home." + args[0] + ".x", locX);
					pconf.set("home." + args[0] + ".y", locY);
					pconf.set("home." + args[0] + ".z", locZ);
					pconf.set("home." + args[0] + ".pitch", locPitch.toString());
					pconf.set("home." + args[0] + ".yaw", locYaw.toString());
					pconf.set("home." + args[0] + ".w", locW);
				} else {
					pconf.set("home.home.set", true);
					pconf.set("home.home.x", locX);
					pconf.set("home.home.y", locY);
					pconf.set("home.home.z", locZ);
					pconf.set("home.home.pitch", locPitch.toString());
					pconf.set("home.home.yaw", locYaw.toString());
					pconf.set("home.home.w", locW);
				}
				try {
					pconf.save(pconfl);
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (args.length > 0) {
					p.sendMessage(ChatColor.BLUE + "Home \"" + ChatColor.GRAY
							+ args[0] + ChatColor.BLUE + "\" set.");
				} else {
					p.sendMessage(ChatColor.BLUE + "Home set.");
				}
				return true;
			}
		}
		return false;
	}

}
