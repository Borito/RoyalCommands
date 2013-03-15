package org.royaldev.royalcommands.rcommands;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdEffect implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdEffect(RoyalCommands instance) {
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

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("effect")) {
            if (!plugin.isAuthorized(cs, "rcmds.effect")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 2) {
                sendPotionTypes(cs);
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            Player t = plugin.getServer().getPlayer(args[0]);
            if (t == null || plugin.isVanished(t, cs)) {
                cs.sendMessage(ChatColor.RED + "That player does not exist!");
                return true;
            }
            if (!t.getName().equals(cs.getName()) && !plugin.isAuthorized(cs, "rcmds.others.effect")) {
                cs.sendMessage(ChatColor.RED + "You cannot apply effects to other players!");
                return true;
            }
            if (!t.getName().equals(cs.getName()) && plugin.isAuthorized(t, "rcmds.exempt.effect")) {
                cs.sendMessage(ChatColor.RED + "You cannot apply effects to that player!");
                return true;
            }
            args = (String[]) ArrayUtils.subarray(args, 1, args.length);
            for (String arg : args) {
                String[] parts = arg.split(",");
                if (parts.length < 3) {
                    cs.sendMessage(ChatColor.GRAY + arg + ChatColor.RED + ": Not a complete effect! (name,duration,amplifier(,ambient)) Skipping.");
                    continue;
                }
                String name = parts[0];
                PotionEffectType pet = PotionEffectType.getByName(name.toUpperCase());
                if (pet == null) {
                    sendPotionTypes(cs);
                    cs.sendMessage(ChatColor.GRAY + arg + ChatColor.RED + ": No such potion effect type!");
                    continue;
                }
                int duration;
                int amplifier;
                boolean ambient = false;
                try {
                    duration = Integer.parseInt(parts[1]);
                    amplifier = Integer.parseInt(parts[2]);
                } catch (NumberFormatException e) {
                    cs.sendMessage(ChatColor.RED + "Duration/amplifier was not a number!");
                    continue;
                }
                if (parts.length > 3) ambient = parts[3].equalsIgnoreCase("true");
                new PotionEffect(pet, duration, amplifier, ambient).apply(t);
                cs.sendMessage(ChatColor.BLUE + "Applied effect to " + ChatColor.GRAY + t.getName() + ChatColor.BLUE + ".");
                return true;
            }
        }
        return false;
    }

}
