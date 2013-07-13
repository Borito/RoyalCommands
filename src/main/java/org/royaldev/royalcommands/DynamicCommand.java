package org.royaldev.royalcommands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class DynamicCommand extends Command implements PluginIdentifiableCommand {

    private final CommandExecutor ce;
    public Plugin plugin;
    public String[] perms = new String[0];

    public DynamicCommand(List<String> aliases, String name, String description, String usage, String[] perms, String permMessage, CommandExecutor ce, Plugin plugin) {
        super(name, description, usage, aliases);
        this.ce = ce;
        this.plugin = plugin;
        if (perms.length > 0) setPermissions(perms);
        if (!permMessage.trim().isEmpty()) setPermissionMessage(MessageColor.NEGATIVE + permMessage);
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        return ce.onCommand(sender, this, label, args);
    }

    public void setPermissions(String[] permissions) {
        this.perms = permissions;
        super.setPermission(StringUtils.join(permissions, ";"));
    }

    @Override
    public Plugin getPlugin() {
        return plugin;
    }
}
