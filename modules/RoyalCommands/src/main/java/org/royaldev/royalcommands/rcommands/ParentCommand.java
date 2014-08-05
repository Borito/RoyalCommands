package org.royaldev.royalcommands.rcommands;

import org.apache.commons.lang.ArrayUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;

import java.util.ArrayList;
import java.util.List;

public class ParentCommand extends TabCommand {

    private final List<SubCommand> subcommands = new ArrayList<>();

    public ParentCommand(final RoyalCommands instance, final String name, final boolean checkPermissions) {
        super(instance, name, checkPermissions, new Short[0]);
        this.setAlwaysUse(CompletionType.LIST);
    }

    private boolean isAuthorized(Object o, SubCommand sc) {
        return this.ah.isAuthorized(o, "rcmds." + sc.getName());
    }

    public void showHelp(CommandSender cs, String label) {
        cs.sendMessage(MessageColor.POSITIVE + "/" + label + " Help");
        final StringBuilder sb = new StringBuilder();
        sb.append(MessageColor.POSITIVE);
        for (int i = 0; i < label.length() + 6; i++) sb.append("=");
        cs.sendMessage(sb.toString());
        for (final SubCommand sc : this.subcommands) {
            if (!this.isAuthorized(cs, sc)) continue;
            cs.sendMessage("  " + MessageColor.POSITIVE + "/" + label + " " + sc.getUsage().replace("<command>", sc.getShortName()));
            cs.sendMessage("    " + MessageColor.NEUTRAL + sc.getDescription());
        }
    }

    protected void addSubCommand(SubCommand sc) {
        this.subcommands.add(sc);
    }

    @Override
    protected List<String> customList(CommandSender cs, Command cmd, String label, String[] args, String arg) {
        // It's not pretty, but it works
        final List<String> completions = new ArrayList<>();
        if (args.length > 1) {
            final SubCommand sc = this.getSubCommand(args[0]);
            if (sc == null) return completions;
            args = (String[]) ArrayUtils.subarray(args, 1, args.length);
            final int ctIndex = args.length - 1;
            if (ctIndex < 0 || ctIndex >= sc.getCompletionTypes().length) return completions;
            for (final CompletionType ct : CompletionType.getCompletionTypes(sc.getCompletionTypes()[args.length - 1])) {
                final List<String> scCompletions = sc.getCompletionsFor(cs, cmd, label, args, ct);
                if (scCompletions == null) continue;
                completions.addAll(scCompletions);
            }
        } else {
            for (final SubCommand sc : this.subcommands) {
                if (sc == null) continue;
                final String name = sc.getShortName();
                if (!this.isAuthorized(cs, sc)) continue;
                if (completions.contains(name)) continue;
                completions.add(name);
            }
        }
        return completions;
    }

    protected SubCommand getSubCommand(String name) {
        for (final SubCommand sc : this.subcommands) {
            if (sc.getShortName().equalsIgnoreCase(name)) return sc;
            for (final String alias : sc.getAliases()) {
                if (alias.equalsIgnoreCase(name)) return sc;
            }
        }
        return null;
    }

    @Override
    protected boolean runCommand(CommandSender cs, Command cmd, String label, String[] eargs, CommandArguments ca) {
        if (eargs.length < 1) {
            this.showHelp(cs, label);
            return true;
        }
        final SubCommand sc = this.getSubCommand(eargs[0]);
        if (sc == null) {
            cs.sendMessage(MessageColor.NEGATIVE + "No such subcommand.");
            return true;
        }
        if (sc.checkPermissions() && !this.isAuthorized(cs, sc)) {
            RUtils.dispNoPerms(cs, new String[]{"rcmds." + sc.getName()});
            return true;
        }
        // Exceptions already caught, since we're in a runCommand
        return sc.runCommand(cs, cmd, label, (String[]) ArrayUtils.subarray(eargs, 1, eargs.length), ca);
    }
}
