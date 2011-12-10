package tk.royalcraf.royalcommands;

import java.io.File;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class RoyalCommandsCommandExecutor implements CommandExecutor {

	Logger log = Logger.getLogger("Minecraft");

	// getFinalArg taken from EssentialsCommand.java - Essentials by
	// EssentialsTeam
	public static String getFinalArg(final String[] args, final int start) {
		final StringBuilder bldr = new StringBuilder();
		for (int i = start; i < args.length; i++) {
			if (i != start) {
				bldr.append(" ");
			}
			bldr.append(args[i]);
		}
		return bldr.toString();
	}

	public static boolean isAuthorized(final Player player, final String node) {
		if (player.isOp()) {
			return true;
		} else if (player.hasPermission(node)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isAuthorized(final CommandSender player,
			final String node) {
		if (player.isOp()) {
			return true;
		} else if (player.hasPermission(node)) {
			return true;
		} else if (!player.hasPermission(node)) {
			return false;
		} else {
			return false;
		}
	}

	private RoyalCommands plugin;

	public RoyalCommandsCommandExecutor(RoyalCommands plugin) {
		this.plugin = plugin;
	}

	public static boolean getOnline(final String person) {
		Player player = Bukkit.getServer().getPlayer(person);

		if (player == null) {
			return false;
		} else {
			return true;
		}

	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args) {

		Player player = null;
		Player victim = null;
		if (sender instanceof Player) {
			player = (Player) sender;
		}

		if (cmd.getName().equalsIgnoreCase("level")) {
			if (player == null) {
				sender.sendMessage(ChatColor.RED
						+ "This command can only be used by players!");
			} else {
				if (!isAuthorized(sender, "rcmds.level")) {
					sender.sendMessage(ChatColor.RED
							+ "You don't have permission for that!");
					log.warning("[RoyalCommands] " + sender.getName()
							+ " was denied access to the command!");
					return true;
				} else {
					player.setLevel(player.getLevel() + 1);
					sender.sendMessage(ChatColor.BLUE
							+ "XP level raised by one! You may need to relog to see the changes.");
					return true;
				}
			}
		} else if (cmd.getName().equalsIgnoreCase("setlevel")) {
			if (player == null) {
				sender.sendMessage(ChatColor.RED
						+ "This command can only be used by players!");
			} else {
				if (!isAuthorized(sender, "rcmds.setlevel")) {
					sender.sendMessage(ChatColor.RED
							+ "You don't have permission for that!");
					log.warning("[RoyalCommands] " + sender.getName()
							+ " was denied access to the command!");
					return true;
				} else {
					if (args.length < 1) {
						return false;
					} else {
						int toLevel = 0;
						if (args.length == 2) {
							if (getOnline(args[1]) == false) {
								sender.sendMessage(ChatColor.RED
										+ "You must input a valid player!");
							} else {
								victim = (Player) plugin.getServer().getPlayer(
										args[1]);
								try {
									toLevel = Integer.parseInt(args[0]);
								} catch (NumberFormatException e) {
									sender.sendMessage(ChatColor.RED
											+ "Your input was not an integer!");
									return false;
								}
								if (toLevel < 0) {
									sender.sendMessage(ChatColor.RED
											+ "You cannot input anything below 0.");
								} else {
									player.setLevel(toLevel);
									sender.sendMessage(ChatColor.BLUE
											+ victim.getName()
											+ "'s XP level was set to "
											+ toLevel
											+ "! They may need to relog to see the changes.");
									victim.sendMessage(ChatColor.BLUE
											+ "Your XP level was set to "
											+ toLevel
											+ " by "
											+ sender.getName()
											+ "! You may need to relog to see these changes.");
								}
							}
						} else if (args.length < 2 && args.length != 0) {
							try {
								toLevel = Integer.parseInt(args[0]);
							} catch (NumberFormatException e) {
								sender.sendMessage(ChatColor.RED
										+ "Your input was not an integer!");
								return false;
							}
							if (toLevel < 0) {
								sender.sendMessage(ChatColor.RED
										+ "You cannot input anything below 0.");
							} else {
								player.setLevel(toLevel);
								sender.sendMessage(ChatColor.BLUE
										+ "Your XP level was set to "
										+ toLevel
										+ "! You may need to relog to see the changes.");
							}
						}
					}
				}

			}
			return true;
		} else if (cmd.getName().equalsIgnoreCase("sci")) {
			if (!isAuthorized(sender, "rcmds.sci")) {
				sender.sendMessage(ChatColor.RED
						+ "You don't have permission for that!");
				log.warning("[RoyalCommands] " + sender.getName()
						+ " was denied access to the command!");
				return true;
			} else {
				int errord = 0;
				if (args.length < 2) {
					return false;
				}
				if (getOnline(args[0]) == false) {
					sender.sendMessage(ChatColor.RED
							+ "You must input an online player.");
					errord = 1;
				}
				if (errord == 0) {
					int removeID = 0;
					victim = (Player) plugin.getServer().getPlayer(args[0]);
					try {
						removeID = Integer.parseInt(args[1]);
					} catch (NumberFormatException e) {
						sender.sendMessage(ChatColor.RED
								+ "You must input a numerical ID to remove.");
						return false;
					}
					if (removeID <= 0 || removeID > 2266) {
						sender.sendMessage(ChatColor.RED
								+ "You must specify a valid item ID.");
						return true;
					} else {
						if (removeID < 2255 && removeID > 382) {
							sender.sendMessage(ChatColor.RED
									+ "You must specify a valid item ID.");
							return true;
						} else {
							if (isAuthorized(victim, "rcmds.sci.exempt")) {
								sender.sendMessage(ChatColor.RED
										+ "You cannot alter that player's inventory.");
								return true;
							} else {
								victim.getInventory().remove(removeID);
								victim.sendMessage(ChatColor.RED
										+ "You have just had all of your item ID "
										+ ChatColor.BLUE + removeID
										+ ChatColor.RED + " removed by "
										+ ChatColor.RED + sender.getName()
										+ ChatColor.BLUE + "!");
								sender.sendMessage(ChatColor.BLUE
										+ "You have just removed all of the item ID "
										+ ChatColor.RED + removeID
										+ ChatColor.BLUE + " from "
										+ ChatColor.RED + victim.getName()
										+ ChatColor.BLUE + "'s inventory.");
								return true;
							}
						}
					}
				}
			}
		} else if (cmd.getName().equalsIgnoreCase("speak")) {
			if (!isAuthorized(sender, "rcmds.speak")) {
				sender.sendMessage(ChatColor.RED
						+ "You don't have permission for that!");
				log.warning("[RoyalCommands] " + sender.getName()
						+ " was denied access to the command!");
				return true;
			} else {
				if (args.length < 2) {
					return false;
				}

				victim = (Player) plugin.getServer().getPlayer(args[0]);

				int errord = 0;
				if (getOnline(args[0]) == false) {
					sender.sendMessage(ChatColor.RED
							+ "You must input an online player.");
					errord = 1;
					return true;
				}
				if (errord == 0) {
					if (args[1].startsWith("/")) {
						sender.sendMessage(ChatColor.RED
								+ "You may not send commands!");
						return true;
					} else {
						if (isAuthorized(victim, "rcmds.speak.exempt")) {
							sender.sendMessage(ChatColor.RED
									+ "You may not make that player speak.");
							return true;
						} else {
							victim.chat(getFinalArg(args, 1));
							log.info(sender.getName()
									+ " has spoofed a message from "
									+ victim.getName() + "!");
							return true;
						}
					}
				}
			}

			return true;

		} else if (cmd.getName().equalsIgnoreCase("facepalm")) {
			if (!isAuthorized(sender, "rcmds.facepalm")) {
				sender.sendMessage(ChatColor.RED
						+ "You don't have permission for that!");
				log.warning("[RoyalCommands] " + sender.getName()
						+ " was denied access to the command!");
				return true;
			} else {
				if (args.length < 1) {
					Bukkit.getServer().broadcastMessage(
							ChatColor.YELLOW + sender.getName()
									+ ChatColor.AQUA + " has facepalmed.");
					return true;
				} else {
					if (getOnline(args[0]) == false) {
						sender.sendMessage(ChatColor.RED
								+ "That player is not online!");
						return true;
					} else {
						victim = (Player) plugin.getServer().getPlayer(args[0]);
						Bukkit.getServer().broadcastMessage(
								ChatColor.YELLOW + sender.getName()
										+ ChatColor.AQUA
										+ " has facepalmed at "
										+ ChatColor.YELLOW + victim.getName()
										+ ".");
						return true;
					}
				}
			}
		} else if (cmd.getName().equalsIgnoreCase("slap")) {
			if (!isAuthorized(sender, "rcmds.slap")) {
				sender.sendMessage(ChatColor.RED
						+ "You don't have permission for that!");
				log.warning("[RoyalCommands] " + sender.getName()
						+ " was denied access to the command!");
				return true;
			} else {
				if (args.length < 1) {
					return false;
				}
				if (getOnline(args[0]) == false) {
					sender.sendMessage(ChatColor.RED
							+ "That person is not online!");
					return true;
				} else {
					victim = (Player) plugin.getServer().getPlayer(args[0]);
					if (isAuthorized(victim, "rcmds.slap.exempt")) {
						sender.sendMessage(ChatColor.RED
								+ "You may not slap that player.");
						return true;
					} else {
						Bukkit.getServer().broadcastMessage(
								ChatColor.GOLD + sender.getName()
										+ ChatColor.WHITE + " slaps "
										+ ChatColor.RED + victim.getName()
										+ ChatColor.WHITE + "!");
						return true;
					}
				}
			}
		} else if (cmd.getName().equalsIgnoreCase("harm")) {
			if (!isAuthorized(sender, "rcmds.harm")) {
				sender.sendMessage(ChatColor.RED
						+ "You don't have permission for that!");
				log.warning("[RoyalCommands] " + sender.getName()
						+ " was denied access to the command!");
				return true;
			} else {

				if (args.length < 2) {
					return false;
				}
				if (getOnline(args[0]) == false) {
					sender.sendMessage(ChatColor.RED
							+ "That person is not online!");
					return true;
				} else {
					int toDamage = 0;
					try {
						toDamage = Integer.parseInt(args[1]);
					} catch (NumberFormatException e) {
						sender.sendMessage(ChatColor.RED
								+ "The damage must be a number between 1 and 20!");
						return false;
					}
					if (toDamage > 20 || toDamage <= 0) {
						sender.sendMessage(ChatColor.RED
								+ "The damage you entered is not within 1 and 20!");
						return true;
					} else {
						victim = (Player) plugin.getServer().getPlayer(args[0]);
						if (isAuthorized(victim, "rcmds.harm.exempt")) {
							sender.sendMessage(ChatColor.RED
									+ "You may not harm that player.");
							return true;
						} else {
							victim.damage(toDamage);
							victim.sendMessage(ChatColor.RED
									+ "You have just been damaged by "
									+ ChatColor.BLUE + sender.getName()
									+ ChatColor.RED + "!");
							sender.sendMessage(ChatColor.BLUE
									+ "You just damaged " + ChatColor.RED
									+ victim.getName() + ChatColor.BLUE + "!");
							return true;
						}
					}
				}
			}
		} else if (cmd.getName().equalsIgnoreCase("starve")) {
			if (!isAuthorized(sender, "rcmds.starve")) {
				sender.sendMessage(ChatColor.RED
						+ "You don't have permission for that!");
				log.warning("[RoyalCommands] " + sender.getName()
						+ " was denied access to the command!");
				return true;
			} else {

				if (args.length < 2) {
					return false;
				}
				if (getOnline(args[0]) == false) {
					sender.sendMessage(ChatColor.RED
							+ "That person is not online!");
					return true;
				} else {
					int toStarve = 0;
					try {
						toStarve = Integer.parseInt(args[1]);
					} catch (NumberFormatException e) {
						sender.sendMessage(ChatColor.RED
								+ "The damage must be a number between 1 and 20!");
						return false;
					}
					if (toStarve > 20 || toStarve <= 0) {
						sender.sendMessage(ChatColor.RED
								+ "The damage you entered is not within 1 and 20!");
						return true;
					} else {
						victim = (Player) plugin.getServer().getPlayer(args[0]);
						if (isAuthorized(victim, "rcmds.starve.exempt")) {
							sender.sendMessage(ChatColor.RED
									+ "You may not starve that player.");
							return true;
						} else {
							int starveLevel = victim.getFoodLevel() - toStarve;
							victim.setFoodLevel(starveLevel);
							victim.sendMessage(ChatColor.RED
									+ "You have just been starved by "
									+ ChatColor.BLUE + sender.getName()
									+ ChatColor.RED + "!");
							sender.sendMessage(ChatColor.BLUE
									+ "You just starved " + ChatColor.RED
									+ victim.getName() + ChatColor.BLUE + "!");
							return true;
						}
					}
				}
			}
		} else if (cmd.getName().equalsIgnoreCase("banned")) {
			if (!isAuthorized(sender, "rcmds.banned")) {
				sender.sendMessage(ChatColor.RED
						+ "You don't have permission for that!");
				log.warning("[RoyalCommands] " + sender.getName()
						+ " was denied access to the command!");
				return true;
			} else {
				if (args.length < 1) {
					return false;
				}
				OfflinePlayer dude = null;
				dude = (OfflinePlayer) plugin.getServer().getOfflinePlayer(
						args[0]);
				boolean banned = dude.isBanned();
				if (banned == false) {
					sender.sendMessage(ChatColor.GREEN + dude.getName()
							+ ChatColor.WHITE + " is not banned.");
					return true;
				} else {
					sender.sendMessage(ChatColor.RED + dude.getName()
							+ ChatColor.WHITE + " is banned.");
					return true;
				}
			}
		} else if (cmd.getName().equalsIgnoreCase("setarmor")) {
			if (!isAuthorized(sender, "rcmds.setarmor")) {
				sender.sendMessage(ChatColor.RED
						+ "You don't have permission for that!");
				log.warning("[RoyalCommands] " + sender.getName()
						+ " was denied access to the command!");
				return true;
			} else {
				if (args.length < 1) {
					return false;
				} else {
					String set = args[0];

					ItemStack[] diamond;
					diamond = new ItemStack[4];
					diamond[3] = new ItemStack(Material.DIAMOND_HELMET);
					diamond[2] = new ItemStack(Material.DIAMOND_CHESTPLATE);
					diamond[1] = new ItemStack(Material.DIAMOND_LEGGINGS);
					diamond[0] = new ItemStack(Material.DIAMOND_BOOTS);

					ItemStack[] gold;
					gold = new ItemStack[4];
					gold[3] = new ItemStack(Material.GOLD_HELMET);
					gold[2] = new ItemStack(Material.GOLD_CHESTPLATE);
					gold[1] = new ItemStack(Material.GOLD_LEGGINGS);
					gold[0] = new ItemStack(Material.GOLD_BOOTS);

					ItemStack[] iron;
					iron = new ItemStack[4];
					iron[3] = new ItemStack(Material.IRON_HELMET);
					iron[2] = new ItemStack(Material.IRON_CHESTPLATE);
					iron[1] = new ItemStack(Material.IRON_LEGGINGS);
					iron[0] = new ItemStack(Material.IRON_BOOTS);

					ItemStack[] leather;
					leather = new ItemStack[4];
					leather[3] = new ItemStack(Material.LEATHER_HELMET);
					leather[2] = new ItemStack(Material.LEATHER_CHESTPLATE);
					leather[1] = new ItemStack(Material.LEATHER_LEGGINGS);
					leather[0] = new ItemStack(Material.LEATHER_BOOTS);

					ItemStack[] none;
					none = new ItemStack[4];
					none[3] = new ItemStack(0);
					none[2] = new ItemStack(0);
					none[1] = new ItemStack(0);
					none[0] = new ItemStack(0);

					if (set.equalsIgnoreCase("diamond")) {
						player.getInventory().setArmorContents(diamond);
						sender.sendMessage(ChatColor.BLUE
								+ "Your armor was set to " + set + ".");
						return true;
					} else if (set.equalsIgnoreCase("gold")) {
						player.getInventory().setArmorContents(gold);
						sender.sendMessage(ChatColor.BLUE
								+ "Your armor was set to " + set + ".");
						return true;
					} else if (set.equalsIgnoreCase("iron")) {
						player.getInventory().setArmorContents(iron);
						sender.sendMessage(ChatColor.BLUE
								+ "Your armor was set to " + set + ".");
						return true;
					} else if (set.equalsIgnoreCase("leather")) {
						player.getInventory().setArmorContents(leather);
						sender.sendMessage(ChatColor.BLUE
								+ "Your armor was set to " + set + ".");
						return true;
					} else if (set.equalsIgnoreCase("none")) {
						player.getInventory().setArmorContents(none);
						sender.sendMessage(ChatColor.BLUE
								+ "Your armor was cleared.");
						return true;
					} else {
						sender.sendMessage(ChatColor.RED
								+ "The armor type must be diamond, gold, iron, leather, or none.");
						return true;
					}
				}
			}
		} else if (cmd.getName().equalsIgnoreCase("getip")) {
			if (!isAuthorized(sender, "rcmds.getip")) {
				sender.sendMessage(ChatColor.RED
						+ "You don't have permission for that!");
				return true;
			} else {
				if (plugin.getConfig().getBoolean("disable_getip") == true) {
					sender.sendMessage(ChatColor.RED
							+ "/getip and /compareip have been disabled.");
					return true;
				} else {
					if (args.length < 1) {
						return false;
					} else {
						OfflinePlayer oplayer = (OfflinePlayer) plugin
								.getServer().getOfflinePlayer(args[0]);
						File oplayerconfl = new File(plugin.getDataFolder()
								+ "/userdata/" + oplayer.getName() + ".yml");
						if (oplayerconfl.exists()) {
							FileConfiguration oplayerconf = YamlConfiguration
									.loadConfiguration(oplayerconfl);
							sender.sendMessage(ChatColor.GRAY
									+ oplayer.getName() + ": "
									+ oplayerconf.getString("ip"));
							return true;
						} else {
							sender.sendMessage(ChatColor.RED + "The player "
									+ oplayer.getName() + " does not exist.");
							return true;
						}
					}
				}
			}
		} else if (cmd.getName().equalsIgnoreCase("compareip")) {
			if (!isAuthorized(sender, "rcmds.compareip")) {
				sender.sendMessage(ChatColor.RED
						+ "You don't have permission for that!");
				return true;
			} else {
				if (plugin.getConfig().getBoolean("disable_getip") == true) {
					sender.sendMessage(ChatColor.RED
							+ "/getip and /compareip have been disabled.");
					return true;
				} else {
					if (args.length < 2) {
						return false;
					} else {
						OfflinePlayer player1 = null;
						OfflinePlayer player2 = null;
						player1 = (OfflinePlayer) plugin.getServer()
								.getOfflinePlayer(args[0]);
						player2 = (OfflinePlayer) plugin.getServer()
								.getOfflinePlayer(args[1]);

						File p1confl = new File(plugin.getDataFolder()
								+ "/userdata/" + player1.getName() + ".yml");
						File p2confl = new File(plugin.getDataFolder()
								+ "/userdata/" + player2.getName() + ".yml");
						if (p1confl.exists()) {
							if (p2confl.exists()) {
								FileConfiguration p1conf = YamlConfiguration
										.loadConfiguration(p1confl);
								FileConfiguration p2conf = YamlConfiguration
										.loadConfiguration(p2confl);
								String p1ip = p1conf.getString("ip");
								String p2ip = p2conf.getString("ip");

								sender.sendMessage(ChatColor.GRAY
										+ player1.getName() + ": " + p1ip);
								sender.sendMessage(ChatColor.GRAY
										+ player2.getName() + ": " + p2ip);
								return true;
							} else {
								sender.sendMessage(ChatColor.RED
										+ "The player " + player2.getName()
										+ " does not exist.");
								return true;
							}
						} else {
							sender.sendMessage(ChatColor.RED + "The player "
									+ player1.getName() + " does not exist.");
							return true;
						}
					}
				}
			}
		} else if (cmd.getName().equalsIgnoreCase("rcmds")) {
			if (!isAuthorized(sender, "rcmds.rcmds")) {
				sender.sendMessage(ChatColor.RED
						+ "You don't have permission for that!");
				return true;
			} else {
				plugin.reloadConfig();
				sender.sendMessage(ChatColor.GREEN + "RoyalCommands "
						+ ChatColor.BLUE + "v0.0.3 reloaded.");
				return true;
			}
		}

		return false;

	}
}
