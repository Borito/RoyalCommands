/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.royaldev.royalcommands.rcommands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

@ReflectCommand
public class CmdRename extends BaseCommand {

    public CmdRename(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] args) {
        if (args.length < 1) {
            cs.sendMessage(cmd.getDescription());
            return false;
        }
        if (!(cs instanceof Player)) {
            cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
            return true;
        }
        Player p = (Player) cs;
        String newName = RUtils.colorize(RoyalCommands.getFinalArg(args, 0));
        ItemStack hand = p.getItemInHand();
        if (hand == null || hand.getType() == Material.AIR) {
            cs.sendMessage(MessageColor.NEGATIVE + "You can't rename air!");
            return true;
        }
        switch (hand.getType()) {
            case BREWING_STAND_ITEM:
            case BREWING_STAND:
            case DISPENSER:
            case DROPPER:
            case FURNACE:
            case BURNING_FURNACE:
            case HOPPER:
            case HOPPER_MINECART:
            case STORAGE_MINECART:
            case MONSTER_EGG:
            case CHEST:
                if (newName.length() > 32) newName = newName.substring(0, 32);
                cs.sendMessage(MessageColor.POSITIVE + "The new name has been shortened to " + MessageColor.NEUTRAL + newName + MessageColor.POSITIVE + " to prevent crashes.");
        }
        ItemStack is = RUtils.renameItem(hand, newName);
        p.setItemInHand(is);
        cs.sendMessage(MessageColor.POSITIVE + "Renamed your " + MessageColor.NEUTRAL + RUtils.getItemName(is) + MessageColor.POSITIVE + " to " + MessageColor.NEUTRAL + newName + MessageColor.POSITIVE + ".");
        return true;
    }
}
