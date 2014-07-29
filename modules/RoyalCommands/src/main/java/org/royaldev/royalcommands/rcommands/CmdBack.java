package org.royaldev.royalcommands.rcommands;

import mkremins.fanciful.FancyMessage;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.royaldev.royalcommands.Config;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@ReflectCommand
public class CmdBack extends BaseCommand {

    private static final Map<UUID, List<Location>> backdb = new HashMap<>();
    private final DecimalFormat df = new DecimalFormat("0.00");

    public CmdBack(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    /**
     * Adds a location to the /back stack. If there are too many locations, the last (oldest) one will be removed.
     *
     * @param p     Player to add a location for
     * @param toAdd Location to add
     */
    public static void addBackLocation(Player p, Location toAdd) {
        if (Config.disabledBackWorlds.contains(toAdd.getWorld().getName())) return;
        int maxStack = Config.maxBackStack;
        synchronized (backdb) {
            List<Location> backs = backdb.get(p.getUniqueId());
            if (backs == null) backs = new ArrayList<>();
            // remove last location if needed
            if (backs.size() > 0 && backs.size() >= maxStack) backs.remove(backs.size() - 1);
            backs.add(0, toAdd);
            backdb.put(p.getUniqueId(), backs);
        }
    }

    @Override
    public boolean runCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player)) {
            cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
            return true;
        }
        Player p = (Player) cs;
        if (!backdb.containsKey(p.getUniqueId())) {
            cs.sendMessage(MessageColor.NEGATIVE + "You have no place to go back to!");
            return true;
        }
        if (label.equalsIgnoreCase("backs")) {
            final List<Location> backs = backdb.get(p.getUniqueId());
            cs.sendMessage(MessageColor.NEUTRAL + "/back locations:");
            for (int i = 0; i < backs.size(); i++) {
                Location l = backs.get(i);
                if (l == null) continue;
                Block b = l.getBlock().getRelative(BlockFace.DOWN);
                // @formatter:off
                RUtils.addCommandTo(new FancyMessage("  ")
                        .then(i + 1 + ": ")
                            .color(MessageColor.NEUTRAL._())
                        .then("on ")
                            .color(MessageColor.POSITIVE._())
                        .then(RUtils.getItemName(b.getType()))
                            .color(MessageColor.NEUTRAL._())
                            .itemTooltip(new ItemStack(b.getType()))
                        .then(" in ")
                            .color(MessageColor.POSITIVE._())
                        .then(RUtils.getFriendlyEnumName(b.getBiome()))
                            .color(MessageColor.NEUTRAL._())
                        .then(" (")
                            .color(MessageColor.POSITIVE._())
                        .then(l.getWorld().getName())
                            .color(MessageColor.NEUTRAL._())
                        .then(", ")
                            .color(MessageColor.POSITIVE._())
                        .then(this.df.format(l.getX()))
                            .color(MessageColor.NEUTRAL._())
                        .then(", ")
                            .color(MessageColor.POSITIVE._())
                        .then(this.df.format(l.getY()))
                            .color(MessageColor.NEUTRAL._())
                        .then(", ")
                            .color(MessageColor.POSITIVE._())
                        .then(this.df.format(l.getZ()))
                            .color(MessageColor.NEUTRAL._())
                        .then(")")
                            .color(MessageColor.POSITIVE._()), "/back " + (i + 1))
                        .send(cs);
                // @formatter:on
            }
            return true;
        }
        int index = 0;
        try {
            if (args.length > 0) {
                index = Integer.parseInt(args[0]);
                index--;
            }
        } catch (NumberFormatException e) {
            cs.sendMessage(MessageColor.NEGATIVE + "The back number was not a valid number!");
            return true;
        }
        List<Location> backs = backdb.get(p.getUniqueId());
        if (index < 0 || index >= backs.size()) {
            cs.sendMessage(MessageColor.NEGATIVE + "No such back number!");
            return true;
        }
        String error = RUtils.teleport(p, backs.get(index));
        if (!error.isEmpty()) {
            p.sendMessage(MessageColor.NEGATIVE + error);
            return true;
        }
        p.sendMessage(MessageColor.POSITIVE + "Returning to your previous location.");
        return true;
    }
}
