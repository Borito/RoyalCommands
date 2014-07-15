package org.royaldev.royalcommands.rcommands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.configuration.PConfManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ReflectCommand
public class CmdSetHome extends BaseCommand {
    private final Map<UUID, Map<String, Long>> overwrites = new HashMap<>();

    public CmdSetHome(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (args.length > 0 && !this.ah.isAuthorized(cs, "rcmds.sethome.multi")) {
            RUtils.dispNoPerms(cs, MessageColor.NEGATIVE + "You don't have permission for multiple homes!");
            return true;
        }
        if (!(cs instanceof Player)) {
            cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
            return true;
        }
        Player p = (Player) cs;
        final Location l = p.getLocation();
        PConfManager pcm = PConfManager.getPConfManager(p);
        String name = "";
        if (args.length > 0) name = args[0];
        if (name.contains(":")) {
            cs.sendMessage(MessageColor.NEGATIVE + "The name of your home cannot contain \":\"!");
            return true;
        }
        int limit = RUtils.getHomeLimit(p);
        int curHomes = RUtils.getCurrentHomes(p);
        if (limit == 0) {
            RUtils.dispNoPerms(cs, MessageColor.NEGATIVE + "Your home limit is set to " + MessageColor.NEUTRAL + "0" + MessageColor.NEGATIVE + "!");
            cs.sendMessage(MessageColor.NEGATIVE + "You can't set any homes!");
            return true;
        } else if (curHomes >= limit && limit > -1) {
            RUtils.dispNoPerms(cs, MessageColor.NEGATIVE + "You've reached your max number of homes! (" + MessageColor.NEUTRAL + limit + MessageColor.NEGATIVE + ")");
            return true;
        }
        final String homePath = (name.equals("")) ? "home.home" : "home." + name;
        if (pcm.isSet(homePath)) {
            boolean overwrite = false;
            Map<String, Long> hto = this.overwrites.get(p.getUniqueId());
            if (this.overwrites.containsKey(p.getUniqueId())) {
                if (hto.containsKey(name) && hto.get(name) != null) {
                    final long expiresAt = hto.get(name) + 10000;
                    overwrite = System.currentTimeMillis() < expiresAt;
                }
            }
            if (!overwrite) {
                cs.sendMessage(MessageColor.NEGATIVE + "Warning! " + MessageColor.POSITIVE + "The home " + MessageColor.NEUTRAL + ((name.isEmpty() ? "home" : name)) + MessageColor.POSITIVE + " is already set. To re-set it, do this command again in the next ten seconds.");
                if (hto == null) hto = new HashMap<>();
                hto.put(name, System.currentTimeMillis());
                this.overwrites.put(p.getUniqueId(), hto);
                return true;
            } else {
                hto.remove(name);
                this.overwrites.put(p.getUniqueId(), hto);
            }
        }
        pcm.setLocation(homePath, l);
        if (args.length > 0) {
            p.sendMessage(MessageColor.POSITIVE + "Home " + MessageColor.NEUTRAL + name + MessageColor.POSITIVE + " set.");
        } else p.sendMessage(MessageColor.POSITIVE + "Home set.");
        return true;
    }
}
