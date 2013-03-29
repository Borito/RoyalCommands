package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.PConfManager;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.util.Date;

public class CmdBan implements CommandExecutor {

    private RoyalCommands plugin;

    public CmdBan(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("ban")) {
            if (!plugin.isAuthorized(cs, "rcmds.ban")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            OfflinePlayer t = plugin.getServer().getPlayer(args[0]);
            if (t == null) t = plugin.getServer().getOfflinePlayer(args[0]);
            PConfManager pcm = PConfManager.getPConfManager(t);
/*            if (!pcm.getConfExists()) {
                if (args.length > 1 && args[1].equalsIgnoreCase("true")) {
                    args = (String[]) ArrayUtils.remove(args, 1);
                } else {
                    cs.sendMessage(ChatColor.RED + "That player does not exist!");
                    return true;
                }
            }*/
            if (!pcm.exists()) pcm.createFile();
            if (plugin.isAuthorized(t, "rcmds.exempt.ban")) {
                cs.sendMessage(ChatColor.RED + "You can't ban that player!");
                return true;
            }
            String banreason = (args.length > 1) ? RoyalCommands.getFinalArg(args, 1) : plugin.banMessage;
            banreason = RUtils.colorize(banreason);
            pcm.set("banreason", banreason);
            pcm.set("banner", cs.getName());
            pcm.set("bannedat", new Date().getTime());
            pcm.set("bantime", null);
            cs.sendMessage(ChatColor.BLUE + "You have banned " + ChatColor.GRAY + t.getName() + ChatColor.BLUE + ".");
            RUtils.banPlayer(t, cs, banreason);
            return true;
        }
        return false;
    }
}
