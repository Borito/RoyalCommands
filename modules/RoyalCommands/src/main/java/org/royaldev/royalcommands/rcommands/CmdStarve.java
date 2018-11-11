/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.AuthorizationHandler.PermType;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RoyalCommands;

@ReflectCommand
public class CmdStarve extends TabCommand {

    public CmdStarve(final RoyalCommands instance, final String name) {
        super(instance, name, true, new Short[]{CompletionType.ONLINE_PLAYER.getShort()});
    }

    @Override
    public boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] args, CommandArguments ca) {
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
        Player t = this.plugin.getServer().getPlayer(args[0]);
        if (!cs.getName().equalsIgnoreCase(t.getName()) && this.ah.isAuthorized(t, cmd, PermType.EXEMPT)) {
            cs.sendMessage(MessageColor.NEGATIVE + "You may not starve that player.");
            return true;
        }
        if (this.plugin.isVanished(t, cs)) {
            cs.sendMessage(MessageColor.NEGATIVE + "That person is not online!");
            return true;
        }
        int starveLevel = t.getFoodLevel() - toStarve;
        t.setFoodLevel(starveLevel);
        t.sendMessage(MessageColor.NEGATIVE + "You have just been starved by " + MessageColor.NEUTRAL + cs.getName() + MessageColor.NEGATIVE + "!");
        cs.sendMessage(MessageColor.POSITIVE + "You just starved " + MessageColor.NEUTRAL + t.getName() + MessageColor.POSITIVE + "!");
        return true;
    }
}
