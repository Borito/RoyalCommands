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

import java.util.ArrayList;
import java.util.List;

@ReflectCommand
public class CmdMail implements CommandExecutor {
    private final RoyalCommands plugin;

    public CmdMail(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    public static String[] splitFirst(String source, String splitter) {
        final List<String> rv = new ArrayList<String>();
        int last = 0;
        int next = source.indexOf(splitter, last);
        if (next != -1) {
            rv.add(source.substring(last, next));
            last = next + splitter.length();
        }
        if (last < source.length()) rv.add(source.substring(last, source.length()));
        return rv.toArray(new String[rv.size()]);
    }

    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("mail")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.mail")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            if (args[0].equalsIgnoreCase("read")) {
                if (!(cs instanceof Player)) {
                    cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
                    return true;
                }
                Player p = (Player) cs;
                final PConfManager pcm = PConfManager.getPConfManager(p);
                final List<String> mails = pcm.getStringList("mail");
                if (mails.isEmpty()) {
                    cs.sendMessage(MessageColor.NEGATIVE + "You have no mail!");
                    return true;
                }
                for (String mail : mails) {
                    final String[] splitMail = splitFirst(mail, ": ");
                    if (splitMail.length < 2) continue;
                    final String user = splitMail[0];
                    final String msg = splitMail[1];
                    cs.sendMessage(MessageColor.POSITIVE + "[" + MessageColor.NEUTRAL + user + MessageColor.POSITIVE + "] " + MessageColor.NEUTRAL + msg);
                }
                cs.sendMessage(MessageColor.POSITIVE + "Use " + MessageColor.NEUTRAL + "/mail clear" + MessageColor.POSITIVE + " to clear your mailbox.");
            } else if (args[0].equalsIgnoreCase("clear")) {
                if (!(cs instanceof Player)) {
                    cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
                    return true;
                }
                Player p = (Player) cs;
                final PConfManager pcm = PConfManager.getPConfManager(p);
                if (!pcm.exists()) pcm.createFile();
                if (pcm.isSet("mail")) pcm.set("mail", null);
                cs.sendMessage(MessageColor.POSITIVE + "Your mailbox has been cleared.");
            } else if (args[0].equalsIgnoreCase("send")) {
                if (!plugin.ah.isAuthorized(cs, "rcmds.mail.send")) {
                    RUtils.dispNoPerms(cs);
                    return true;
                }
                if (args.length < 3) {
                    cs.sendMessage(cmd.getDescription());
                    return false;
                }
                OfflinePlayer op = plugin.getServer().getOfflinePlayer(args[1]);
                if (!op.hasPlayedBefore()) {
                    cs.sendMessage(MessageColor.NEGATIVE + "That player does not exist!");
                    return true;
                }
                final String senderName = cs.getName();
                final String newmail = senderName + ": " + RoyalCommands.getFinalArg(args, 2);
                final PConfManager pcm = PConfManager.getPConfManager(op);
                List<String> mail = pcm.getStringList("mail");
                mail.add(newmail);
                pcm.set("mail", mail);
                cs.sendMessage(MessageColor.POSITIVE + "Mail has been sent.");
            } else {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            return true;
        }
        return false;
    }
}
