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
public class CmdRepair extends BaseCommand {

    public CmdRepair(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player)) {
            cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
            return true;
        }
        if (args.length < 1) {
            Player p = (Player) cs;
            ItemStack hand = p.getItemInHand();
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
