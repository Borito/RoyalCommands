package org.royaldev.royalcommands.rcommands;

import mkremins.fanciful.FancyMessage;
import net.minecraft.util.org.apache.commons.lang3.ArrayUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.libs.com.google.gson.Gson;
import org.royaldev.royalcommands.AuthorizationHandler;
import org.royaldev.royalcommands.Config;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public abstract class BaseCommand implements CommandExecutor {
    final RoyalCommands plugin;
    /**
     * The AuthorizationHandler for this command. This is essentially an alias of this.plugin.ah.
     */
    final AuthorizationHandler ah;
    private final String name;
    private final boolean checkPermissions;

    /**
     * Constructs a BaseCommand. This command has some backend utilities to help speed up command development and make
     * usage better overall.
     *
     * @param instance         The instance of RoyalCommands that this command is being registered with
     * @param name             The name of this command
     * @param checkPermissions Whether to check permissions using the auto-generated permission
     */
    public BaseCommand(final RoyalCommands instance, final String name, final boolean checkPermissions) {
        this.plugin = instance;
        this.ah = this.plugin.ah;
        this.name = name;
        this.checkPermissions = checkPermissions;
    }

    /**
     * Gets a link to a Hastebin paste with the given content.
     *
     * @param paste Content to paste
     * @return Link to Hastebin paste with the given content
     * @throws IOException Upon any issue
     */
    private String hastebin(String paste) throws IOException {
        final URL obj = new URL("http://hastebin.com/documents");
        final HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);
        final DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(paste);
        wr.flush();
        wr.close();
        final BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        final StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) response.append(inputLine);
        in.close();
        final HastebinData hd = new Gson().fromJson(response.toString(), HastebinData.class);
        return "http://hastebin.com/" + hd.getKey() + ".txt";
    }

    /**
     * Schedules a thread-safe implementation of {@link #hastebin(String)}.
     *
     * @param cs    CommandSender to send the generated link to
     * @param paste Content to paste
     */
    private void scheduleErrorHastebin(final CommandSender cs, final String paste) {
        this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, new Runnable() {
            @Override
            public void run() {
                String tempURL = null;
                if (Config.hastebinErrors) {
                    try {
                        tempURL = BaseCommand.this.hastebin(paste);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        tempURL = null;
                    }
                }
                final String url = tempURL;
                BaseCommand.this.plugin.getServer().getScheduler().runTask(BaseCommand.this.plugin, new Runnable() {
                    @Override
                    public void run() {
                        if (url != null) {
                            BaseCommand.this.plugin.getLogger().warning("Error paste: " + url);
                            // @formatter:off
                            new FancyMessage("Click ")
                                    .color(MessageColor.NEGATIVE._())
                                .then("here")
                                    .color(MessageColor.NEUTRAL._())
                                    .tooltip("Click here to find out more.")
                                    .link(url)
                                .then(" to find out more.")
                                    .color(MessageColor.NEGATIVE._())
                                .send(cs);
                            // @formatter:on
                        } else {
                            new FancyMessage(Config.hastebinErrors ? "An error occurred while trying to paste the stack trace." : "Error pasting is disabled.").color(MessageColor.NEGATIVE._()).send(cs);
                        }
                    }
                });
            }
        });
    }

    /**
     * Gets the CommandArguments from the given arguments. This allows for flags to be used.
     *
     * @param args Arguments to generate CommandArguments from
     * @return CommandArguments
     * @see org.royaldev.royalcommands.rcommands.CACommand
     */
    CommandArguments getCommandArguments(String[] args) {
        return new CommandArguments(args);
    }

    void handleException(CommandSender cs, Command cmd, String label, String[] args, Throwable t) {
        this.handleException(cs, cmd, label, args, t, "An exception occurred while processing that command.");
    }

    /**
     * Handles an exception. Generates a useful debug paste and sends it to the user if enabled. Also prints the stack
     * trace to the console and tells the user that an exception occurred.
     *
     * @param cs      The CommandSender using the command
     * @param cmd     The Command being used
     * @param label   The label of the command (alias)
     * @param args    The arguments passed to the command
     * @param message Message to be shown about the exception
     * @param t       The exception thrown
     */
    void handleException(CommandSender cs, Command cmd, String label, String[] args, Throwable t, String message) {
        new FancyMessage(message).color(MessageColor.NEGATIVE._()).send(cs);
        t.printStackTrace();
        if (Config.hastebinErrors) {
            final StringBuilder sb = new StringBuilder();
            sb
                    // @formatter:off
                    .append("An error occurred while handling a command. Please report this to jkcclemens or WizardCM.\n")
                    .append("They are available at #bukkit @ irc.royaldev.org. If you don't know what that means, then\n")
                    .append("go to the following URL: https://irc.royaldev.org/#bukkit\n\n")
                    .append("---DEBUG INFO---\n\n");
                    // @formatter:on
            if (cs != null) {
                sb
                        // @formatter:off
                        .append("CommandSender\n")
                        .append("\tName:\t\t").append(cs.getName()).append("\n")
                        .append("\tClass:\t\t").append(cs.getClass().getName());
                        // @formatter:on
            } else sb.append("CommandSender:\t\tnull");
            if (cmd != null) {
                sb
                        // @formatter:off
                        .append("\n\nCommand\n")
                        .append("\tName:\t\t").append(cmd.getName()).append("\n")
                        .append("\tClass:\t\t").append(cmd.getClass().getName());
                        // @formatter:on
            } else sb.append("\n\nCommand:\t\tnull");
            sb.append("\n\nLabel:\t\t").append(label);
            sb.append("\n\nArguments");
            if (args != null) {
                for (final String arg : args) sb.append("\n").append("\t").append(arg);
            } else sb.append("\t\tnull");
            final StringWriter sw = new StringWriter();
            t.printStackTrace(new PrintWriter(sw));
            sb.append("\n\n---STRACK TRACE---\n\n").append(sw.toString());
            this.scheduleErrorHastebin(cs, sb.toString());
        }
    }

    /**
     * The real onCommand that will be called by Bukkit. This checks for the command name to match, permissions if
     * specified, and runs the
     * {@link #runCommand(org.bukkit.command.CommandSender, org.bukkit.command.Command, String, String[])} method. If
     * any exception is unhandled in that method, it will be handled and displayed to the user in a friendly format.
     * <p/>
     * Due to the nature of this class, this method cannot be overridden.
     *
     * @param cs    The CommandSender using the command
     * @param cmd   The Command being used
     * @param label The label of the command (alias)
     * @param args  The arguments passed to the command
     * @return true to not display usage, false to display usage (essentially)
     */
    @Override
    public final boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase(this.name)) return false;
        if (this.checkPermissions && !this.ah.isAuthorized(cs, cmd)) {
            RUtils.dispNoPerms(cs, new String[]{this.ah.getPermission(cmd)}); // ensure calling to varargs method
            return true;
        }
        try {
            return this.runCommand(cs, cmd, label, args);
        } catch (Throwable t) {
            this.handleException(cs, cmd, label, args, t);
            return true;
        }
    }

    /**
     * The body of the command to be run. Depending on the constructor
     * ({@link #BaseCommand(org.royaldev.royalcommands.RoyalCommands, String, boolean)}), permissions will have already
     * been checked. The command name matching the name of this command is already checked. All unhandled exceptions
     * will be caught and displayed to the user in a friendly format.
     *
     * @param cs    The CommandSender using the command
     * @param cmd   The Command being used
     * @param label The label of the command (alias)
     * @param args  The arguments passed to the command
     * @return true to not display usage, false to display usage (essentially)
     */
    abstract boolean runCommand(CommandSender cs, Command cmd, String label, String[] args);

    /**
     * /**
     * Schedules a thread-safe implementation of {@link #hastebin(String)}.
     *
     * @param cs            CommandSender to send the generated link to
     * @param paste         Content to paste
     * @param messageBefore Message before the URL
     * @param urlMessage    Message for the URL
     * @param messageAfter  Message after the URL
     * @param urlTooltip    Tooltip for the URL (can be null for none)
     */
    protected void scheduleHastebin(final CommandSender cs, final String paste, final String messageBefore, final String urlMessage, final String messageAfter, final String urlTooltip) {
        this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, new Runnable() {
            @Override
            public void run() {
                String tempURL = null;
                if (Config.hastebinGeneral) {
                    try {
                        tempURL = BaseCommand.this.hastebin(paste);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        tempURL = null;
                    }
                }
                final String url = tempURL;
                BaseCommand.this.plugin.getServer().getScheduler().runTask(BaseCommand.this.plugin, new Runnable() {
                    @Override
                    public void run() {
                        if (url != null) {
                            BaseCommand.this.plugin.getLogger().info("Paste: " + url);
                            final FancyMessage fm = new FancyMessage(messageBefore).then(urlMessage);
                            if (urlTooltip != null) fm.tooltip(urlTooltip);
                            fm.link(url).then(messageAfter).send(cs);
                        } else {
                            new FancyMessage(Config.hastebinGeneral ? "An error occurred while trying to paste." : "Pasting is disabled.").color(MessageColor.NEGATIVE._()).send(cs);
                        }
                    }
                });
            }
        });
    }

    private class HastebinData {
        @SuppressWarnings("UnusedDeclaration")
        private String key;

        public String getKey() {
            return this.key;
        }
    }

    /**
     * A class that contains flags and their parameters, along with extra parameters.
     */
    class CommandArguments extends HashMap<String, String[]> {

        private String[] extraParameters = new String[0];

        CommandArguments(final String[] givenArguments) {
            this.processArguments(givenArguments);
        }

        CommandArguments(final String givenArguments) {
            this(givenArguments.split(" "));
        }

        /**
         * Gets the name of the given flag. This strips the beginning hyphen or double-hyphen.
         *
         * @param s Flag
         * @return Flag name
         */
        private String getFlagName(String s) {
            if (!this.isFlag(s)) throw new IllegalArgumentException("Not a flag.");
            return s.substring(s.length() > 2 && s.substring(1).startsWith("-") ? 2 : 1);
        }

        /**
         * Gets if this argument is a flag. This will not match the flag terminator.
         *
         * @param s Argument
         * @return If the argument is a flag
         */
        private boolean isFlag(String s) {
            return s.startsWith("-") && !this.isFlagTerminator(s);
        }

        /**
         * Gets if the argument is the flag terminator. In this implementation, this can occur multiple times.
         *
         * @param s Argument
         * @return If the argument is the flag terminator
         */
        private boolean isFlagTerminator(String s) {
            return s.equals("--");
        }

        /**
         * Gets any parameters that did not belong to a flag.
         *
         * @return Extra parameters
         */
        String[] getExtraParameters() {
            return this.extraParameters.clone();
        }

        /**
         * Gets any parameters that belonged to the given flags.
         *
         * @param flags Flags to get parameters for
         * @return Parameters
         */
        String[] getFlag(String... flags) {
            final List<String> combinedParameters = new ArrayList<>();
            for (String flag : flags) {
                if (!this.containsKey(flag)) continue;
                combinedParameters.addAll(Arrays.asList(this.get(flag)));
            }
            return combinedParameters.toArray(new String[combinedParameters.size()]);
        }

        /**
         * Gets the result of {@link #getFlag(String...)} joined by a space.
         *
         * @param flags Flags to get parameters for
         * @return Space-joined string of {@link #getFlag(String...)}
         */
        String getFlagString(String... flags) {
            return RUtils.join(this.getFlag(flags), " ");
        }

        /**
         * The same as {@link #hasFlag(String...)}, except that it checks if the result of
         * {@link #getFlagString(String...)} is not empty.
         *
         * @param flags Flags to check for
         * @return boolean
         */
        boolean hasContentFlag(String... flags) {
            for (final String flag : flags) {
                if (!this.containsKey(flag)) continue;
                if (this.getFlagString(flag).trim().isEmpty()) continue;
                return true;
            }
            return false;
        }

        /**
         * Checks if flags are set. Useful for boolean flags.
         *
         * @param flags Flags to check for
         * @return boolean
         */
        boolean hasFlag(String... flags) {
            for (final String flag : flags) {
                if (!this.containsKey(flag)) continue;
                return true;
            }
            return false;
        }

        /**
         * Processes additional arguments for this instance.
         *
         * @param arguments Arguments to process
         */
        void processArguments(String[] arguments) {
            String currentFlag = null;
            final List<String> parameters = new ArrayList<>();
            final List<String> extraParameters = new ArrayList<>();
            for (String arg : arguments) {
                if (this.isFlag(arg) || this.isFlagTerminator(arg)) {
                    this.put(currentFlag, parameters.toArray(new String[parameters.size()]));
                    parameters.clear();
                    currentFlag = this.isFlagTerminator(arg) ? null : this.getFlagName(arg);
                    continue;
                }
                arg = arg.replace("\\-", "-");
                if (currentFlag != null) parameters.add(arg);
                else extraParameters.add(arg);
            }
            this.put(currentFlag, parameters.toArray(new String[parameters.size()])); // last arg can't be neglected
            this.extraParameters = ArrayUtils.addAll(this.extraParameters, extraParameters.toArray(new String[extraParameters.size()]));
        }
    }
}
