package org.royaldev.royalcommands.rcommands;

import org.bukkit.Material;
import org.bukkit.command.Command;
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
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.exceptions.InvalidItemNameException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@ReflectCommand
public class CmdUses extends TabCommand {

    private final Map<String, Integer> tasks = new HashMap<>();

    public CmdUses(final RoyalCommands instance, final String name) {
        super(instance, name, true, new Short[]{CompletionType.ITEM_ALIAS.getShort()});
        this.plugin.getServer().getPluginManager().registerEvents(new WorkbenchCloseListener(), this.plugin);
    }

    private void cancelTask(final Player p) {
        if (!this.tasks.containsKey(p.getName())) return;
        final int taskID = this.tasks.get(p.getName());
        if (taskID != -1) this.plugin.getServer().getScheduler().cancelTask(taskID);
        this.tasks.remove(p.getName());
    }

    private boolean containsItemStack(Collection<? extends ItemStack> collection, ItemStack b) {
        for (final ItemStack a : collection) {
            if (this.itemStackEquals(a, b)) return true;
        }
        return false;
    }

    private boolean itemStackEquals(ItemStack a, ItemStack b) {
        return !(a == null || b == null) && a.getType() == b.getType() && (a.getDurability() == -1 || a.getDurability() == Short.MAX_VALUE || a.getDurability() == b.getDurability());
    }

    private void scheduleUsesTask(final Player p, ItemStack is) {
        final List<Inventory> workbenches = new ArrayList<>();
        final Iterator<Recipe> recipeIterator = this.plugin.getServer().recipeIterator();
        while (recipeIterator.hasNext()) {
            final Recipe r = recipeIterator.next();
            final Inventory i;
            if (r instanceof ShapedRecipe) {
                final ShapedRecipe sr = (ShapedRecipe) r;
                if (!this.containsItemStack(sr.getIngredientMap().values(), is)) continue;
                i = this.plugin.getServer().createInventory(new UsesHolder(), InventoryType.WORKBENCH);
                final Map<Character, ItemStack> im = sr.getIngredientMap();
                final String[] lines = sr.getShape();
                for (int lineNum = 0; lineNum < lines.length; lineNum++) {
                    final String line = lines[lineNum];
                    for (int slot = 1; slot <= 3; slot++) {
                        if (slot > line.length()) continue;
                        final ItemStack slotItem = im.get(line.charAt(slot - 1));
                        if (slotItem == null) continue;
                        i.setItem(slot + (lineNum * 3), this.syncDurabilities(slotItem, is));
                    }
                }
                i.setItem(0, sr.getResult());
            } else if (r instanceof ShapelessRecipe) {
                final ShapelessRecipe sr = (ShapelessRecipe) r;
                if (!this.containsItemStack(sr.getIngredientList(), is)) continue;
                i = this.plugin.getServer().createInventory(new UsesHolder(), InventoryType.WORKBENCH);
                final List<ItemStack> ingredients = sr.getIngredientList();
                for (int slot = 1; slot <= ingredients.size(); slot++) {
                    if (slot > ingredients.size()) continue;
                    i.setItem(slot, this.syncDurabilities(ingredients.get(slot - 1), is));
                }
                i.setItem(0, sr.getResult());
            } else if (r instanceof FurnaceRecipe) {
                final FurnaceRecipe fr = (FurnaceRecipe) r;
                if (!this.itemStackEquals(fr.getInput(), is)) continue;
                i = this.plugin.getServer().createInventory(new UsesHolder(), InventoryType.FURNACE);
                i.setItem(0, this.syncDurabilities(fr.getInput(), is));
                i.setItem(2, fr.getResult());
            } else continue;
            workbenches.add(i);
        }
        if (workbenches.size() < 1) {
            p.sendMessage(MessageColor.NEGATIVE + "No uses for that item!");
            return;
        }
        final Runnable r = new Runnable() {
            private int currentRecipe = 0;
            private boolean display = true;

            private void setClosing(boolean closing) {
                final InventoryHolder ih = p.getOpenInventory().getTopInventory().getHolder();
                if (!(ih instanceof UsesHolder)) return;
                final UsesHolder uh = (UsesHolder) ih;
                uh.setClosing(closing);
            }

            @Override
            public void run() {
                // let's not open new workbenches, as that can cause the items to disappear
                if (!this.display) return;
                if (!CmdUses.this.tasks.containsKey(p.getName())) return;
                if (this.currentRecipe >= workbenches.size()) this.currentRecipe = 0;
                this.setClosing(true);
                p.openInventory(workbenches.get(this.currentRecipe));
                this.setClosing(false);
                this.currentRecipe++;
                if (workbenches.size() == 1) this.display = false;
            }
        };
        final int taskID = this.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(this.plugin, r, 0L, 30L);
        if (taskID == -1) {
            p.sendMessage(MessageColor.NEGATIVE + "Could not schedule task!");
            return;
        }
        this.cancelTask(p);
        this.tasks.put(p.getName(), taskID);
    }

    private ItemStack syncDurabilities(ItemStack base, ItemStack copyDurability) {
        if (base.getType() != copyDurability.getType()) return base;
        if (base.getDurability() != -1 && base.getDurability() != Short.MAX_VALUE) return base;
        base.setDurability(copyDurability.getDurability());
        return base;
    }

    @Override
    protected boolean runCommand(CommandSender cs, Command cmd, String label, String[] eargs, CommandArguments ca) {
        if (eargs.length < 1) {
            cs.sendMessage(cmd.getDescription());
            return false;
        }
        if (!(cs instanceof Player)) {
            cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
            return true;
        }
        final Player p = (Player) cs;
        ItemStack is;
        if (eargs[0].equalsIgnoreCase("hand")) {
            is = p.getItemInHand();
        } else {
            try {
                is = RUtils.getItemFromAlias(eargs[0], 1);
            } catch (InvalidItemNameException e) {
                is = RUtils.getItem(eargs[0], 1);
            } catch (NullPointerException e) {
                cs.sendMessage(MessageColor.NEGATIVE + "ItemNameManager was not loaded. Let an administrator know.");
                return true;
            }
        }
        if (is == null) {
            cs.sendMessage(MessageColor.NEGATIVE + "Invalid item name!");
            return true;
        }
        this.scheduleUsesTask(p, is);
        return true;
    }

    private class WorkbenchCloseListener implements Listener {

        @EventHandler(ignoreCancelled = true)
        public void workbenchClick(InventoryClickEvent e) {
            if (!(e.getWhoClicked() instanceof Player)) return;
            final ItemStack is = e.getCurrentItem();
            if (is == null || is.getType() == Material.AIR) return;
            final InventoryType it = e.getInventory().getType();
            if (it != InventoryType.WORKBENCH && it != InventoryType.FURNACE) return;
            if (!(e.getInventory().getHolder() instanceof UsesHolder)) return;
            e.setCancelled(true);
            if (!(e.getWhoClicked() instanceof Player)) return;
            final Player p = (Player) e.getWhoClicked();
            CmdUses.this.scheduleUsesTask(p, is);
        }

        @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
        public void workbenchClose(InventoryCloseEvent e) {
            if (!(e.getPlayer() instanceof Player)) return;
            final Player p = (Player) e.getPlayer();
            final InventoryType it = e.getInventory().getType();
            if (it != InventoryType.WORKBENCH && it != InventoryType.FURNACE) return;
            if (!CmdUses.this.tasks.containsKey(p.getName())) return;
            if (!(e.getInventory().getHolder() instanceof UsesHolder)) return;
            final UsesHolder uh = (UsesHolder) e.getInventory().getHolder();
            if (uh.isClosing()) return;
            CmdUses.this.cancelTask(p);
        }
    }

    private class UsesHolder implements InventoryHolder {

        private boolean closing = false;

        private boolean isClosing() {
            return this.closing;
        }

        private void setClosing(boolean closing) {
            this.closing = closing;
        }

        @Override
        public Inventory getInventory() {
            return null;
        }
    }
}
