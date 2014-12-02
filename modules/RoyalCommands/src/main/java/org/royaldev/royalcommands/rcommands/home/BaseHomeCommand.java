package org.royaldev.royalcommands.rcommands.home;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.AuthorizationHandler.PermType;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.rcommands.TabCommand;
import org.royaldev.royalcommands.wrappers.RPlayer;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseHomeCommand extends TabCommand {

    protected final Flag<String> playerFlag = new Flag<>(String.class, "player", "p");
    private final boolean consoleAllowed;

    protected BaseHomeCommand(final RoyalCommands instance, final String name, final boolean checkPermissions, final Short[] cts, final boolean consoleAllowed) {
        super(instance, name, checkPermissions, cts);
        this.consoleAllowed =  consoleAllowed;
    }

    protected abstract boolean continueCommand(final CommandSender cs, final Player p, final Command cmd, final String label, final String[] eargs, final CommandArguments ca, final Home home);

    @Override
    protected List<String> customList(final CommandSender cs, final Command cmd, final String label, final String[] args, final String arg) {
        if (!(cs instanceof Player)) return new ArrayList<>();
        final RPlayer rp = RPlayer.getRPlayer((Player) cs);
        return new ArrayList<>(rp.getHomeNames());
    }

    @Override
    protected boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] eargs, final CommandArguments ca) {
        final boolean isPlayer = cs instanceof Player;
        if (!isPlayer && !this.consoleAllowed) {
            cs.sendMessage(ChatColor.RED + "This command is only available to players.");
            return true;
        }
        final Player p = isPlayer ? (Player) cs : null;
        String homeName = eargs.length < 1 ? "home" : eargs[0];
        if (ca.hasContentFlag(this.playerFlag)) {
            homeName = ca.getFlag(this.playerFlag).getValue() + ":" + homeName;
        }
        if (!isPlayer && !homeName.contains(":")) {
            cs.sendMessage(MessageColor.NEGATIVE + "You must specify a player.");
            return true;
        }
        final Home home = Home.fromNotation(p == null ? null : p.getUniqueId(), homeName);
        if (home == null) {
            cs.sendMessage(MessageColor.NEGATIVE + "The home " + MessageColor.NEUTRAL + homeName + MessageColor.NEGATIVE + " does not exist.");
            return true;
        }
        if (isPlayer && !home.getUUID().equals(p.getUniqueId()) && !this.ah.isAuthorized(cs, cmd, PermType.OTHERS)) {
            cs.sendMessage(MessageColor.NEGATIVE + "You are not allowed to use other players' homes.");
            return true;
        }
        return this.continueCommand(cs, p, cmd, label, eargs, ca, home);
    }
}
