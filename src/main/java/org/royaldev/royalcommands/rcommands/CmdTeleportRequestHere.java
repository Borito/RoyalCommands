package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.util.HashMap;

public class CmdTeleportRequestHere implements CommandExecutor {

    private RoyalCommands plugin;

    public CmdTeleportRequestHere(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    /**
     * Send a teleport request to the sender's location.
     *
     * @param target Person to send the request to
     * @param sender Person sending the request
     */
    public static void sendTpRequest(Player target, CommandSender sender) {
        tprhdb.put(target, sender);
        target.sendMessage(ChatColor.GRAY + sender.getName() + ChatColor.BLUE + " has requested you to teleport to them.");
        target.sendMessage(ChatColor.BLUE + "Type " + ChatColor.GRAY + "/tpaccept" + ChatColor.BLUE + " or " + ChatColor.GRAY + "/tpdeny" + ChatColor.BLUE + ".");
    }

    public static HashMap<Player, CommandSender> tprhdb = new HashMap<Player, CommandSender>();

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("teleportrequesthere")) {
            if (!plugin.isAuthorized(cs, "rcmds.teleportrequesthere")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(ChatColor.RED + "This command is only available to players!");
                return true;
            }
            Player t = plugin.getServer().getPlayer(args[0]);
            if (t == null || plugin.isVanished(t, cs)) {
                cs.sendMessage(ChatColor.RED + "That player does not exist!");
                return true;
            }
            if (!RUtils.isTeleportAllowed(t) && !plugin.isAuthorized(cs, "rcmds.tpoverride")) {
                cs.sendMessage(ChatColor.RED + "That player has teleportation off!");
                return true;
            }
            sendTpRequest(t, cs);
            cs.sendMessage(ChatColor.BLUE + "Sent request to " + ChatColor.GRAY + t.getName() + ChatColor.BLUE + ".");
            return true;
        }
        return false;
    }

}
