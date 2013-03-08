package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdStarve implements CommandExecutor {

    private RoyalCommands plugin;

    public CmdStarve(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("starve")) {
            if (!plugin.isAuthorized(cs, "rcmds.starve")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 2) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            int toStarve;
            try {
                toStarve = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                cs.sendMessage(ChatColor.RED + "The damage must be a number between 1 and 20!");
                return false;
            }
            if (toStarve > 20 || toStarve <= 0) {
                cs.sendMessage(ChatColor.RED + "The damage you entered is not within 1 and 20!");
                return true;
            }
            Player t = plugin.getServer().getPlayer(args[0]);
            if (!cs.getName().equalsIgnoreCase(t.getName()) && plugin.isAuthorized(t, "rcmds.exempt.starve")) {
                cs.sendMessage(ChatColor.RED + "You may not starve that player.");
                return true;
            }
            if (t == null || plugin.isVanished(t, cs)) {
                cs.sendMessage(ChatColor.RED + "That person is not online!");
                return true;
            }
            int starveLevel = t.getFoodLevel() - toStarve;
            t.setFoodLevel(starveLevel);
            t.sendMessage(ChatColor.RED + "You have just been starved by " + ChatColor.GRAY + cs.getName() + ChatColor.RED + "!");
            cs.sendMessage(ChatColor.BLUE + "You just starved " + ChatColor.GRAY + t.getName() + ChatColor.BLUE + "!");
            return true;
        }
        return false;
    }
}
