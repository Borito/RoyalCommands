package org.royaldev.royalcommands.rcommands;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.royaldev.royalcommands.AuthorizationHandler.PermType;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

@ReflectCommand
public class CmdGameMode extends BaseCommand {

    public CmdGameMode(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    /**
     * Gets a GameMode from a string. Can be in word format ("creative") or number format ("1").
     *
     * @param s String representing GameMode
     * @return GameMode
     */
    public GameMode getGameMode(String s) {
        if (s == null) return null;
        GameMode toRet;
        try {
            toRet = GameMode.valueOf(s.toUpperCase());
        } catch (IllegalArgumentException e) {
            try {
                Integer i = Integer.valueOf(s);
                if (i > 2 || i < 0) return null;
                switch (i) {
                    case 0:
                        toRet = GameMode.SURVIVAL;
                        break;
                    case 1:
                        toRet = GameMode.CREATIVE;
                        break;
                    case 2:
                        toRet = GameMode.ADVENTURE;
                        break;
                    default:
                        toRet = null;
                }
            } catch (NumberFormatException e2) {
                return null;
            }
        }
        return toRet;
    }

    @Override
    public boolean runCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player) && args.length < 1) {
            cs.sendMessage(cmd.getDescription());
            return false;
        }
        if (args.length < 1 && cs instanceof Player) {
            Player p = (Player) cs;
            GameMode toSet = (p.getGameMode().equals(GameMode.CREATIVE)) ? GameMode.SURVIVAL : GameMode.CREATIVE;
            p.setGameMode(toSet);
            p.sendMessage(MessageColor.POSITIVE + "Your game mode has been set to " + MessageColor.NEUTRAL + toSet.name().toLowerCase() + MessageColor.POSITIVE + ".");
            return true;
        }
        if (args.length > 0) {
            Player t = this.plugin.getServer().getPlayer(args[0]);
            if (t == null || this.plugin.isVanished(t, cs)) {
                cs.sendMessage(MessageColor.NEGATIVE + "That player does not exist!");
                return true;
            }
            if (!t.equals(cs) && !this.ah.isAuthorized(cs, cmd, PermType.OTHERS)) {
                cs.sendMessage(MessageColor.NEGATIVE + "You can't change other players' gamemodes!");
                return true;
            }
            if (!t.equals(cs) && this.ah.isAuthorized(t, cmd, PermType.EXEMPT)) {
                cs.sendMessage(MessageColor.NEGATIVE + "You cannot change that player's gamemode.");
                return true;
            }
            GameMode toSet = (t.getGameMode().equals(GameMode.CREATIVE)) ? GameMode.SURVIVAL : GameMode.CREATIVE;
            if (args.length > 1) {
                GameMode result = getGameMode(args[1]);
                if (result == null) {
                    cs.sendMessage(MessageColor.NEGATIVE + "Invalid gamemode!");
                    cs.sendMessage(MessageColor.NEUTRAL + RUtils.join(GameMode.values(), MessageColor.RESET + ", " + MessageColor.NEUTRAL));
                    return true;
                }
                toSet = result;
            }
            t.setGameMode(toSet);
            if (cs instanceof Player && !cs.equals(t))
                cs.sendMessage(MessageColor.POSITIVE + "You have changed " + MessageColor.NEUTRAL + t.getName() + "\'s" + MessageColor.POSITIVE + " game mode to " + MessageColor.NEUTRAL + toSet.name().toLowerCase() + MessageColor.POSITIVE + ".");
            t.sendMessage(MessageColor.POSITIVE + "Your game mode has been changed to " + MessageColor.NEUTRAL + toSet.name().toLowerCase() + MessageColor.POSITIVE + ".");
            return true;
        }
        return true;
    }
}
