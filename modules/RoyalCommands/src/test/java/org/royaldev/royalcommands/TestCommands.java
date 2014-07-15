package org.royaldev.royalcommands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.Test;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.royaldev.royalcommands.rcommands.ReflectCommand;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertTrue;

public class TestCommands {

    private Set<Class<?>> allClasses = null;
    private YamlConfiguration pluginYml = null;

    private Set<Class<?>> getAllCommandClasses() {
        if (this.allClasses == null) {
            final List<ClassLoader> classLoadersList = new LinkedList<>();
            classLoadersList.add(ClasspathHelper.contextClassLoader());
            classLoadersList.add(ClasspathHelper.staticClassLoader());
            final Reflections r = new Reflections(new ConfigurationBuilder().setScanners(new SubTypesScanner(false), new ResourcesScanner()).setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[classLoadersList.size()]))).filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix("org.royaldev.royalcommands.rcommands"))));
            this.allClasses = r.getSubTypesOf(Object.class);
        }
        return this.allClasses;
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
        final Set<Class<?>> classes = this.getAllCommandClasses();
        for (final Class<?> clazz : classes) {
            if (!clazz.getSimpleName().startsWith("Cmd")) continue; // not a command, so ignore
            assertTrue(clazz.getName() + " is not annotated with ReflectCommand!", clazz.isAnnotationPresent(ReflectCommand.class));
            clazz.getMethod("onCommand", CommandSender.class, Command.class, String.class, String[].class);
        }
    }

    @Test
    public void testRegistration() {
        final YamlConfiguration pluginYml = this.getPluginYml();
        final ConfigurationSection reflectCommands = pluginYml.getConfigurationSection("reflectcommands");
        final List<String> registeredClasses = new ArrayList<>();
        for (String key : reflectCommands.getKeys(false)) {
            if (reflectCommands.isSet(key + ".class")) registeredClasses.add(reflectCommands.getString(key + ".class"));
        }
        final Set<Class<?>> classes = this.getAllCommandClasses();
        for (final Class<?> clazz : classes) {
            if (!clazz.getSimpleName().startsWith("Cmd")) continue;
            assertTrue(clazz.getName() + " is not registered in the plugin.yml!", registeredClasses.contains(clazz.getSimpleName()));
        }
    }

    @Test
    public void testAnnotation() {
        final Set<Class<?>> classes = this.getAllCommandClasses();
        for (final Class<?> clazz : classes) {
            if (!clazz.getSimpleName().startsWith("Cmd")) continue;
            assertTrue(clazz.getName() + " does not have a ReflectCommand annotation.", clazz.isAnnotationPresent(ReflectCommand.class));
        }
    }

}
