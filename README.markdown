RoyalCommands
=============

RoyalCommands is a general plugin with lots of commands. Yeah, that's my description. Go look at the [BukkitDev](http://dev.bukkit.org/server-mods/royalcommands) if you want a better one.

RoyalCommands is coded in Eclipse 3.7.1 with the following libraries:

 * bukkit-1.0.0-RC2-SNAPSHOT.jar
 * Vault.jar

### Contact

 * Email: jkc.clemens@gmail.com / clemens.kyle@me.com / jkc.clemens@royaldev.org
 * Minecraft username: jkcclemens

### Caveats

Unless you can turn off [PLAYER_COMMAND] in the Essentials config, you will need to compile it for yourself to stop it from sending duplicate messages with this plugin.
RoyalCommands sends its own [PLAYER_COMMAND] for every command sent to the server, and plugins that already do this will make duplicate entries. If you choose to
compile Essentials for yourself, remove the following from Essentials.java (line 313 at time of writing):

	LOGGER.log(Level.INFO, String.format("[PLAYER_COMMAND] %s: /%s %s ", ((Player)sender).getName(), commandLabel, EssentialsCommand.getFinalArg(args, 0)));

### Bleeding Edge Builds

Changes pushed to GitHub will be auto-compiled on [Jenkins](http://royalcraftci.no-ip.org).

### About the developer

My name is Kyle Clemens. I run a cracked [Minecraft server](http://royaldev.org) at royaldev.org, write (currently) four different plugins: RoyalCommands, RoyalChat, RoyalMessages, and RoyalMisc.
RoyalMisc is only available on RoyalCraft.

I currently attend school and hope to continue learning Java to better this plugin.

