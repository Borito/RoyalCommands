package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.Config;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RoyalCommands;

import java.util.Arrays;
import java.util.List;

@ReflectCommand
public class CmdWhitelist extends TabCommand {

    public CmdWhitelist(final RoyalCommands instance, final String name) {
        super(instance, name, true, new Integer[]{CompletionType.LIST.getInt(), CompletionType.ONLINE_PLAYER.getInt()});
    }

    @Override
    public List<String> customList(CommandSender cs, Command cmd, String label, String[] args, String arg) {
        return Arrays.asList("add", "remove", "check");
    }

    public void reloadWhitelist() {
        Config.whitelist = this.plugin.whl.getStringList("whitelist");
    }

    @Override
    public boolean runCommand(CommandSender cs, Command cmd, String label, String[] eargs, CommandArguments ca) {
        if (this.plugin.whl == null) {
            cs.sendMessage(MessageColor.NEGATIVE + "The whitelist.yml file was invalid! Cannot use whitelist.");
            return true;
        }
        if (eargs.length < 1) {
            cs.sendMessage(cmd.getDescription());
            return false;
        }
        String command = eargs[0];
        if (eargs.length < 2) {
            cs.sendMessage(cmd.getDescription());
            return false;
        }
        String player = eargs[1];
        if (command.equalsIgnoreCase("add")) {
            if (Config.whitelist.contains(player)) {
                cs.sendMessage(MessageColor.NEGATIVE + "That player is already whitelisted!");
                return true;
            }
            Config.whitelist.add(player);
            this.plugin.whl.set("whitelist", Config.whitelist);
            reloadWhitelist();
            cs.sendMessage(MessageColor.POSITIVE + "Added " + MessageColor.NEUTRAL + player + MessageColor.POSITIVE + " to whitelist.");
            return true;
        } else if (command.equalsIgnoreCase("remove")) {
            if (!Config.whitelist.contains(player)) {
                cs.sendMessage(MessageColor.NEGATIVE + "That player is not whitelisted!");
                return true;
            }
            Config.whitelist.remove(player);
            this.plugin.whl.set("whitelist", Config.whitelist);
            reloadWhitelist();
            cs.sendMessage(MessageColor.POSITIVE + "Removed " + MessageColor.NEUTRAL + player + MessageColor.POSITIVE + " from whitelist.");
            return true;
        } else if (command.equalsIgnoreCase("check")) {
            String message = (Config.whitelist.contains(player)) ? MessageColor.NEUTRAL + player + MessageColor.POSITIVE + " is in the whitelist." : MessageColor.NEUTRAL + player + MessageColor.NEGATIVE + " is not in the whitelist.";
            cs.sendMessage(message);
            return true;
        } else {
            cs.sendMessage(MessageColor.NEGATIVE + "Unknown subcommand!");
            return true;
        }
    }
}
