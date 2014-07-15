package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RoyalCommands;

@ReflectCommand
public class CmdHug extends BaseCommand {

    public CmdHug(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (args.length < 1) {
            cs.sendMessage(cmd.getDescription());
            return false;
        }
        Player t = plugin.getServer().getPlayer(args[0]);
        if (t == null) {
            cs.sendMessage(MessageColor.NEGATIVE + "That person is not online!");
            return true;
        }
        if (plugin.isVanished(t, cs)) {
            cs.sendMessage(MessageColor.NEGATIVE + "That player does not exist!");
            return true;
        }
        plugin.getServer().broadcastMessage(ChatColor.DARK_GREEN + cs.getName() + ChatColor.LIGHT_PURPLE + " hugs " + ChatColor.DARK_GREEN + t.getName() + ChatColor.LIGHT_PURPLE + "! " + MessageColor.NEGATIVE + "<3");
        return true;
    }
}
