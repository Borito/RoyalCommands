package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.AuthorizationHandler.PermType;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.configuration.PConfManager;

import java.util.ArrayList;
import java.util.List;

@ReflectCommand
public class CmdIgnore extends BaseCommand {

    public CmdIgnore(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player)) {
            cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
            return true;
        }
        if (args.length < 1) {
            cs.sendMessage(cmd.getDescription());
            return false;
        }
        String name = args[0].toLowerCase();

        Player t = this.plugin.getServer().getPlayer(name);
        if (t == null || this.plugin.isVanished(t, cs)) {
            cs.sendMessage(MessageColor.NEGATIVE + "That player does not exist!");
            return true;
        }
        if (this.ah.isAuthorized(t, cmd, PermType.EXEMPT)) {
            cs.sendMessage(MessageColor.NEGATIVE + "You cannot ignore that player!");
            return true;
        }
        PConfManager pcm = PConfManager.getPConfManager(t);
        List<String> players = pcm.getStringList("ignoredby");
        if (players == null) players = new ArrayList<>();
        for (String ignored : players) {
            if (ignored.toLowerCase().equals(cs.getName().toLowerCase())) {
                players.remove(ignored);
                pcm.set("ignoredby", players);
                cs.sendMessage(MessageColor.POSITIVE + "You have stopped ignoring " + MessageColor.NEUTRAL + t.getName() + MessageColor.POSITIVE + ".");
                return true;
            }
        }
        players.add(cs.getName());
        pcm.set("ignoredby", players);
        cs.sendMessage(MessageColor.POSITIVE + "You are now ignoring " + MessageColor.NEUTRAL + t.getName() + MessageColor.POSITIVE + ".");
        return true;
    }
}
