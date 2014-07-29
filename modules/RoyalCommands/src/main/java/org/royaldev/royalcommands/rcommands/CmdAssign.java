package org.royaldev.royalcommands.rcommands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.configuration.PConfManager;

import java.util.ArrayList;
import java.util.List;

@ReflectCommand
public class CmdAssign extends TabCommand {

    public CmdAssign(final RoyalCommands instance, final String name) {
        super(instance, name, true, new Integer[]{CompletionType.ANY_COMMAND.getInt()});
    }

    @Override
    public boolean runCommand(CommandSender cs, Command cmd, String label, String[] eargs, CommandArguments ca) {
        if (!(cs instanceof Player)) {
            cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
            return true;
        }
        final Player p = (Player) cs;
        final PConfManager pcm = PConfManager.getPConfManager(p);
        final ItemStack hand = p.getItemInHand();
        if (hand == null || hand.getType() == Material.AIR) {
            cs.sendMessage(MessageColor.NEGATIVE + "You can't modify commands on air!");
            return true;
        }
        List<String> cmds = RUtils.getAssignment(hand, pcm);
        if (cmds == null) cmds = new ArrayList<>();
        if (ca.hasFlag("r", "remove")) {
            final String content = ca.getFlagString("r", "remove");
            if (content.isEmpty()) {
                cs.sendMessage(MessageColor.NEGATIVE + "Please include a number to remove.");
                return true;
            }
            int toRemove;
            try {
                toRemove = Integer.parseInt(content);
            } catch (NumberFormatException e) {
                cs.sendMessage(MessageColor.NEGATIVE + "The number specified to remove was not a valid number!");
                return true;
            }
            if (toRemove <= 0 || toRemove > cmds.size()) {
                cs.sendMessage(MessageColor.NEGATIVE + "The number specified does not exist!");
                return true;
            }
            toRemove--;
            cmds.remove(toRemove);
            RUtils.setAssignment(hand, cmds, pcm);
            cs.sendMessage(MessageColor.POSITIVE + "Removed command " + MessageColor.NEUTRAL + (toRemove + 1) + MessageColor.POSITIVE + ".");
            return true;
        } else if (ca.hasFlag("l", "list")) {
            cs.sendMessage(MessageColor.POSITIVE + "Commands on " + MessageColor.NEUTRAL + RUtils.getItemName(hand) + MessageColor.POSITIVE + ":");
            if (cmds.isEmpty()) {
                cs.sendMessage(MessageColor.NEUTRAL + "None.");
                return true;
            }
            for (int i = 0; i < cmds.size(); i++)
                cs.sendMessage("  " + MessageColor.NEUTRAL + (i + 1) + MessageColor.POSITIVE + ": " + MessageColor.NEUTRAL + cmds.get(i));
            return true;
        }
        if (eargs.length < 1) {
            RUtils.removeAssignment(hand, PConfManager.getPConfManager(p));
            p.sendMessage(MessageColor.POSITIVE + "All commands removed from " + MessageColor.NEUTRAL + RUtils.getFriendlyEnumName(hand.getType()) + MessageColor.POSITIVE + ".");
            return true;
        }
        cmds.add(RoyalCommands.getFinalArg(eargs, 0));
        RUtils.setAssignment(hand, cmds, pcm);
        String message = (RoyalCommands.getFinalArg(eargs, 0).toLowerCase().startsWith("c:")) ? MessageColor.POSITIVE + "Added message " + MessageColor.NEUTRAL + RoyalCommands.getFinalArg(eargs, 0).substring(2) + MessageColor.POSITIVE + " to that item." : MessageColor.POSITIVE + "Added command " + MessageColor.NEUTRAL + "/" + RoyalCommands.getFinalArg(eargs, 0) + MessageColor.POSITIVE + " to that item.";
        p.sendMessage(message);
        return true;
    }
}
