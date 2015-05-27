/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.royaldev.royalcommands.shaded.com.sk89q.util.config;

/**
 * Configuration exception.
 */
public class ConfigurationException extends Exception {

    private static final long serialVersionUID = -2442886939908724203L;

    public ConfigurationException() {
        super();
    }

    public ConfigurationException(String msg) {
        super(msg);
    }
}
