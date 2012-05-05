package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdMegaStrike implements CommandExecutor {

    RoyalCommands plugin;

    public CmdMegaStrike(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("megastrike")) {
            if (!plugin.isAuthorized(cs, "rcmds.megastrike")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            Player p = (Player) cs;
            if (args.length < 1) {
                BlockIterator b = new BlockIterator(p, 0);
                if (!b.hasNext()) {
                    cs.sendMessage(ChatColor.RED + "Cannot megastrike there!");
                    return true;
                }
                Block bb = b.next();
                while (b.hasNext()) {
                    if (b.next().getTypeId() == 0) continue;
                    bb = b.next();
                    break;
                }
                for (int i = 0; i < 15; i++) p.getWorld().strikeLightning(bb.getLocation());
                return true;
            } else {
                if (!plugin.isAuthorized(cs, "rcmds.others.megastrike")) {
                    cs.sendMessage(ChatColor.RED + "You don't have permission for that!");
                    return true;
                }
                Player target = plugin.getServer().getPlayer(args[0]);
                if (target == null || plugin.isVanished(target)) {
                    cs.sendMessage(ChatColor.RED + "That player does not exist!");
                    return true;
                }
                cs.sendMessage(ChatColor.BLUE + "Megasmiting " + ChatColor.GRAY + target.getName() + ChatColor.BLUE + ".");
                target.sendMessage(ChatColor.RED + "You have been megasmited by " + ChatColor.GRAY + cs.getName() + ChatColor.RED + ".");
                for (int i = 0; i < 15; i++) p.getWorld().strikeLightning(target.getLocation());
                return true;

            }
        }
        return false;
    }

}
