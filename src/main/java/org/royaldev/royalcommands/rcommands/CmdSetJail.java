package org.royaldev.royalcommands.rcommands;

import org.royaldev.royalcommands.MessageColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.io.File;
import java.io.IOException;

public class CmdSetJail implements CommandExecutor {

    private RoyalCommands plugin;

    public CmdSetJail(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("setjail")) {
            if (!plugin.isAuthorized(cs, "rcmds.setjail")) {
                RUtils.dispNoPerms(cs);
                return true;
            }

            if (!(cs instanceof Player)) {
                cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
                return true;
            }

            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }

            Player p = (Player) cs;

            double locX = p.getLocation().getX();
            double locY = p.getLocation().getY();
            double locZ = p.getLocation().getZ();
            float locYaw = p.getLocation().getYaw();
            float locPitch = p.getLocation().getPitch();
            String locW = p.getWorld().getName();

            File pconfl = new File(plugin.getDataFolder() + File.separator + "jails.yml");
            if (!pconfl.exists()) {
                try {
                    pconfl.createNewFile();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            FileConfiguration pconf = YamlConfiguration.loadConfiguration(pconfl);
            pconf.set("jails." + args[0] + ".set", true);
            pconf.set("jails." + args[0] + ".x", locX);
            pconf.set("jails." + args[0] + ".y", locY);
            pconf.set("jails." + args[0] + ".z", locZ);
            pconf.set("jails." + args[0] + ".pitch", locPitch);
            pconf.set("jails." + args[0] + ".yaw", locYaw);
            pconf.set("jails." + args[0] + ".w", locW);
            try {
                pconf.save(pconfl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            p.sendMessage(MessageColor.POSITIVE + "Jail \"" + MessageColor.NEUTRAL + args[0] + MessageColor.POSITIVE + "\" set.");
            return true;
        }
        return false;
    }

}
