package org.royaldev.royalcommands.rcommands;

import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.rcommands.worldmanager.SCmdCreate;
import org.royaldev.royalcommands.rcommands.worldmanager.SCmdDelete;
import org.royaldev.royalcommands.rcommands.worldmanager.SCmdInfo;
import org.royaldev.royalcommands.rcommands.worldmanager.SCmdList;
import org.royaldev.royalcommands.rcommands.worldmanager.SCmdLoad;
import org.royaldev.royalcommands.rcommands.worldmanager.SCmdTeleport;
import org.royaldev.royalcommands.rcommands.worldmanager.SCmdUnload;
import org.royaldev.royalcommands.rcommands.worldmanager.SCmdWho;

import java.util.Random;

@ReflectCommand
public class CmdWorldManager extends ParentCommand {

    private final Random r = new Random();

    public CmdWorldManager(final RoyalCommands instance, final String name) {
        super(instance, name, false);
        this.addSubCommand(new SCmdCreate(this.plugin, this));
        this.addSubCommand(new SCmdDelete(this.plugin, this));
        this.addSubCommand(new SCmdInfo(this.plugin, this));
        this.addSubCommand(new SCmdList(this.plugin, this));
        this.addSubCommand(new SCmdLoad(this.plugin, this));
        this.addSubCommand(new SCmdTeleport(this.plugin, this));
        this.addSubCommand(new SCmdUnload(this.plugin, this));
        this.addSubCommand(new SCmdWho(this.plugin, this));
    }

    public Random getRandom() {
        return this.r;
    }
}
