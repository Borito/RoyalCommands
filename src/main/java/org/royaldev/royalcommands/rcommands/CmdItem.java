package org.royaldev.royalcommands.rcommands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.royaldev.royalcommands.Config;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.exceptions.InvalidItemNameException;

import java.util.HashMap;

public class CmdItem implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdItem(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("item")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.item")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                cs.sendMessage(cmd.getUsage().replace("<command>", label));
                return true;
            }
            Player p = (Player) cs;
            String item = args[0];

            int amount = Config.defaultStack;
            if (args.length == 2) {
                try {
                    amount = Integer.parseInt(args[1]);
                } catch (Exception e) {
                    cs.sendMessage(MessageColor.NEGATIVE + "The amount was not a number!");
                    return true;
                }
                if (amount < 1) {
                    cs.sendMessage(MessageColor.NEGATIVE + "Invalid amount! You must specify a positive amount.");
                    return true;
                }
            }
            ItemStack toInv;
            try {
                toInv = RUtils.getItemFromAlias(item, amount);
            } catch (InvalidItemNameException e) {
                toInv = RUtils.getItem(item, amount);
            } catch (NullPointerException e) {
                cs.sendMessage(MessageColor.NEGATIVE + "ItemNameManager was not loaded. Let an administrator know.");
                return true;
            }
            if (toInv == null) {
                cs.sendMessage(MessageColor.NEGATIVE + "Invalid item name!");
                return true;
            }
            Integer itemid = toInv.getTypeId();
            if (itemid == 0) {
                cs.sendMessage(MessageColor.NEGATIVE + "You cannot spawn air!");
                return true;
            }
            if (Config.blockedItems.contains(itemid.toString()) && !plugin.ah.isAuthorized(cs, "rcmds.allowed.item") && !plugin.ah.isAuthorized(cs, "rcmds.allowed.item." + itemid.toString())) {
                cs.sendMessage(MessageColor.NEGATIVE + "You are not allowed to spawn that item!");
                plugin.log.warning("[RoyalCommands] " + cs.getName() + " was denied access to the command!");
                return true;
            }
            HashMap<Integer, ItemStack> left = p.getInventory().addItem(toInv);
            if (!left.isEmpty() && Config.dropExtras) for (ItemStack i : left.values())
                p.getWorld().dropItemNaturally(p.getLocation(), i);
            cs.sendMessage(MessageColor.POSITIVE + "Giving " + MessageColor.NEUTRAL + amount + MessageColor.POSITIVE + " of " + MessageColor.NEUTRAL + RUtils.getItemName(Material.getMaterial(itemid)) + MessageColor.POSITIVE + " to " + MessageColor.NEUTRAL + p.getName() + MessageColor.POSITIVE + ".");
            return true;
        }
        return false;
    }
}
