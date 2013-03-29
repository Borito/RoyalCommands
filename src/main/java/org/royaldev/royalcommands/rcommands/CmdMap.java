package org.royaldev.royalcommands.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapView;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public class CmdMap implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdMap(RoyalCommands instance) {
        plugin = instance;
    }

    private boolean subcommandMatches(String subcommand, String... matchAgainst) {
        for (String aMatchAgainst : matchAgainst) if (subcommand.equalsIgnoreCase(aMatchAgainst)) return true;
        return false;
    }

    private String combineEnums(Enum[] es) {
        StringBuilder sb = new StringBuilder();
        for (Enum e : es) {
            sb.append(ChatColor.GRAY);
            sb.append(e.name());
            sb.append(ChatColor.RESET);
            sb.append(", ");
        }
        return sb.substring(0, sb.length() - 4);
    }

    private void updateMap(Player p, MapView mv) {
        p.sendMap(mv);
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("map")) {
            if (!plugin.isAuthorized(cs, "rcmds.map")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(ChatColor.RED + "This command is only available to players!");
                return true;
            }
            Player p = (Player) cs;
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            String subcommand = args[0].toLowerCase();
            if (subcommandMatches(subcommand, "help", "?")) {
                cs.sendMessage(ChatColor.GRAY + "/" + label + ChatColor.BLUE + " help:");
                cs.sendMessage("  " + ChatColor.BLUE + "/" + label + ChatColor.GRAY + " scale [scaletype]" + ChatColor.BLUE + " - " + ChatColor.GRAY + "Sets the scale of the map in hand.");
                cs.sendMessage("  " + ChatColor.BLUE + "/" + label + ChatColor.GRAY + " position [x] [z]" + ChatColor.BLUE + " - " + ChatColor.GRAY + "Sets the center position of the map in hand.");
                cs.sendMessage("  " + ChatColor.BLUE + "/" + label + ChatColor.GRAY + " info" + ChatColor.BLUE + " - " + ChatColor.GRAY + "Displays information about the map in hand.");
                cs.sendMessage("  " + ChatColor.BLUE + "/" + label + ChatColor.GRAY + " help" + ChatColor.BLUE + " - " + ChatColor.GRAY + "Displays this help.");
                //cs.sendMessage("  " + ChatColor.BLUE + "/" + label + ChatColor.GRAY + " subcommand" + ChatColor.BLUE + " - " + ChatColor.GRAY + "Description");
                return true;
            }
            ItemStack hand = p.getItemInHand();
            if (hand == null || hand.getType() != Material.MAP) {
                cs.sendMessage(ChatColor.RED + "You must be holding a map to use this subcommand!");
                return true;
            }
            MapView mv = plugin.getServer().getMap(hand.getDurability());
            if (subcommandMatches(subcommand, "scale", "scaling", "setscale", "setscaling")) {
                if (args.length < 2) {
                    cs.sendMessage(combineEnums(MapView.Scale.values()));
                    cs.sendMessage(ChatColor.RED + "Please specify a scale.");
                    return true;
                }
                String sscale = args[1].toUpperCase();
                MapView.Scale mvs;
                try {
                    mvs = MapView.Scale.valueOf(sscale);
                } catch (IllegalArgumentException e) {
                    cs.sendMessage(combineEnums(MapView.Scale.values()));
                    cs.sendMessage(ChatColor.RED + "Invalid scale type.");
                    return true;
                }
                mv.setScale(mvs);
                updateMap(p, mv);
                cs.sendMessage(ChatColor.BLUE + "Set the scale of map " + ChatColor.GRAY + mv.getId() + ChatColor.BLUE + " to " + ChatColor.GRAY + mvs.name().toLowerCase().replace("_", " ") + ChatColor.BLUE + ".");
                return true;
            } else if (subcommandMatches(subcommand, "reposition", "position", "pos", "repos", "setposition", "setpos", "coords", "coordinates", "setcoords", "setcoordinates")) {
                if (args.length < 3) {
                    cs.sendMessage(ChatColor.RED + "Please specify the new X and Z coordinates for the center of the map.");
                    return true;
                }
                int x, z;
                try {
                    x = Integer.valueOf(args[1]);
                    z = Integer.valueOf(args[2]);
                } catch (NumberFormatException e) {
                    cs.sendMessage(ChatColor.RED + "Those coordinates were invalid!");
                    return true;
                }
                mv.setCenterX(x);
                mv.setCenterZ(z);
                updateMap(p, mv);
                cs.sendMessage(ChatColor.BLUE + "Set the center of map " + ChatColor.GRAY + mv.getId() + ChatColor.BLUE + " to " + ChatColor.GRAY + x + ChatColor.BLUE + ", " + ChatColor.GRAY + z + ChatColor.BLUE + ".");
                return true;
            } else if (subcommandMatches(subcommand, "world", "setworld")) {
                if (args.length < 2) {
                    cs.sendMessage(ChatColor.RED + "Specify the world to set this map to.");
                    return true;
                }
                String sworld = args[0];
                World w = RUtils.getWorld(sworld);
                if (w == null) {
                    cs.sendMessage(ChatColor.RED + "No such world!");
                    return true;
                }
                mv.setWorld(w);
                updateMap(p, mv);
                cs.sendMessage(ChatColor.BLUE + "Set the world of map " + ChatColor.GRAY + mv.getId() + ChatColor.BLUE + " to " + ChatColor.GRAY + w.getName() + ChatColor.BLUE + ".");
                return true;
            } else if (subcommandMatches(subcommand, "info", "getinfo", "information", "getinformation")) {
                cs.sendMessage(ChatColor.BLUE + "Information about map " + ChatColor.GRAY + mv.getId() + ChatColor.BLUE + ":");
                cs.sendMessage("  " + ChatColor.BLUE + "Center coordinates: " + ChatColor.GRAY + mv.getCenterX() + ChatColor.BLUE + ", " + ChatColor.GRAY + mv.getCenterZ());
                cs.sendMessage("  " + ChatColor.BLUE + "World: " + ChatColor.GRAY + mv.getWorld().getName());
                cs.sendMessage("  " + ChatColor.BLUE + "Scale: " + ChatColor.GRAY + mv.getScale().name().toLowerCase().replace("_", " "));
                //cs.sendMessage("  " + ChatColor.BLUE + "Char: " + ChatColor.GRAY + "stuff");
                return true;
            //} else if (subcommandMatches(subcommand, "render", "fullrender")) {
            } else {
                cs.sendMessage(ChatColor.RED + "Unknown subcommand. Try " + ChatColor.GRAY + "/" + label + " help" + ChatColor.RED + ".");
                return true;
            }
        }
        return false;
    }

}
