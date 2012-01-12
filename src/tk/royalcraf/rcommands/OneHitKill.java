package tk.royalcraf.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tk.royalcraf.royalcommands.PConfManager;
import tk.royalcraf.royalcommands.RoyalCommands;

public class OneHitKill implements CommandExecutor {

    RoyalCommands plugin;

    public OneHitKill(RoyalCommands instance) {
        this.plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label,
                             String[] args) {
        if (cmd.getName().equalsIgnoreCase("onehitkill")) {
            if (!plugin.isAuthorized(cs, "rcmds.onehitkill")) {
                cs.sendMessage(ChatColor.RED
                        + "You don't have permission for that!");
                plugin.log.warning("[RoyalCommands] " + cs.getName()
                        + " was denied access to the command!");
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(ChatColor.RED
                        + "This command is only available to players!");
                return true;
            }
            Player p = (Player) cs;
            Boolean ohk = PConfManager.getPValBoolean(p, "ohk");
            if (ohk == null || !ohk) {
                PConfManager.setPValBoolean(p, true, "ohk");
                p.sendMessage(ChatColor.BLUE
                        + "You have enabled onehitkill for yourself.");
                return true;
            }
            PConfManager.setPValBoolean(p, false, "ohk");
            p.sendMessage(ChatColor.BLUE
                    + "You have disabled onehitkill for yourself.");
            return true;
        }
        return false;
    }

}
