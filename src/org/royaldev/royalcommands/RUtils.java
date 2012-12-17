package org.royaldev.royalcommands;

import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import net.minecraft.server.v1_4_5.NBTTagCompound;
import net.minecraft.server.v1_4_5.NBTTagList;
import net.minecraft.server.v1_4_5.NBTTagString;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_4_5.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.royaldev.royalchat.RoyalChat;
import org.royaldev.royalcommands.exceptions.InvalidItemNameException;
import org.royaldev.royalcommands.rcommands.CmdBack;
import org.royaldev.royalcommands.serializable.SerializableCraftInventory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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

    /**
     * Wraps text to fit evenly in the chat box
     *
     * @param text Text to wrap
     * @param len  Length to wrap at
     * @return Array of strings
     */
    public static String[] wrapText(String text, int len) {
        // return empty array for null text
        if (text == null) return new String[]{};

        // return text if len is zero or less
        if (len <= 0) return new String[]{text};

        // return text if less than length
        if (text.length() <= len) return new String[]{text};
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

    /**
     * Gets the block the player is looking at
     *
     * @param p Player to get block from
     * @return Block player is looking at
     */
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

    /**
     * Shows a chest filled of an item to player
     *
     * @param p    Player to show chest to
     * @param name Name of item to fill chest with
     */
    public static void showFilledChest(Player p, String name) {
        Inventory inv = Bukkit.createInventory(null, InventoryType.CHEST);
        ItemStack stack = getItem(name, 64);
        for (int i = 0; i < inv.getSize(); i++) inv.addItem(stack);
        p.openInventory(inv);
    }

    /**
     * Shows a temporary empty chest to the player
     *
     * @param player Player to show chest to
     */
    public static void showEmptyChest(Player player) {
        player.openInventory(Bukkit.createInventory(null, InventoryType.CHEST));
    }

    /**
     * Charges a CommandSender an amount of money
     *
     * @param cs     CommandSender to charge
     * @param amount Amount to charge cs
     * @return true if transaction was successful, false if otherwise
     */
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

    /**
     * Sends the standard message of no permission to console and command sender
     *
     * @param cs CommandSender to send message to
     */
    public static void dispNoPerms(CommandSender cs) {
        cs.sendMessage(ChatColor.RED + "You don't have permission for that!");
        log.warning("[RoyalCommands] " + cs.getName() + " was denied access to that!");
    }

    /**
     * Displays a no permissions message to the command sender and console/
     *
     * @param cs      CommandSender to send message to
     * @param message Custom message to send
     */
    public static void dispNoPerms(CommandSender cs, String message) {
        cs.sendMessage(message);
        log.warning("[RoyalCommands] " + cs.getName() + " was denied access to that!");
    }

    /**
     * Returns the Double from a String
     *
     * @param number String to get double from
     * @return Double or null if string was not a valid double
     */
    public static Double getDouble(String number) {
        try {
            return Double.valueOf(number);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Returns the Integer from a String
     *
     * @param number String to get int from
     * @return Integer or null if string was not a valid integer
     */
    public static Integer getInt(String number) {
        try {
            return Integer.valueOf(number);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Checks to see if the timestamp is greater than the current time.
     *
     * @param p     OfflinePlayer to check for
     * @param title Path of timestamp to check
     * @return true if the timestamp has not been passed, false if otherwise
     */
    public static boolean isTimeStampValid(OfflinePlayer p, String title) {
        PConfManager pcm = new PConfManager(p);
        if (pcm.get(title) == null) return false;
        long time = new Date().getTime();
        long overall = pcm.getLong(title);
        return time < overall;
    }

    public static boolean isTimeStampValidAddTime(OfflinePlayer p, String title) {
        PConfManager pcm = new PConfManager(p);
        if (pcm.get(title) == null) return false;
        long time = new Date().getTime();
        long overall = (pcm.getLong(title) * 1000L) + time;
        return time < overall;
    }

    /**
     * Sets a timestamp in a player's userdata file
     *
     * @param p       OfflinePlayer to set the timestamp on
     * @param seconds Seconds relative to the current time for timestamp
     * @param title   Path to timestamp
     */
    public static void setTimeStamp(OfflinePlayer p, long seconds, String title) {
        PConfManager pcm = new PConfManager(p);
        pcm.setLong((seconds * 1000) + new Date().getTime(), title);
    }

    /**
     * Gets a timestamp from a player's userdata file.
     *
     * @param p     OfflinePlayer to get timestamp from
     * @param title Path of timestamp
     * @return timestamp or -1 if there was no such timestamp
     */
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
        int[] types = new int[]{Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH, Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND};
        String[] names = new String[]{"year", "years", "month", "months", "day", "days", "hour", "hours", "minute", "minutes", "second", "seconds"};
        for (int i = 0; i < types.length; i++) {
            int diff = dateDiff(types[i], fromDate, toDate, future);
            if (diff > 0)
                sb.append(" ").append(diff).append(" ").append(names[i * 2 + (diff > 1 ? 1 : 0)]);
        }
        if (sb.length() == 0) return " now";
        return sb.toString();
    }

    /**
     * Checks to see if teleport is allowed for the specified OfflinePlayer
     *
     * @param p OfflinePlayer to check teleportation status on
     * @return true or false
     */
    public static boolean isTeleportAllowed(OfflinePlayer p) {
        PConfManager pcm = new PConfManager(p);
        return pcm.get("allow-tp") == null || pcm.getBoolean("allow-tp");
    }

    /**
     * Replaces raw color codes with processed color codes
     *
     * @param text String with codes to be converted
     * @return Processed string
     */
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
        if (name == null) return null;
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
            if (datas != null) return null;
            else data = null;
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
        synchronized (teleRunners) {
            if (RoyalCommands.instance.teleportWarmup > 0 && !teleRunners.containsKey(p.getName()) && !RoyalCommands.hasPerm(p, "rcmds.exempt.teleportwarmup")) {
                makeTeleportRunner(p, l);
                return "";
            } else if (RoyalCommands.instance.teleportWarmup > 0 && teleRunners.containsKey(p.getName()) && !teleAllowed.contains(p.getName())) {
                return "";
            }
        }
        if (!RoyalCommands.safeTeleport) {
            CmdBack.backdb.put(p.getName(), p.getLocation());
            p.teleport(l);
        } else {
            Location toTele = getSafeLocation(l);
            if (toTele == null) return "There is no ground below.";
            CmdBack.backdb.put(p.getName(), p.getLocation());
            p.teleport(toTele);
        }
        return "";
    }

    private final static Map<String, Integer> teleRunners = new HashMap<String, Integer>();
    private final static List<String> teleAllowed = new ArrayList<String>();

    public static void cancelTeleportRunner(final Player p) {
        synchronized (teleRunners) {
            if (teleRunners.containsKey(p.getName())) {
                Bukkit.getScheduler().cancelTask(teleRunners.get(p.getName()));
                teleRunners.remove(p.getName());
            }
        }
    }

    /**
     * Makes a scheduled Bukkit task for watching a player when he's warming up for teleport.
     *
     * @param p Player to teleport when warmup is finished
     * @param t Location to teleport to when warmup is finished
     * @return ID of Bukkit task
     */
    private static int makeTeleportRunner(final Player p, final Location t) {
        synchronized (teleRunners) {
            if (teleRunners.containsKey(p.getName())) cancelTeleportRunner(p);
        }
        p.sendMessage(ChatColor.BLUE + "Please wait " + ChatColor.GRAY + RoyalCommands.instance.teleportWarmup + ChatColor.BLUE + " seconds for your teleport.");
        final PConfManager pcm = new PConfManager(p);
        pcm.setLong(new Date().getTime(), "teleport_warmup");
        Runnable r = new Runnable() {
            @Override
            public void run() {
                Long l = pcm.getLong("teleport_warmup");
                if (l == null || l < 0) {
                    cancelTeleportRunner(p);
                    return;
                }
                int toAdd = RoyalCommands.instance.teleportWarmup * 1000;
                l = l + toAdd;
                long c = new Date().getTime();
                if (l < c) {
                    p.sendMessage(ChatColor.BLUE + "Teleporting...");
                    teleAllowed.add(p.getName());
                    String error = teleport(p, t);
                    teleAllowed.remove(p.getName());
                    if (!error.isEmpty()) p.sendMessage(ChatColor.RED + error);
                    cancelTeleportRunner(p);
                }
            }
        };
        int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(RoyalCommands.instance, r, 0, 10);
        synchronized (teleRunners) {
            teleRunners.put(p.getName(), id);
        }
        return id;
    }

    /**
     * Makes a scheduled Bukkit task for watching a player when he's warming up for teleport.
     *
     * @param p Player to teleport when warmup is finished
     * @param t Entity to teleport to when warmup is finished
     * @return ID of Bukkit task
     */
    private static int makeTeleportRunner(final Player p, final Entity t) {
        synchronized (teleRunners) {
            if (teleRunners.containsKey(p.getName())) cancelTeleportRunner(p);
        }
        p.sendMessage(ChatColor.BLUE + "Please wait " + ChatColor.GRAY + RoyalCommands.instance.teleportWarmup + ChatColor.BLUE + " seconds for your teleport.");
        final PConfManager pcm = new PConfManager(p);
        pcm.setLong(new Date().getTime(), "teleport_warmup");
        Runnable r = new Runnable() {
            @Override
            public void run() {
                Long l = pcm.getLong("teleport_warmup");
                if (l == null || l < 0) {
                    cancelTeleportRunner(p);
                    return;
                }
                int toAdd = RoyalCommands.instance.teleportWarmup * 1000;
                l = l + toAdd;
                long c = new Date().getTime();
                if (l < c) {
                    p.sendMessage(ChatColor.BLUE + "Teleporting...");
                    teleAllowed.add(p.getName());
                    String error = teleport(p, t);
                    teleAllowed.remove(p.getName());
                    if (!error.isEmpty()) p.sendMessage(ChatColor.RED + error);
                    cancelTeleportRunner(p);
                }
            }
        };
        int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(RoyalCommands.instance, r, 0, 10);
        synchronized (teleRunners) {
            teleRunners.put(p.getName(), id);
        }
        return id;
    }

    /**
     * Teleports a player without registering it in /back.
     *
     * @param p Player to teleport
     * @param l Location to teleport to
     * @return Error message if any.
     */
    public static String silentTeleport(Player p, Location l) {
        synchronized (teleRunners) {
            if (RoyalCommands.instance.teleportWarmup > 0 && !teleRunners.containsKey(p.getName()) && !RoyalCommands.hasPerm(p, "rcmds.exempt.teleportwarmup")) {
                makeTeleportRunner(p, l);
                return "";
            } else if (RoyalCommands.instance.teleportWarmup > 0 && teleRunners.containsKey(p.getName()) && !teleAllowed.contains(p.getName())) {
                return "";
            }
        }
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
        synchronized (teleRunners) {
            if (RoyalCommands.instance.teleportWarmup > 0 && !teleRunners.containsKey(p.getName()) && !RoyalCommands.hasPerm(p, "rcmds.exempt.teleportwarmup")) {
                makeTeleportRunner(p, e);
                return "";
            } else if (RoyalCommands.instance.teleportWarmup > 0 && teleRunners.containsKey(p.getName()) && !teleAllowed.contains(p.getName())) {
                return "";
            }
        }
        if (!RoyalCommands.safeTeleport) {
            CmdBack.backdb.put(p.getName(), p.getLocation());
            p.teleport(e);
        } else {
            Location toTele = getSafeLocation(e);
            if (toTele == null) return "There is no ground below.";
            CmdBack.backdb.put(p.getName(), p.getLocation());
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
        synchronized (teleRunners) {
            if (RoyalCommands.instance.teleportWarmup > 0 && !teleRunners.containsKey(p.getName()) && !RoyalCommands.hasPerm(p, "rcmds.exempt.teleportwarmup")) {
                makeTeleportRunner(p, e);
                return "";
            } else if (RoyalCommands.instance.teleportWarmup > 0 && teleRunners.containsKey(p.getName()) && !teleAllowed.contains(p.getName())) {
                return "";
            }
        }
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

    /**
     * Kicks a player.
     *
     * @param p      Player to kick
     * @param reason Reason for kick
     */
    public static void kickPlayer(Player p, String reason) {
        p.kickPlayer(reason);
    }

    /**
     * Schedules a player kick via the Bukkit scheduler. Will run as soon as a spot frees for the event.
     *
     * @param p      Player to kick
     * @param reason Reason for kick
     * @throws IllegalArgumentException If p or reason is null
     * @throws NullPointerException     If method could not get the RoyalCommands plugin to schedule with via Bukkit
     */
    public static void scheduleKick(final Player p, final String reason) throws IllegalArgumentException, NullPointerException {
        if (p == null) throw new IllegalArgumentException("Player cannot be null!");
        if (reason == null) throw new IllegalArgumentException("Reason cannot be null!");
        Runnable r = new Runnable() {
            @Override
            public void run() {
                p.kickPlayer(reason);
            }
        };
        Plugin plugin = RoyalCommands.instance;
        if (plugin == null)
            throw new NullPointerException("Could not get the RoyalCommands plugin.");
        RoyalCommands.instance.getServer().getScheduler().scheduleSyncDelayedTask(plugin, r);
    }

    /**
     * Gets an ItemStack from an alias and an amount.
     *
     * @param alias  Alias of the item name
     * @param amount Amount of the item to be in the stack
     * @return ItemStack
     * @throws InvalidItemNameException If item alias is not valid
     * @throws NullPointerException     If ItemNameManager is not loaded
     */
    public static ItemStack getItemFromAlias(String alias, int amount) throws InvalidItemNameException, NullPointerException {
        ItemStack toRet;
        if (RoyalCommands.inm == null)
            throw new NullPointerException("ItemNameManager is not loaded!");
        toRet = getItem(RoyalCommands.inm.getIDFromAlias(alias), amount);
        if (toRet == null)
            throw new InvalidItemNameException(alias + " is not a valid alias!");
        return toRet;
    }

    public static String getMVWorldName(World w) {
        if (w == null) throw new NullPointerException("w can't be null!");
        if (!RoyalCommands.multiverseNames || RoyalCommands.mvc == null)
            return RoyalCommands.wm.getConfig().getString("worlds." + w.getName() + ".displayname", w.getName());
        return RoyalCommands.mvc.getMVWorldManager().getMVWorld(w).getColoredWorldString();
    }

    public static void silentKick(final Player t, final String reason) {
        t.kickPlayer(reason + "\00-silent");
    }

    /**
     * Gets the size of an iterator.
     *
     * @param i Iterator
     * @return Size
     */
    public static int getSize(Iterator i) {
        int size = 0;
        while (i.hasNext()) {
            size++;
            i.next();
        }
        return size;
    }

    public static boolean downloadFile(String s, String saveto) {
        try {
            URL website = new URL(s);
            ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            File save = new File(saveto);
            if (!save.exists()) {
                save.getParentFile().mkdirs();
                save.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(saveto);
            while (fos.getChannel().transferFrom(rbc, 0, 1 << 24) > 0) {
            }
            fos.flush();
            fos.close();
            rbc.close();
            return true;
        } catch (MalformedURLException e) {
            RoyalCommands.instance.getLogger().severe("Could not download " + s + ": " + e.getMessage());
        } catch (IOException e) {
            RoyalCommands.instance.getLogger().severe("Could not download " + s + ": " + e.getMessage());
        }
        return false;
    }

    /**
     * Gets a world via its real name, Multiverse name, or WorldManager name.
     *
     * @param name Name of world to get
     * @return World or null if none exists
     */
    public static World getWorld(String name) {
        World w;
        w = Bukkit.getWorld(name);
        if (w != null) return w;
        if (RoyalCommands.mvc != null) {
            MultiverseWorld mvw = RoyalCommands.mvc.getMVWorldManager().getMVWorld(name);
            w = (mvw == null) ? null : mvw.getCBWorld();
            if (w != null) return w;
        }
        w = RoyalCommands.wm.getWorld(name);
        return w;
    }

    public static String getRChatPrefix(final Player p) {
        try {
            Class.forName("org.royaldev.royalchat.DataManager");
        } catch (ClassNotFoundException e) {
            return null;
        }
        RoyalChat rc = (RoyalChat) Bukkit.getPluginManager().getPlugin("RoyalChat");
        String prefix = rc.dm.getPrefix(p);
        if (prefix.isEmpty()) prefix = null;
        return prefix;
    }

    public static String getRChatSuffix(final Player p) {
        try {
            Class.forName("org.royaldev.royalchat.DataManager");
        } catch (ClassNotFoundException e) {
            return null;
        }
        RoyalChat rc = (RoyalChat) Bukkit.getPluginManager().getPlugin("RoyalChat");
        String suffix = rc.dm.getSuffix(p);
        if (suffix.isEmpty()) suffix = null;
        return suffix;
    }

    public static String replaceVars(final String orig, final Player p) {
        String repld = orig;
        repld = repld.replace("{name}", p.getName()).replace("{dispname}", p.getDisplayName()).replace("{world}", getMVWorldName(p.getWorld()));
        try {
            repld = repld.replace("{group}", RoyalCommands.permission.getPrimaryGroup(p));
        } catch (Exception ignored) {
        }
        try {
            repld = repld.replace("{prefix}", RoyalCommands.chat.getPlayerPrefix(p));
        } catch (Exception ignored) {
            String prefix = getRChatPrefix(p);
            if (prefix != null) repld = repld.replace("{prefix}", prefix);
        }
        try {
            repld = repld.replace("{suffix}", RoyalCommands.chat.getPlayerSuffix(p));
        } catch (Exception ignored) {
            String suffix = getRChatSuffix(p);
            if (suffix != null) repld = repld.replace("{suffix}", suffix);
        }
        return repld;
    }

    /**
     * Gets the message shown to other players on the server when someone is disconnected (kicked).
     *
     * @param format Format of the message
     * @param reason Reason for disconnect
     * @param kicked Person disconnected
     * @param kicker Person who caused disconnect
     * @return Formatted string
     */
    public static String getInGameMessage(final String format, final String reason, final OfflinePlayer kicked, final CommandSender kicker) {
        if (reason == null || kicked == null || kicker == null) return null;
        String message = format;
        message = colorize(message);
        if (kicked.isOnline())
            message = message.replace("{kdispname}", ((Player) kicked).getDisplayName());
        else
            message = message.replace("{kdispname}", kicked.getName());
        message = message.replace("{kname}", kicked.getName());
        message = message.replace("{name}", kicker.getName());
        if (kicker instanceof Player)
            message = message.replace("{dispname}", ((Player) kicker).getDisplayName());
        else
            message = message.replace("{dispname}", kicker.getName());
        message = message.replace("{reason}", reason);
        return message;
    }

    /**
     * Gets the message shown to a player on disconnect.
     *
     * @param message Format of the message
     * @param reason  Reason for disconnect.
     * @param kicker  Person who caused disconnect.
     * @return Formatted string
     */
    public static String getMessage(final String message, final String reason, final CommandSender kicker) {
        String format = message;
        format = colorize(format);
        if (kicker instanceof Player)
            format = format.replace("{dispname}", ((Player) kicker).getDisplayName());
        else
            format = format.replace("{dispname}", kicker.getName());
        format = format.replace("{name}", kicker.getName());
        format = format.replace("{reason}", reason);
        return format;
    }

    public static String getMessage(final String message, final String reason, final String kicker) {
        String format = message;
        format = colorize(format);
        format = format.replace("{dispname}", kicker);
        format = format.replace("{name}", kicker);
        format = format.replace("{reason}", reason);
        return format;
    }

    public static boolean isIPBanned(Player p) {
        return Bukkit.getIPBans().contains(p.getAddress().getAddress().toString().replace("/", ""));
    }

    public static boolean isIPBanned(String ip) {
        return Bukkit.getIPBans().contains(ip);
    }

    private static boolean isInt(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    private static boolean isInt(char c) {
        try {
            Integer.parseInt(String.valueOf(c));
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    /**
     * Returns the amount of seconds from a string like "6y5d4h3m2s"
     *
     * @param format String like "5y4d3h2m1s"
     * @return -1 if no numbers or incorrect format, the number provided if no letters, and the seconds if correct format
     */
    public static int timeFormatToSeconds(String format) {
        format = format.toLowerCase();
        if (!format.contains("y") && !format.contains("d") && !format.contains("h") && !format.contains("m") && !format.contains("s")) {
            if (isInt(format)) return Integer.valueOf(format);
            return -1;
        }
        String nums = "";
        int num;
        int seconds = 0;
        for (int i = 0; i < format.length(); i++) {
            char c = format.charAt(i);
            if (isInt(c)) {
                nums += c;
                continue;
            }
            if (nums.isEmpty()) return -1; // this will happen if someone enters 5dd3h, etc. (invalid format)
            switch (c) {
                case 'y':
                    num = Integer.valueOf(nums);
                    seconds += num * 31556926;
                    nums = "";
                    break;
                case 'd':
                    num = Integer.valueOf(nums);
                    seconds += num * 86400;
                    nums = "";
                    break;
                case 'h':
                    num = Integer.valueOf(nums);
                    seconds += num * 3600;
                    nums = "";
                    break;
                case 'm':
                    num = Integer.valueOf(nums);
                    seconds += num * 60;
                    nums = "";
                    break;
                case 's':
                    num = Integer.valueOf(nums);
                    seconds += num;
                    nums = "";
                    break;
                default:
                    return -1;
            }
        }
        return seconds + 1; // must add +1 to make parseTimeFormat() or whatever be accurate
    }

    /**
     * Gets enchantments from form "name:level,..." (e.g. "damage_all:2,durability:1")
     *
     * @param enchant String of enchantment
     * @return Map of Enchantments and their levels or null if invalid
     */
    public static Map<Enchantment, Integer> getEnchantments(String enchant) {
        final Map<Enchantment, Integer> enchants = new HashMap<Enchantment, Integer>();
        for (String enc : enchant.split(",")) {
            enc = enc.replace(" ", "");
            String[] data = enc.split(":");
            if (data.length < 2) return null;
            String name = data[0];
            int lvl;
            try {
                lvl = Integer.parseInt(data[1]);
            } catch (NumberFormatException e) {
                continue;
            }
            Enchantment e = Enchantment.getByName(name.toUpperCase());
            if (e == null) continue;
            enchants.put(e, lvl);
        }
        return enchants;
    }

    /**
     * Renames an ItemStack.
     *
     * @param is      ItemStack to rename
     * @param newName Name to give ItemStack
     * @return ItemStack with new name
     */
    public static ItemStack renameItem(ItemStack is, String newName) {
        if (newName == null) return is;
        CraftItemStack cis;
        try { // This allows one to use newly created Bukkit ItemStacks and ItemStacks already with CIS components
            cis = (CraftItemStack) is;
        } catch (ClassCastException e) {
            cis = new CraftItemStack(is);
        }
        net.minecraft.server.v1_4_5.ItemStack nms = cis.getHandle();
        NBTTagCompound tag = nms.tag;
        if (tag == null) tag = new NBTTagCompound();
        NBTTagCompound display = tag.getCompound("display");
        if (display == null) display = new NBTTagCompound();
        display.setString("Name", newName);
        tag.setCompound("display", display);
        nms.tag = tag;
        return cis;
    }

    /**
     * Adds lore to an ItemStack.
     *
     * @param is      ItemStack to add lore to
     * @param newLore Lore to add
     * @return ItemStack with added lore
     */
    public static ItemStack addLore(ItemStack is, String newLore) {
        CraftItemStack cis;
        try {
            cis = (CraftItemStack) is;
        } catch (ClassCastException e) {
            cis = new CraftItemStack(is);
        }
        net.minecraft.server.v1_4_5.ItemStack nmsis = cis.getHandle();
        if (nmsis.tag == null) nmsis.tag = new NBTTagCompound();
        NBTTagCompound display = nmsis.tag.getCompound("display");
        if (display == null) display = new NBTTagCompound();
        NBTTagList nbttl = (display.getList("Lore") == null) ? new NBTTagList("Lore") : display.getList("Lore");
        String[] lores = newLore.split("\\n");
        for (String l : lores) nbttl.add(new NBTTagString("", RUtils.colorize(l)));
        display.set("Lore", nbttl);
        nmsis.tag.set("display", display);
        return cis;
    }

    /**
     * Clears lore on an ItemStack.
     *
     * @param is ItemStack to clear lore from
     * @return ItemStack with no lore
     */
    public static ItemStack clearLore(ItemStack is) {
        CraftItemStack cis;
        try {
            cis = (CraftItemStack) is;
        } catch (ClassCastException e) {
            cis = new CraftItemStack(is);
        }
        net.minecraft.server.v1_4_5.ItemStack nmsis = cis.getHandle();
        if (nmsis.tag == null) nmsis.tag = new NBTTagCompound();
        NBTTagCompound display = nmsis.tag.getCompound("display");
        if (display == null) display = new NBTTagCompound();
        display.set("Lore", new NBTTagList("Lore"));
        nmsis.tag.set("display", display);
        return cis;
    }
}
