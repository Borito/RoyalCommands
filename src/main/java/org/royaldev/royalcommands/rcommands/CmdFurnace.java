package org.royaldev.royalcommands.rcommands;

import org.bukkit.block.Furnace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.FurnaceInventory;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.util.HashMap;

public class CmdFurnace implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdFurnace(RoyalCommands instance) {
        plugin = instance;
    }

    public HashMap<Player, Furnace> furnacedb = new HashMap<Player, Furnace>();

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("furnace")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.furnace")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                cs.sendMessage(cmd.getUsage().replace("<command>", label));
                return true;
            }
            Player p = (Player) cs;
            String command = args[0].toLowerCase();
            if (command.equals("set")) {
                if (!(RUtils.getTarget(p).getState() instanceof Furnace)) {
                    cs.sendMessage(MessageColor.NEGATIVE + "That's not a furnace!");
                    return true;
                }
                Furnace f = (Furnace) RUtils.getTarget(p).getState();
                furnacedb.put(p, f);
                cs.sendMessage(MessageColor.POSITIVE + "Furnace set.");
                return true;
            } else if (command.equals("show")) {
                if (!furnacedb.containsKey(p)) {
                    cs.sendMessage(MessageColor.NEGATIVE + "You must first set a furnace!");
                    return true;
                }
                Furnace f = furnacedb.get(p);
                if (!(f.getBlock().getState() instanceof Furnace)) {
                    cs.sendMessage(MessageColor.NEGATIVE + "The furnace is no longer there!");
                    return true;
                }
                f = (Furnace) f.getBlock().getState();
                FurnaceInventory fi = f.getInventory();
                p.openInventory(fi);
                cs.sendMessage(MessageColor.POSITIVE + "Opened your furnace for you.");
                return true;
            } else {
                cs.sendMessage(MessageColor.NEGATIVE + "Try " + MessageColor.NEUTRAL + "/" + label + MessageColor.POSITIVE + ".");
                return true;
            }
        }
        return false;
    }

}
