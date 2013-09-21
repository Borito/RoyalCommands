package org.royaldev.royalcommands.rcommands;

import org.apache.commons.lang.BooleanUtils;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.configuration.ConfManager;

@ReflectCommand
public class CmdFreezeTime implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdFreezeTime(RoyalCommands instance) {
        plugin = instance;
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("freezetime")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.freezetime")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            World w = plugin.getServer().getWorld(args[0]);
            if (w == null) {
                cs.sendMessage(MessageColor.NEGATIVE + "No such world!");
                return true;
            }
            ConfManager cm = RoyalCommands.wm.getConfig();
            boolean isFrozen = cm.getBoolean("worlds." + w.getName() + ".freezetime", false);
            cm.set("worlds." + w.getName() + ".freezetime", !isFrozen);
            cm.set("worlds." + w.getName() + ".frozenat", w.getTime());
            cs.sendMessage(MessageColor.POSITIVE + "Turned freezetime on " + MessageColor.NEUTRAL + RUtils.getMVWorldName(w) + MessageColor.POSITIVE + " to " + MessageColor.NEUTRAL + BooleanUtils.toStringOnOff(!isFrozen) + MessageColor.POSITIVE + ".");
            return true;
        }
        return false;
    }

}
