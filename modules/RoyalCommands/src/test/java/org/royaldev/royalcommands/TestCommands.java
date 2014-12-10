package org.royaldev.royalcommands;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.Test;
import org.reflections.Reflections;
import org.royaldev.royalcommands.rcommands.BaseCommand;
import org.royaldev.royalcommands.rcommands.ReflectCommand;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertTrue;

public class TestCommands {

    private Set<Class<? extends BaseCommand>> allCommands = null;
    private YamlConfiguration pluginYml = null;

    private Set<Class<? extends BaseCommand>> getAllCommandClasses() {
        if (this.allCommands == null) {
            final Reflections r = new Reflections("org.royaldev.royalcommands.rcommands");
            this.allCommands = r.getSubTypesOf(BaseCommand.class);
        }
        return this.allCommands;
    }

    private YamlConfiguration getPluginYml() {
        if (this.pluginYml == null) {
            final File pluginFile = new File("src/main/resources/plugin.yml");
            assertTrue("No plugin.yml found!", pluginFile.exists());
            this.pluginYml = YamlConfiguration.loadConfiguration(pluginFile);
        }
        return this.pluginYml;
    }

    @Test
    public void testValidity() throws Throwable {
        final Set<Class<? extends BaseCommand>> classes = this.getAllCommandClasses();
        for (final Class<?> clazz : classes) {
            if (!clazz.getSimpleName().startsWith("Cmd")) continue; // not a command, so ignore
            assertTrue(clazz.getName() + " is not annotated with ReflectCommand!", clazz.isAnnotationPresent(ReflectCommand.class));
        }
    }

    @Test
    public void testRegistration() {
        final YamlConfiguration pluginYml = this.getPluginYml();
        final ConfigurationSection reflectCommands = pluginYml.getConfigurationSection("reflectcommands");
        final List<String> registeredClasses = new ArrayList<>();
        for (final String key : reflectCommands.getKeys(false)) {
            if (reflectCommands.isSet(key + ".class")) registeredClasses.add(reflectCommands.getString(key + ".class"));
        }
        final Set<Class<? extends BaseCommand>> classes = this.getAllCommandClasses();
        for (final Class<?> clazz : classes) {
            if (!clazz.getSimpleName().startsWith("Cmd")) continue;
            assertTrue(clazz.getName() + " is not registered in the plugin.yml!", registeredClasses.contains(clazz.getSimpleName()));
        }
    }

}
