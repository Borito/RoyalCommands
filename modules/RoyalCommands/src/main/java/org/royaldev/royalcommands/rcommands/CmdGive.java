package org.royaldev.royalcommands.rcommands;

import mkremins.fanciful.FancyMessage;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.royaldev.royalcommands.Config;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.exceptions.InvalidItemNameException;

import java.util.HashMap;

@ReflectCommand
public class CmdGive extends CACommand {

    public CmdGive(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    public static boolean validItem(String itemname) {
        ItemStack stack = RUtils.getItem(itemname, null);
        return stack != null;
    }

    public static boolean giveItemStandalone(CommandSender cs, Player target, String itemname, int amount) {
        if (target == null) return false;
        if (amount < 0) {
            target.sendMessage(MessageColor.NEGATIVE + "The amount must be positive!");
            return false;
        }
        ItemStack stack;
        try {
            stack = RUtils.getItemFromAlias(itemname, amount);
        } catch (InvalidItemNameException e) {
            stack = RUtils.getItem(itemname, amount);
        } catch (NullPointerException e) {
            target.sendMessage(MessageColor.NEGATIVE + "ItemNameManager was not loaded. Let an administrator know.");
            return false;
        }
        if (stack == null) {
            target.sendMessage(MessageColor.NEGATIVE + "Invalid item name!");
            return false;
        }
        Material m = stack.getType();
        if (m == Material.AIR) {
            target.sendMessage(MessageColor.NEGATIVE + "You cannot spawn air!");
            return false;
        }
        // @formatter:off
        new FancyMessage("Giving ")
                .color(MessageColor.POSITIVE._())
            .then(String.valueOf(amount))
                .color(MessageColor.NEUTRAL._())
            .then(" of ")
                .color(MessageColor.POSITIVE._())
            .then(RUtils.getItemName(m))
                .color(MessageColor.NEUTRAL._())
                .itemTooltip(stack)
            .then(" to ")
                .color(MessageColor.POSITIVE._())
            .then(target.getName())
                .color(MessageColor.NEUTRAL._())
                .formattedTooltip(RUtils.getPlayerTooltip(target))
            .then(".")
                .color(MessageColor.POSITIVE._())
            .send(target);
        // @formatter:on
        if (Config.itemSpawnTag && cs != null)
            stack = RUtils.applySpawnLore(RUtils.setItemStackSpawned(stack, cs.getName(), true));
        HashMap<Integer, ItemStack> left = target.getInventory().addItem(stack);
        if (!left.isEmpty() && Config.dropExtras) {
            for (ItemStack item : left.values()) {
                if (Config.itemSpawnTag && cs != null)
                    item = RUtils.applySpawnLore(RUtils.setItemStackSpawned(item, cs.getName(), true));
                target.getWorld().dropItemNaturally(target.getLocation(), item);
            }
        }
        return true;
    }

    @Override
    public boolean runCommand(CommandSender cs, Command cmd, String label, String[] eargs, CommandArguments ca) {
        if (eargs.length < 2) {
            cs.sendMessage(cmd.getDescription());
            return false;
        }
        final Player t = this.plugin.getServer().getPlayer(eargs[0]);
        if (t == null) {
            cs.sendMessage(MessageColor.NEGATIVE + "That player is not online!");
            return true;
        }
        int amount = Config.defaultStack;
        if (eargs.length == 3) {
            try {
                amount = Integer.parseInt(eargs[2]);
            } catch (Exception e) {
                cs.sendMessage(MessageColor.NEGATIVE + "The amount was not a number!");
                return true;
            }
        }
        if (amount < 1) {
            cs.sendMessage(MessageColor.NEGATIVE + "Invalid amount! You must specify a positive amount.");
            return true;
        }
        String name = eargs[1];
        ItemStack toInv;
        try {
            toInv = RUtils.getItemFromAlias(name, amount);
        } catch (InvalidItemNameException e) {
            toInv = RUtils.getItem(name, amount);
        } catch (NullPointerException e) {
            cs.sendMessage(MessageColor.NEGATIVE + "ItemNameManager was not loaded. Let an administrator know.");
            return true;
        }
        if (toInv == null) {
            cs.sendMessage(MessageColor.NEGATIVE + "Invalid item name!");
            return true;
        }
        Material m = toInv.getType();
        if (m == Material.AIR) {
            cs.sendMessage(MessageColor.NEGATIVE + "You cannot spawn air!");
            return true;
        }
        if (Config.blockedItems.contains(m.name()) && !this.ah.isAuthorized(cs, "rcmds.allowed.item." + m.name())) {
            cs.sendMessage(MessageColor.NEGATIVE + "You are not allowed to spawn that item!");
            this.plugin.getLogger().warning("[RoyalCommands] " + cs.getName() + " was denied access to the command!");
            return true;
        }
        toInv = CmdItem.applyMeta(toInv, ca, cs);
        if (Config.itemSpawnTag) toInv = RUtils.applySpawnLore(RUtils.setItemStackSpawned(toInv, cs.getName(), true));
        HashMap<Integer, ItemStack> left = t.getInventory().addItem(toInv);
        if (!left.isEmpty() && Config.dropExtras) {
            for (ItemStack item : left.values()) {
                if (Config.itemSpawnTag)
                    item = RUtils.applySpawnLore(RUtils.setItemStackSpawned(item, cs.getName(), true));
                t.getWorld().dropItemNaturally(t.getLocation(), item);
            }
        }
        // @formatter:off
        new FancyMessage("Giving ")
                .color(MessageColor.POSITIVE._())
            .then(String.valueOf(amount))
                .color(MessageColor.NEUTRAL._())
            .then(" of ")
                .color(MessageColor.POSITIVE._())
            .then(RUtils.getItemName(m))
                .color(MessageColor.NEUTRAL._())
                .itemTooltip(toInv)
            .then(" to ")
                .color(MessageColor.POSITIVE._())
            .then(t.getName())
                .color(MessageColor.NEUTRAL._())
                .formattedTooltip(RUtils.getPlayerTooltip(t))
            .then(".")
                .color(MessageColor.POSITIVE._())
            .send(cs);
        new FancyMessage("You have been given ")
                .color(MessageColor.POSITIVE._())
            .then(String.valueOf(amount))
                .color(MessageColor.NEUTRAL._())
            .then(" of ")
                .color(MessageColor.POSITIVE._())
            .then(RUtils.getItemName(m))
                .color(MessageColor.NEUTRAL._())
                .itemTooltip(toInv)
            .then(".")
                .color(MessageColor.POSITIVE._())
            .send(t);
        // @formatter:on
        return true;
    }
}
