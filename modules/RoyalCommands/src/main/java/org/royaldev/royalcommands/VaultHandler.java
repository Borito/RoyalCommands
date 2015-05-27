/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
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
        this.plugin = instance;
    }

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> chatProvider = this.plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
        if (chatProvider != null) chat = chatProvider.getProvider();
        return chat != null;
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = this.plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) economy = economyProvider.getProvider();
        return economy != null;
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> permissionProvider = this.plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) permission = permissionProvider.getProvider();
        return permission != null;
    }

    public Chat getChat() {
        if (!usingVault()) return null;
        return chat;
    }

    public Economy getEconomy() {
        if (!usingVault()) return null;
        return economy;
    }

    public Permission getPermission() {
        if (!usingVault()) return null;
        return permission;
    }

    public void removeVault() {
        permission = null;
        chat = null;
        economy = null;
    }

    public void setUpVault() {
        if (!usingVault()) return;
        this.setupEconomy();
        this.setupChat();
        this.setupPermissions();
    }

    public boolean usingVault() {
        final Plugin p = this.plugin.getServer().getPluginManager().getPlugin("Vault");
        return p != null && p.isEnabled();
    }

}
