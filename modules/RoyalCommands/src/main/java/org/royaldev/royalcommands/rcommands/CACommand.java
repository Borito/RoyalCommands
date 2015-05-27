/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.RoyalCommands;

public abstract class CACommand extends BaseCommand {

    public CACommand(final RoyalCommands instance, final String name, final boolean checkPermissions) {
        super(instance, name, checkPermissions);
    }

    protected abstract boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] eargs, final CommandArguments ca);

    @Override
    protected boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] args) {
        final CommandArguments ca = this.getCommandArguments(args);
        return this.runCommand(cs, cmd, label, ca.getExtraParameters(), ca);
    }
}
