package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.util.Map;

@ReflectCommand
public class CmdGetID implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdGetID(RoyalCommands instance) {
        plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("getid")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.getid")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
                return true;
            }
            Player p = (Player) cs;
            ItemStack hand = p.getItemInHand();
            int id = hand.getTypeId();
            short damage = hand.getDurability();
            byte data = hand.getData().getData();
            String name = RUtils.getItemName(hand);
            Map<Enchantment, Integer> enchants = hand.getEnchantments();
            cs.sendMessage(MessageColor.NEUTRAL + name + MessageColor.POSITIVE + ": " + MessageColor.NEUTRAL + id + MessageColor.POSITIVE + " (damage: " + MessageColor.NEUTRAL + damage + MessageColor.POSITIVE + ", materialdata: " + MessageColor.NEUTRAL + data + MessageColor.POSITIVE + ")");
            if (!enchants.isEmpty()) {
                cs.sendMessage(MessageColor.POSITIVE + "Enchantments:");
                for (Enchantment e : enchants.keySet()) {
                    int lvl = enchants.get(e);
                    cs.sendMessage(" " + MessageColor.NEUTRAL + e.getName().toLowerCase() + " " + lvl);
                }
            }
            return true;
        }
        return false;
    }
}
