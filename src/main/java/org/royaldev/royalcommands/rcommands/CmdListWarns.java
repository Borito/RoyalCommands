package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.PConfManager;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.util.List;

public class CmdListWarns implements CommandExecutor {

    private RoyalCommands plugin;

    public CmdListWarns(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("listwarns")) {
            if (!plugin.isAuthorized(cs, "rcmds.listwarns")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player) && args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            if (args.length > 1 && !plugin.isAuthorized(cs, "rcmds.others.listwarns")) {
                RUtils.dispNoPerms(cs, "You're not allowed to view other players' warnings.");
                return true;
            }
            String target = (args.length > 0) ? args[0] : cs.getName();
            PConfManager pcm = new PConfManager(target);
            if (!pcm.exists()) {
                cs.sendMessage(ChatColor.RED + "That player does not exist!");
                return true;
            }
            List<String> warns = pcm.getStringList("warns");
            if (warns == null || warns.isEmpty()) {
                cs.sendMessage(ChatColor.GRAY + target + ChatColor.BLUE + " has no warnings!");
                return true;
            }
            for (int i = 0; i < warns.size(); i++)
                cs.sendMessage(ChatColor.GRAY + "" + (i + 1) + ". " + warns.get(i).split("\\u00b5")[0]);
            return true;
        }
        return false;
    }
}
