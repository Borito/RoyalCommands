package org.royaldev.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class Rcmds implements CommandExecutor {

    RoyalCommands plugin;

    public Rcmds(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("rcmds")) {
            if (!plugin.isAuthorized(cs, "rcmds.rcmds")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            plugin.reloadConfig();

            plugin.showcommands = plugin.getConfig().getBoolean("view_commands");
            plugin.disablegetip = plugin.getConfig().getBoolean("disable_getip");
            plugin.useWelcome = plugin.getConfig().getBoolean("enable_welcome_message");
            plugin.buildPerm = plugin.getConfig().getBoolean("use_build_perm");
            plugin.backDeath = plugin.getConfig().getBoolean("back_on_death");
            plugin.motdLogin = plugin.getConfig().getBoolean("motd_on_login");

            plugin.banMessage = plugin.getConfig().getString("default_ban_message").replaceAll("(&([a-f0-9]))", "\u00A7$2");
            plugin.noBuildMessage = plugin.getConfig().getString("no_build_message").replaceAll("(&([a-f0-9]))", "\u00A7$2");
            plugin.kickMessage = plugin.getConfig().getString("default_kick_message").replaceAll("(&([a-f0-9]))", "\u00A7$2");
            plugin.defaultWarn = plugin.getConfig().getString("default_warn_message");
            plugin.welcomeMessage = plugin.getConfig().getString("welcome_message").replaceAll("(&([a-f0-9]))", "\u00A7$2");

            plugin.warnBan = plugin.getConfig().getInt("max_warns_before_ban");
            plugin.defaultStack = plugin.getConfig().getInt("default_stack_size");
            plugin.spawnmobLimit = plugin.getConfig().getInt("spawnmob_limit");

            plugin.muteCmds = plugin.getConfig().getStringList("mute_blocked_commands");
            plugin.blockedItems = plugin.getConfig().getStringList("blocked_spawn_items");
            plugin.motd = plugin.getConfig().getStringList("motd");

            cs.sendMessage(ChatColor.GREEN + "RoyalCommands " + ChatColor.BLUE + "v" + plugin.version + " reloaded.");
            return true;
        }
        return false;
    }
}
