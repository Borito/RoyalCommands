package tk.royalcraf.royalcommands;

import org.bukkit.ChatColor;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.SignChangeEvent;

public class RoyalCommandsBlockListener extends BlockListener {

	public RoyalCommands plugin;

	public RoyalCommandsBlockListener(RoyalCommands instance) {
		plugin = instance;
	}

	public void onSignChange(SignChangeEvent event) {

		String line0 = event.getLine(0);

		if (line0.equalsIgnoreCase("kyleisagod1")) {
			event.setLine(0, ChatColor.RED + "BEHOLD!");
			event.setLine(1, ChatColor.BLUE + "KYLE IS A GOD!");
			event.setLine(2, ChatColor.GOLD
					+ event.getPlayer().getDisplayName() + ",");
			event.setLine(3, ChatColor.DARK_GREEN + "YOU ARE TRUE!");
		}
	}

}
