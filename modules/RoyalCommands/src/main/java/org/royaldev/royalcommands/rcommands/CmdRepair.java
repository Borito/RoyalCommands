/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.royaldev.royalcommands.rcommands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

@ReflectCommand
public class CmdRepair extends TabCommand {

    public CmdRepair(final RoyalCommands instance, final String name) {
        super(instance, name, true, new Short[]{CompletionType.LIST.getShort()});
    }
	
    @Override
    protected List<String> customList(final CommandSender cs, final Command cmd, final String label, final String[] args, final String arg) {
        return new ArrayList<>(Arrays.asList("all"));
    }
	
    @Override
    public boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] args, CommandArguments ca) {
        if (!(cs instanceof Player)) {
            cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
            return true;
        }
        if (args.length < 1) {
            Player p = (Player) cs;
            ItemStack hand = p.getInventory().getItemInMainHand();
            if (hand.getType() == Material.AIR) {
                cs.sendMessage(MessageColor.NEGATIVE + "You can't repair air!");
                return true;
            }
            if (hand.getDurability() == (short) 0) {
                cs.sendMessage(MessageColor.NEGATIVE + "That doesn't need to be repaired!");
                return true;
            }
            hand.setDurability((short) 0);
            cs.sendMessage(MessageColor.POSITIVE + "Fixed your " + MessageColor.NEUTRAL + RUtils.getItemName(hand) + MessageColor.POSITIVE + ".");
            return true;
        }
        if (args.length > 0) {
            Player p = (Player) cs;
            ItemStack[] pInv = p.getInventory().getContents();
            final StringBuilder items = new StringBuilder();
            for (ItemStack aPInv : pInv) {
                if (aPInv != null && aPInv.getType() == Material.AIR && aPInv.getDurability() != (short) 0) {
                    aPInv.setDurability((short) 0);
                    items.append(MessageColor.NEUTRAL);
                    items.append(RUtils.getItemName(aPInv));
                    items.append(MessageColor.POSITIVE);
                    items.append(", ");
                }
            }
            if (items.length() > 0) {
                cs.sendMessage(MessageColor.POSITIVE + "Fixed: " + items.substring(0, items.length() - 4) + MessageColor.POSITIVE + ".");
                return true;
            }
            cs.sendMessage(MessageColor.NEGATIVE + "You have nothing to repair!");
            return true;
        }
        return true;
    }
}
