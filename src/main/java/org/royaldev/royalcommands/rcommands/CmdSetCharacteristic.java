package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdSetCharacteristic implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdSetCharacteristic(RoyalCommands instance) {
        plugin = instance;
    }

    private Integer toInt(Object o) {
        try {
            return Integer.parseInt(o.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Float toFloat(Object o) {
        try {
            return Float.parseFloat(o.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("setcharacteristic")) {
            if (!plugin.isAuthorized(cs, "rcmds.setcharacteristic")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length > 0 && args[0].equalsIgnoreCase("help")) {
                cs.sendMessage(ChatColor.BLUE + "/" + label + " help:");
                cs.sendMessage(ChatColor.BLUE + "/" + label + ChatColor.GRAY + " [player] maxhealth [half-hearts]");
                cs.sendMessage(ChatColor.BLUE + "/" + label + ChatColor.GRAY + " [player] maxair [ticks]");
                cs.sendMessage(ChatColor.BLUE + "/" + label + ChatColor.GRAY + " [player] exp [percentage]");
                cs.sendMessage(ChatColor.BLUE + "/" + label + ChatColor.GRAY + " [player] canpickupitems [boolean]");
                return true;
            }
            if (args.length < 3) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            Player p = plugin.getServer().getPlayer(args[0]);
            if (p == null || plugin.isVanished(p, cs)) {
                cs.sendMessage(ChatColor.RED + "That player does not exist!");
                return true;
            }
            String subcommand = args[1];
            if (subcommand.equalsIgnoreCase("maxhealth")) {
                Integer i = toInt(args[2]);
                if (i == null) {
                    cs.sendMessage(ChatColor.RED + "The max health was not a number!");
                    return true;
                }
                if (i < 1) {
                    cs.sendMessage(ChatColor.RED + "Cannot set maxhealth to less than 1.");
                    return true;
                }
                p.setMaxHealth(i);
                cs.sendMessage(ChatColor.BLUE + "Set max health to " + ChatColor.GRAY + i + ChatColor.BLUE + ".");
            } else if (subcommand.equalsIgnoreCase("maxair")) {
                Integer i = toInt(args[2]);
                if (i == null) {
                    cs.sendMessage(ChatColor.RED + "The max air was not a number!");
                    return true;
                }
                p.setMaximumAir(i);
                cs.sendMessage(ChatColor.BLUE + "Set max air to " + ChatColor.GRAY + i + ChatColor.BLUE + ".");
            } else if (subcommand.equalsIgnoreCase("exp")) {
                Float f = toFloat(args[2]);
                if (f == null) {
                    cs.sendMessage(ChatColor.RED + "The exp was not a number!");
                    return true;
                }
                f /= 100F;
                if (f < 0F || f > 1F) {
                    cs.sendMessage(ChatColor.RED + "Exp must be a percentage between 0 and 100.");
                    return true;
                }
                p.setExp(f);
                cs.sendMessage(ChatColor.BLUE + "Set exp to " + ChatColor.GRAY + (f *= 100F) + "%" + ChatColor.BLUE + ".");
            } else if (subcommand.equalsIgnoreCase("canpickupitems")) {
                p.setCanPickupItems(args[2].equalsIgnoreCase("true"));
                cs.sendMessage(ChatColor.BLUE + "Set can pick up items to " + ChatColor.GRAY + Boolean.toString(p.getCanPickupItems()) + ChatColor.BLUE + ".");
            } else cs.sendMessage(ChatColor.RED + "No such subcommand!");
            return true;
        }
        return false;
    }

}
