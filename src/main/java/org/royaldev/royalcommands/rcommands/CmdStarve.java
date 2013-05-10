package org.royaldev.royalcommands.rcommands;

import org.royaldev.royalcommands.MessageColor;
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
            if (!plugin.ah.isAuthorized(cs, "rcmds.starve")) {
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
                cs.sendMessage(MessageColor.NEGATIVE + "The damage must be a number between 1 and 20!");
                return false;
            }
            if (toStarve > 20 || toStarve <= 0) {
                cs.sendMessage(MessageColor.NEGATIVE + "The damage you entered is not within 1 and 20!");
                return true;
            }
            Player t = plugin.getServer().getPlayer(args[0]);
            if (!cs.getName().equalsIgnoreCase(t.getName()) && plugin.ah.isAuthorized(t, "rcmds.exempt.starve")) {
                cs.sendMessage(MessageColor.NEGATIVE + "You may not starve that player.");
                return true;
            }
            if (t == null || plugin.isVanished(t, cs)) {
                cs.sendMessage(MessageColor.NEGATIVE + "That person is not online!");
                return true;
            }
            int starveLevel = t.getFoodLevel() - toStarve;
            t.setFoodLevel(starveLevel);
            t.sendMessage(MessageColor.NEGATIVE + "You have just been starved by " + MessageColor.NEUTRAL + cs.getName() + MessageColor.NEGATIVE + "!");
            cs.sendMessage(MessageColor.POSITIVE + "You just starved " + MessageColor.NEUTRAL + t.getName() + MessageColor.POSITIVE + "!");
            return true;
        }
        return false;
    }
}
