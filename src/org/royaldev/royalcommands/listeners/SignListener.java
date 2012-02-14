package org.royaldev.royalcommands.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.rcommands.*;

public class SignListener implements Listener {

    RoyalCommands plugin;

    public SignListener(RoyalCommands instance) {
        this.plugin = instance;
    }

    public Double getCharge(String line) {
        if (!line.isEmpty()) {
            Double amount;
            try {
                amount = Double.parseDouble(line);
            } catch (Exception ex) {
                return null;
            }
            if (amount < 0) {
                return null;
            }
            return amount;
        }
        return (double) -1;
    }

    @EventHandler()
    public void onInt(PlayerInteractEvent e) {
        if (e.isCancelled()) return;
        if (e.getClickedBlock() == null) return;
        if (!(e.getClickedBlock().getState() instanceof Sign)) return;
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Sign s = (Sign) e.getClickedBlock().getState();
        if (e.getPlayer() == null) return;
        Player p = e.getPlayer();
        String line1 = ChatColor.stripColor(s.getLine(0));
        String line2 = ChatColor.stripColor(s.getLine(1));
        String line3 = ChatColor.stripColor(s.getLine(2));
        String line4 = ChatColor.stripColor(s.getLine(3));

        //Warp signs
        if (line1.equalsIgnoreCase("[warp]")) {
            if (!plugin.isAuthorized(p, "rcmds.sign.use.warp")) {
                RUtils.dispNoPerms(p);
                s.setLine(0, "");
                return;
            }
            if (line2.isEmpty()) {
                s.setLine(0, ChatColor.RED + line1);
                p.sendMessage(ChatColor.RED + "No warp specified.");
                return;
            }

            Double charge = getCharge(line3);
            if (charge == null) {
                p.sendMessage(ChatColor.RED + "The cost is invalid!");
                s.setLine(0, ChatColor.RED + line1);
                return;
            } else if (charge >= 0) {
                s.setLine(2, ChatColor.DARK_GREEN + line3);
                boolean did = RUtils.chargePlayer(p, charge);
                if (!did) return;
            }

            Location warpLoc = Warp.pWarp(p, plugin, line2.toLowerCase());
            if (warpLoc == null) {
                return;
            }
            Back.backdb.put(p, p.getLocation());
            p.teleport(warpLoc);
        }

        //Time signs
        if (line1.equalsIgnoreCase(ChatColor.stripColor("[time]"))) {
            if (!plugin.isAuthorized(p, "rcmds.sign.use.time")) {
                RUtils.dispNoPerms(p);
                s.setLine(0, "");
                return;
            }
            if (line2.isEmpty() || (Time.getValidTime(line2) == null)) {
                s.setLine(0, ChatColor.RED + line1);
                p.sendMessage(ChatColor.RED + "Invalid time specified!");
                return;
            }

            Double charge = getCharge(line3);
            if (charge == null) {
                p.sendMessage(ChatColor.RED + "The cost is invalid!");
                s.setLine(0, ChatColor.RED + line1);
                return;
            } else if (charge >= 0) {
                s.setLine(2, ChatColor.DARK_GREEN + line3);
                boolean did = RUtils.chargePlayer(p, charge);
                if (!did) return;
            }

            long time = Time.getValidTime(line2);
            p.getWorld().setTime(time);
        }

        //Disposal signs
        if (line1.equalsIgnoreCase("[disposal]")) {
            if (!plugin.isAuthorized(p, "rcmds.sign.use.disposal")) {
                RUtils.dispNoPerms(p);
                s.setLine(0, "");
                return;
            }

            Double charge = getCharge(line2);
            if (charge == null) {
                p.sendMessage(ChatColor.RED + "The cost is invalid!");
                s.setLine(0, ChatColor.RED + line1);
                return;
            } else if (charge >= 0) {
                s.setLine(1, ChatColor.DARK_GREEN + line2);
                boolean did = RUtils.chargePlayer(p, charge);
                if (!did) return;
            }

            RUtils.showEmptyChest(p);
        }

        //Heal signs
        if (line1.equalsIgnoreCase("[heal]")) {
            if (!plugin.isAuthorized(p, "rcmds.sign.use.heal")) {
                RUtils.dispNoPerms(p);
                s.setLine(0, "");
                return;
            }

            Double charge = getCharge(line2);
            if (charge == null) {
                p.sendMessage(ChatColor.RED + "The cost is invalid!");
                s.setLine(0, ChatColor.RED + line1);
                return;
            } else if (charge >= 0) {
                s.setLine(1, ChatColor.DARK_GREEN + line2);
                boolean did = RUtils.chargePlayer(p, charge);
                if (!did) return;
            }

            p.setHealth(20);
        }

        //Weather signs
        if (line1.equalsIgnoreCase("[weather]")) {
            if (!plugin.isAuthorized(p, "rcmds.sign.use.weather")) {
                RUtils.dispNoPerms(p);
                s.setLine(0, "");
                return;
            }
            Double charge = getCharge(line3);
            if (charge == null) {
                p.sendMessage(ChatColor.RED + "The cost is invalid!");
                s.setLine(0, ChatColor.RED + line1);
                return;
            } else if (charge >= 0) {
                s.setLine(2, ChatColor.DARK_GREEN + line3);
                boolean did = RUtils.chargePlayer(p, charge);
                if (!did) return;
            }
            Weather.changeWeather(p, line2.trim());
        }

        //Give signs
        if (line1.equalsIgnoreCase("[give]")) {
            if (!plugin.isAuthorized(p, "rcmds.sign.use.give")) {
                RUtils.dispNoPerms(p);
                s.setLine(0, "");
                return;
            }
            int amount;
            try {
                amount = Integer.parseInt(line3);
            } catch (Exception ex) {
                p.sendMessage(ChatColor.RED + "The amount was not a valid number!");
                return;
            }
            Double charge = getCharge(line4);
            if (charge == null) {
                p.sendMessage(ChatColor.RED + "The cost is invalid!");
                s.setLine(0, ChatColor.RED + line1);
                return;
            } else if (charge >= 0) {
                s.setLine(3, ChatColor.DARK_GREEN + line4);
                boolean did = RUtils.chargePlayer(p, charge);
                if (!did) return;
            }
            Give.giveItemStandalone(p, plugin, line2, amount);
        }

        //Command signs
        if (line1.equalsIgnoreCase("[command]")) {
            if (!plugin.isAuthorized(p, "rcmds.sign.use.command")) {
                RUtils.dispNoPerms(p);
                s.setLine(0, "");
                return;
            }

            Double charge = getCharge(line3);
            if (charge == null) {
                p.sendMessage(ChatColor.RED + "The cost is invalid!");
                s.setLine(0, ChatColor.RED + line1);
                return;
            } else if (charge >= 0) {
                s.setLine(2, ChatColor.DARK_GREEN + line3);
                boolean did = RUtils.chargePlayer(p, charge);
                if (!did) return;
            }
            if (line2.isEmpty()) {
                s.setLine(0, ChatColor.RED + line1);
                p.sendMessage(ChatColor.RED + "No command specified!");
                return;
            }
            p.performCommand(line2.trim());
        }
    }

    @EventHandler()
    public void onSignChange(SignChangeEvent e) {
        if (e.isCancelled()) return;
        if (e.getPlayer() == null) return;
        Player p = e.getPlayer();
        String line1 = ChatColor.stripColor(e.getLine(0));
        String line2 = ChatColor.stripColor(e.getLine(1));
        String line3 = ChatColor.stripColor(e.getLine(2));
        String line4 = ChatColor.stripColor(e.getLine(3));

        //Warp signs
        if (line1.equalsIgnoreCase("[warp]")) {
            if (!plugin.isAuthorized(p, "rcmds.sign.warp")) {
                RUtils.dispNoPerms(p);
                return;
            }
            if (line2.isEmpty()) {
                e.setLine(0, ChatColor.RED + line1);
                p.sendMessage(ChatColor.RED + "No warp specified.");
                return;
            }

            Double charge = getCharge(line3);
            if (charge == null) {
                p.sendMessage(ChatColor.RED + "The cost is invalid!");
                e.setLine(0, ChatColor.RED + line1);
                return;
            } else if (charge >= 0) {
                e.setLine(2, ChatColor.DARK_GREEN + line3);
            }

            Location warpLoc = Warp.pWarp(p, plugin, line2.toLowerCase());
            if (warpLoc == null) {
                e.setLine(0, ChatColor.RED + line1);
                return;
            }
            e.setLine(0, ChatColor.BLUE + line1);
            p.sendMessage(ChatColor.BLUE + "Warp sign created successfully.");
        }

        //Time signs
        if (line1.equalsIgnoreCase("[time]")) {
            if (!plugin.isAuthorized(p, "rcmds.sign.time")) {
                RUtils.dispNoPerms(p);
                return;
            }
            if (line2.isEmpty() || (Time.getValidTime(line2)) == null) {
                e.setLine(0, ChatColor.RED + line1);
                return;
            }

            Double charge = getCharge(line3);
            if (charge == null) {
                p.sendMessage(ChatColor.RED + "The cost is invalid!");
                e.setLine(0, ChatColor.RED + line1);
                return;
            } else if (charge >= 0) {
                e.setLine(2, ChatColor.DARK_GREEN + line3);
            }

            e.setLine(0, ChatColor.BLUE + line1);
            p.sendMessage(ChatColor.BLUE + "Time sign created successfully!");
        }

        //Disposal signs
        if (line1.equalsIgnoreCase("[disposal]")) {
            if (!plugin.isAuthorized(p, "rcmds.sign.disposal")) {
                RUtils.dispNoPerms(p);
                return;
            }

            Double charge = getCharge(line2);
            if (charge == null) {
                p.sendMessage(ChatColor.RED + "The cost is invalid!");
                e.setLine(0, ChatColor.RED + line1);
                return;
            } else if (charge >= 0) {
                e.setLine(1, ChatColor.DARK_GREEN + line2);
            }

            e.setLine(0, ChatColor.BLUE + line1);
            p.sendMessage(ChatColor.BLUE + "Disposal sign created successfully!");
        }

        //Heal signs
        if (line1.equalsIgnoreCase("[heal]")) {
            if (!plugin.isAuthorized(p, "rcmds.sign.heal")) {
                RUtils.dispNoPerms(p);
                return;
            }

            Double charge = getCharge(line2);
            if (charge == null) {
                p.sendMessage(ChatColor.RED + "The cost is invalid!");
                e.setLine(0, ChatColor.RED + line1);
                return;
            } else if (charge >= 0) {
                e.setLine(1, ChatColor.DARK_GREEN + line2);
            }

            e.setLine(0, ChatColor.BLUE + line1);
            p.sendMessage(ChatColor.BLUE + "Heal sign created successfully!");
        }

        //Weather signs
        if (line1.equalsIgnoreCase("[weather]")) {
            if (!plugin.isAuthorized(p, "rcmds.sign.weather")) {
                RUtils.dispNoPerms(p);
                return;
            }

            Double charge = getCharge(line3);
            if (charge == null) {
                p.sendMessage(ChatColor.RED + "The cost is invalid!");
                e.setLine(0, ChatColor.RED + line1);
                return;
            } else if (charge >= 0) {
                e.setLine(2, ChatColor.DARK_GREEN + line3);
            }

            boolean valid = Weather.validWeather(line2.trim());
            if (!valid) {
                p.sendMessage(ChatColor.RED + "Invalid weather condition!");
                e.setLine(0, ChatColor.RED + line1);
                return;
            }
            e.setLine(0, ChatColor.BLUE + line1);
            p.sendMessage(ChatColor.BLUE + "Weather sign created successfully!");
        }

        //Give signs
        if (line1.equalsIgnoreCase("[give]")) {
            if (!plugin.isAuthorized(p, "rcmds.sign.give")) {
                RUtils.dispNoPerms(p);
                return;
            }

            Double charge = getCharge(line4);
            if (charge == null) {
                p.sendMessage(ChatColor.RED + "The cost is invalid!");
                e.setLine(0, ChatColor.RED + line1);
                return;
            } else if (charge >= 0) {
                e.setLine(3, ChatColor.DARK_GREEN + line4);
            }

            boolean valid = Give.validItem(line2.trim());
            if (!valid) {
                p.sendMessage(ChatColor.RED + "That item is invalid!");
                e.setLine(0, ChatColor.RED + line1);
                return;
            }
            try {
                Integer.parseInt(line3);
            } catch (Exception ex) {
                p.sendMessage(ChatColor.RED + "The amount was not a valid number!");
                return;
            }
            e.setLine(0, ChatColor.BLUE + line1);
            p.sendMessage(ChatColor.BLUE + "Give sign created successfully!");
        }

        //Command signs
        if (line1.equalsIgnoreCase("[command]")) {
            if (!plugin.isAuthorized(p, "rcmds.sign.command")) {
                RUtils.dispNoPerms(p);
                return;
            }

            Double charge = getCharge(line3);
            if (charge == null) {
                p.sendMessage(ChatColor.RED + "The cost is invalid!");
                e.setLine(0, ChatColor.RED + line1);
                return;
            } else if (charge >= 0) {
                e.setLine(2, ChatColor.DARK_GREEN + line3);
            }
            if (line2.isEmpty()) {
                e.setLine(0, ChatColor.RED + line1);
                p.sendMessage(ChatColor.RED + "No command specified!");
                return;
            }
            p.sendMessage(ChatColor.BLUE + "Command sign created successfully.");
            e.setLine(0, ChatColor.BLUE + line1);
        }
    }

}
