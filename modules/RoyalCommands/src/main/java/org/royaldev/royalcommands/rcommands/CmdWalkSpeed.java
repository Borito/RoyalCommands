package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RoyalCommands;

@ReflectCommand
public class CmdWalkSpeed extends BaseCommand {

    public CmdWalkSpeed(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (args.length < 1) {
            cs.sendMessage(cmd.getDescription());
            return false;
        }
        if (!(cs instanceof Player)) {
            cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
            return true;
        }
        Player p = (Player) cs;
        float walkSpeed;
        try {
            walkSpeed = Float.valueOf(args[0]);
        } catch (NumberFormatException e) {
            cs.sendMessage(MessageColor.NEGATIVE + "Please enter a valid number!");
            return true;
        }
        if (walkSpeed < -1F || walkSpeed > 1F) {
            cs.sendMessage(MessageColor.NEGATIVE + "Speed must be between -1 and 1!");
            return true;
        }
        cs.sendMessage(MessageColor.POSITIVE + "Set your walk speed to " + MessageColor.NEUTRAL + walkSpeed + MessageColor.POSITIVE + ".");
        p.setWalkSpeed(walkSpeed);
        return true;
    }
}
