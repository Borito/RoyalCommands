package org.royaldev.royalcommands.rcommands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
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
public class CmdAssign implements CommandExecutor {

    private final RoyalCommands plugin;

    public CmdAssign(RoyalCommands instance) {
        plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("assign")) {
            if (!this.plugin.ah.isAuthorized(cs, cmd)) {
                RUtils.dispNoPerms(cs);
                return true;
            }
            if (!(cs instanceof Player)) {
                cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
                return true;
            }
            if (args.length < 1) {
                Player p = (Player) cs;
                ItemStack hand = p.getItemInHand();
                if (hand == null || hand.getType() == Material.AIR) {
                    cs.sendMessage(MessageColor.NEGATIVE + "You can't remove commands from air!");
                    return true;
                }
                RUtils.removeAssignment(hand, PConfManager.getPConfManager(p));
                p.sendMessage(MessageColor.POSITIVE + "All commands removed from " + MessageColor.NEUTRAL + RUtils.getFriendlyEnumName(hand.getType()) + MessageColor.POSITIVE + ".");
                return true;
            }
            String command = args[0];
            Player p = (Player) cs;
            final PConfManager pcm = PConfManager.getPConfManager(p);
            ItemStack hand = p.getItemInHand();
            if (hand == null || hand.getType() == Material.AIR) {
                cs.sendMessage(MessageColor.NEGATIVE + "You can't assign commands to air!");
                return true;
            }
            List<String> cmds = RUtils.getAssignment(hand, pcm);
            if (cmds == null) cmds = new ArrayList<>();
            if (command.matches("\\-\\d+")) {
                int toRemove;
                try {
                    toRemove = Integer.parseInt(command.substring(1));
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
            } else if (command.equals("~")) {
                cs.sendMessage(MessageColor.POSITIVE + "Commands on " + MessageColor.NEUTRAL + RUtils.getItemName(hand) + MessageColor.POSITIVE + ":");
                if (cmds.isEmpty()) {
                    cs.sendMessage(MessageColor.NEUTRAL + "None.");
                    return true;
                }
                for (int i = 0; i < cmds.size(); i++)
                    cs.sendMessage("  " + MessageColor.NEUTRAL + (i + 1) + MessageColor.POSITIVE + ": " + MessageColor.NEUTRAL + cmds.get(i));
                return true;
            }
            cmds.add(RoyalCommands.getFinalArg(args, 0));
            RUtils.setAssignment(hand, cmds, pcm);
            String message = (RoyalCommands.getFinalArg(args, 0).toLowerCase().startsWith("c:")) ? MessageColor.POSITIVE + "Added message " + MessageColor.NEUTRAL + RoyalCommands.getFinalArg(args, 0).substring(2) + MessageColor.POSITIVE + " to that item." : MessageColor.POSITIVE + "Added command " + MessageColor.NEUTRAL + "/" + RoyalCommands.getFinalArg(args, 0) + MessageColor.POSITIVE + " to that item.";
            p.sendMessage(message);
            return true;
        }
        return false;
    }

}
