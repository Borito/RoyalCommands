/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.royaldev.royalcommands.rcommands;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.shaded.mkremins.fanciful.FancyMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

@ReflectCommand
public class CmdAccountStatus extends BaseCommand {

    public CmdAccountStatus(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] args) {
        if (args.length < 1) {
            cs.sendMessage(cmd.getDescription());
            return false;
        }
        String name = args[0];
        final OfflinePlayer p = RUtils.getOfflinePlayer(name);
        name = p.getName();
        URL u;
        try {
            u = new URL("https://minecraft.net/haspaid.jsp?user=" + URLEncoder.encode(name, "UTF-8"));
        } catch (final MalformedURLException ex) {
            cs.sendMessage(MessageColor.NEGATIVE + "An unthinkable error happened. Please let the developer know.");
            return true;
        } catch (final UnsupportedEncodingException ex) {
            cs.sendMessage(MessageColor.NEGATIVE + "The UTF-8 encoding is not supported on this system!");
            return true;
        }
        boolean isPremium;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(u.openStream()));
            final String line = br.readLine();
            if (line == null) {
                cs.sendMessage(MessageColor.NEGATIVE + "Could not read from Minecraft's servers!");
                return true;
            } else isPremium = "true".equalsIgnoreCase(line);
        } catch (final IOException ex) {
            cs.sendMessage(MessageColor.NEGATIVE + "Could not read from Minecraft's servers!");
            cs.sendMessage(MessageColor.NEGATIVE + ex.getMessage());
            return true;
        } finally {
            try {
                if (br != null) br.close();
            } catch (final IOException ignored) {
            }
        }
        // @formatter:off
        new FancyMessage(name)
                .color(MessageColor.NEUTRAL.cc())
                .formattedTooltip(RUtils.getPlayerTooltip(p))
            .then(" has ")
                .color(MessageColor.POSITIVE.cc())
            .then(isPremium ? "paid" : "not paid")
                .color(MessageColor.NEUTRAL.cc())
            .then(" for Minecraft.")
                .color(MessageColor.POSITIVE.cc())
            .send(cs);
        // @formatter:on
        return true;
    }

}
