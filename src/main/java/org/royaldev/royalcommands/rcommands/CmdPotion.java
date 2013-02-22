package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdPotion implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdPotion(RoyalCommands instance) {
        plugin = instance;
    }

    private void sendPotionTypes(CommandSender cs) {
        StringBuilder sb = new StringBuilder();
        for (PotionEffectType pet : PotionEffectType.values()) {
            if (pet == null) continue;
            sb.append(ChatColor.GRAY);
            sb.append(pet.getName());
            sb.append(ChatColor.RESET);
            sb.append(", ");
        }
        cs.sendMessage(sb.substring(0, sb.length() - 4)); // "&r, " = 4
    }

    private PotionEffect getPotionEffect(String s) {
        String[] comp = s.split(",");
        if (comp.length < 3)
            throw new IllegalArgumentException(ChatColor.GRAY + s + ChatColor.RED + ": Not a complete effect! (name,duration,amplifier(,ambient)) Skipping.");
        PotionEffectType pet = PotionEffectType.getByName(comp[0].toUpperCase());
        if (pet == null) {
            throw new IllegalArgumentException(ChatColor.GRAY + s + ChatColor.RED + " is not a valid potion effect type.");
        }
        int duration, amplifier;
        try {
            duration = Integer.parseInt(comp[1]);
            amplifier = Integer.parseInt(comp[2]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ChatColor.RED + "Duration or amplifier was not an integer.");
        }
        boolean ambient = comp.length > 3 && comp[3].equalsIgnoreCase("true");
        return new PotionEffect(pet, duration, amplifier, ambient);
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("potion")) {
            if (!plugin.isAuthorized(cs, "rcmds.potion")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(ChatColor.RED + "This command is only available to players!");
                return true;
            }
            if (args.length < 1) {
                sendPotionTypes(cs);
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            Player p = (Player) cs;
            ItemStack hand = p.getItemInHand();
            if (!(hand.getItemMeta() instanceof PotionMeta)) {
                cs.sendMessage(ChatColor.RED + "That item's meta is not potion meta.");
                return true;
            }
            PotionMeta pm = (PotionMeta) hand.getItemMeta();
            if (hand.getType() != Material.POTION) {
                cs.sendMessage(ChatColor.RED + "You must be holding a water bottle or potion!");
                return true;
            }
            for (String arg : args) {
                if (arg.startsWith("main:")) {
                    arg = arg.substring(5);
                    PotionEffectType pet = PotionEffectType.getByName(arg.toUpperCase());
                    if (pet == null) {
                        cs.sendMessage(ChatColor.GRAY + arg + ChatColor.RED + " is not a potion effect type.");
                        continue;
                    }
                    pm.setMainEffect(pet);
                } else if (arg.startsWith("effect:")) {
                    arg = arg.substring(7);
                    PotionEffect pe;
                    try {
                        pe = getPotionEffect(arg);
                    } catch (IllegalArgumentException e) {
                        cs.sendMessage(e.getMessage());
                        continue;
                    }
                    pm.addCustomEffect(pe, true);
                } else if (arg.startsWith("remove:")) {
                    arg = arg.substring(7);
                    PotionEffectType pet = PotionEffectType.getByName(arg.toUpperCase());
                    if (pet == null) {
                        cs.sendMessage(ChatColor.GRAY + arg + ChatColor.RED + " is not a potion effect type.");
                        continue;
                    }
                    pm.removeCustomEffect(pet);
                } else if (arg.startsWith("clear")) {
                    if (pm.clearCustomEffects()) cs.sendMessage(ChatColor.BLUE + "Cleared all custom effects.");
                    else cs.sendMessage(ChatColor.RED + "Couldn't clear any custom effects.");
                }
            }
            hand.setItemMeta(pm);
            cs.sendMessage(ChatColor.BLUE + "You have set the effects on that potion.");
            return true;
        }
        return false;
    }

}
