package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.AuthorizationHandler.PermType;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.rcommands.home.Home;
import org.royaldev.royalcommands.wrappers.RPlayer;

import java.util.ArrayList;
import java.util.List;

@ReflectCommand
public class CmdHome extends TabCommand {

    private final Flag<String> playerFlag = new Flag<>(String.class, "player", "p");

    public CmdHome(final RoyalCommands instance, final String name) {
        super(instance, name, true, new Short[]{CompletionType.LIST.getShort()});
    }

    @Override
    protected List<String> customList(final CommandSender cs, final Command cmd, final String label, final String[] args, final String arg) {
        if (!(cs instanceof Player)) return new ArrayList<>(); // TODO: Console fun
        final RPlayer rp = RPlayer.getRPlayer((Player) cs);
        return new ArrayList<>(rp.getHomeNames());
    }

    @Override
    public boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] eargs, final CommandArguments ca) {
        if (!(cs instanceof Player)) {
            cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
            return true;
        }
        final Player p = (Player) cs;
        String homeName = eargs.length < 1 ? "home" : eargs[0];
        if (ca.hasContentFlag(this.playerFlag)) {
            homeName = ca.getFlag(this.playerFlag).getValue() + ":" + homeName;
        }
        final Home home = Home.fromNotation(p.getUniqueId(), homeName);
        if (home == null) {
            cs.sendMessage(MessageColor.NEGATIVE + "The home " + MessageColor.NEUTRAL + homeName + MessageColor.NEGATIVE + " does not exist.");
            return true;
        }
        if (!home.getUUID().equals(p.getUniqueId()) && !this.ah.isAuthorized(cs, cmd, PermType.OTHERS)) {
            cs.sendMessage(MessageColor.NEGATIVE + "You are not allowed to use other players' homes.");
            return true;
        }
        final String error = RUtils.teleport(p, home.getLocation());
        if (!error.isEmpty()) cs.sendMessage(MessageColor.NEGATIVE + error);
        else {
            cs.sendMessage(MessageColor.POSITIVE + "Teleported to home " + MessageColor.NEUTRAL + home.getName() + MessageColor.POSITIVE + " for " + MessageColor.NEUTRAL + home.getRPlayer().getName() + MessageColor.POSITIVE + ".");
        }
        return true;
    }
}
