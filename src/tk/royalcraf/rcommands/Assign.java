package tk.royalcraf.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import tk.royalcraf.royalcommands.PConfManager;
import tk.royalcraf.royalcommands.RoyalCommands;

public class Assign implements CommandExecutor {

    RoyalCommands plugin;

    public Assign(RoyalCommands instance) {
        this.plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label,
                             String[] args) {
        if (cmd.getName().equalsIgnoreCase("assign")) {
            if (!plugin.isAuthorized(cs, "rcmds.assign")) {
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
            if (args.length < 1) {
                Player p = (Player) cs;
                ItemStack hand = p.getItemInHand();
                if (hand == null || hand.getTypeId() == 0) {
                    cs.sendMessage(ChatColor.RED
                            + "You can't remove commands from air!");
                    return true;
                }
                PConfManager.setPValString(p, null, "assign."
                        + hand.getTypeId());
                p.sendMessage(ChatColor.BLUE
                        + "All commands removed from that item.");
                return true;
            }
            Player p = (Player) cs;
            ItemStack hand = p.getItemInHand();
            if (hand == null || hand.getTypeId() == 0) {
                cs.sendMessage(ChatColor.RED
                        + "You can't assign commands to air!");
                return true;
            }
            PConfManager.setPValString(p,
                    plugin.getFinalArg(args, 0), "assign." + hand.getTypeId());
            p.sendMessage(ChatColor.BLUE + "Added command " + ChatColor.GRAY
                    + "/" + plugin.getFinalArg(args, 0) + ChatColor.BLUE
                    + " to that item.");
            return true;
        }
        return false;
    }

}
