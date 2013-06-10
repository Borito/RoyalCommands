package org.royaldev.royalcommands.rcommands;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.configuration.PConfManager;

public class CmdGod implements CommandExecutor {

    private RoyalCommands plugin;

    public CmdGod(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("god")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.god")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                if (!(cs instanceof Player)) {
                    cs.sendMessage(cmd.getDescription());
                    cs.sendMessage(cmd.getUsage().replace("<command>", label));
                    return true;
                }
                Player t = (Player) cs;
                PConfManager pcm = PConfManager.getPConfManager(t);
                t.setHealth(t.getMaxHealth());
                t.setFoodLevel(20);
                t.setSaturation(20F);
                if (!pcm.getBoolean("godmode")) {
                    cs.sendMessage(MessageColor.POSITIVE + "You have enabled godmode for yourself.");
                    pcm.set("godmode", true);
                    return true;
                } else {
                    cs.sendMessage(MessageColor.POSITIVE + "You have disabled godmode for yourself.");
                    pcm.set("godmode", false);
                    return true;
                }
            }
            if (args.length > 0) {
                if (!plugin.ah.isAuthorized(cs, "rcmds.others.god")) {
                    cs.sendMessage(MessageColor.NEGATIVE + "You don't have permission for that!");
                    plugin.log.warning("[RoyalCommands] " + cs.getName() + " was denied access to the command!");
                    return true;
                }
                Player t = plugin.getServer().getPlayer(args[0]);
                PConfManager pcm = PConfManager.getPConfManager(t);
                if (t != null) {
                    if (!pcm.getBoolean("godmode")) {
                        if (!pcm.exists()) {
                            cs.sendMessage(MessageColor.NEGATIVE + "That player doesn't exist!");
                            return true;
                        }
                        t.setHealth(t.getMaxHealth());
                        t.setFoodLevel(20);
                        t.setSaturation(20F);
                        t.sendMessage(MessageColor.POSITIVE + "The player " + MessageColor.NEUTRAL + cs.getName() + MessageColor.POSITIVE + " has enabled godmode for you!");
                        cs.sendMessage(MessageColor.POSITIVE + "You have enabled godmode for " + MessageColor.NEUTRAL + t.getName() + MessageColor.POSITIVE + ".");
                        pcm.set("godmode", true);
                        return true;
                    } else {
                        t.setHealth(t.getMaxHealth());
                        t.setFoodLevel(20);
                        t.setSaturation(20F);
                        t.sendMessage(MessageColor.NEGATIVE + "The player " + MessageColor.NEUTRAL + cs.getName() + MessageColor.NEGATIVE + " has disabled godmode for you!");
                    }
                    cs.sendMessage(MessageColor.POSITIVE + "You have disabled godmode for " + MessageColor.NEUTRAL + t.getName() + MessageColor.POSITIVE + ".");
                    pcm.set("godmode", false);
                    return true;
                }
            }
            OfflinePlayer t2 = plugin.getServer().getOfflinePlayer(args[0]);
            PConfManager pcm = PConfManager.getPConfManager(t2);
            if (!pcm.getBoolean("godmode")) {
                if (!pcm.exists()) {
                    cs.sendMessage(MessageColor.NEGATIVE + "That player doesn't exist!");
                    return true;
                }
                if (t2.isOnline()) {
                    Player pl = (Player) t2;
                    pl.setHealth(pl.getMaxHealth());
                    pl.setFoodLevel(20);
                    pl.setSaturation(20F);
                    pl.sendMessage(MessageColor.POSITIVE + "The player " + MessageColor.NEUTRAL + cs.getName() + MessageColor.POSITIVE + " has enabled godmode for you!");
                }
                cs.sendMessage(MessageColor.POSITIVE + "You have enabled godmode for " + MessageColor.NEUTRAL + t2.getName() + MessageColor.POSITIVE + ".");
                pcm.set("godmode", true);
                return true;
            } else {
                if (t2.isOnline()) {
                    Player pl = (Player) t2;
                    pl.setHealth(pl.getMaxHealth());
                    pl.setFoodLevel(20);
                    pl.setSaturation(20F);
                    pl.sendMessage(MessageColor.NEGATIVE + "The player " + MessageColor.NEUTRAL + cs.getName() + MessageColor.NEGATIVE + " has disabled godmode for you!");
                }
                cs.sendMessage(MessageColor.POSITIVE + "You have disabled godmode for " + MessageColor.NEUTRAL + t2.getName() + MessageColor.POSITIVE + ".");
                pcm.set("godmode", false);
                return true;
            }
        }
        return false;
    }

}
