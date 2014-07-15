package org.royaldev.royalcommands.rcommands;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.configuration.PConfManager;

@ReflectCommand
public class CmdVip extends BaseCommand {

    public CmdVip(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (args.length < 1) {
            cs.sendMessage(cmd.getDescription());
            return false;
        }
        String command = args[0];
        if (command.equalsIgnoreCase("add")) {
            if (args.length < 2) {
                cs.sendMessage(MessageColor.NEGATIVE + "No player specified!");
                return true;
            }
            OfflinePlayer t = plugin.getServer().getOfflinePlayer(args[1]);
            PConfManager pcm = PConfManager.getPConfManager(t);
            if (!pcm.exists()) {
                cs.sendMessage(MessageColor.NEGATIVE + "That player does not exist!");
                return true;
            }
            if (pcm.get("vip") != null && pcm.getBoolean("vip")) {
                cs.sendMessage(MessageColor.NEGATIVE + "That player is already in the VIP list.");
                return true;
            }
            pcm.set("vip", true);
            cs.sendMessage(MessageColor.POSITIVE + "Successfully added " + MessageColor.NEUTRAL + t.getName() + MessageColor.POSITIVE + " to the VIP list.");
            return true;
        } else if (command.equalsIgnoreCase("remove")) {
            if (args.length < 2) {
                cs.sendMessage(MessageColor.NEGATIVE + "No player specified!");
                return true;
            }
            OfflinePlayer t = plugin.getServer().getOfflinePlayer(args[1]);
            PConfManager pcm = PConfManager.getPConfManager(t);
            if (!pcm.exists()) {
                cs.sendMessage(MessageColor.NEGATIVE + "That player does not exist!");
                return true;
            }
            if (pcm.get("vip") == null || !pcm.getBoolean("vip")) {
                cs.sendMessage(MessageColor.NEGATIVE + "That player is not in the VIP list.");
                return true;
            }
            pcm.set("vip", false);
            cs.sendMessage(MessageColor.POSITIVE + "Successfully removed " + MessageColor.NEUTRAL + t.getName() + MessageColor.POSITIVE + " to the VIP list.");
            return true;
        } else if (command.equalsIgnoreCase("check")) {
            if (args.length < 2) {
                cs.sendMessage(MessageColor.NEGATIVE + "No player specified!");
                return true;
            }
            OfflinePlayer t = plugin.getServer().getOfflinePlayer(args[1]);
            PConfManager pcm = PConfManager.getPConfManager(t);
            if (!pcm.exists()) {
                cs.sendMessage(MessageColor.NEGATIVE + "That player does not exist!");
                return true;
            }
            boolean inList = pcm.getBoolean("vip");
            if (inList) {
                cs.sendMessage(MessageColor.POSITIVE + "The player " + MessageColor.NEUTRAL + t.getName() + MessageColor.POSITIVE + " is in the VIP list.");
                return true;
            }
            cs.sendMessage(MessageColor.NEGATIVE + "The player " + MessageColor.NEUTRAL + t.getName() + MessageColor.NEGATIVE + " is not in the VIP list.");
            return true;
        } else if (command.equalsIgnoreCase("?")) {
            String cmdName = cmd.getName();
            cs.sendMessage(MessageColor.NEUTRAL + "/" + cmdName + " add [player] " + MessageColor.POSITIVE + " - Adds a player to the VIP list.");
            cs.sendMessage(MessageColor.NEUTRAL + "/" + cmdName + " remove [player] " + MessageColor.POSITIVE + " - Removes a player from the VIP list.");
            cs.sendMessage(MessageColor.NEUTRAL + "/" + cmdName + " check [player] " + MessageColor.POSITIVE + " - Checks if a player is in the VIP list.");
            cs.sendMessage(MessageColor.NEUTRAL + "/" + cmdName + " ? " + MessageColor.POSITIVE + " - Displays this help.");
            return true;
        } else {
            cs.sendMessage(MessageColor.NEGATIVE + "Invalid subcommand!");
            cs.sendMessage(MessageColor.NEGATIVE + "Try " + MessageColor.NEUTRAL + "/" + cmd.getName() + " ?" + MessageColor.NEGATIVE + ".");
            return true;
        }
    }
}
