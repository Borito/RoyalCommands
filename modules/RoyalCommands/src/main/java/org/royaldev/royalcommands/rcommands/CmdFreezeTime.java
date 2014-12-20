package org.royaldev.royalcommands.rcommands;

import org.apache.commons.lang.BooleanUtils;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.configuration.Configuration;

@ReflectCommand
public class CmdFreezeTime extends BaseCommand {

    public CmdFreezeTime(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] args) {
        if (args.length < 1) {
            cs.sendMessage(cmd.getDescription());
            return false;
        }
        World w = this.plugin.getServer().getWorld(args[0]);
        if (w == null) {
            cs.sendMessage(MessageColor.NEGATIVE + "No such world!");
            return true;
        }
        Configuration cm = RoyalCommands.wm.getConfig();
        boolean isFrozen = cm.getBoolean("worlds." + w.getName() + ".freezetime", false);
        cm.set("worlds." + w.getName() + ".freezetime", !isFrozen);
        cm.set("worlds." + w.getName() + ".frozenat", w.getTime());
        cs.sendMessage(MessageColor.POSITIVE + "Turned freezetime on " + MessageColor.NEUTRAL + RUtils.getMVWorldName(w) + MessageColor.POSITIVE + " to " + MessageColor.NEUTRAL + BooleanUtils.toStringOnOff(!isFrozen) + MessageColor.POSITIVE + ".");
        return true;
    }
}
