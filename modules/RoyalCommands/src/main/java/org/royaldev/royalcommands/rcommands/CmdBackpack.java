package org.royaldev.royalcommands.rcommands;

import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.royaldev.royalcommands.AuthorizationHandler.PermType;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

@ReflectCommand
public class CmdBackpack extends BaseCommand {

    public CmdBackpack(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player)) {
            cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
            return true;
        }
        Player p = (Player) cs;
        if (args.length > 0) {
            if (!this.ah.isAuthorized(cs, cmd, PermType.OTHERS)) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (args.length < 2) {
                cs.sendMessage(MessageColor.NEGATIVE + "No world specified.");
                return true;
            }
            World w = this.plugin.getServer().getWorld(args[1]);
            if (w == null) {
                cs.sendMessage(MessageColor.NEGATIVE + "No such world!");
                return true;
            }
            final OfflinePlayer t = RUtils.getOfflinePlayer(args[0]);
            if (this.ah.isAuthorized(t, cmd, PermType.EXEMPT)) {
                RUtils.dispNoPerms(cs, MessageColor.NEGATIVE + "You cannot access that player's backpack!");
                return true;
            }
            final Inventory i = RUtils.getBackpack(t.getUniqueId(), w);
            if (i == null) {
                cs.sendMessage(MessageColor.NEGATIVE + "That player does not exist!");
                return true;
            }
            p.openInventory(i);
            return true;
        }
        final Inventory i = RUtils.getBackpack(p);
        p.openInventory(i);
        return true;
    }
}
