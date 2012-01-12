package tk.royalcraf.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import tk.royalcraf.royalcommands.RoyalCommands;

public class Item implements CommandExecutor {

    RoyalCommands plugin;

    public Item(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label,
                             String[] args) {
        if (cmd.getName().equalsIgnoreCase("item")) {
            if (!plugin.isAuthorized(cs, "rcmds.item")) {
                cs.sendMessage(ChatColor.RED
                        + "You don't have permission for that!");
                plugin.log.warning("[RoyalCommands] " + cs.getName()
                        + " was denied access to the command!");
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(ChatColor.RED
                        + "This command is only available to players!");
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            if (args.length < 2) {
                Player p = (Player) cs;
                String called = args[0];
                String data = null;
                if (called.contains(":")) {
                    String[] calleds = called.split(":");
                    called = calleds[0].trim();
                    data = calleds[1].trim();
                }
                Integer iblock;
                try {
                    iblock = Integer.parseInt(called);
                } catch (Exception e) {
                    try {
                        iblock = Material.getMaterial(
                                called.trim().replace(" ", "_").toUpperCase())
                                .getId();
                    } catch (Exception e2) {
                        cs.sendMessage(ChatColor.RED
                                + "That block does not exist!");
                        return true;
                    }
                }
                if (iblock != 0) {
                    ItemStack toInv;
                    if (Material.getMaterial(iblock) == null) {
                        cs.sendMessage(ChatColor.RED + "Invalid item ID!");
                        return true;
                    }
                    if (data != null) {
                        int data2;
                        try {
                            data2 = Integer.parseInt(data);
                        } catch (Exception e) {
                            cs.sendMessage(ChatColor.RED
                                    + "The metadata was invalid!");
                            return true;
                        }
                        if (data2 < 0) {
                            cs.sendMessage(ChatColor.RED
                                    + "The metadata was invalid!");
                            return true;
                        } else {
                            toInv = new ItemStack(Material.getMaterial(iblock)
                                    .getId(), plugin.defaultStack,
                                    (short) data2);
                        }
                    } else {
                        toInv = new ItemStack(Material.getMaterial(iblock)
                                .getId(), plugin.defaultStack);
                    }
                    p.getInventory().addItem(toInv);
                    cs.sendMessage(ChatColor.BLUE
                            + "Giving "
                            + ChatColor.GRAY
                            + plugin.defaultStack
                            + ChatColor.BLUE
                            + " of "
                            + ChatColor.GRAY
                            + Material.getMaterial(iblock).toString()
                            .toLowerCase().replace("_", " ")
                            + ChatColor.BLUE + " to " + ChatColor.GRAY
                            + p.getName() + ChatColor.BLUE + ".");
                    return true;
                } else {
                    cs.sendMessage(ChatColor.RED + "You cannot spawn air!");
                    return true;
                }
            }
            if (args.length == 2) {
                Player p = (Player) cs;
                String called = args[0];
                Integer amount;
                String data = null;
                if (called.contains(":")) {
                    String[] calleds = called.split(":");
                    called = calleds[0].trim();
                    data = calleds[1].trim();
                }
                try {
                    amount = Integer.parseInt(args[1]);
                } catch (Exception e) {
                    cs.sendMessage(ChatColor.RED
                            + "The amount was not a number!");
                    return true;
                }
                /*
                     * if (amount > 64) { amount = 64; }
                     */
                if (amount < 1) {
                    amount = 1;
                }
                Integer iblock;
                try {
                    iblock = Integer.parseInt(called);
                } catch (Exception e) {
                    try {
                        iblock = Material.getMaterial(
                                called.trim().replace(" ", "_").toUpperCase())
                                .getId();
                    } catch (Exception e2) {
                        cs.sendMessage(ChatColor.RED
                                + "That block does not exist!");
                        return true;
                    }
                }
                if (iblock != 0) {
                    ItemStack toInv;
                    if (data != null) {
                        if (Material.getMaterial(iblock) == null) {
                            cs.sendMessage(ChatColor.RED + "Invalid item ID!");
                            return true;
                        }
                        int data2;
                        try {
                            data2 = Integer.parseInt(data);
                        } catch (Exception e) {
                            cs.sendMessage(ChatColor.RED
                                    + "The metadata was invalid!");
                            return true;
                        }
                        if (data2 < 0) {
                            cs.sendMessage(ChatColor.RED
                                    + "The metadata was invalid!");
                            return true;
                        } else {
                            toInv = new ItemStack(Material.getMaterial(iblock)
                                    .getId(), amount, (short) data2);
                        }
                    } else {
                        toInv = new ItemStack(Material.getMaterial(iblock)
                                .getId(), amount);
                    }
                    p.getInventory().addItem(toInv);
                    cs.sendMessage(ChatColor.BLUE
                            + "Giving "
                            + ChatColor.GRAY
                            + amount
                            + ChatColor.BLUE
                            + " of "
                            + ChatColor.GRAY
                            + Material.getMaterial(iblock).toString()
                            .toLowerCase().replace("_", " ")
                            + ChatColor.BLUE + " to " + ChatColor.GRAY
                            + p.getName() + ChatColor.BLUE + ".");
                    return true;
                } else {
                    cs.sendMessage(ChatColor.RED + "You cannot spawn air!");
                    return true;
                }
            }
        }
        return false;
    }
}
