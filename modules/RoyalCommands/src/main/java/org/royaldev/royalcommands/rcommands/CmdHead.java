package org.royaldev.royalcommands.rcommands;

import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.royaldev.royalcommands.AuthorizationHandler.PermType;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

@ReflectCommand
public class CmdHead extends BaseCommand {

    public CmdHead(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player)) {
            cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
            return true;
        }
        if (args.length < 1) {
            cs.sendMessage(cmd.getDescription());
            return false;
        }
        Player p = (Player) cs;
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        if (!(head.getItemMeta() instanceof SkullMeta)) {
            cs.sendMessage(MessageColor.NEGATIVE + "The head had incorrect item metadata!");
            return true;
        }
        SkullMeta sm = (SkullMeta) head.getItemMeta();
        final OfflinePlayer t = RUtils.getOfflinePlayer(args[0]);
        if (!t.getName().equalsIgnoreCase(p.getName()) && !this.ah.isAuthorized(cs, cmd, PermType.OTHERS)) {
            RUtils.dispNoPerms(cs);
            return true;
        }
        if (!t.getName().equalsIgnoreCase(p.getName()) && this.ah.isAuthorized(t, cmd, PermType.EXEMPT)) {
            cs.sendMessage(MessageColor.NEGATIVE + "You cannot spawn that player's head!");
            return true;
        }
        sm.setOwner(t.getName());
        head.setItemMeta(sm);
        head = RUtils.renameItem(head, "Head of " + t.getName());
        p.getInventory().addItem(head);
        cs.sendMessage(MessageColor.POSITIVE + "You have been given the head of " + MessageColor.NEUTRAL + t.getName() + MessageColor.POSITIVE + ".");
        return true;
    }
}
