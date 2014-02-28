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

@ReflectCommand
public class CmdSetArmor implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdSetArmor(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("setarmor")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.setarmor")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
                return true;
            }
            final Player player;
            if (args.length > 1) {
                if (!plugin.ah.isAuthorized(cs, "rcmds.others.setarmor")) {
                    RUtils.dispNoPerms(cs);
                    return true;
                }
                player = plugin.getServer().getPlayer(args[1]);
                if (player == null || plugin.isVanished(player, cs)) {
                    cs.sendMessage(MessageColor.NEGATIVE + "That player does not exist!");
                    return true;
                }
                if (plugin.ah.isAuthorized(player, "rcmds.exempt.setarmor")) {
                    cs.sendMessage(MessageColor.NEGATIVE + "You can't modify that player's armor!");
                    return true;
                }
            } else player = (Player) cs;
            String set = args[0];
            ItemStack[] diamond = new ItemStack[]{new ItemStack(Material.DIAMOND_HELMET), new ItemStack(Material.DIAMOND_CHESTPLATE), new ItemStack(Material.DIAMOND_LEGGINGS), new ItemStack(Material.DIAMOND_BOOTS)};
            ItemStack[] gold = new ItemStack[]{new ItemStack(Material.GOLD_HELMET), new ItemStack(Material.GOLD_CHESTPLATE), new ItemStack(Material.GOLD_LEGGINGS), new ItemStack(Material.GOLD_BOOTS)};
            ItemStack[] iron = new ItemStack[]{new ItemStack(Material.IRON_HELMET), new ItemStack(Material.IRON_CHESTPLATE), new ItemStack(Material.IRON_LEGGINGS), new ItemStack(Material.IRON_BOOTS)};
            ItemStack[] leather = new ItemStack[]{new ItemStack(Material.LEATHER_HELMET), new ItemStack(Material.LEATHER_CHESTPLATE), new ItemStack(Material.LEATHER_LEGGINGS), new ItemStack(Material.LEATHER_BOOTS)};
            ItemStack[] chain = new ItemStack[]{new ItemStack(Material.CHAINMAIL_HELMET), new ItemStack(Material.CHAINMAIL_CHESTPLATE), new ItemStack(Material.CHAINMAIL_LEGGINGS), new ItemStack(Material.CHAINMAIL_BOOTS)};
            ItemStack[] none = new ItemStack[]{new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR)};
            if (set.equalsIgnoreCase("diamond")) {
                if (!plugin.ah.isAuthorized(cs, "rcmds.setarmor.diamond")) {
                    cs.sendMessage(MessageColor.NEGATIVE + "You don't have permission for that type of material!");
                    return true;
                } else {
                    player.getInventory().setArmorContents(diamond);
                    cs.sendMessage(MessageColor.POSITIVE + "Armor was set to " + set + ".");
                    return true;
                }
            } else if (set.equalsIgnoreCase("gold")) {
                if (!plugin.ah.isAuthorized(cs, "rcmds.setarmor.gold")) {
                    cs.sendMessage(MessageColor.NEGATIVE + "You don't have permission for that type of material!");
                    return true;
                } else {
                    player.getInventory().setArmorContents(gold);
                    cs.sendMessage(MessageColor.POSITIVE + "Armor was set to " + set + ".");
                    return true;
                }
            } else if (set.equalsIgnoreCase("iron")) {
                if (!plugin.ah.isAuthorized(cs, "rcmds.setarmor.iron")) {
                    cs.sendMessage(MessageColor.NEGATIVE + "You don't have permission for that type of material!");
                    return true;
                } else {
                    player.getInventory().setArmorContents(iron);
                    cs.sendMessage(MessageColor.POSITIVE + "Armor was set to " + set + ".");
                    return true;
                }
            } else if (set.equalsIgnoreCase("leather")) {
                if (!plugin.ah.isAuthorized(cs, "rcmds.setarmor.leather")) {
                    cs.sendMessage(MessageColor.NEGATIVE + "You don't have permission for that type of material!");
                    return true;
                } else {
                    player.getInventory().setArmorContents(leather);
                    cs.sendMessage(MessageColor.POSITIVE + "Armor was set to " + set + ".");
                    return true;
                }
            } else if (set.equalsIgnoreCase("chain")) {
                if (!plugin.ah.isAuthorized(cs, "rcmds.setarmor.chain")) {
                    cs.sendMessage(MessageColor.NEGATIVE + "You don't have permission for that type of material!");
                    return true;
                } else {
                    player.getInventory().setArmorContents(chain);
                    player.sendMessage(MessageColor.POSITIVE + "Armor was set to " + set + ".");
                    return true;
                }
            } else if (set.equalsIgnoreCase("none")) {
                if (!plugin.ah.isAuthorized(cs, "rcmds.setarmor.none")) {
                    cs.sendMessage(MessageColor.NEGATIVE + "You don't have permission for that type of material!");
                    return true;
                } else {
                    player.getInventory().setArmorContents(none);
                    cs.sendMessage(MessageColor.POSITIVE + "Armor was cleared.");
                    return true;
                }
            } else {
                cs.sendMessage(MessageColor.NEGATIVE + "The armor type must be diamond, gold, iron, leather, chain, or none.");
                return true;
            }
        }
        return false;
    }

}
