package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.configuration.ConfManager;

@ReflectCommand
public class CmdSetSpawn extends BaseCommand {

    public CmdSetSpawn(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player)) {
            cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
            return true;
        }
        Player p = (Player) cs;
        String group = (args.length > 0) ? "." + args[0].toLowerCase() : "";
        ConfManager spawns = ConfManager.getConfManager("spawns.yml");
        double x = p.getLocation().getX();
        double y = p.getLocation().getY();
        double z = p.getLocation().getZ();
        String w = p.getWorld().getName();
        if (group.equals("")) p.getWorld().setSpawnLocation((int) x, (int) y, (int) z);
        spawns.setLocation("spawns." + w + group, p.getLocation());
        p.getWorld().setSpawnLocation((int) x, (int) y, (int) z);
        String forGroup = (group.isEmpty()) ? "" : " for group " + MessageColor.NEUTRAL + group + MessageColor.POSITIVE;
        cs.sendMessage(MessageColor.POSITIVE + "The spawn point of " + MessageColor.NEUTRAL + RUtils.getMVWorldName(p.getWorld()) + MessageColor.POSITIVE + " is set" + forGroup + ".");
        return true;
    }
}
