RoyalCommands
=============

RoyalCommands is a set of EXTRA commands made for primary use on my server (RoyalCraft, royalcraf.tk), with many suggestions coming from my staff.

This plugin is not intended to replace Essentials or AdminCmd, etc. It is intended to be used alongside them.

I am very much open for new command ideas.

RoyalCommands is coded in Eclipse 3.7.1 with the following libraries:

 * bukkit-1.0.0-RC1-SNAPSHOT.jar

### Contact

 * Email: jkc.clemens@gmail.com
 * Minecraft username: jkcclemens

### Caveats

Unless you can turn off [PLAYER_COMMAND] in the Essentials config, you will need to compile it for yourself to stop it from sending duplicate messages with this plugin.
RoyalCommands sends its own [PLAYER_COMMAND] for every command sent to the server, and plugins that already do this will make duplicate entries. If you choose to
compile Essentials for yourself, remove the following from Essentials.class (line 313 at time of writing):

	LOGGER.log(Level.INFO, String.format("[PLAYER_COMMAND] %s: /%s %s ", ((Player)sender).getName(), commandLabel, EssentialsCommand.getFinalArg(args, 0)));

### Bleeding Edge Builds

Changes pushed to GitHub will be auto-compiled on [Jenkins](http://royalcraftci.no-ip.org:8080).