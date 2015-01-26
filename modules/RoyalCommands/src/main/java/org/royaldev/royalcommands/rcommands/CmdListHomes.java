package org.royaldev.royalcommands.rcommands;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.AuthorizationHandler.PermType;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.rcommands.home.Home;
import org.royaldev.royalcommands.shaded.mkremins.fanciful.FancyMessage;
import org.royaldev.royalcommands.wrappers.player.MemoryRPlayer;
import org.royaldev.royalcommands.wrappers.player.RPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
        final RPlayer rp = MemoryRPlayer.getRPlayer(t);
        final Map<World, List<Home>> sortedHomes = new HashMap<>();
        for (final Home h : rp.getHomes()) {
            if (h == null) continue;
            final Location l = h.getLocation();
            if (l == null) continue;
            final World w = l.getWorld();
            final List<Home> homes = sortedHomes.containsKey(w) ? sortedHomes.get(w) : new ArrayList<Home>();
            homes.add(h);
            sortedHomes.put(h.getLocation().getWorld(), homes);
        }
        final int homeLimit = rp.getHomeLimit();
        cs.sendMessage(MessageColor.POSITIVE + "Homes (" + MessageColor.NEUTRAL + rp.getHomes().size() + MessageColor.POSITIVE + "/" + MessageColor.NEUTRAL + ((homeLimit < 0) ? "Unlimited" : homeLimit) + MessageColor.POSITIVE + "):");
        for (final Map.Entry<World, List<Home>> entry : sortedHomes.entrySet()) {
            final World w = entry.getKey();
            cs.sendMessage(MessageColor.POSITIVE + "  Homes for " + MessageColor.NEUTRAL + (w == null ? "an invalid world" : w.getName()) + MessageColor.POSITIVE + ":");
            final FancyMessage fm = new FancyMessage("    ");
            final Iterator<Home> homes = entry.getValue().iterator();
            while (homes.hasNext()) {
                final Home home = homes.next();
                fm.then(home.getName()).color(MessageColor.NEUTRAL._()).command("/home " + home.getFullName());
                if (homes.hasNext()) fm.then(MessageColor.RESET + ", "); // it's not a color OR a style
            }
            fm.send(cs);
        }
        return true;
    }
}
