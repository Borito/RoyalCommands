package org.royaldev.royalcommands.rcommands;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.util.List;

@ReflectCommand
public class CmdWorld extends BaseCommand {

    public CmdWorld(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player)) {
            cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
            return true;
        }
        if (args.length < 1) {
            List<World> ws = this.plugin.getServer().getWorlds();
            String worlds = "";
            for (World w : ws)
                worlds = (worlds.equals("")) ? worlds.concat(MessageColor.NEUTRAL + RUtils.getMVWorldName(w)) : worlds.concat(MessageColor.RESET + ", " + MessageColor.NEUTRAL + RUtils.getMVWorldName(w));
            cs.sendMessage(MessageColor.POSITIVE + "Worlds: " + worlds);
            return true;
        }
        World w = this.plugin.getServer().getWorld(args[0]);
        if (w == null) {
            cs.sendMessage(MessageColor.NEGATIVE + "That world does not exist!");
            List<World> ws = this.plugin.getServer().getWorlds();
            String worlds = "";
            for (World w2 : ws) {
                if (worlds.equals("")) worlds = worlds.concat(MessageColor.NEUTRAL + RUtils.getMVWorldName(w2));
                else
                    worlds = worlds.concat(MessageColor.RESET + ", " + MessageColor.NEUTRAL + RUtils.getMVWorldName(w2));
            }
            cs.sendMessage(MessageColor.POSITIVE + "Worlds: " + worlds);
            return true;
        }
        Player p = (Player) cs;
        p.sendMessage(MessageColor.POSITIVE + "Teleporting you to world " + MessageColor.NEUTRAL + RUtils.getMVWorldName(w) + MessageColor.POSITIVE + ".");
        String error = RUtils.teleport(p, CmdSpawn.getWorldSpawn(w));
        if (!error.isEmpty()) {
            p.sendMessage(MessageColor.NEGATIVE + error);
            return true;
        }
        return true;
    }
}
