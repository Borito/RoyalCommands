package org.royaldev.royalcommands.rcommands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.spawninfo.Attributes;
import org.royaldev.royalcommands.spawninfo.Attributes.Attribute;
import org.royaldev.royalcommands.spawninfo.Attributes.Attribute.Builder;
import org.royaldev.royalcommands.spawninfo.Attributes.AttributeType;
import org.royaldev.royalcommands.spawninfo.Attributes.Operation;

import java.util.UUID;

public class CmdAttributes extends BaseCommand {

    public CmdAttributes(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    private void sendAttributes(CommandSender cs) {
        final StringBuilder sb = new StringBuilder();
        for (AttributeTypes at : AttributeTypes.values())
            sb.append(MessageColor.NEUTRAL).append(at.name()).append(MessageColor.RESET).append(", ");
        cs.sendMessage(sb.substring(0, sb.length() - 4)); // "&r, "
    }

    @Override
    public boolean runCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player)) {
            cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
            return true;
        }
        if (args.length < 1) {
            this.sendAttributes(cs);
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
                    for (Attribute a : attr.values()) {
                        if (!a.getUUID().equals(uuid)) continue;
                        attr.remove(a);
                    }
                    hand = attr.getStack();
                } else if (parts[1].equalsIgnoreCase("name")) {
                    for (Attribute a : attr.values()) {
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
            Operation o;
            try {
                o = Operation.valueOf(parts[1]);
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
            if (parts[4].equalsIgnoreCase("gen") || parts[4].equalsIgnoreCase("generate")) uuid = UUID.randomUUID();
            else {
                try {
                    uuid = UUID.fromString(parts[4]);
                } catch (IllegalArgumentException e) {
                    continue;
                }
            }
            Builder b = Attribute.newBuilder();
            b.name(parts[3]).operation(o).type(ats.getAttributeType()).amount(amount).uuid(uuid);
            attr.add(b.build());
            hand = attr.getStack();
        }
        p.setItemInHand(hand);
        cs.sendMessage(MessageColor.POSITIVE + "All attributes applied.");
        return true;
    }

    private static enum AttributeTypes {
        ATTACK_DAMAGE(AttributeType.GENERIC_ATTACK_DAMAGE),
        FOLLOW_RANGE(AttributeType.GENERIC_FOLLOW_RANGE),
        KNOCKBACK_RESISTANCE(AttributeType.GENERIC_KNOCKBACK_RESISTANCE),
        MAX_HEALTH(AttributeType.GENERIC_MAX_HEALTH),
        MOVEMENT_SPEED(AttributeType.GENERIC_MOVEMENT_SPEED);

        private final AttributeType at;

        AttributeTypes(AttributeType at) {
            this.at = at;
        }

        private AttributeType getAttributeType() {
            return at;
        }
    }
}
