package org.royaldev.royalcommands;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.entity.Player;

public class AuthorizationHandler {

    private final RoyalCommands plugin;

    public AuthorizationHandler(RoyalCommands instance) {
        plugin = instance;
    }

    private boolean permissionsLoaded() {
        return plugin.vh.usingVault() && plugin.vh.getPermission() != null;
    }

    private boolean chatLoaded() {
        return plugin.vh.usingVault() && plugin.vh.getChat() != null;
    }

    private boolean economyLoaded() {
        return plugin.vh.usingVault() && plugin.vh.getEconomy() != null;
    }

    /**
     * Returns if something has the specified permission node. That something can be one of the following:
     * <ul>
     * <li>Player</li>
     * <li>OfflinePlayer</li>
     * <li>CommandSender</li>
     * <li>RemoteConsoleCommandSender</li>
     * <li>BlockCommandSender</li>
     * </ul>
     * Anything else will throw an IllegalArgumentException.
     *
     * @param o    Thing to check for permissions
     * @param node Node to check for
     * @return true or false
     * @throws IllegalArgumentException If invalid type if passed for <code>o</code>.
     */
    public boolean isAuthorized(Object o, String node) {
        if (o instanceof RemoteConsoleCommandSender)
            return iARemoteConsoleCommandSender((RemoteConsoleCommandSender) o, node);
        else if (o instanceof Player) return iAPlayer((Player) o, node);
        else if (o instanceof OfflinePlayer) return iAOfflinePlayer((OfflinePlayer) o, node);
        else if (o instanceof BlockCommandSender) return iABlockCommandSender((BlockCommandSender) o, node);
        else if (o instanceof CommandSender) return iACommandSender((CommandSender) o, node);
        else throw new IllegalArgumentException("Object was not a valid authorizable!");
    }

    private boolean iAPlayer(Player p, String node) {
        if (plugin.vh.usingVault() && permissionsLoaded()) return plugin.vh.getPermission().has(p, node);
        return p.hasPermission(node);
    }

    private boolean iAOfflinePlayer(OfflinePlayer op, String node) {
        if (op.isOnline()) return iAPlayer(op.getPlayer(), node);
        if (plugin.vh.usingVault() && permissionsLoaded()) {
            final String world = plugin.getServer().getWorlds().get(0).getName();
            return plugin.vh.getPermission().has(world, op.getName(), node);
        }
        return false;
    }

    private boolean iARemoteConsoleCommandSender(RemoteConsoleCommandSender rccs, String node) {
        return true;
    }

    private boolean iABlockCommandSender(BlockCommandSender cb, String node) {
        return true;
    }

    private boolean iACommandSender(CommandSender cs, String node) {
        if (plugin.vh.usingVault() && permissionsLoaded()) plugin.vh.getPermission().has(cs, node);
        return cs.hasPermission(node);
    }

}
