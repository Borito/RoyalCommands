package org.royaldev.royalcommands.rcommands;

import org.royaldev.royalcommands.RoyalCommands;

public abstract class SubCommand<T extends BaseCommand> extends TabCommand {

    private final T parent;
    private final String name, description, usage;
    private final String[] aliases;
    private final boolean checkPermissions;
    private final Short[] completionTypes;

    protected SubCommand(final RoyalCommands instance, final T parent, final String name, final boolean checkPermissions, final String description, final String usage, final String[] aliases, final Short[] cts) {
        super(instance, parent.getName() + "." + name, checkPermissions, cts);
        this.parent = parent;
        this.name = name;
        this.description = description;
        this.usage = usage;
        this.aliases = aliases;
        this.checkPermissions = checkPermissions;
        this.completionTypes = cts;
    }

    protected boolean checkPermissions() {
        return this.checkPermissions;
    }

    protected String[] getAliases() {
        return this.aliases;
    }

    protected Short[] getCompletionTypes() {
        return this.completionTypes;
    }

    protected String getDescription() {
        return this.description;
    }

    @Override
    public String getName() {
        return this.parent.getName() + "." + this.name;
    }

    protected T getParent() {
        return this.parent;
    }

    protected String getShortName() {
        return this.name;
    }

    protected String getUsage() {
        return this.usage;
    }
}
