package tk.royalcraf.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Player;
import tk.royalcraf.royalcommands.RoyalCommands;

import java.util.HashSet;
import java.util.Set;

public class Spawner implements CommandExecutor {

    RoyalCommands plugin;

    public Spawner(RoyalCommands instance) {
        this.plugin = instance;
    }

    // Borrowed list of materials from Essentials
    private static final Set<Integer> AIR_MATERIALS = new HashSet<Integer>();
    private static final HashSet<Byte> AIR_MATERIALS_TARGET = new HashSet<Byte>();

    static
    {
        AIR_MATERIALS.add(Material.AIR.getId());
        AIR_MATERIALS.add(Material.SAPLING.getId());
        AIR_MATERIALS.add(Material.POWERED_RAIL.getId());
        AIR_MATERIALS.add(Material.DETECTOR_RAIL.getId());
        AIR_MATERIALS.add(Material.LONG_GRASS.getId());
        AIR_MATERIALS.add(Material.DEAD_BUSH.getId());
        AIR_MATERIALS.add(Material.YELLOW_FLOWER.getId());
        AIR_MATERIALS.add(Material.RED_ROSE.getId());
        AIR_MATERIALS.add(Material.BROWN_MUSHROOM.getId());
        AIR_MATERIALS.add(Material.RED_MUSHROOM.getId());
        AIR_MATERIALS.add(Material.TORCH.getId());
        AIR_MATERIALS.add(Material.REDSTONE_WIRE.getId());
        AIR_MATERIALS.add(Material.SEEDS.getId());
        AIR_MATERIALS.add(Material.SIGN_POST.getId());
        AIR_MATERIALS.add(Material.WOODEN_DOOR.getId());
        AIR_MATERIALS.add(Material.LADDER.getId());
        AIR_MATERIALS.add(Material.RAILS.getId());
        AIR_MATERIALS.add(Material.WALL_SIGN.getId());
        AIR_MATERIALS.add(Material.LEVER.getId());
        AIR_MATERIALS.add(Material.STONE_PLATE.getId());
        AIR_MATERIALS.add(Material.IRON_DOOR_BLOCK.getId());
        AIR_MATERIALS.add(Material.WOOD_PLATE.getId());
        AIR_MATERIALS.add(Material.REDSTONE_TORCH_OFF.getId());
        AIR_MATERIALS.add(Material.REDSTONE_TORCH_ON.getId());
        AIR_MATERIALS.add(Material.STONE_BUTTON.getId());
        AIR_MATERIALS.add(Material.SUGAR_CANE_BLOCK.getId());
        AIR_MATERIALS.add(Material.DIODE_BLOCK_OFF.getId());
        AIR_MATERIALS.add(Material.DIODE_BLOCK_ON.getId());
        AIR_MATERIALS.add(Material.TRAP_DOOR.getId());
        AIR_MATERIALS.add(Material.PUMPKIN_STEM.getId());
        AIR_MATERIALS.add(Material.MELON_STEM.getId());
        AIR_MATERIALS.add(Material.VINE.getId());
        AIR_MATERIALS.add(Material.NETHER_WARTS.getId());
        AIR_MATERIALS.add(Material.WATER_LILY.getId());
        AIR_MATERIALS.add(Material.SNOW.getId());

        for (Integer integer : AIR_MATERIALS)
        {
            AIR_MATERIALS_TARGET.add(integer.byteValue());
        }
        AIR_MATERIALS_TARGET.add((byte)Material.WATER.getId());
        AIR_MATERIALS_TARGET.add((byte)Material.STATIONARY_WATER.getId());
    }
    
    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("spawner")) {
            if (!plugin.isAuthorized(cs, "rcmds.spawner")) {
                cs.sendMessage(ChatColor.RED
                        + "You don't have permission for that!");
                plugin.log.warning("[RoyalCommands] " + cs.getName()
                        + " was denied access to the command!");
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(ChatColor.RED + "This command is only available to players!");
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            Player p = (Player) cs;
            Block bb = p.getTargetBlock(AIR_MATERIALS_TARGET, 300);
            if (bb == null) {
                cs.sendMessage(ChatColor.RED + "No block found!");
                return true;
            }
            if (!(bb.getState() instanceof CreatureSpawner)) {
                cs.sendMessage(ChatColor.RED + "That's not a mob spawner!");
                return true;
            }
            CreatureSpawner crs = (CreatureSpawner) bb.getState();
            CreatureType ct;
            try {
                ct = CreatureType.valueOf(args[0].toUpperCase());
            } catch (Exception e) {
                cs.sendMessage(ChatColor.RED + "Invalid mob!");
                return true;
            }
            crs.setCreatureType(ct);
            cs.sendMessage(ChatColor.BLUE + "Spawner type set to " + ChatColor.GRAY + crs.getCreatureTypeId().toLowerCase().replace("_", " ") + ChatColor.BLUE + ".");
            return true;
        }
        return false;
    }
}
