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
import org.royaldev.royalcommands.wrappers.player.MemoryRPlayer;
import org.royaldev.royalcommands.wrappers.player.RPlayer;

@ReflectCommand
public class CmdLoadPlayer extends BaseCommand {

    public CmdLoadPlayer(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] args) {
        if (args.length < 1) {
            cs.sendMessage(cmd.getDescription());
            return false;
        }
        final RPlayer rp = MemoryRPlayer.getRPlayer(args[0]);
        final Player t = rp.getPlayer();
        if (t == null) {
            cs.sendMessage(MessageColor.NEGATIVE + "No such player!");
            return true;
        }
        if ((cs instanceof Player && !rp.isSameAs((Player) cs)) && !this.ah.isAuthorized(cs, cmd, PermType.OTHERS)) {
            cs.sendMessage(MessageColor.NEGATIVE + "You cannot load other players' data!");
            return true;
        }
        t.loadData();
        cs.sendMessage(MessageColor.POSITIVE + "Data loaded.");
        return true;
    }
}
