/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.royaldev.royalcommands.rcommands;

import java.util.Map;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.rcommands.kits.Kit;
import org.royaldev.royalcommands.shaded.com.sk89q.util.config.ConfigurationNode;
import org.royaldev.royalcommands.shaded.com.sk89q.util.config.FancyConfiguration;

@ReflectCommand
public class CmdKits extends TabCommand {

    public CmdKits(final RoyalCommands instance, final String name) {
        super(instance, name, true, new Short[]{});
    }

    @Override
    public boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] args, CommandArguments ca) {
        final FancyConfiguration fc = this.plugin.getFancyConfig();
        final Map<String, ConfigurationNode> kits = fc.getNodes("kits.list");
        if (kits.isEmpty()) {
            cs.sendMessage(MessageColor.NEGATIVE + "No kits found!");
            return true;
        }
        cs.sendMessage(MessageColor.POSITIVE + "Kits:");
        for (final Map.Entry<String, ConfigurationNode> entry : kits.entrySet()) {
            final Kit kit = new Kit(entry.getKey(), entry.getValue());
            cs.sendMessage(MessageColor.POSITIVE + "  " + kit.getName() + ":");
            cs.sendMessage(MessageColor.NEUTRAL + "    " + kit.getDescription());
            final int items = kit.getItems().size();
            cs.sendMessage(MessageColor.NEUTRAL + "    " + items + MessageColor.POSITIVE + " item" + (items == 1 ? "" : "s") + ".");
            final long cooldown = kit.getCooldown();
            if (cooldown != 0L) {
                if (cooldown == -1L) {
                    cs.sendMessage(MessageColor.NEUTRAL + "    Kit can only be used once.");
                } else {
                    cs.sendMessage(MessageColor.NEUTRAL + "   " + RUtils.formatDateDiff(System.currentTimeMillis() + cooldown * 1000L) + MessageColor.POSITIVE + " cooldown.");
                }
            }
        }
        return true;

    }
}
