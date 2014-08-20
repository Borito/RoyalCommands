package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.Config;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RoyalCommands;

import java.util.Set;

@ReflectCommand
public class CmdKits extends BaseCommand {

    public CmdKits(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(CommandSender cs, Command cmd, String label, String[] args) {
        final Set<String> kits = this.plugin.getConfig().getConfigurationSection("kits.list").getKeys(false);
        if (kits.isEmpty()) {
            cs.sendMessage(MessageColor.NEGATIVE + "No kits found!");
            return true;
        }
        final StringBuilder sb = new StringBuilder();
        for (final String kit : kits) {
            if (Config.kitPerms && !this.ah.isAuthorized(cs, "rcmds.kit." + kit)) continue;
            sb.append(kit).append(", ");
        }
        cs.sendMessage(MessageColor.POSITIVE + "Kits:");
        if (sb.length() == 0) return true;
        cs.sendMessage(sb.substring(0, sb.length() - 2)); // ", ";
        return true;

    }
}
