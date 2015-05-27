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

public class HelpItem extends GUIItem {

    public HelpItem() {
        super(
            Material.PAPER,
            MessageColor.RESET + "Help",
            Arrays.asList(
                MessageColor.NEUTRAL + "Gives you a book about trades."
            )
        );
    }
}
