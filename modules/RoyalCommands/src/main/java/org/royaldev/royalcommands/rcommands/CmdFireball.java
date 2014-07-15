package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RoyalCommands;

@ReflectCommand
public class CmdFireball extends BaseCommand {

    public CmdFireball(final RoyalCommands instance, final String name) {
        super(instance, name, true);
    }

    @Override
    public boolean runCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!(cs instanceof Player)) {
            cs.sendMessage(MessageColor.NEGATIVE + "This command is only available to players!");
            return true;
        }
        Player p = (Player) cs;
        //Fireball fb = p.launchProjectile(Fireball.class);
        Vector dir = p.getEyeLocation().getDirection().multiply(2);
        Fireball fb = p.getWorld().spawn(p.getEyeLocation().add(dir.getX(), dir.getY(), dir.getZ()), Fireball.class);
        fb.setDirection(dir);
        //fb.teleport(p.getEyeLocation().add(dir.getX(), dir.getY(), dir.getZ()));
        fb.setIsIncendiary(true);
        return true;
    }
}
