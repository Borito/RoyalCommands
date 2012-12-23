package org.royaldev.royalcommands.rcommands;

import net.minecraft.server.v1_4_6.EntityPlayer;
import net.minecraft.server.v1_4_6.PlayerInventory;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_4_6.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_4_6.inventory.CraftInventory;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.util.HashMap;

public class CmdTrade implements CommandExecutor {

    private RoyalCommands plugin;

    public CmdTrade(RoyalCommands instance) {
        plugin = instance;
    }

    public static void sendTradeRequest(Player target, Player sender) {
        tradedb.put(sender.getName(), target.getName());
        target.sendMessage(ChatColor.GRAY + sender.getName() + ChatColor.BLUE + " has requested to trade with you.");
        target.sendMessage(ChatColor.BLUE + "Type " + ChatColor.GRAY + "/trade " + sender.getName() + ChatColor.BLUE + " to accept.");
    }

    public static HashMap<String, String> tradedb = new HashMap<String, String>();
    public static HashMap<HashMap<String, String>, CraftInventory> trades = new HashMap<HashMap<String, String>, CraftInventory>();

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("trade")) {
            if (!plugin.isAuthorized(cs, "rcmds.trade")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(ChatColor.RED + "This command is only available to players!");
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            Player p = (Player) cs;
            Player t = plugin.getServer().getPlayer(args[0]);
            if (t == null || plugin.isVanished(t, cs)) {
                cs.sendMessage(ChatColor.RED + "That player does not exist!");
                return true;
            }
            if (t.equals(p)) {
                cs.sendMessage(ChatColor.RED + "You can't trade with yourself!");
                return true;
            }
            CraftInventory inv;
            for (HashMap<String, String> set : trades.keySet()) {
                if ((set.containsKey(t.getName()) && set.get(t.getName()).equals(p.getName())) || (set.containsKey(p.getName()) && set.get(p.getName()).equals(t.getName()))) {
                    inv = trades.get(set);
                    p.sendMessage(ChatColor.BLUE + "Resumed trading with " + ChatColor.GRAY + t.getName() + ChatColor.BLUE + ".");
                    p.openInventory(inv);
                    return true;
                }
            }
            if (tradedb.containsKey(t.getName())) {
                EntityPlayer ep = ((CraftPlayer) p).getHandle();
                inv = new CraftInventory(new PlayerInventory(ep));
                inv.clear();
                p.sendMessage(ChatColor.BLUE + "Opened trading interface.");
                p.openInventory(inv);
                t.openInventory(inv);
                HashMap<String, String> trade = new HashMap<String, String>();
                trade.put(p.getName(), t.getName());
                trades.put(trade, inv);
                tradedb.remove(t.getName());
                return true;
            } else {
                sendTradeRequest(t, p);
                p.sendMessage(ChatColor.BLUE + "Sent a trade request to " + ChatColor.GRAY + t.getName() + ChatColor.BLUE + ".");
                return true;
            }
        }
        return false;
    }

}
