/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.royaldev.royalcommands.rcommands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

@ReflectCommand
public class CmdSignEdit extends TabCommand {

    public CmdSignEdit(final RoyalCommands instance, final String name) {
        super(instance, name, true, new Short[]{CompletionType.LIST.getShort()});
    }

    @Override
    protected List<String> customList(final CommandSender cs, final Command cmd, final String label, final String[] args, final String arg) {
        return new ArrayList<>(Arrays.asList("1", "2", "3", "4"));
    }
	
    @Override
    public boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] args, CommandArguments ca) {
        if (!(cs instanceof Player)) {
            cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
            return true;
        }
        Player p = (Player) cs;
        if (args.length < 1) {
            cs.sendMessage(cmd.getDescription());
            return false;
        }
        Block b = p.getTargetBlock((Set<Material>) null, 100); // RUtils has signs as transparent;
        if (b == null || !(b.getState() instanceof Sign)) {
            cs.sendMessage(MessageColor.NEGATIVE + "The block in sight is not a sign!");
            return true;
        }
            /*if (!plugin.canAccessChest(p, b)) {
                cs.sendMessage(MessageColor.NEGATIVE + "You cannot access that sign!");
                return true;
            }*/
        Sign s = (Sign) b.getState();
        int lineNumber;
        try {
            lineNumber = Integer.parseInt(args[0]);
            lineNumber--;
        } catch (NumberFormatException e) {
            cs.sendMessage(MessageColor.NEGATIVE + "The line number was not a number!");
            return true;
        }
        if (lineNumber < 0 || lineNumber > 3) {
            cs.sendMessage(MessageColor.NEGATIVE + "The line number can't be less than one or greater than four.");
            return true;
        }
        if (args.length < 2) {
            s.setLine(lineNumber, "");
            s.update();
            cs.sendMessage(MessageColor.POSITIVE + "Cleared line " + MessageColor.NEUTRAL + (lineNumber + 1) + MessageColor.POSITIVE + ".");
            return true;
        }
        String text = RoyalCommands.getFinalArg(args, 1);
        if (this.ah.isAuthorized(cs, "rcmds.signedit.color")) text = RUtils.colorize(text);
        s.setLine(lineNumber, text);
        s.update();
        cs.sendMessage(MessageColor.POSITIVE + "Set line " + MessageColor.NEUTRAL + (lineNumber + 1) + MessageColor.POSITIVE + ".");
        return true;
    }
}
