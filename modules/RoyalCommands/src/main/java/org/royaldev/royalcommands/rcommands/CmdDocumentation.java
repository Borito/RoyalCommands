package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.YamlConfiguration;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@ReflectCommand
public class CmdDocumentation implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdDocumentation(RoyalCommands instance) {
        this.plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("documentation")) {
            if (!this.plugin.ah.isAuthorized(cs, cmd)) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 2) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            switch (args[0].toLowerCase()) {
                case "command":
                case "cmd":
                case "c":
                    String commandName = args[1];
                    if (commandName.startsWith("/")) commandName = commandName.substring(1);
                    final PluginCommand pc = this.plugin.getServer().getPluginCommand(commandName);
                    if (!this.plugin.getClass().isInstance(pc.getPlugin())) {
                        cs.sendMessage(MessageColor.NEGATIVE + "Not a " + this.plugin.getName() + " plugin. Try " + MessageColor.NEUTRAL + "/usage" + MessageColor.NEGATIVE + ".");
                        return true;
                    }
                    cs.sendMessage(MessageColor.POSITIVE + "Link: " + MessageColor.NEUTRAL + "https://docs.royaldev.org/commands/" + pc.getName().toLowerCase());
                    break;
                case "permission":
                case "perm":
                case "p":
                    final YamlConfiguration pluginYml = this.plugin.getPluginYml();
                    final String perm = args[1].toLowerCase();
                    if (!pluginYml.isSet("permissions." + perm)) {
                        cs.sendMessage(MessageColor.NEGATIVE + "No such permission registered!");
                        return true;
                    }
                    try {
                        cs.sendMessage(MessageColor.POSITIVE + "Link: " + MessageColor.NEUTRAL + "https://docs.royaldev.org/permissions?search=" + URLEncoder.encode(perm, "UTf-8"));
                    } catch (UnsupportedEncodingException e) {
                        cs.sendMessage(MessageColor.NEGATIVE + "Error (" + MessageColor.NEUTRAL + e.getClass().getSimpleName() + MessageColor.NEGATIVE + "): " + MessageColor.NEUTRAL + e.getMessage());
                    }
                    break;
                default:
                    cs.sendMessage(MessageColor.NEGATIVE + "Invalid type. Either " + MessageColor.NEUTRAL + "command" + MessageColor.NEGATIVE + " or " + MessageColor.NEUTRAL + "permission" + MessageColor.NEGATIVE + " must be specified.");
            }
            return true;
        }
        return false;
    }

}
