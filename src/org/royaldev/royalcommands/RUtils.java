package org.royaldev.royalcommands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.royaldev.royalcommands.rcommands.CmdBack;
import org.royaldev.royalcommands.serializable.SerializableCraftInventory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Logger;

@SuppressWarnings("unchecked, unused")
public class RUtils {

    static Logger log = Logger.getLogger("Minecraft");

    // Borrowed list of materials from Essentials
    public static final Set<Integer> AIR_MATERIALS = new HashSet<Integer>();
    public static final HashSet<Byte> AIR_MATERIALS_TARGET = new HashSet<Byte>();

    static {
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
            AIR_MATERIALS_TARGET.add(integer.byteValue());
        AIR_MATERIALS_TARGET.add((byte) Material.WATER.getId());
        AIR_MATERIALS_TARGET.add((byte) Material.STATIONARY_WATER.getId());
    }

    public static String[] wrapText(String text, int len) {
        // return empty array for null text
        if (text == null)
            return new String[]{};

        // return text if len is zero or less
        if (len <= 0)
            return new String[]{text};

        // return text if less than length
        if (text.length() <= len)
            return new String[]{text};
        char[] chars = text.toCharArray();
        Vector lines = new Vector();
        StringBuilder line = new StringBuilder();
        StringBuilder word = new StringBuilder();

        for (char aChar : chars) {
            word.append(aChar);

            if (aChar == ' ') {
                if ((line.length() + word.length()) > len) {
                    lines.add(line.toString());
                    line.delete(0, line.length());
                }

                line.append(word);
                word.delete(0, word.length());
            }
        }

        // handle any extra chars in current word
        if (word.length() > 0) {
            if ((line.length() + word.length()) > len) {
                lines.add(line.toString());
                line.delete(0, line.length());
            }
            line.append(word);
        }

        // handle extra line
        if (line.length() > 0) lines.add(line.toString());

        String[] ret = new String[lines.size()];
        int c = 0; // counter
        for (Enumeration e = lines.elements(); e.hasMoreElements(); c++)
            ret[c] = (String) e.nextElement();
        return ret;
    }

    public static Block getTarget(Player p) {
        return p.getTargetBlock(AIR_MATERIALS_TARGET, 300);
    }

    public static String join(Iterable<String> i, String between) {
        String ret = "";
        for (String s : i)
            ret = (ret.equals("")) ? ret.concat(s) : ret.concat(between + s);
        return ret;
    }

    public static String join(String[] i, String between) {
        String ret = "";
        for (String s : i)
            ret = (ret.equals("")) ? ret.concat(s) : ret.concat(between + s);
        return ret;
    }

    public static String join(Object[] i, String between) {
        String ret = "";
        for (Object o : i)
            ret = (ret.equals("")) ? ret.concat(o.toString().toLowerCase()) : ret.concat(between + o.toString().toLowerCase());
        return ret;
    }

    public static void showFilledChest(Player p, String name) {
        Inventory inv = Bukkit.createInventory(null, InventoryType.CHEST);
        ItemStack stack = getItem(name, 64);
        for (int i = 0; i < inv.getSize(); i++) inv.addItem(stack);
        p.openInventory(inv);
    }

    public static void showEmptyChest(Player player) {
        player.openInventory(Bukkit.createInventory(null, InventoryType.CHEST));
    }

    public static boolean chargePlayer(CommandSender cs, double amount) {
        if (RoyalCommands.economy == null) {
            cs.sendMessage(ChatColor.RED + "No economy! Continuing without charging.");
            return true;
        }
        if (!RoyalCommands.economy.hasAccount(cs.getName())) {
            cs.sendMessage(ChatColor.RED + "You don't have a bank account!");
            return false;
        }
        if (RoyalCommands.economy.getBalance(cs.getName()) < amount) {
            cs.sendMessage(ChatColor.RED + "You don't have enough money!");
            return false;
        }
        RoyalCommands.economy.withdrawPlayer(cs.getName(), amount);
        cs.sendMessage(ChatColor.BLUE + "You have had " + ChatColor.GRAY + RoyalCommands.economy.format(amount) + ChatColor.BLUE + " removed from your account.");
        return true;
    }

    public static void dispNoPerms(CommandSender cs) {
        cs.sendMessage(ChatColor.RED + "You don't have permission for that!");
        log.warning("[RoyalCommands] " + cs.getName() + " was denied access to that!");
    }

    public static void dispNoPerms(CommandSender cs, String message) {
        cs.sendMessage(message);
        log.warning("[RoyalCommands] " + cs.getName() + " was denied access to that!");
    }

    public static Double getDouble(String number) {
        try {
            return Double.valueOf(number);
        } catch (Exception e) {
            return null;
        }
    }

    public static Integer getInt(String number) {
        try {
            return Integer.valueOf(number);
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean isTimeStampValid(OfflinePlayer p, String title) {
        PConfManager pcm = new PConfManager(p);
        if (pcm.get(title) == null) return false;
        long time = new Date().getTime();
        long overall = pcm.getLong(title);
        return time < overall;
    }

    public static void setTimeStamp(OfflinePlayer p, long seconds, String title) {
        PConfManager pcm = new PConfManager(p);
        pcm.setLong((seconds * 1000) + new Date().getTime(), title);
    }

    public static long getTimeStamp(OfflinePlayer p, String title) {
        PConfManager pcm = new PConfManager(p);
        if (pcm.get(title) == null) return -1;
        return pcm.getLong(title);
    }

    //if it isn't obvious, Essentials wrote this code. no way in hell I could manage this
    public static String formatDateDiff(long date) {
        Calendar c = new GregorianCalendar();
        c.setTimeInMillis(date);
        Calendar now = new GregorianCalendar();
        return formatDateDiff(now, c);
    }

    private static int dateDiff(int type, Calendar fromDate, Calendar toDate, boolean future) {
        int diff = 0;
        long savedDate = fromDate.getTimeInMillis();
        while ((future && !fromDate.after(toDate)) || (!future && !fromDate.before(toDate))) {
            savedDate = fromDate.getTimeInMillis();
            fromDate.add(type, future ? 1 : -1);
            diff++;
        }
        diff--;
        fromDate.setTimeInMillis(savedDate);
        return diff;
    }

    public static String formatDateDiff(Calendar fromDate, Calendar toDate) {
        boolean future = false;
        if (toDate.equals(fromDate)) return " now";
        if (toDate.after(fromDate)) future = true;

        StringBuilder sb = new StringBuilder();
        int[] types = new int[]
                {
                        Calendar.YEAR,
                        Calendar.MONTH,
                        Calendar.DAY_OF_MONTH,
                        Calendar.HOUR_OF_DAY,
                        Calendar.MINUTE,
                        Calendar.SECOND
                };
        String[] names = new String[]
                {
                        "year",
                        "years",
                        "month",
                        "months",
                        "day",
                        "days",
                        "hour",
                        "hours",
                        "minute",
                        "minutes",
                        "second",
                        "seconds"
                };
        for (int i = 0; i < types.length; i++) {
            int diff = dateDiff(types[i], fromDate, toDate, future);
            if (diff > 0)
                sb.append(" ").append(diff).append(" ").append(names[i * 2 + (diff > 1 ? 1 : 0)]);
        }
        if (sb.length() == 0) return " now";
        return sb.toString();
    }

    public static boolean isTeleportAllowed(OfflinePlayer p) {
        PConfManager pcm = new PConfManager(p);
        return pcm.get("allow-tp") == null || pcm.getBoolean("allow-tp");
    }

    public static String colorize(String text) {
        if (text == null) return null;
        return text.replaceAll("(&([a-f0-9k-or]))", "\u00A7$2");
    }

    /**
     * Returns the ItemStack for any material name and amount.
     * If amount is null, will be default stack size.
     * <p/>
     * name can contain a ":" to specify data
     *
     * @param name   Name of the material
     * @param amount Amount of items or null for default
     * @return ItemStack or null if no such material
     */
    public static ItemStack getItem(String name, Integer amount) {
        Short data;
        String datas = null;
        name = name.trim().toUpperCase();
        if (name.contains(":")) {
            if (name.split(":").length < 2) {
                datas = null;
                name = name.split(":")[0];
            } else {
                datas = name.split(":")[1];
                name = name.split(":")[0];
            }
        }
        try {
            data = Short.valueOf(datas);
        } catch (Exception e) {
            data = null;
        }
        Material mat = Material.getMaterial(name);
        if (mat == null) {
            try {
                mat = Material.getMaterial(Integer.valueOf(name));
                if (mat == null) return null;
            } catch (Exception e) {
                return null;
            }
        }
        if (amount == null) amount = RoyalCommands.defaultStack;
        ItemStack stack = new ItemStack(mat, amount);
        if (data != null) stack.setDurability(data);
        return stack;
    }

    /**
     * Teleports a player and registers it in /back.
     *
     * @param p Player to teleport
     * @param l Location to teleport to
     * @return Error message if any.
     */
    public static String teleport(Player p, Location l) {
        if (!RoyalCommands.safeTeleport) {
            CmdBack.backdb.put(p, p.getLocation());
            p.teleport(l);
        } else {
            Location toTele = getSafeLocation(l);
            if (toTele == null) return "There is no ground below.";
            CmdBack.backdb.put(p, p.getLocation());
            p.teleport(toTele);
        }
        return "";
    }

    /*
     * If teleport warmup is on, determines if the player can teleport.
     *
     * @param p Player to determine teleportation status for
     * @return true if player can teleport, false if otherwise
     */
    /*public static boolean teleWait(Player p) {
        if (RoyalCommands.teleWarmup < 1) return true;
        synchronized (CmdTeleport.waitingToTele) {
            if (!CmdTeleport.waitingToTele.contains(p.getName())) {
                CmdTeleport.waitingToTele.add(p.getName());
                p.sendMessage(ChatColor.BLUE + "Please wait for " + ChatColor.GRAY + RoyalCommands.teleWarmup + " seconds" + ChatColor.BLUE + " before teleporting.");
                return false;
            } else CmdTeleport.waitingToTele.remove(p.getName());
        }
        return true;
    }*/

    /**
     * Teleports a player without registering it in /back.
     *
     * @param p Player to teleport
     * @param l Location to teleport to
     * @return Error message if any.
     */
    public static String silentTeleport(Player p, Location l) {
        //if (!teleWait(p)) return "";
        if (!RoyalCommands.safeTeleport) p.teleport(l);
        else {
            Location toTele = getSafeLocation(l);
            if (toTele == null) return "There is no ground below.";
            p.teleport(toTele);
        }
        return "";
    }

    /**
     * Teleports a player and registers it in /back.
     *
     * @param p Player to teleport
     * @param e Entity to teleport to
     * @return Error message if any.
     */
    public static String teleport(Player p, Entity e) {
        //if (!teleWait(p)) return "";
        if (!RoyalCommands.safeTeleport) {
            CmdBack.backdb.put(p, p.getLocation());
            p.teleport(e);
        } else {
            Location toTele = getSafeLocation(e);
            if (toTele == null) return "There is no ground below.";
            CmdBack.backdb.put(p, p.getLocation());
            p.teleport(toTele);
        }
        return "";
    }

    /**
     * Teleports a player without registering it in /back.
     *
     * @param p Player to teleport
     * @param e Entity to teleport to
     * @return Error message if any.
     */
    public static String silentTeleport(Player p, Entity e) {
        if (p == null || e == null) return "Player/entity was null!";
        if (!RoyalCommands.safeTeleport) p.teleport(e);
        else {
            Location toTele = getSafeLocation(e);
            if (toTele == null) return "There is no ground below.";
            p.teleport(toTele);
        }
        return "";
    }

    /**
     * Returns a location that is always above ground.
     * If there is no ground under the location, returns
     * null.
     *
     * @param l Location to find safe location for
     * @return Safe location or null if no ground
     */
    public static Location getSafeLocation(Location l) {
        int unsafeY = l.getBlockY();
        if (unsafeY < 0) return null;
        for (int i = unsafeY; i >= 0; i--) {
            if (i < 0) return null;
            Block b = l.getWorld().getBlockAt(l.getBlockX(), i, l.getBlockZ());
            if (b == null) return null;
            if (b.getType().equals(Material.AIR)) continue;
            Location bLoc = b.getLocation();
            double safeY = l.getY() - (unsafeY - i);
            return new Location(l.getWorld(), l.getX(), safeY + 1, l.getZ(), l.getYaw(), l.getPitch());
        }
        return null;
    }

    /**
     * Returns a location that is always above ground.
     * If there is no ground under the location, returns
     * null.
     *
     * @param e Entity to derive location from
     * @return Safe location or null if no ground
     */
    public static Location getSafeLocation(Entity e) {
        Location l = e.getLocation();
        int unsafeY = l.getBlockY();
        if (unsafeY < 0) return null;
        for (int i = unsafeY; i >= 0; i--) {
            if (i < 0) return null;
            Block b = l.getWorld().getBlockAt(l.getBlockX(), i, l.getBlockZ());
            if (b == null) return null;
            if (b.getType().equals(Material.AIR)) continue;
            Location bLoc = b.getLocation();
            double safeY = l.getY() - (unsafeY - i);
            return new Location(l.getWorld(), l.getX(), safeY + 1, l.getZ(), l.getYaw(), l.getPitch());
        }
        return null;
    }

    /**
     * Returns formatted name of an ItemStack.
     *
     * @param is ItemStack to get name for
     * @return Name of item (formatted)
     */
    public static String getItemName(ItemStack is) {
        return is.getType().name().toLowerCase().replace("_", " ");
    }

    /**
     * Returns formatted name of a Material
     *
     * @param m Material to get name for
     * @return Name of item (formatted)
     */
    public static String getItemName(Material m) {
        return m.name().toLowerCase().replace("_", " ");
    }

    /**
     * Saves a HashMap to a file.
     *
     * @param hash HashMap to save
     * @param path Path to file to save to
     */
    public static void saveHash(Object hash, String path) {
        try {
            ObjectOutputStream st = new ObjectOutputStream(new FileOutputStream(path));
            st.writeObject(hash);
            st.flush();
            st.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads a HashMap from a file.
     * <p/>
     * Never returns null.
     *
     * @param path Path to file to load from
     * @return HashMap
     */
    public static HashMap loadHash(String path) {

        try {
            if (!new File(path).exists()) {
                new File(path).createNewFile();
                return new HashMap();
            }
            ObjectInputStream st = new ObjectInputStream(new FileInputStream(path));
            Object o = st.readObject();
            if (o == null) return new HashMap();
            return (HashMap) o;
        } catch (Exception e) {
            return new HashMap();
        }
    }

    /**
     * Returns an empty inventory for use.
     *
     * @param handler May be null - owner of inventory
     * @param size    Size of inventory - MUST be divisible by 9
     * @param name    May be null (default to Chest) - name of inventory to open
     * @return Inventory or null if size not divisible by 9
     */
    public static Inventory createInv(InventoryHolder handler, Integer size, String name) {
        if (size == null) size = 27;
        //if (size % 9 != 0) return null;
        final Inventory i = Bukkit.getServer().createInventory(handler, size, name);
        i.clear();
        return i;
    }

    /**
     * Converts a normal inventory into a serializable one.
     *
     * @param i Inventory to convert
     * @return SerializableCraftInventory from Inventory
     */
    public static SerializableCraftInventory convInvToSCI(Inventory i) {
        return new SerializableCraftInventory(i);
    }

    /**
     * Sets the holder of an Inventory
     *
     * @param i  Inventory to set holder of
     * @param ih InventoryHolder to be set
     * @return New inventory with updated holder
     */
    public static Inventory setHolder(Inventory i, InventoryHolder ih) {
        Inventory ii = createInv(ih, i.getSize(), i.getName());
        ii.setContents(i.getContents());
        return ii;
    }

    public static void kickPlayer(Player p, String reason) {
        p.kickPlayer(reason);
    }
}
