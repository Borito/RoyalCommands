package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.exceptions.InvalidItemNameException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CmdRecipe implements CommandExecutor {

    private final RoyalCommands plugin;
    private final Map<String, Integer> tasks = new HashMap<String, Integer>();

    public CmdRecipe(RoyalCommands instance) {
        plugin = instance;
        plugin.getServer().getPluginManager().registerEvents(new WorkbenchCloseListener(), plugin);
    }

    private void cancelTask(final Player p) {
        if (!tasks.containsKey(p.getName())) return;
        final int taskID = tasks.get(p.getName());
        if (taskID == -1) return;
        plugin.getServer().getScheduler().cancelTask(taskID);
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("recipe")) {
            if (!plugin.ah.isAuthorized(cs, "rcmds.recipe")) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
                return true;
            }
            final Player p = (Player) cs;
            ItemStack is;
            try {
                is = RUtils.getItemFromAlias(args[0], 1);
            } catch (InvalidItemNameException e) {
                is = RUtils.getItem(args[0], 1);
            } catch (NullPointerException e) {
                cs.sendMessage(MessageColor.NEGATIVE + "ItemNameManager was not loaded. Let an administrator know.");
                return true;
            }
            if (is == null) {
                cs.sendMessage(MessageColor.NEGATIVE + "Invalid item name!");
                return true;
            }
            is.setDurability((short) -1);
            final List<Recipe> rs = plugin.getServer().getRecipesFor(is);
            if (rs.size() < 1) {
                cs.sendMessage(MessageColor.NEGATIVE + "No recipes for that item!");
                return true;
            }
            final List<Inventory> workbenches = new ArrayList<Inventory>();
            for (Recipe r : rs) {
                final Inventory i;
                if (r instanceof ShapedRecipe) {
                    i = plugin.getServer().createInventory(null, InventoryType.WORKBENCH);
                    final ShapedRecipe sr = (ShapedRecipe) r;
                    final Map<Character, ItemStack> im = sr.getIngredientMap();
                    final String[] lines = sr.getShape();
                    for (int lineNum = 0; lineNum < lines.length; lineNum++) {
                        final String line = lines[lineNum];
                        for (int slot = 1; slot <= 3; slot++) {
                            if (slot > line.length()) continue;
                            final ItemStack slotItem = im.get(line.charAt(slot - 1));
                            if (slotItem == null) continue;
                            i.setItem(slot + (lineNum * 3), slotItem);
                        }
                    }
                    i.setItem(0, sr.getResult());
                } else if (r instanceof ShapelessRecipe) {
                    i = plugin.getServer().createInventory(null, InventoryType.WORKBENCH);
                    final ShapelessRecipe sr = (ShapelessRecipe) r;
                    final List<ItemStack> ingredients = sr.getIngredientList();
                    for (int slot = 1; slot <= ingredients.size(); slot++) {
                        if (slot > ingredients.size()) continue;
                        i.setItem(slot, ingredients.get(slot - 1));
                    }
                    i.setItem(0, sr.getResult());
                } else if (r instanceof FurnaceRecipe) {
                    i = plugin.getServer().createInventory(null, InventoryType.FURNACE);
                    final FurnaceRecipe fr = (FurnaceRecipe) r;
                    i.setItem(0, fr.getInput());
                    i.setItem(2, fr.getResult());
                } else continue;
                workbenches.add(i);
            }
            final Runnable r = new Runnable() {
                private int currentRecipe = 0;
                private boolean display = true;

                private void setRecipeMaxStackSize(final Player p, final int size) {
                    final Inventory i = p.getOpenInventory().getTopInventory();
                    switch (i.getType()) {
                        case FURNACE:
                        case WORKBENCH:
                            i.setMaxStackSize(size);
                    }
                }

                @Override
                public void run() {
                    if (!display) return; // let's not open new workbenches, as that can cause the items to disappear
                    if (!tasks.containsKey(p.getName())) return;
                    if (currentRecipe >= workbenches.size()) currentRecipe = 0;
                    setRecipeMaxStackSize(p, 65);
                    p.openInventory(workbenches.get(currentRecipe));
                    setRecipeMaxStackSize(p, 64);
                    currentRecipe++;
                    if (workbenches.size() == 1) display = false;
                }
            };
            int taskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, r, 0L, 30L);
            if (taskID == -1) {
                cs.sendMessage(MessageColor.NEGATIVE + "Could not schedule task!");
                return true;
            }
            cancelTask(p);
            tasks.put(p.getName(), taskID);
            return true;
        }
        return false;
    }

    private class WorkbenchCloseListener implements Listener {
        @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
        public void workbenchClose(InventoryCloseEvent e) {
            if (!(e.getPlayer() instanceof Player)) return;
            final Player p = (Player) e.getPlayer();
            final InventoryType it = e.getInventory().getType();
            if (it != InventoryType.WORKBENCH && it != InventoryType.FURNACE) return;
            if (!tasks.containsKey(p.getName())) return;
            if (e.getInventory().getMaxStackSize() == 65) return;
            final int taskID = tasks.get(p.getName());
            if (taskID == -1) return;
            plugin.getServer().getScheduler().cancelTask(taskID);
            tasks.remove(p.getName());
        }

        @EventHandler(ignoreCancelled = true)
        public void workbenchClick(InventoryClickEvent e) {
            if (!(e.getWhoClicked() instanceof Player)) return;
            final Player p = (Player) e.getWhoClicked();
            final InventoryType it = e.getInventory().getType();
            if (it != InventoryType.WORKBENCH && it != InventoryType.FURNACE) return;
            if (!tasks.containsKey(p.getName())) return;
            e.setCancelled(true);
        }
    }

}
