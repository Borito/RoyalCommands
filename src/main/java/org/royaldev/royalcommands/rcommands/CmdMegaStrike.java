package org.royaldev.royalcommands.rcommands;

import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.BlockIterator;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdMegaStrike implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdMegaStrike(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("megastrike")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.megastrike")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player) && args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                cs.sendMessage(cmd.getUsage().replace("<command>", label));
                return true;
            }
            if (args.length < 1) {
                Player p = (Player) cs;
                BlockIterator b = new BlockIterator(p, 0);
                if (!b.hasNext()) {
                    cs.sendMessage(MessageColor.NEGATIVE + "Cannot megastrike there!");
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
                if (!plugin.ah.isAuthorized(cs, "rcmds.others.megastrike")) {
                    cs.sendMessage(MessageColor.NEGATIVE + "You don't have permission for that!");
                    return true;
                }
                Player target = plugin.getServer().getPlayer(args[0]);
                if (target == null || plugin.isVanished(target, cs)) {
                    cs.sendMessage(MessageColor.NEGATIVE + "That player does not exist!");
                    return true;
                }
                cs.sendMessage(MessageColor.POSITIVE + "Megasmiting " + MessageColor.NEUTRAL + target.getName() + MessageColor.POSITIVE + ".");
                target.sendMessage(MessageColor.NEGATIVE + "You have been megasmited by " + MessageColor.NEUTRAL + cs.getName() + MessageColor.NEGATIVE + ".");
                for (int i = 0; i < 15; i++) target.getWorld().strikeLightning(target.getLocation());
                return true;

            }
        }
        return false;
    }

}
