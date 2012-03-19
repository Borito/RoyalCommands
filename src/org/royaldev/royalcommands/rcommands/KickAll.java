package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class KickAll implements CommandExecutor {

    RoyalCommands plugin;

    public KickAll(RoyalCommands instance) {
        this.plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("kickall")) {
            if (!plugin.isAuthorized(cs, "rcmds.kickall")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            String kickreason = plugin.kickMessage;
            if (args.length > 0) kickreason = plugin.getFinalArg(args, 0);
            kickreason = kickreason.replaceAll("(&([a-f0-9]))", "\u00A7$2");
            Player p = null;
            if (cs instanceof Player) p = (Player) cs;
            for (Player t : plugin.getServer().getOnlinePlayers()) {
                if (!t.equals(p)) t.kickPlayer(kickreason);
            }
            return true;
        }
        return false;
    }

}
