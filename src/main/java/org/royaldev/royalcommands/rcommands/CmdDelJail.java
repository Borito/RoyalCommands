package org.royaldev.royalcommands.rcommands;

import org.royaldev.royalcommands.MessageColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.io.File;
import java.io.IOException;

public class CmdDelJail implements CommandExecutor {

    private RoyalCommands plugin;

    public CmdDelJail(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("deljail")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.deljail")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                cs.sendMessage(cmd.getUsage());
                return true;
            }
            File pconfl = new File(plugin.getDataFolder() + "/jails.yml");
            if (pconfl.exists()) {
                FileConfiguration pconf = YamlConfiguration.loadConfiguration(pconfl);
                if (pconf.get("jails." + args[0]) == null) {
                    cs.sendMessage(MessageColor.NEGATIVE + "That jail does not exist!");
                    return true;
                }
                pconf.set("jails." + args[0], null);
                try {
                    pconf.save(pconfl);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                cs.sendMessage(MessageColor.POSITIVE + "The jail \"" + MessageColor.NEUTRAL + args[0] + MessageColor.POSITIVE + "\" has been deleted.");
                return true;
            }
        }
        return false;
    }

}
