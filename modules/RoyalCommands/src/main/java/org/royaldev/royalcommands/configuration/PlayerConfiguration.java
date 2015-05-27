/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
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
