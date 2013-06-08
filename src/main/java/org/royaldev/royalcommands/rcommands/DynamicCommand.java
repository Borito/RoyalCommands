package org.royaldev.royalcommands.rcommands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;

public class DynamicCommand extends Command implements PluginIdentifiableCommand {

    public CommandExecutor owner;
    public Object registeredWith;
    public Plugin owningPlugin;
    public String[] permissions = new String[0];

    public DynamicCommand(String[] aliases, String name, String description, String usage, String[] perms, String permMessage, CommandExecutor owner, Object registeredWith, Plugin plugin) {
        super(name, description, usage, Arrays.asList(aliases));
        this.owner = owner;
        this.owningPlugin = plugin;
        this.registeredWith = registeredWith;
        if (perms.length > 0) setPermissions(perms);
        if (!permMessage.trim().isEmpty()) setPermissionMessage(ChatColor.RED + permMessage);
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        return owner.onCommand(sender, this, label, args);
    }

    public void setPermissions(String[] permissions) {
        this.permissions = permissions;
        super.setPermission(StringUtils.join(permissions, ";"));
    }

    @Override
    public Plugin getPlugin() {
        return owningPlugin;
    }
}
