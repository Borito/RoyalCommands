package org.royaldev.royalcommands.rcommands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RoyalCommands;

@ReflectCommand
public class CmdPotion extends BaseCommand {

    public CmdPotion(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    private PotionEffect getPotionEffect(String s) {
        String[] comp = s.split(",");
        if (comp.length < 3)
            throw new IllegalArgumentException(MessageColor.NEUTRAL + s + MessageColor.NEGATIVE + ": Not a complete effect! (name,duration,amplifier(,ambient)) Skipping.");
        PotionEffectType pet = PotionEffectType.getByName(comp[0].toUpperCase());
        if (pet == null) {
            throw new IllegalArgumentException(MessageColor.NEUTRAL + s + MessageColor.NEGATIVE + " is not a valid potion effect type.");
        }
        int duration, amplifier;
        try {
            duration = Integer.parseInt(comp[1]);
            amplifier = Integer.parseInt(comp[2]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(MessageColor.NEGATIVE + "Duration or amplifier was not an integer.");
        }
        boolean ambient = comp.length > 3 && comp[3].equalsIgnoreCase("true");
        return new PotionEffect(pet, duration, amplifier, ambient);
    }

    private void sendPotionTypes(CommandSender cs) {
        StringBuilder sb = new StringBuilder();
        for (PotionEffectType pet : PotionEffectType.values()) {
            if (pet == null) continue;
            sb.append(MessageColor.NEUTRAL);
            sb.append(pet.getName());
            sb.append(MessageColor.RESET);
            sb.append(", ");
        }
        cs.sendMessage(sb.substring(0, sb.length() - 4)); // "&r, " = 4
    }

    @Override
    public boolean runCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player)) {
            cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
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
            cs.sendMessage(MessageColor.NEGATIVE + "That item's meta is not potion meta.");
            return true;
        }
        PotionMeta pm = (PotionMeta) hand.getItemMeta();
        if (hand.getType() != Material.POTION) {
            cs.sendMessage(MessageColor.NEGATIVE + "You must be holding a water bottle or potion!");
            return true;
        }
        for (String arg : args) {
            if (arg.startsWith("main:")) {
                arg = arg.substring(5);
                PotionEffectType pet = PotionEffectType.getByName(arg.toUpperCase());
                if (pet == null) {
                    cs.sendMessage(MessageColor.NEUTRAL + arg + MessageColor.NEGATIVE + " is not a potion effect type.");
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
                    cs.sendMessage(MessageColor.NEUTRAL + arg + MessageColor.NEGATIVE + " is not a potion effect type.");
                    continue;
                }
                pm.removeCustomEffect(pet);
            } else if (arg.startsWith("clear")) {
                if (pm.clearCustomEffects()) cs.sendMessage(MessageColor.POSITIVE + "Cleared all custom effects.");
                else cs.sendMessage(MessageColor.NEGATIVE + "Couldn't clear any custom effects.");
            }
        }
        hand.setItemMeta(pm);
        cs.sendMessage(MessageColor.POSITIVE + "You have set the effects on that potion.");
        return true;
    }
}
