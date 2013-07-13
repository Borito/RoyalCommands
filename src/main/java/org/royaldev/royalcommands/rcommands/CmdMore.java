package org.royaldev.royalcommands.rcommands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdMore implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdMore(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("more")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.more")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
                return true;
            }
            Player p = (Player) cs;
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("all")) {
                    for (ItemStack i : p.getInventory()) {
                        if (i == null || i.getType().equals(Material.AIR)) continue;
                        i.setAmount(64);
                    }
                    cs.sendMessage(MessageColor.POSITIVE + "You have been given more of every item in your inventory.");
                    return true;
                } else if (args[0].equalsIgnoreCase("inventory")) {
                    ItemStack hand = p.getItemInHand();
                    if (hand.getTypeId() == 0) {
                        cs.sendMessage(MessageColor.NEGATIVE + "You can't spawn air!");
                        return true;
                    }
                    hand.setAmount(64);
                    for (int slot = 0; slot < p.getInventory().getSize(); slot++) {
                        ItemStack i = p.getInventory().getItem(slot);
                        if (i != null && i.getType() != Material.AIR) continue;
                        p.getInventory().setItem(slot, hand);
                    }
                    cs.sendMessage(MessageColor.POSITIVE + "Filled inventory with the item in hand!");
                    return true;
                } else {
                    cs.sendMessage(MessageColor.NEGATIVE + "Invalid argument!");
                    return true;
                }
            }
            ItemStack hand = p.getItemInHand();
            if (hand.getTypeId() == 0) {
                cs.sendMessage(MessageColor.NEGATIVE + "You can't spawn air!");
                return true;
            }
            hand.setAmount(64);
            cs.sendMessage(MessageColor.POSITIVE + "You have been given more of the item in hand.");
            return true;
        }
        return false;
    }

}
