package org.royaldev.royalcommands.rcommands;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.Config;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CmdBack implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdBack(RoyalCommands plugin) {
        this.plugin = plugin;
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
            List<Location> backs = backdb.get(p.getName());
            if (backs == null) backs = new ArrayList<Location>();
            // remove last location if needed
            if (backs.size() > 0 && backs.size() >= maxStack) backs.remove(backs.size() - 1);
            backs.add(0, toAdd);
            backdb.put(p.getName(), backs);
        }
    }

    private static final HashMap<String, List<Location>> backdb = new HashMap<String, List<Location>>();

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("back")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.back")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
                return true;
            }
            Player p = (Player) cs;
            if (!backdb.containsKey(p.getName())) {
                cs.sendMessage(MessageColor.NEGATIVE + "You have no place to go back to!");
                return true;
            }
            if (label.equalsIgnoreCase("backs")) {
                List<Location> backs = backdb.get(p.getName());
                cs.sendMessage(MessageColor.NEUTRAL + "/back locations:");
                for (int i = 0; i < backs.size(); i++) {
                    Location l = backs.get(i);
                    if (l == null) continue;
                    Block b = l.getBlock().getRelative(BlockFace.DOWN);
                    String onTopOf = "on " + MessageColor.NEUTRAL + RUtils.getItemName(b.getType()) + MessageColor.POSITIVE + " in " + MessageColor.NEUTRAL + RUtils.getFriendlyEnumName(b.getBiome());
                    cs.sendMessage(MessageColor.NEUTRAL + "  " + (i + 1) + ": " + MessageColor.POSITIVE + onTopOf + MessageColor.POSITIVE + " (" + MessageColor.NEUTRAL + l.getWorld().getName() + MessageColor.POSITIVE + ", " + MessageColor.NEUTRAL + l.getX() + MessageColor.POSITIVE + ", " + MessageColor.NEUTRAL + l.getY() + MessageColor.POSITIVE + ", " + MessageColor.NEUTRAL + l.getZ() + MessageColor.POSITIVE + ")");
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
            List<Location> backs = backdb.get(p.getName());
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
        return false;
    }

}
