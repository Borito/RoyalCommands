package org.royaldev.royalcommands;

import org.bukkit.Material;
import org.junit.Test;
import org.royaldev.royalcommands.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.logging.Logger;

import static org.junit.Assert.assertTrue;

public class TestAliases {

    @Test
    public void testAliases() throws Throwable {
        final Logger l = Logger.getLogger("org.royaldev.royalcommands");
        final File csv = new File("src/main/resources/items.csv");
        assertTrue("No items.csv found!", csv.exists());
        final Reader in = new FileReader(csv);
        final ItemNameManager inm = new ItemNameManager(new CSVReader(in).readAll());
        boolean allAliasesExist = true;
        for (final Material m : Material.values()) {
            if (inm.aliasExists(m)) continue;
            if (allAliasesExist) allAliasesExist = false;
            l.warning("Missing alias for ID " + m.getId() + ".");
        }
        assertTrue("Missing aliases!", allAliasesExist);
    }

}
