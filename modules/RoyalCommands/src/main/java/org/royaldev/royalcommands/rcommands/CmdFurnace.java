/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.royaldev.royalcommands.rcommands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.block.Furnace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.FurnaceInventory;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

@ReflectCommand
public class CmdFurnace extends TabCommand {

    public final Map<UUID, Furnace> furnacedb = new HashMap<>();

    public CmdFurnace(final RoyalCommands instance, final String name) {
        super(instance, name, true, new Short[]{CompletionType.LIST.getShort()});
    }
	
    @Override
    protected List<String> customList(final CommandSender cs, final Command cmd, final String label, final String[] args, final String arg) {
        return new ArrayList<>(Arrays.asList("set", "show"));
    }

    @Override
    public boolean runCommand(final CommandSender cs, final Command cmd, final String label, final String[] args, CommandArguments ca) {
        if (!(cs instanceof Player)) {
            cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
            return true;
        }
        if (args.length < 1) {
            cs.sendMessage(cmd.getDescription());
            return false;
        }
        Player p = (Player) cs;
        String command = args[0].toLowerCase();
        switch (command) {
            case "set": {
                if (!(RUtils.getTarget(p).getState() instanceof Furnace)) {
                    cs.sendMessage(MessageColor.NEGATIVE + "That's not a furnace!");
                    return true;
                }
                Furnace f = (Furnace) RUtils.getTarget(p).getState();
                this.furnacedb.put(p.getUniqueId(), f);
                cs.sendMessage(MessageColor.POSITIVE + "Furnace set.");
                return true;
            }
            case "show": {
                if (!this.furnacedb.containsKey(p.getUniqueId())) {
                    cs.sendMessage(MessageColor.NEGATIVE + "You must first set a furnace!");
                    return true;
                }
                Furnace f = this.furnacedb.get(p.getUniqueId());
                if (!(f.getBlock().getState() instanceof Furnace)) {
                    cs.sendMessage(MessageColor.NEGATIVE + "The furnace is no longer there!");
                    return true;
                }
                f = (Furnace) f.getBlock().getState();
                FurnaceInventory fi = f.getInventory();
                p.openInventory(fi);
                cs.sendMessage(MessageColor.POSITIVE + "Opened your furnace for you.");
                return true;
            }
            default:
                cs.sendMessage(MessageColor.NEGATIVE + "Try " + MessageColor.NEUTRAL + "/" + label + MessageColor.POSITIVE + ".");
                return true;
        }
    }
}
