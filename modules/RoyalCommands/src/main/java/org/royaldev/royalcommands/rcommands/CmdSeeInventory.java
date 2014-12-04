package org.royaldev.royalcommands.rcommands;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.royaldev.royalcommands.AuthorizationHandler.PermType;
import org.royaldev.royalcommands.Config;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.WorldManager;

@ReflectCommand
public class CmdSeeInventory extends BaseCommand {

    public CmdSeeInventory(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] args) {
        if (args.length < 1) {
            cs.sendMessage(cmd.getDescription());
            return false;
        }
        if (!(cs instanceof Player)) {
            cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
            return true;
        }
        Player p = (Player) cs;
        Player t = this.plugin.getServer().getPlayer(args[0]);
        if (t == null && args.length > 1) {
            if (!Config.separateInv) {
                cs.sendMessage(MessageColor.NEGATIVE + "Cannot open offline players' inventories unless inventory separation is on!");
                return true;
            }
            OfflinePlayer op = RUtils.getOfflinePlayer(args[0]);
            String world = args[1];
            if (this.plugin.getServer().getWorld(world) == null) {
                cs.sendMessage(MessageColor.NEGATIVE + "Invalid world!");
                return true;
            }
            final Inventory i = WorldManager.il.getOfflinePlayerInventory(op, world);
            if (i == null) {
                cs.sendMessage(MessageColor.NEGATIVE + "Could not find an inventory!");
                return true;
            }
            String possessive = (op.getName().toLowerCase().endsWith("s")) ? "'" : "'s";
            cs.sendMessage(MessageColor.POSITIVE + "Opened " + MessageColor.NEUTRAL + op.getName() + possessive + MessageColor.POSITIVE + " inventory.");
            p.openInventory(i);
            return true;
        }
        if (t == null || this.plugin.isVanished(t, cs)) {
            cs.sendMessage(MessageColor.NEGATIVE + "That player doesn't exist!");
            return true;
        }
        if (this.ah.isAuthorized(t, cmd, PermType.EXEMPT)) {
            cs.sendMessage(MessageColor.NEGATIVE + "You cannot see that player's inventory.");
            return true;
        }
        final Inventory i = this.plugin.getServer().createInventory(new InvSeeHolder(), InventoryType.PLAYER, t.getName());
        i.setContents(t.getInventory().getContents());
        p.openInventory(i);
        String possessive = (t.getName().toLowerCase().endsWith("s")) ? "'" : "'s";
        cs.sendMessage(MessageColor.POSITIVE + "Opened " + MessageColor.NEUTRAL + t.getName() + possessive + MessageColor.POSITIVE + " inventory.");
        return true;
    }

    public class InvSeeHolder implements InventoryHolder {

        @Override
        public Inventory getInventory() {
            return null;
        }
    }
}
