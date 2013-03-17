package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdHead implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdHead(RoyalCommands instance) {
        plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("head")) {
            if (!plugin.isAuthorized(cs, "rcmds.head")) {
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
            ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
            if (!(head.getItemMeta() instanceof SkullMeta)) {
                cs.sendMessage(ChatColor.RED + "The head had incorrect item metadata!");
                return true;
            }
            SkullMeta sm = (SkullMeta) head.getItemMeta();
            Player t = plugin.getServer().getPlayer(args[0]);
            if (t == null) plugin.getServer().getOfflinePlayer(args[0]);
            if (t == null) { // satisfies IntelliJ
                cs.sendMessage(ChatColor.RED + "That player does not exist!");
                cs.sendMessage(ChatColor.RED + "Please alert the developer about this error.");
                return true;
            }
            if (!t.getName().equalsIgnoreCase(p.getName()) && !plugin.isAuthorized(cs, "rcmds.others.head")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!t.getName().equalsIgnoreCase(p.getName()) && plugin.isAuthorized(t, "rcmds.exempt.head")) {
                cs.sendMessage(ChatColor.RED + "You cannot spawn that player's head!");
                return true;
            }
            sm.setOwner(t.getName());
            head.setItemMeta(sm);
            head = RUtils.renameItem(head, "Head of " + t.getName());
            p.getInventory().addItem(head);
            cs.sendMessage(ChatColor.BLUE + "You have been given the head of " + ChatColor.GRAY + t.getName() + ChatColor.BLUE + ".");
            return true;
        }
        return false;
    }

}
