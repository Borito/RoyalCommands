package org.royaldev.royalcommands.configuration;

import java.util.UUID;

public interface PlayerConfiguration extends GeneralConfiguration {

    boolean createFile();

    void discard();

    void discard(boolean save);

    boolean exists();

    void forceSave();

    UUID getManagerPlayerUUID();

    boolean isFirstJoin();

    void setFirstJoin(boolean firstJoin);
}
