package tk.royalcraf.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tk.royalcraf.royalcommands.RoyalCommands;

public class Gamemode implements CommandExecutor {

    RoyalCommands plugin;

    public Gamemode(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label,
                             String[] args) {
        if (cmd.getName().equalsIgnoreCase("gamemode")) {
            if (!plugin.isAuthorized(cs, "rcmds.gamemode")) {
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
            if (p.getGameMode() == GameMode.CREATIVE) {
                p.setGameMode(GameMode.SURVIVAL);
                p.sendMessage(ChatColor.BLUE
                        + "Your game mode has been set to " + ChatColor.GRAY
                        + "survival" + ChatColor.BLUE + ".");
                return true;
            } else if (p.getGameMode() == GameMode.SURVIVAL) {
                p.setGameMode(GameMode.CREATIVE);
                p.sendMessage(ChatColor.BLUE
                        + "Your game mode has been set to " + ChatColor.GRAY
                        + "creative" + ChatColor.BLUE + ".");
                return true;
            }
        }
        return false;
    }

}
