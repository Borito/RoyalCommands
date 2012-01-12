package tk.royalcraf.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tk.royalcraf.royalcommands.PConfManager;
import tk.royalcraf.royalcommands.RoyalCommands;

public class Reply implements CommandExecutor {

    RoyalCommands plugin;

    public Reply(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label,
                             String[] args) {
        if (cmd.getName().equalsIgnoreCase("reply")) {
            if (!plugin.isAuthorized(cs, "rcmds.reply")) {
                cs.sendMessage(ChatColor.RED
                        + "You don't have permission for that!");
                plugin.log.warning("[RoyalCommands] " + cs.getName()
                        + " was denied access to the command!");
                return true;
            } else {
                if (args.length < 1) {
                    cs.sendMessage(cmd.getDescription());
                    return false;
                } else {
                    if (!(cs instanceof Player)) {
                        cs.sendMessage(ChatColor.RED
                                + "This command is only available to players!");
                        return true;
                    } else {
                        Player p = (Player) cs;
                        if (Message.replydb.containsKey(p)) {
                            Player t = (Player) Message.replydb.get(p);
                            if (!Message.replydb.containsKey(t)) {
                                Message.replydb.put(t, cs);
                            } else if (Message.replydb.containsKey(t)) {
                                if (Message.replydb.get(t) != cs) {
                                    Message.replydb.remove(t);
                                    Message.replydb.put(t, cs);
                                }
                            }
                            p.sendMessage(ChatColor.GRAY + "[" + ChatColor.BLUE
                                    + "You" + ChatColor.GRAY + " -> "
                                    + ChatColor.BLUE + t.getName()
                                    + ChatColor.GRAY + "] "
                                    + plugin.getFinalArg(args, 0));
                            t.sendMessage(ChatColor.GRAY + "[" + ChatColor.BLUE
                                    + p.getName() + ChatColor.GRAY + " -> "
                                    + ChatColor.BLUE + "You" + ChatColor.GRAY
                                    + "] " + plugin.getFinalArg(args, 0));
                            for (int i = 0; i < plugin.getServer()
                                    .getOnlinePlayers().length; i++) {
                                if (PConfManager.getPValBoolean(plugin
                                        .getServer().getOnlinePlayers()[i],
                                        "spy")) {
                                    plugin.getServer().getOnlinePlayers()[i]
                                            .sendMessage(ChatColor.GRAY
                                                    + "["
                                                    + ChatColor.BLUE
                                                    + cs.getName()
                                                    + ChatColor.GRAY
                                                    + " -> "
                                                    + ChatColor.BLUE
                                                    + t.getName()
                                                    + ChatColor.GRAY
                                                    + "] "
                                                    + plugin.getFinalArg(args,
                                                    0));
                                }
                            }
                            return true;
                        } else {
                            cs.sendMessage(ChatColor.RED
                                    + "You have no one to reply to!");
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

}
