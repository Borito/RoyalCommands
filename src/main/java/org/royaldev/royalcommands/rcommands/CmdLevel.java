package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdLevel implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdLevel(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("level")) {
            if (!(cs instanceof Player)) {
                cs.sendMessage(MessageColor.NEGATIVE + "This command can only be used by players!");
                return true;
            }
            if (!plugin.ah.isAuthorized(cs, "rcmds.level")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            Player player = (Player) cs;
            player.setLevel(player.getLevel() + 1);
            cs.sendMessage(MessageColor.POSITIVE + "XP level raised by one!");
            return true;
        }
        return false;
    }
}
