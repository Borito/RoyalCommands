package org.royaldev.royalcommands.rcommands;

import mkremins.fanciful.FancyMessage;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.AuthorizationHandler.PermType;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.configuration.PConfManager;

import java.util.Iterator;
import java.util.Map;

@ReflectCommand
public class CmdListHomes extends BaseCommand {

    public CmdListHomes(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player) && args.length < 1) {
            cs.sendMessage(cmd.getDescription());
            return false;
        }
        OfflinePlayer t;
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

        final PConfManager pcm = PConfManager.getPConfManager(t);
        if (!pcm.exists()) {
            cs.sendMessage(MessageColor.NEGATIVE + "No such player!");
            return true;
        }
        ConfigurationSection cfgs = pcm.getConfigurationSection("home");
        if (cfgs == null) {
            cs.sendMessage(MessageColor.NEGATIVE + "No homes found!");
            return true;
        }
        final Map<String, Object> opts = cfgs.getValues(false);
        if (opts.keySet().isEmpty()) {
            cs.sendMessage(MessageColor.NEGATIVE + "No homes found!");
            return true;
        }
        final FancyMessage fm = new FancyMessage("");
        final Iterator<String> homes = opts.keySet().iterator();
        while (homes.hasNext()) {
            final String home = homes.next();
            fm.then(home).color(MessageColor.NEUTRAL._()).command("/home " + home);
            if (homes.hasNext()) fm.then(MessageColor.RESET + ", "); // it's not a color OR a style
        }
        if (cs instanceof Player && cs.getName().equalsIgnoreCase(t.getName())) {
            final Player p = (Player) cs;
            final int homeLimit = RUtils.getHomeLimit(p);
            cs.sendMessage(MessageColor.POSITIVE + "Homes (" + MessageColor.NEUTRAL + RUtils.getCurrentHomes(p) + MessageColor.POSITIVE + "/" + MessageColor.NEUTRAL + ((homeLimit < 0) ? "Unlimited" : homeLimit) + MessageColor.POSITIVE + "):");
        } else cs.sendMessage(MessageColor.POSITIVE + "Homes:");
        fm.send(cs);
        return true;
    }
}
