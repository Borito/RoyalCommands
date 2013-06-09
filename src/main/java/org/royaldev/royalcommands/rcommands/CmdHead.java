package org.royaldev.royalcommands.rcommands;

import org.royaldev.royalcommands.MessageColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
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
            if (!plugin.ah.isAuthorized(cs, "rcmds.head")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                cs.sendMessage(cmd.getUsage());
                return true;
            }
            Player p = (Player) cs;
            ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
            if (!(head.getItemMeta() instanceof SkullMeta)) {
                cs.sendMessage(MessageColor.NEGATIVE + "The head had incorrect item metadata!");
                return true;
            }
            SkullMeta sm = (SkullMeta) head.getItemMeta();
            OfflinePlayer t = plugin.getServer().getPlayer(args[0]);
            if (t == null) t = plugin.getServer().getOfflinePlayer(args[0]);
            if (!t.getName().equalsIgnoreCase(p.getName()) && !plugin.ah.isAuthorized(cs, "rcmds.others.head")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!t.getName().equalsIgnoreCase(p.getName()) && plugin.ah.isAuthorized(t, "rcmds.exempt.head")) {
                cs.sendMessage(MessageColor.NEGATIVE + "You cannot spawn that player's head!");
                return true;
            }
            sm.setOwner(t.getName());
            head.setItemMeta(sm);
            head = RUtils.renameItem(head, "Head of " + t.getName());
            p.getInventory().addItem(head);
            cs.sendMessage(MessageColor.POSITIVE + "You have been given the head of " + MessageColor.NEUTRAL + t.getName() + MessageColor.POSITIVE + ".");
            return true;
        }
        return false;
    }

}
