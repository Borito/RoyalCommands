package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.Config;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RoyalCommands;

import java.util.Map;

@ReflectCommand
public class CmdKits extends BaseCommand {

    public CmdKits(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(CommandSender cs, Command cmd, String label, String[] args) {
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
            if (Config.kitPerms && this.ah.isAuthorized(cs, "rcmds.kit." + s)) sb.append(s).append(", ");
            else if (!Config.kitPerms) sb.append(s).append(", ");
        }
        cs.sendMessage(MessageColor.POSITIVE + "Kits:");
        if (sb.length() == 0) return true;
        cs.sendMessage(sb.substring(0, sb.length() - 2)); // ", ";
        return true;

    }
}
