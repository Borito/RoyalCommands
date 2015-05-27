/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.royaldev.royalcommands.rcommands.trade.guiitems;

import org.bukkit.Material;
import org.royaldev.royalcommands.MessageColor;
import org.royaldev.royalcommands.gui.inventory.GUIItem;

import java.util.Arrays;

public class RemindItem extends GUIItem {

    public RemindItem() {
        super(
            Material.BEACON,
            MessageColor.RESET + "Remind Other Party",
            Arrays.asList(
                MessageColor.NEUTRAL + "Sends a notification to the",
                MessageColor.NEUTRAL + "other party to check this trade."
            )
        );
    }
}
