package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.util.Random;

public class CmdSlap implements CommandExecutor {

    private RoyalCommands plugin;

    public CmdSlap(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("slap")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.slap")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                cs.sendMessage(cmd.getUsage().replace("<command>", label));
                return true;
            }
            Player victim;
            victim = plugin.getServer().getPlayer(args[0]);
            if (victim == null || plugin.isVanished(victim, cs)) {
                cs.sendMessage(MessageColor.NEGATIVE + "That person is not online!");
                return true;
            }
            if (plugin.ah.isAuthorized(victim, "rcmds.exempt.slap")) {
                cs.sendMessage(MessageColor.NEGATIVE + "You may not slap that player.");
                return true;
            }
            Random r = new Random();
            Vector push = new Vector();
            push.setY(r.nextInt(2));
            push.setX(r.nextInt(4) - 2);
            push.setZ(r.nextInt(4) - 2);
            victim.setVelocity(push);
            plugin.getServer().broadcastMessage(ChatColor.GOLD + cs.getName() + MessageColor.RESET + " slaps " + MessageColor.NEGATIVE + victim.getName() + MessageColor.RESET + "!");
            return true;
        }
        return false;
    }

}
