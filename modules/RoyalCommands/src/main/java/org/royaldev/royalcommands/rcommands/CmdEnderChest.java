/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.royaldev.royalcommands.rcommands;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.royaldev.royalcommands.AuthorizationHandler.PermType;
import org.royaldev.royalcommands.Config;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.WorldManager;

@ReflectCommand
public class CmdEnderChest extends BaseCommand {

    public CmdEnderChest(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] args) {
        if (!(cs instanceof Player)) {
            cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
            return true;
        }
        if (args.length < 2) {
            cs.sendMessage(cmd.getDescription());
            return false;
        }
        if (!Config.separateInv || !Config.separateEnder) {
            cs.sendMessage(MessageColor.NEGATIVE + "Cannot open ender chests unless they are separated!");
            return true;
        }
        Player p = (Player) cs;
        final OfflinePlayer op = RUtils.getOfflinePlayer(args[0]);
        if (this.ah.isAuthorized(op, cmd, PermType.EXEMPT) && !cs.getName().equals(op.getName())) {
            cs.sendMessage(MessageColor.NEGATIVE + "You cannot open that player's ender chest!");
            return true;
        }
        if (this.plugin.getServer().getWorld(args[1]) == null) {
            cs.sendMessage(MessageColor.NEGATIVE + "Invalid world specified!");
            return true;
        }
        final Inventory i = WorldManager.il.getOfflinePlayerEnderInventory(op, args[1]);
        if (i == null) {
            cs.sendMessage(MessageColor.NEGATIVE + "No inventory found!");
            return true;
        }
        p.sendMessage(MessageColor.POSITIVE + "Opened the ender chest of " + MessageColor.NEUTRAL + op.getName() + MessageColor.POSITIVE + ".");
        p.openInventory(i);
        return true;
    }
}
