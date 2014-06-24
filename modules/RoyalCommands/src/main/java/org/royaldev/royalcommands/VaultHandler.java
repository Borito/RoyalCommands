package org.royaldev.royalcommands;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultHandler {

    private static Permission permission = null;
    private static Economy economy = null;
    private static Chat chat = null;
    private final RoyalCommands plugin;

    public VaultHandler(RoyalCommands instance) {
        plugin = instance;
    }

    public boolean usingVault() {
        final Plugin p = plugin.getServer().getPluginManager().getPlugin("Vault");
        return p != null && p.isEnabled();
    }

    public Permission getPermission() {
        if (!usingVault()) return null;
        return permission;
    }

    public Chat getChat() {
        if (!usingVault()) return null;
        return chat;
    }

    public Economy getEconomy() {
        if (!usingVault()) return null;
        return economy;
    }

    public void setUpVault() {
        if (!usingVault()) return;
        this.setupEconomy();
        this.setupChat();
        this.setupPermissions();
    }

    public void removeVault() {
        permission = null;
        chat = null;
        economy = null;
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> permissionProvider = plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) permission = permissionProvider.getProvider();
        return permission != null;
    }

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> chatProvider = plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
        if (chatProvider != null) chat = chatProvider.getProvider();
        return chat != null;
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) economy = economyProvider.getProvider();
        return economy != null;
    }

}
