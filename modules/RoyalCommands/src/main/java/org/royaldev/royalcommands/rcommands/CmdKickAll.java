package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.Config;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

@ReflectCommand
public class CmdKickAll extends BaseCommand {

    public CmdKickAll(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] args) {
        String kickreason = Config.kickMessage;
        if (args.length > 0) kickreason = RoyalCommands.getFinalArg(args, 0);
        kickreason = RUtils.colorize(kickreason);
        Player p = null;
        if (cs instanceof Player) p = (Player) cs;
        for (Player t : this.plugin.getServer().getOnlinePlayers()) {
            if (!t.equals(p)) RUtils.kickPlayer(t, cs, kickreason);
        }
        return true;
    }
}
