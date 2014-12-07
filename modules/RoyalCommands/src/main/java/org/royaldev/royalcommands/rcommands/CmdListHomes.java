package org.royaldev.royalcommands.rcommands;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.AuthorizationHandler.PermType;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.rcommands.home.Home;
import org.royaldev.royalcommands.shaded.mkremins.fanciful.FancyMessage;
import org.royaldev.royalcommands.wrappers.RPlayer;

import java.util.Iterator;

@ReflectCommand
public class CmdListHomes extends BaseCommand {

    public CmdListHomes(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] args) {
        if (!(cs instanceof Player) && args.length < 1) {
            cs.sendMessage(cmd.getDescription());
            return false;
        }
        final OfflinePlayer t;
        if (args.length < 1) t = (OfflinePlayer) cs;
        else {
            if (!this.ah.isAuthorized(cs, cmd, PermType.OTHERS)) {
                cs.sendMessage(MessageColor.NEGATIVE + "You cannot list other players' homes!");
                return true;
            }
            t = RUtils.getOfflinePlayer(args[0]);
            if (this.ah.isAuthorized(t, cmd, PermType.EXEMPT)) {
                cs.sendMessage(MessageColor.NEGATIVE + "You can't list that player's homes!");
                return true;
            }
        }
        final RPlayer rp = RPlayer.getRPlayer(t);
        final Iterator<Home> homes = rp.getHomes().iterator();
        final FancyMessage fm = new FancyMessage("");
        while (homes.hasNext()) {
            final Home home = homes.next();
            fm.then(home.getName()).color(MessageColor.NEUTRAL._()).command("/home " + home.getFullName());
            if (homes.hasNext()) fm.then(MessageColor.RESET + ", "); // it's not a color OR a style
        }
        final int homeLimit = rp.getHomeLimit();
        cs.sendMessage(MessageColor.POSITIVE + "Homes (" + MessageColor.NEUTRAL + rp.getHomes().size() + MessageColor.POSITIVE + "/" + MessageColor.NEUTRAL + ((homeLimit < 0) ? "Unlimited" : homeLimit) + MessageColor.POSITIVE + "):");
        fm.send(cs);
        return true;
    }
}
