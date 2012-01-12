package tk.royalcraf.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tk.royalcraf.royalcommands.RoyalCommands;

public class FixChunk implements CommandExecutor {

    RoyalCommands plugin;

    public FixChunk(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label,
                             String[] args) {
        if (cmd.getName().equalsIgnoreCase("fixchunk")) {
            if (!plugin.isAuthorized(cs, "rcmds.fixchunk")) {
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
            Chunk c = p.getLocation().getChunk();
            p.getWorld().unloadChunk(c);
            p.getWorld().loadChunk(c);
            cs.sendMessage(ChatColor.BLUE
                    + "The chunk you're standing in has been reloaded!");
            return true;
        }
        return false;
    }

}
