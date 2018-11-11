/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.royaldev.royalcommands.rcommands;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.AuthorizationHandler.PermType;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.configuration.PlayerConfiguration;
import org.royaldev.royalcommands.configuration.PlayerConfigurationManager;

/*
FROM PLAYER
  /kh          : Show kick history of self (first)
  /kh 2        : Show kick history of self (second)
  /kh person   : Show kick history of person (first)
  /kh person 2 : Show kick history of person (second)
FROM CONSOLE
  /kh          : Usage
  /kh 2        : Usage
  /kh player   : Show kick history of player (first)
  /kh player 2 : Show kick history of player (second)
*/

@ReflectCommand
public class CmdKickHistory extends TabCommand {

    public CmdKickHistory(final RoyalCommands instance, final String name) {
        super(instance, name, true, new Short[]{CompletionType.ONLINE_PLAYER.getShort(), CompletionType.LIST.getShort()});
    }
	
	@Override
	protected List<String> customList(final CommandSender cs, final Command cmd, final String label, final String[] args, final String arg) {
		/**
		 * TODO: For the second item, return indexes based on the number of known kicks for the player. Use runCommand's code as a base.
		 */
		return new ArrayList<>();
	}
	
    @Override
    public boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] args, CommandArguments ca) {
        int index;
        OfflinePlayer t;
        final boolean isPlayer = cs instanceof Player;
        if (isPlayer && args.length < 1) {
            index = 1;
            t = (Player) cs;
        } else if (!isPlayer && args.length < 1) {
            cs.sendMessage(cmd.getDescription());
            return false;
        } else if (args.length == 1) {
            final Integer given = (isPlayer) ? RUtils.getInt(args[0]) : null;
            index = (given == null) ? 1 : given;
            t = (given == null) ? RUtils.getOfflinePlayer(args[0]) : (Player) cs;
        } else {
            final Integer given = RUtils.getInt(args[1]);
            if (given == null) {
                cs.sendMessage(MessageColor.NEGATIVE + "That was not a number!");
                return true;
            }
            index = given;
            t = RUtils.getOfflinePlayer(args[0]);
        }
        index--;
        boolean isSame = t.getName().equalsIgnoreCase(cs.getName());
        if (!isSame && !this.ah.isAuthorized(cs, cmd, PermType.OTHERS)) {
            cs.sendMessage(MessageColor.NEGATIVE + "You do not have permission to view the kicks of other players.");
            return true;
        }
        if (!isSame && this.ah.isAuthorized(t, cmd, PermType.EXEMPT)) {
            cs.sendMessage(MessageColor.NEGATIVE + "You cannot view the kicks of that player.");
            return true;
        }
        final PlayerConfiguration pcm = PlayerConfigurationManager.getConfiguration(t);
        if (!pcm.isSet("kick_history." + index)) {
            cs.sendMessage(MessageColor.NEGATIVE + "No such kick for " + MessageColor.NEUTRAL + t.getName() + MessageColor.NEGATIVE + ".");
            return true;
        }
        final ConfigurationSection kick = pcm.getConfigurationSection("kick_history." + index);
        final String timestamp = kick.isSet("timestamp") ? new SimpleDateFormat("MM/dd/yy hh:mm:ss a").format(kick.getLong("timestamp")) : "Unknown time";
        cs.sendMessage(MessageColor.POSITIVE + "Kick " + MessageColor.NEUTRAL + (index + 1) + MessageColor.POSITIVE + " of " + MessageColor.NEUTRAL + kick.getParent().getKeys(false).size() + MessageColor.POSITIVE + " for " + MessageColor.NEUTRAL + t.getName() + MessageColor.POSITIVE + ".");
        cs.sendMessage(MessageColor.NEUTRAL + String.valueOf(index + 1) + MessageColor.POSITIVE + ": [" + MessageColor.NEUTRAL + timestamp + MessageColor.POSITIVE + "] (" + MessageColor.NEUTRAL + kick.getString("kicker", "Unknown player") + MessageColor.POSITIVE + ") " + MessageColor.NEUTRAL + kick.getString("reason", "Unknown reason"));
        return true;
    }
}
