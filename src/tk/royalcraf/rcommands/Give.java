package tk.royalcraf.rcommands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import tk.royalcraf.royalcommands.RoyalCommands;

public class Give implements CommandExecutor {

	RoyalCommands plugin;

	public Give(RoyalCommands plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("give")) {
			if (!plugin.isAuthorized(cs, "rcmds.give")) {
				cs.sendMessage(ChatColor.RED
						+ "You don't have permission for that!");
				plugin.log.warning("[RoyalCommands] " + cs.getName()
						+ " was denied access to the command!");
				return true;
			} else {
				if (args.length < 2) {
					return false;
				} else if (args.length < 3) {
					Player target = plugin.getServer()
							.getPlayer(args[0].trim());
					if (target == null) {
						cs.sendMessage(ChatColor.RED
								+ "That player is not online!");
						return true;
					} else {
						String called = args[1];
						String data = null;
						if (called.contains(":")) {
							String[] calleds = called.split(":");
							called = calleds[0].trim();
							data = calleds[1].trim();
						}
						Integer iblock = null;
						try {
							iblock = Integer.parseInt(called);
						} catch (Exception e) {
							try {
								iblock = Material.getMaterial(
										called.trim().replace(" ", "_")
												.toUpperCase()).getId();
							} catch (Exception e2) {
								cs.sendMessage(ChatColor.RED
										+ "That block does not exist!");
								return true;
							}
						}
						if (iblock != 0) {
							ItemStack toInv = null;
							if (data != null) {
								if (Material.getMaterial(iblock) == null) {
									cs.sendMessage(ChatColor.RED
											+ "Invalid item ID!");
									return true;
								}
								int data2 = -1;
								try {
									data2 = Integer.parseInt(data);
								} catch (Exception e) {
									cs.sendMessage(ChatColor.RED
											+ "The metadata was invalid!");
									return true;
								}
								if (data2 < 0) {
									cs.sendMessage(ChatColor.RED
											+ "The metadata was invalid!");
									return true;
								} else {
									toInv = new ItemStack(Material.getMaterial(
											iblock).getId(),
											plugin.defaultStack, (short) data2);
								}
							} else {
								toInv = new ItemStack(Material.getMaterial(
										iblock).getId(), plugin.defaultStack);
							}
							target.getInventory().addItem(toInv);
							cs.sendMessage(ChatColor.BLUE
									+ "Giving "
									+ ChatColor.GRAY
									+ plugin.defaultStack
									+ ChatColor.BLUE
									+ " of "
									+ ChatColor.GRAY
									+ Material.getMaterial(iblock).toString()
											.toLowerCase().replace("_", " ")
									+ ChatColor.BLUE + " to " + ChatColor.GRAY
									+ target.getName() + ChatColor.BLUE + ".");
							target.sendMessage(ChatColor.BLUE
									+ "You have been given "
									+ ChatColor.GRAY
									+ plugin.defaultStack
									+ ChatColor.BLUE
									+ " of "
									+ ChatColor.GRAY
									+ Material.getMaterial(iblock).toString()
											.toLowerCase().replace("_", " ")
									+ ChatColor.BLUE + ".");
							return true;
						} else {
							cs.sendMessage(ChatColor.RED
									+ "You cannot spawn air!");
							return true;
						}
					}
				} else if (args.length == 3) {
					Player target = plugin.getServer().getPlayer(args[0]);
					if (target == null) {
						cs.sendMessage(ChatColor.RED
								+ "That player is not online!");
						return true;
					}
					String called = args[1];
					Integer amount = null;
					String data = null;
					if (called.contains(":")) {
						String[] calleds = called.split(":");
						called = calleds[0].trim();
						data = calleds[1].trim();
					}
					try {
						amount = Integer.parseInt(args[2]);
					} catch (Exception e) {
						cs.sendMessage(ChatColor.RED
								+ "The amount was not a number!");
						return true;
					}
					if (amount > 64) {
						amount = 64;
					}
					if (amount < 0) {
						amount = 1;
					}
					Integer iblock = null;
					try {
						iblock = Integer.parseInt(called);
					} catch (Exception e) {
						try {
							iblock = Material.getMaterial(
									called.trim().replace(" ", "_")
											.toUpperCase()).getId();
						} catch (Exception e2) {
							cs.sendMessage(ChatColor.RED
									+ "That block does not exist!");
							return true;
						}
					}
					if (iblock != 0) {
						ItemStack toInv = null;
						if (data != null) {
							if (Material.getMaterial(iblock) == null) {
								cs.sendMessage(ChatColor.RED
										+ "Invalid item ID!");
								return true;
							}
							int data2 = -1;
							try {
								data2 = Integer.parseInt(data);
							} catch (Exception e) {
								cs.sendMessage(ChatColor.RED
										+ "The metadata was invalid!");
								return true;
							}
							if (data2 < 0) {
								cs.sendMessage(ChatColor.RED
										+ "The metadata was invalid!");
								return true;
							} else {
								toInv = new ItemStack(Material.getMaterial(
										iblock).getId(), amount, (short) data2);
							}
						} else {
							toInv = new ItemStack(Material.getMaterial(iblock)
									.getId(), amount);
						}
						target.getInventory().addItem(toInv);
						cs.sendMessage(ChatColor.BLUE
								+ "Giving "
								+ ChatColor.GRAY
								+ amount
								+ ChatColor.BLUE
								+ " of "
								+ ChatColor.GRAY
								+ Material.getMaterial(iblock).toString()
										.toLowerCase().replace("_", " ")
								+ ChatColor.BLUE + " to " + ChatColor.GRAY
								+ target.getName() + ChatColor.BLUE + ".");
						target.sendMessage(ChatColor.BLUE
								+ "You have been given "
								+ ChatColor.GRAY
								+ amount
								+ ChatColor.BLUE
								+ " of "
								+ ChatColor.GRAY
								+ Material.getMaterial(iblock).toString()
										.toLowerCase().replace("_", " ")
								+ ChatColor.BLUE + ".");
						return true;
					} else {
						cs.sendMessage(ChatColor.RED + "You cannot spawn air!");
						return true;
					}
				}
			}
		}
		return false;
	}
}
