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
import org.royaldev.royalcommands.spawninfo.Attributes;

import java.util.UUID;

@ReflectCommand
public class CmdAttributes implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdAttributes(RoyalCommands instance) {
        plugin = instance;
    }

    private void sendAttributes(CommandSender cs) {
        final StringBuilder sb = new StringBuilder();
        for (AttributeTypes at : AttributeTypes.values())
            sb.append(MessageColor.NEUTRAL).append(at.name()).append(MessageColor.RESET).append(", ");
        cs.sendMessage(sb.substring(0, sb.length() - 4)); // "&r, "
    }

    private static enum AttributeTypes {
        ATTACK_DAMAGE(Attributes.AttributeType.GENERIC_ATTACK_DAMAGE),
        FOLLOW_RANGE(Attributes.AttributeType.GENERIC_FOLLOW_RANGE),
        KNOCKBACK_RESISTANCE(Attributes.AttributeType.GENERIC_KNOCKBACK_RESISTANCE),
        MAX_HEALTH(Attributes.AttributeType.GENERIC_MAX_HEALTH),
        MOVEMENT_SPEED(Attributes.AttributeType.GENERIC_MOVEMENT_SPEED);

        private final Attributes.AttributeType at;

        AttributeTypes(Attributes.AttributeType at) {
            this.at = at;
        }

        private Attributes.AttributeType getAttributeType() {
            return at;
        }
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("attributes")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.attributes")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
                return true;
            }
            if (args.length < 1) {
                sendAttributes(cs);
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            final Player p = (Player) cs;
            ItemStack hand = p.getItemInHand();
            if (hand.getType() == Material.AIR) {
                cs.sendMessage(MessageColor.NEGATIVE + "You cannot apply attributes to air!");
                return true;
            }
            for (String arg : args) {
                Attributes attr = new Attributes(hand);
                if (arg.equalsIgnoreCase("clear")) {
                    attr.clear();
                    hand = attr.getStack();
                    break;
                }
                if (arg.toLowerCase().startsWith("remove:")) {
                    final String[] parts = arg.split(":");
                    if (parts.length < 3) continue;
                    if (parts[1].equalsIgnoreCase("uuid")) {
                        UUID uuid;
                        try {
                            uuid = UUID.fromString(parts[2]);
                        } catch (IllegalArgumentException e) {
                            continue;
                        }
                        for (Attributes.Attribute a : attr.values()) {
                            if (!a.getUUID().equals(uuid)) continue;
                            attr.remove(a);
                        }
                        hand = attr.getStack();
                    } else if (parts[1].equalsIgnoreCase("name")) {
                        for (Attributes.Attribute a : attr.values()) {
                            if (!a.getName().equalsIgnoreCase(parts[2])) continue;
                            attr.remove(a);
                        }
                        hand = attr.getStack();
                    }
                }
                final String[] parts = arg.split(",");
                if (parts.length < 5) continue;
                AttributeTypes ats;
                try {
                    ats = AttributeTypes.valueOf(parts[0]);
                } catch (IllegalArgumentException e) {
                    continue;
                }
                Attributes.Operation o;
                try {
                    o = Attributes.Operation.valueOf(parts[1]);
                } catch (IllegalArgumentException e) {
                    continue;
                }
                double amount;
                try {
                    amount = Double.parseDouble(parts[2]);
                } catch (NumberFormatException e) {
                    continue;
                }
                UUID uuid;
                try {
                    uuid = UUID.fromString(parts[4]);
                } catch (IllegalArgumentException e) {
                    continue;
                }
                Attributes.Attribute.Builder b = Attributes.Attribute.newBuilder();
                b.name(parts[3]).operation(o).type(ats.getAttributeType()).amount(amount).uuid(uuid);
                attr.add(b.build());
                hand = attr.getStack();
            }
            p.setItemInHand(hand);
            cs.sendMessage(MessageColor.POSITIVE + "All attributes applied.");
            return true;
        }
        return false;
    }

}
