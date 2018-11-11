/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.royaldev.royalcommands.rcommands;

import java.text.DecimalFormat;
import java.util.Date;
import org.apache.commons.lang.BooleanUtils;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.configuration.PlayerConfiguration;
import org.royaldev.royalcommands.configuration.PlayerConfigurationManager;

@ReflectCommand
public class CmdWhois extends BaseCommand {

    public CmdWhois(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] args) {
        if (args.length < 1) {
            cs.sendMessage(cmd.getDescription());
            return false;
        }
        final OfflinePlayer t = RUtils.getOfflinePlayer(args[0]);
        if (!t.isOnline() && !t.hasPlayedBefore()) {
            cs.sendMessage(MessageColor.NEGATIVE + "That player has never played before!");
            return true;
        }
        PlayerConfiguration pcm = PlayerConfigurationManager.getConfiguration(t);
        DecimalFormat df = new DecimalFormat("#.##");
        String ip = pcm.getString("ip");
        String name = pcm.getString("name");
        String dispname = pcm.getString("dispname");
        cs.sendMessage(MessageColor.POSITIVE + "=====================");
        cs.sendMessage(MessageColor.POSITIVE + " " + ((t.isOnline()) ? "Whois" : "Whowas") + " for " + MessageColor.NEUTRAL + name);
        cs.sendMessage(MessageColor.POSITIVE + " Nickname: " + MessageColor.NEUTRAL + dispname);
        cs.sendMessage(MessageColor.POSITIVE + " UUID: " + MessageColor.NEUTRAL + t.getUniqueId());
        cs.sendMessage(MessageColor.POSITIVE + " IP: " + MessageColor.NEUTRAL + ip);
        cs.sendMessage(MessageColor.POSITIVE + " VIP: " + MessageColor.NEUTRAL + BooleanUtils.toStringYesNo(pcm.getBoolean("vip")));
        cs.sendMessage(MessageColor.POSITIVE + " Muted/Frozen/Jailed: " + MessageColor.NEUTRAL + BooleanUtils.toStringYesNo(pcm.getBoolean("muted")) + MessageColor.POSITIVE + " / " + MessageColor.NEUTRAL + BooleanUtils.toStringYesNo(pcm.getBoolean("frozen")) + MessageColor.POSITIVE + " / " + MessageColor.NEUTRAL + BooleanUtils.toStringYesNo(pcm.getBoolean("jailed")));
        long timestamp = RUtils.getTimeStamp(t, "seen");
        String lastseen = (timestamp < 0) ? "unknown" : RUtils.formatDateDiff(timestamp);
        cs.sendMessage(MessageColor.POSITIVE + " Last seen:" + MessageColor.NEUTRAL + ((t.isOnline()) ? " now" : lastseen));
        cs.sendMessage(MessageColor.POSITIVE + " First played:" + MessageColor.NEUTRAL + RUtils.formatDateDiff(t.getFirstPlayed()) + " ago");
        if (t.isOnline()) {
            final Player p = (Player) t;
            cs.sendMessage(MessageColor.POSITIVE + " Gamemode: " + MessageColor.NEUTRAL + p.getGameMode().name().toLowerCase());
            cs.sendMessage(MessageColor.POSITIVE + " Can fly: " + MessageColor.NEUTRAL + BooleanUtils.toStringYesNo(p.getAllowFlight()));
            cs.sendMessage(MessageColor.POSITIVE + " Health/Hunger/Saturation: " + MessageColor.NEUTRAL + df.format(p.getHealth() / 2) + MessageColor.POSITIVE + " / " + MessageColor.NEUTRAL + p.getFoodLevel() / 2 + MessageColor.POSITIVE + " / " + MessageColor.NEUTRAL + p.getSaturation() / 2);
            cs.sendMessage(MessageColor.POSITIVE + " Total Exp/Exp %/Level: " + MessageColor.NEUTRAL + p.getTotalExperience() + MessageColor.POSITIVE + " / " + MessageColor.NEUTRAL + df.format(p.getExp() * 100) + "%" + MessageColor.POSITIVE + " / " + MessageColor.NEUTRAL + p.getLevel());
            cs.sendMessage(MessageColor.POSITIVE + " Item in hand: " + MessageColor.NEUTRAL + RUtils.getItemName(p.getInventory().getItemInMainHand()));
            cs.sendMessage(MessageColor.POSITIVE + " Alive for:" + MessageColor.NEUTRAL + RUtils.formatDateDiff(new Date().getTime() - p.getTicksLived() * 50));
            Location l = p.getLocation();
            cs.sendMessage(MessageColor.POSITIVE + " Last position: " + "(" + MessageColor.NEUTRAL + df.format(l.getX()) + MessageColor.POSITIVE + ", " + MessageColor.NEUTRAL + df.format(l.getY()) + MessageColor.POSITIVE + ", " + MessageColor.NEUTRAL + df.format(l.getZ()) + MessageColor.POSITIVE + ") in " + MessageColor.NEUTRAL + RUtils.getMVWorldName(l.getWorld()));
        } else {
            String lP = "lastposition.";
            World w = (pcm.isSet(lP + "world")) ? this.plugin.getServer().getWorld(pcm.getString(lP + "world")) : null;
            if (w != null) {
                Location l = new Location(w, pcm.getDouble(lP + "x"), pcm.getDouble(lP + "y"), pcm.getDouble(lP + "z"));
                cs.sendMessage(MessageColor.POSITIVE + " Last position: " + "(" + MessageColor.NEUTRAL + df.format(l.getX()) + MessageColor.POSITIVE + ", " + MessageColor.NEUTRAL + df.format(l.getY()) + MessageColor.POSITIVE + ", " + MessageColor.NEUTRAL + df.format(l.getZ()) + MessageColor.POSITIVE + ") in " + MessageColor.NEUTRAL + RUtils.getMVWorldName(l.getWorld()));
            }
        }
        cs.sendMessage(MessageColor.POSITIVE + "=====================");
        return true;
    }
}
