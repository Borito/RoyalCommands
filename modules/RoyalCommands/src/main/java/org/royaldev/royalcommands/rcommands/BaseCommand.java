package org.royaldev.royalcommands.rcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.AuthorizationHandler;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

public abstract class BaseCommand implements CommandExecutor {
    final RoyalCommands plugin;
    final AuthorizationHandler ah;
    private final String name;
    private final boolean checkPermissions;

    public BaseCommand(final RoyalCommands instance, final String name, final boolean checkPermissions) {
        this.plugin = instance;
        this.ah = this.plugin.ah;
        this.name = name;
        this.checkPermissions = checkPermissions;
    }

    abstract boolean runCommand(CommandSender cs, Command cmd, String label, String[] args);

    @Override
    public final boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase(this.name)) return false;
        if (this.checkPermissions && !this.ah.isAuthorized(cs, cmd)) {
            RUtils.dispNoPerms(cs, new String[]{this.ah.getPermission(cmd)}); // ensure calling to varargs method
            return true;
        }
        return this.runCommand(cs, cmd, label, args);
    }
}
