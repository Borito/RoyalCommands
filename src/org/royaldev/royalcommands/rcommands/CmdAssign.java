package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.royaldev.royalcommands.PConfManager;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.util.ArrayList;

public class CmdAssign implements CommandExecutor {

    RoyalCommands plugin;

    public CmdAssign(RoyalCommands instance) {
        this.plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("assign")) {
            if (!plugin.isAuthorized(cs, "rcmds.assign")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(ChatColor.RED + "This command is only available to players!");
                return true;
            }
            if (args.length < 1) {
                Player p = (Player) cs;
                ItemStack hand = p.getItemInHand();
                if (hand == null || hand.getTypeId() == 0) {
                    cs.sendMessage(ChatColor.RED + "You can't remove commands from air!");
                    return true;
                }
                PConfManager.setPValStringList(p, null, "assign." + hand.getTypeId());
                p.sendMessage(ChatColor.BLUE + "All commands removed from " + ChatColor.GRAY + hand.getType().toString().toLowerCase().replace("_", " ") + ChatColor.BLUE + ".");
                return true;
            }
            Player p = (Player) cs;
            ItemStack hand = p.getItemInHand();
            if (hand == null || hand.getTypeId() == 0) {
                cs.sendMessage(ChatColor.RED + "You can't assign commands to air!");
                return true;
            }
            java.util.List<String> cmds = PConfManager.getPValStringList(p, "assign." + hand.getTypeId());
            if (cmds == null) {
                cmds = new ArrayList<String>();
                cmds.add(plugin.getFinalArg(args, 0));
            } else cmds.add(plugin.getFinalArg(args, 0));
            PConfManager.setPValStringList(p, cmds, "assign." + hand.getTypeId());
            String message = (plugin.getFinalArg(args, 0).toLowerCase().startsWith("c:"))
                    ? ChatColor.BLUE + "Added message " + ChatColor.GRAY + plugin.getFinalArg(args, 0).substring(2) + ChatColor.BLUE + " to that item."
                    : ChatColor.BLUE + "Added command " + ChatColor.GRAY + "/" + plugin.getFinalArg(args, 0) + ChatColor.BLUE + " to that item.";
            p.sendMessage(message);
            return true;
        }
        return false;
    }

}
