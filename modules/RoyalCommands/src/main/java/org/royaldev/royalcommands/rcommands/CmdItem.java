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
public class CmdItem extends BaseCommand {

    public CmdItem(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player)) {
            cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
            return true;
        }
        if (args.length < 1) {
            cs.sendMessage(cmd.getDescription());
            return false;
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
        final Material m = toInv.getType();
        if (m == Material.AIR) {
            cs.sendMessage(MessageColor.NEGATIVE + "You cannot spawn air!");
            return true;
        }
        if (Config.blockedItems.contains(m.name()) && !this.ah.isAuthorized(cs, "rcmds.allowed.item." + m.name())) {
            cs.sendMessage(MessageColor.NEGATIVE + "You are not allowed to spawn that item!");
            this.plugin.getLogger().warning(cs.getName() + " was denied access to the command!");
            return true;
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
            .then(p.getName())
                .color(MessageColor.NEUTRAL._())
            .then(".")
                .color(MessageColor.POSITIVE._())
            .send(cs);
        // @formatter:on
        if (Config.itemSpawnTag) toInv = RUtils.applySpawnLore(RUtils.setItemStackSpawned(toInv, cs.getName(), true));
        HashMap<Integer, ItemStack> left = p.getInventory().addItem(toInv);
        if (!left.isEmpty() && Config.dropExtras) {
            for (ItemStack i : left.values()) {
                if (Config.itemSpawnTag) i = RUtils.applySpawnLore(RUtils.setItemStackSpawned(i, cs.getName(), true));
                p.getWorld().dropItemNaturally(p.getLocation(), i);
            }
        }
        return true;
    }
}
