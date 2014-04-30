package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.Config;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.util.Map;

@ReflectCommand
public class CmdKits implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdKits(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("kits")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.kits")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player) && args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            final Map<String, Object> opts = plugin.getConfig().getConfigurationSection("kits").getValues(false);
            if (opts.keySet().isEmpty()) {
                cs.sendMessage(MessageColor.NEGATIVE + "No kits found!");
                return true;
            }
            final StringBuilder sb = new StringBuilder();
            for (String s : opts.keySet()) {
                if (Config.kitPerms && plugin.ah.isAuthorized(cs, "rcmds.kit." + s)) sb.append(s).append(", ");
                else if (!Config.kitPerms) sb.append(s).append(", ");
            }
            cs.sendMessage(MessageColor.POSITIVE + "Kits:");
            if (sb.length() == 0) return true;
            cs.sendMessage(sb.substring(0, sb.length() - 2)); // ", "
            return true;
        }
        return false;

    }
}
