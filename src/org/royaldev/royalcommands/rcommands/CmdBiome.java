package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdBiome implements CommandExecutor {

    RoyalCommands plugin;

    public CmdBiome(RoyalCommands plugin) {
        this.plugin = plugin;
    }

    public void sendBiomeList(CommandSender cs) {
        String biomes = "";
        for (Biome b : Biome.values())
            biomes = (biomes.equals("")) ? biomes.concat(b.name() + ChatColor.WHITE) : biomes.concat(", " + b.name() + ChatColor.WHITE);
        cs.sendMessage(biomes);
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("biome")) {
            if (!plugin.isAuthorized(cs, "rcmds.biome")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(ChatColor.RED + "This command is only available to players!");
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(ChatColor.BLUE + "Biomes:");
                sendBiomeList(cs);
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            final Player p = (Player) cs;
            final Biome b;
            try {
                b = Biome.valueOf(args[0].toUpperCase());
            } catch (Exception e) {
                p.sendMessage(ChatColor.RED + "No such biome!");
                sendBiomeList(p);
                return true;
            }
            final Chunk c = p.getLocation().getChunk();
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    for (int x = 0; x <= 16; x++)
                        for (int y = 0; y <= 256; y++)
                            for (int z = 0; z <= 16; z++) {
                                Block bl = c.getBlock(x, y, z);
                                bl.setBiome(b);
                            }
                    p.sendMessage(ChatColor.BLUE + "Set biome of this chunk to " + ChatColor.GRAY + b.name().toLowerCase().replace("_", " ") + ChatColor.BLUE + ".");
                }
            };
            plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, r);
            return true;
        }
        return false;
    }

}
