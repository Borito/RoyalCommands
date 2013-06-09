package org.royaldev.royalcommands.rcommands;

import org.royaldev.royalcommands.MessageColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.util.HashMap;

public class CmdInvsee implements CommandExecutor {

    private RoyalCommands plugin;

    public CmdInvsee(RoyalCommands instance) {
        this.plugin = instance;
    }

    public HashMap<Player, ItemStack[]> invseedb = new HashMap<Player, ItemStack[]>();

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("invsee")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.invsee")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
                return true;
            }
            Player p = (Player) cs;
            if (args.length < 1) {
                if (invseedb.containsKey(p)) {
                    ItemStack[] pi = invseedb.get(p);
                    invseedb.remove(p);
                    p.getInventory().setContents(pi);
                    p.sendMessage(MessageColor.POSITIVE + "Your inventory was restored.");
                    return true;
                }
                cs.sendMessage(cmd.getDescription());
                cs.sendMessage(cmd.getUsage());
                return true;
            }
            Player t = plugin.getServer().getPlayer(args[0]);
            if (t == null || plugin.isVanished(t, cs)) {
                cs.sendMessage(MessageColor.NEGATIVE + "That player does not exist!");
                return true;
            }
            if (invseedb.containsKey(p)) {
                ItemStack[] pi = invseedb.get(p);
                invseedb.remove(p);
                p.getInventory().setContents(pi);
                p.sendMessage(MessageColor.POSITIVE + "Your inventory was restored.");
                return true;
            }
            invseedb.put(p, p.getInventory().getContents());
            ItemStack[] ti = t.getInventory().getContents();
            p.getInventory().clear();
            p.getInventory().setContents(ti);
            p.sendMessage(MessageColor.POSITIVE + "Copied inventory of " + MessageColor.NEUTRAL + t.getName() + MessageColor.POSITIVE + ".");
            return true;
        }
        return false;
    }
}
