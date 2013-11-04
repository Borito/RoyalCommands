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
import org.royaldev.royalcommands.spawninfo.SpawnInfo;

@ReflectCommand
public class CmdSpawnInfo implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdSpawnInfo(final RoyalCommands instance) {
        plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("spawninfo")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.spawninfo")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            final Player p = (Player) cs;
            ItemStack hand = p.getItemInHand();
            if (hand.getType() == Material.AIR) {
                cs.sendMessage(MessageColor.NEGATIVE + "You must be holding something to use this command.");
                return true;
            }
            final SpawnInfo si = SpawnInfo.SpawnInfoManager.getSpawnInfo(hand);
            final String subcommand = args[0];
            if (subcommand.equalsIgnoreCase("check")) {
                cs.sendMessage(MessageColor.POSITIVE + "Spawn information on " + MessageColor.NEUTRAL + RUtils.getItemName(hand) + ":" + hand.getDurability() + MessageColor.POSITIVE + ":");
                cs.sendMessage(MessageColor.POSITIVE + "  Is spawned: " + MessageColor.NEUTRAL + ((si.isSpawned()) ? "Yes" : "No"));
                if (si.isSpawned())
                    cs.sendMessage(MessageColor.POSITIVE + "  Spawned by: " + MessageColor.NEUTRAL + si.getSpawner());
                cs.sendMessage(MessageColor.POSITIVE + "  Made with spawned items: " + MessageColor.NEUTRAL + ((si.hasComponents()) ? "Yes" : "No"));
                if (si.hasComponents()) {
                    cs.sendMessage(MessageColor.POSITIVE + "  Components:");
                    for (String component : si.getComponents())
                        cs.sendMessage(MessageColor.POSITIVE + "    - " + MessageColor.NEUTRAL + component);
                }
                return true;
            } else if (subcommand.equalsIgnoreCase("set")) {
                cs.sendMessage(MessageColor.NEGATIVE + "This mode has not yet been implemented!");
                return true;
            } else if (subcommand.equalsIgnoreCase("remove")) {
                p.setItemInHand(SpawnInfo.SpawnInfoManager.removeSpawnInfo(hand));
                cs.sendMessage(MessageColor.POSITIVE + "Spawn information removed from the item in hand.");
                return true;
            } else if (subcommand.equalsIgnoreCase("help") || subcommand.equals("?")) {
                cs.sendMessage(MessageColor.POSITIVE + "RoyalCommands SpawnInfo Help");
                cs.sendMessage(MessageColor.POSITIVE + "===============================");
                cs.sendMessage("  " + MessageColor.NEUTRAL + "/" + label + " check" + MessageColor.POSITIVE + " - Checks for and displays spawn information.");
                cs.sendMessage("  " + MessageColor.NEUTRAL + "/" + label + " set" + MessageColor.POSITIVE + " - Sets and edits spawn information.");
                cs.sendMessage("  " + MessageColor.NEUTRAL + "/" + label + " remove" + MessageColor.POSITIVE + " - Removes all spawn information from an item.");
                cs.sendMessage("  " + MessageColor.NEUTRAL + "/" + label + " help" + MessageColor.POSITIVE + " - Displays this help.");
                return true;
            } else {
                cs.sendMessage(MessageColor.NEGATIVE + "Invalid subcommand.");
                cs.sendMessage(MessageColor.NEGATIVE + "Try " + MessageColor.NEUTRAL + "/" + label + " help" + MessageColor.NEGATIVE + ".");
                return true;
            }
        }
        return false;
    }

}
