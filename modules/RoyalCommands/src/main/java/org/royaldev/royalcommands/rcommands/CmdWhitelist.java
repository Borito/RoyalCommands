package org.royaldev.royalcommands.rcommands;

import org.royaldev.royalcommands.Config;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.rcommands.whitelist.SCmdAdd;
import org.royaldev.royalcommands.rcommands.whitelist.SCmdCheck;
import org.royaldev.royalcommands.rcommands.whitelist.SCmdRemove;

@ReflectCommand
public class CmdWhitelist extends ParentCommand {

    public CmdWhitelist(final RoyalCommands instance, final String name) {
        super(instance, name, false);
        this.addSubCommand(new SCmdAdd(this.plugin, this));
        this.addSubCommand(new SCmdCheck(this.plugin, this));
        this.addSubCommand(new SCmdRemove(this.plugin, this));
    }

    public void reloadWhitelist() {
        Config.whitelist = this.plugin.whl.getStringList("whitelist");
    }
}
