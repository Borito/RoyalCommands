package org.royaldev.royalcommands.rcommands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.UnknownDependencyException;
import org.royaldev.royalcommands.RUtils;
import org.royaldev.royalcommands.RoyalCommands;
import org.royaldev.royalcommands.UnZip;
import org.royaldev.royalcommands.io.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CmdPluginManager implements CommandExecutor {

    RoyalCommands plugin;

    public CmdPluginManager(RoyalCommands instance) {
        plugin = instance;
    }

    public String updateCheck(String name, String currentVersion) throws Exception {
        String pluginUrlString = "http://dev.bukkit.org/server-mods/" + name.toLowerCase() + "/files.rss";
        URL url = new URL(pluginUrlString);
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(url.openConnection().getInputStream());
        doc.getDocumentElement().normalize();
        NodeList nodes = doc.getElementsByTagName("item");
        Node firstNode = nodes.item(0);
        if (firstNode.getNodeType() == 1) {
            Element firstElement = (Element) firstNode;
            NodeList firstElementTagName = firstElement.getElementsByTagName("title");
            Element firstNameElement = (Element) firstElementTagName.item(0);
            NodeList firstNodes = firstNameElement.getChildNodes();
            return firstNodes.item(0).getNodeValue();
        }
        return currentVersion;
    }

    public boolean onCommand(final CommandSender cs, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("pluginmanager")) {
            if (args.length < 1) {
                cs.sendMessage(cmd.getDescription());
                return false;
            }
            String subcmd = args[0];
            final PluginManager pm = plugin.getServer().getPluginManager();
            if (subcmd.equalsIgnoreCase("load")) {
                if (!plugin.isAuthorized(cs, "rcmds.pluginmanager.load")) {
                    RUtils.dispNoPerms(cs);
                    return true;
                }
                if (args.length < 2) {
                    cs.sendMessage(ChatColor.RED + "Please provide the name of the jar to load!");
                    return true;
                }
                File f = new File(plugin.getDataFolder().getParentFile() + File.separator + args[1]);
                if (!f.exists()) {
                    cs.sendMessage(ChatColor.RED + "That file does not exist!");
                    return true;
                }
                if (!f.canRead()) {
                    cs.sendMessage(ChatColor.RED + "Can't read that file!");
                    return true;
                }
                Plugin p;
                try {
                    p = pm.loadPlugin(f);
                    if (p == null) {
                        cs.sendMessage(ChatColor.RED + "Could not load plugin: plugin was invalid.");
                        cs.sendMessage(ChatColor.RED + "Make sure it ends with .jar!");
                        return true;
                    }
                    pm.enablePlugin(p);
                } catch (InvalidPluginException e) {
                    cs.sendMessage(ChatColor.RED + "That file is not a plugin!");
                    return true;
                } catch (UnknownDependencyException e) {
                    cs.sendMessage(ChatColor.RED + "Missing dependency: " + e.getMessage());
                    return true;
                } catch (InvalidDescriptionException e) {
                    cs.sendMessage(ChatColor.RED + "That plugin contained an invalid description!");
                    return true;
                }
                if (p.isEnabled())
                    cs.sendMessage(ChatColor.BLUE + "Loaded and enabled " + ChatColor.GRAY + p.getName() + ChatColor.BLUE + " successfully.");
                else
                    cs.sendMessage(ChatColor.RED + "Could not load and enable " + ChatColor.GRAY + p.getName() + ChatColor.RED + ".");
                return true;
            } else if (subcmd.equalsIgnoreCase("disable")) {
                if (!plugin.isAuthorized(cs, "rcmds.pluginmanager.disable")) {
                    RUtils.dispNoPerms(cs);
                    return true;
                }
                if (args.length < 2) {
                    cs.sendMessage(ChatColor.RED + "Please provide the name of the plugin to disable!");
                    return true;
                }
                Plugin p = pm.getPlugin(args[1]);
                if (p == null) {
                    cs.sendMessage(ChatColor.RED + "No such plugin!");
                    return true;
                }
                if (!p.isEnabled()) {
                    cs.sendMessage(ChatColor.GRAY + p.getName() + ChatColor.RED + "is already disabled!");
                }
                pm.disablePlugin(p);
                if (!p.isEnabled())
                    cs.sendMessage(ChatColor.BLUE + "Disabled " + ChatColor.GRAY + p.getName() + ChatColor.BLUE + " successfully!");
                else cs.sendMessage(ChatColor.RED + "Could not disabled that plugin!");
                return true;
            } else if (subcmd.equalsIgnoreCase("enable")) {
                if (!plugin.isAuthorized(cs, "rcmds.pluginmanager.enable")) {
                    RUtils.dispNoPerms(cs);
                    return true;
                }
                if (args.length < 2) {
                    cs.sendMessage(ChatColor.RED + "Please provide the name of the plugin to enable!");
                    return true;
                }
                Plugin p = pm.getPlugin(args[1]);
                if (p == null) {
                    cs.sendMessage(ChatColor.RED + "No such plugin!");
                    return true;
                }
                if (p.isEnabled()) {
                    cs.sendMessage(ChatColor.RED + "Plugin is already enabled!");
                    return true;
                }
                pm.enablePlugin(p);
                if (p.isEnabled())
                    cs.sendMessage(ChatColor.BLUE + "Successfully enabled " + ChatColor.GRAY + p.getName() + ChatColor.BLUE + "!");
                else
                    cs.sendMessage(ChatColor.RED + "Could not enable " + ChatColor.GRAY + p.getName() + ChatColor.RED + ".");
                return true;
            } else if (subcmd.equalsIgnoreCase("reload")) {
                if (!plugin.isAuthorized(cs, "rcmds.pluginmanager.reload")) {
                    RUtils.dispNoPerms(cs);
                    return true;
                }
                if (args.length < 2) {
                    cs.sendMessage(ChatColor.RED + "Please provide the name of the plugin to reload!");
                    return true;
                }
                Plugin p = pm.getPlugin(args[1]);
                if (p == null) {
                    cs.sendMessage(ChatColor.RED + "No such plugin!");
                    return true;
                }
                pm.disablePlugin(p);
                pm.enablePlugin(p);
                cs.sendMessage(ChatColor.BLUE + "Reloaded " + ChatColor.GRAY + p.getName() + ChatColor.BLUE + ".");
                return true;
            } else if (subcmd.equalsIgnoreCase("update")) {
                if (!plugin.isAuthorized(cs, "rcmds.pluginmanager.update")) {
                    RUtils.dispNoPerms(cs);
                    return true;
                }
                if (args.length < 3) {
                    cs.sendMessage(ChatColor.RED + "Please provide the name of the plugin to update and its filename!");
                    return true;
                }
                Plugin p = pm.getPlugin(args[1]);
                if (p == null) {
                    cs.sendMessage(ChatColor.RED + "No such plugin!");
                    return true;
                }
                File f = new File(plugin.getDataFolder().getParentFile() + File.separator + args[2]);
                if (!f.exists()) {
                    cs.sendMessage(ChatColor.RED + "That file does not exist!");
                    return true;
                }
                if (!f.canRead()) {
                    cs.sendMessage(ChatColor.RED + "Can't read that file!");
                    return true;
                }
                pm.disablePlugin(p);
                try {
                    p = pm.loadPlugin(f);
                    if (p == null) {
                        cs.sendMessage(ChatColor.RED + "Could not load plugin: plugin was invalid.");
                        cs.sendMessage(ChatColor.RED + "Make sure it ends with .jar!");
                        return true;
                    }
                    pm.enablePlugin(p);
                } catch (InvalidPluginException e) {
                    cs.sendMessage(ChatColor.RED + "That file is not a plugin!");
                    return true;
                } catch (UnknownDependencyException e) {
                    cs.sendMessage(ChatColor.RED + "Missing dependency: " + e.getMessage());
                    return true;
                } catch (InvalidDescriptionException e) {
                    cs.sendMessage(ChatColor.RED + "That plugin contained an invalid description!");
                    return true;
                }
                cs.sendMessage(ChatColor.BLUE + "Updated " + ChatColor.GRAY + p.getName() + ChatColor.BLUE + " successfully.");
                return true;
            } else if (subcmd.equalsIgnoreCase("reloadall")) {
                if (!plugin.isAuthorized(cs, "rcmds.pluginmanager.reloadall")) {
                    RUtils.dispNoPerms(cs);
                    return true;
                }
                for (Plugin p : pm.getPlugins()) {
                    pm.disablePlugin(p);
                    pm.enablePlugin(p);
                }
                cs.sendMessage(ChatColor.BLUE + "Reloaded all plugins!");
                return true;
            } else if (subcmd.equalsIgnoreCase("list")) {
                if (!plugin.isAuthorized(cs, "rcmds.pluginmanager.list")) {
                    RUtils.dispNoPerms(cs);
                    return true;
                }
                Plugin[] ps = pm.getPlugins();
                StringBuilder list = new StringBuilder();
                int enabled = 0;
                int disabled = 0;
                for (Plugin p : ps) {
                    String name = p.getName();
                    if (!p.isEnabled()) {
                        name = name + " (disabled)";
                        disabled = disabled + 1;
                    } else enabled = enabled + 1;
                    list.append(ChatColor.GRAY);
                    list.append(name);
                    list.append(ChatColor.RESET);
                    list.append(", ");
                }
                cs.sendMessage(ChatColor.BLUE + "Plugins (" + ChatColor.GRAY + enabled + ((disabled > 0) ? ChatColor.BLUE + "/" + ChatColor.GRAY + disabled + " disabled" : "") + ChatColor.BLUE + "): " + list.substring(0, list.length() - 4));
                return true;
            } else if (subcmd.equalsIgnoreCase("info")) {
                if (!plugin.isAuthorized(cs, "rcmds.pluginmanager.info")) {
                    RUtils.dispNoPerms(cs);
                    return true;
                }
                if (args.length < 2) {
                    cs.sendMessage(ChatColor.RED + "Please provide the name of the plugin!");
                    return true;
                }
                Plugin p = pm.getPlugin(args[1]);
                if (p == null) {
                    cs.sendMessage(ChatColor.RED + "No such plugin!");
                    return true;
                }
                PluginDescriptionFile pdf = p.getDescription();
                if (pdf == null) {
                    cs.sendMessage(ChatColor.RED + "Can't get information from " + ChatColor.GRAY + p.getName() + ChatColor.RED + ".");
                    return true;
                }
                String version = pdf.getVersion();
                List<String> authors = pdf.getAuthors();
                String site = pdf.getWebsite();
                List<String> softDep = pdf.getSoftDepend();
                List<String> dep = pdf.getDepend();
                String name = pdf.getName();
                String desc = pdf.getDescription();
                if (name != null && !name.isEmpty())
                    cs.sendMessage(ChatColor.BLUE + "Name: " + ChatColor.GRAY + name);
                if (version != null && !version.isEmpty())
                    cs.sendMessage(ChatColor.BLUE + "Version: " + ChatColor.GRAY + version);
                if (site != null && !site.isEmpty())
                    cs.sendMessage(ChatColor.BLUE + "Site: " + ChatColor.GRAY + site);
                if (desc != null && !desc.isEmpty())
                    cs.sendMessage(ChatColor.BLUE + "Description: " + ChatColor.GRAY + desc.replaceAll("\r?\n", ""));
                if (authors != null && !authors.isEmpty())
                    cs.sendMessage(ChatColor.BLUE + "Author" + ((authors.size() > 1) ? "s" : "") + ": " + ChatColor.GRAY + RUtils.join(authors, ChatColor.RESET + ", " + ChatColor.GRAY));
                if (softDep != null && !softDep.isEmpty())
                    cs.sendMessage(ChatColor.BLUE + "Soft Dependencies: " + ChatColor.GRAY + RUtils.join(softDep, ChatColor.RESET + ", " + ChatColor.GRAY));
                if (dep != null && !dep.isEmpty())
                    cs.sendMessage(ChatColor.BLUE + "Dependencies: " + ChatColor.GRAY + RUtils.join(dep, ChatColor.RESET + ", " + ChatColor.GRAY));
                return true;
            } else if (subcmd.equalsIgnoreCase("commands")) {
                if (!plugin.isAuthorized(cs, "rcmds.pluginmanager.commands")) {
                    RUtils.dispNoPerms(cs);
                    return true;
                }
                if (args.length < 2) {
                    cs.sendMessage(ChatColor.RED + "Please provide the name of the plugin!");
                    return true;
                }
                Plugin p = pm.getPlugin(args[1]);
                if (p == null) {
                    cs.sendMessage(ChatColor.RED + "No such plugin!");
                    return true;
                }
                Map<String, Map<String, Object>> commands = p.getDescription().getCommands();
                if (commands == null) {
                    cs.sendMessage(ChatColor.GRAY + p.getName() + ChatColor.RED + " has no registered commands.");
                    return true;
                }
                for (String command : commands.keySet()) {
                    Object odesc = commands.get(command).get("description");
                    String desc = (odesc != null) ? odesc.toString() : "";
                    cs.sendMessage(ChatColor.GRAY + "/" + command + ((desc.equals("")) ? "" : ChatColor.BLUE + " - " + desc));
                }
                return true;
            } else if (subcmd.equalsIgnoreCase("help") || subcmd.equals("?")) {
                if (!plugin.isAuthorized(cs, "rcmds.pluginmanager.help")) {
                    RUtils.dispNoPerms(cs);
                    return true;
                }
                cs.sendMessage(ChatColor.BLUE + "RoyalCommands PluginManager Help");
                cs.sendMessage(ChatColor.BLUE + "================================");
                cs.sendMessage("* " + ChatColor.GRAY + "/" + label + " load [jar]" + ChatColor.BLUE + " - Loads and enables a new plugin");
                cs.sendMessage("* " + ChatColor.GRAY + "/" + label + " disable [plugin]" + ChatColor.BLUE + " - Disables an already loaded plugin");
                cs.sendMessage("* " + ChatColor.GRAY + "/" + label + " enable [plugin]" + ChatColor.BLUE + " - Enables a disabled plugin");
                cs.sendMessage("* " + ChatColor.GRAY + "/" + label + " reload [plugin]" + ChatColor.BLUE + " - Disables then enables a plugin");
                cs.sendMessage("* " + ChatColor.GRAY + "/" + label + " reloadall" + ChatColor.BLUE + " - Reloads every plugin");
                cs.sendMessage("* " + ChatColor.GRAY + "/" + label + " delete [jar]" + ChatColor.BLUE + " - Tries to delete the specified jar");
                cs.sendMessage("* " + ChatColor.GRAY + "/" + label + " update [plugin] [jar]" + ChatColor.BLUE + " - Disables the plugin and loads the new jar");
                cs.sendMessage("* " + ChatColor.GRAY + "/" + label + " commands [plugin]" + ChatColor.BLUE + " - Lists all registered commands and their description of a plugin");
                cs.sendMessage("* " + ChatColor.GRAY + "/" + label + " list" + ChatColor.BLUE + " - Lists all the plugins");
                cs.sendMessage("* " + ChatColor.GRAY + "/" + label + " info [plugin]" + ChatColor.BLUE + " - Displays information about a plugin");
                cs.sendMessage("* " + ChatColor.GRAY + "/" + label + " updatecheck [plugin] (tag)" + ChatColor.BLUE + " - Attempts to check for the newest version of a plugin; may not always work correctly");
                cs.sendMessage("* " + ChatColor.GRAY + "/" + label + " updatecheckall" + ChatColor.BLUE + " - Attempts to check for newest version of all plugins");
                cs.sendMessage("* " + ChatColor.GRAY + "/" + label + " download [tag] (recursive)" + ChatColor.BLUE + " - Attempts to download a plugin from BukkitDev using its tag - recursive can be \"true\" if you would like the plugin to search for jars in all subdirectories of an archive downloaded");
                cs.sendMessage("* " + ChatColor.GRAY + "/" + label + " findtag [search]" + ChatColor.BLUE + " - Searches BukkitDev for a tag to use in download");
                return true;
            } else if (subcmd.equalsIgnoreCase("download")) {
                if (!plugin.isAuthorized(cs, "rcmds.pluginmanager.download")) {
                    RUtils.dispNoPerms(cs);
                    return true;
                }
                if (args.length < 2) {
                    cs.sendMessage(ChatColor.RED + "Please provide plugin tag!");
                    cs.sendMessage(ChatColor.RED + "http://dev.bukkit.org/server-mods/" + ChatColor.GRAY + "royalcommands" + ChatColor.RED + "/");
                    return true;
                }
                boolean recursive = args.length > 2 && args[2].equalsIgnoreCase("true");
                cs.sendMessage(ChatColor.BLUE + "Getting download link...");
                String pluginUrlString = "http://dev.bukkit.org/server-mods/" + args[1].toLowerCase() + "/files.rss";
                String file;
                try {
                    URL url = new URL(pluginUrlString);
                    Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(url.openConnection().getInputStream());
                    doc.getDocumentElement().normalize();
                    NodeList nodes = doc.getElementsByTagName("item");
                    Node firstNode = nodes.item(0);
                    if (firstNode.getNodeType() == 1) {
                        Element firstElement = (Element) firstNode;
                        NodeList firstElementTagName = firstElement.getElementsByTagName("link");
                        Element firstNameElement = (Element) firstElementTagName.item(0);
                        NodeList firstNodes = firstNameElement.getChildNodes();
                        String link = firstNodes.item(0).getNodeValue();
                        URL dpage = new URL(link);
                        BufferedReader br = new BufferedReader(new InputStreamReader(dpage.openStream()));
                        String inputLine;
                        StringBuilder content = new StringBuilder();
                        while ((inputLine = br.readLine()) != null)
                            content.append(inputLine);
                        br.close();
                        file = StringUtils.substringBetween(content.toString(), "<li class=\"user-action user-action-download\"><span><a href=\"", "\">Download</a></span></li>");
                    } else throw new Exception();
                } catch (Exception e) {
                    cs.sendMessage(ChatColor.RED + "Could not fetch download link! Either this plugin has no downloads, or you specified an invalid tag.");
                    cs.sendMessage(ChatColor.RED + "Tag: http://dev.bukkit.org/server-mods/" + ChatColor.GRAY + "plugin-name" + ChatColor.RED + "/");
                    return true;
                }
                BufferedInputStream bis;
                try {
                    bis = new BufferedInputStream(new URL(file).openStream());
                } catch (MalformedURLException e) {
                    cs.sendMessage(ChatColor.RED + "The received download link was invalid!");
                    return true;
                } catch (IOException e) {
                    cs.sendMessage(ChatColor.RED + "An internal input/output error occurred. Please try again.");
                    return true;
                }
                Pattern p = Pattern.compile("https?://dev\\.bukkit\\.org/media/files[\\d/]+([\\w\\W]+)");
                Matcher m = p.matcher(file);
                m.find();
                String fileName = m.group(1).trim();
                cs.sendMessage(ChatColor.BLUE + "Creating temporary folder...");
                File f = new File(System.getProperty("java.io.tmpdir") + File.separator + UUID.randomUUID().toString() + File.separator + fileName);
                while (f.getParentFile().exists())
                    f = new File(System.getProperty("java.io.tmpdir") + File.separator + UUID.randomUUID().toString() + File.separator + fileName);
                if (!fileName.endsWith(".zip") && !fileName.endsWith(".jar")) {
                    cs.sendMessage(ChatColor.RED + "The file wasn't a zip or jar file, so it was not downloaded.");
                    cs.sendMessage(ChatColor.RED + "Filename: " + ChatColor.GRAY + fileName);
                    return true;
                }
                f.getParentFile().mkdirs();
                BufferedOutputStream bos;
                try {
                    bos = new BufferedOutputStream(new FileOutputStream(f));
                } catch (FileNotFoundException e) {
                    cs.sendMessage(ChatColor.RED + "The temporary download folder was not found. Make sure that " + ChatColor.GRAY + System.getProperty("java.io.tmpdir") + ChatColor.RED + " is writable.");
                    return true;
                }
                int b;
                cs.sendMessage(ChatColor.BLUE + "Downloading file " + ChatColor.GRAY + fileName + ChatColor.BLUE + "...");
                try {
                    while ((b = bis.read()) != -1) bos.write(b);
                    bos.flush();
                    bos.close();
                } catch (IOException e) {
                    cs.sendMessage(ChatColor.RED + "An internal input/output error occurred. Please try again.");
                    return true;
                }
                if (fileName.endsWith(".zip")) {
                    cs.sendMessage(ChatColor.BLUE + "Decompressing zip...");
                    UnZip.decompress(f.getAbsolutePath(), f.getParent());
                }
                for (File fi : FileUtils.listFiles(f.getParentFile(), null, recursive)) {
                    if (!fi.getName().endsWith(".jar")) continue;
//                  String extraFile = (f.getParent().equals(fi.getParent())) ? "" : fi.getParentFile().getName() + File.separator;
                    cs.sendMessage(ChatColor.BLUE + "Moving " + ChatColor.GRAY + fi.getName() + ChatColor.BLUE + " to plugins folder...");
                    boolean s = fi.renameTo(new File(plugin.getDataFolder().getParentFile() + File.separator + fi.getName()));
                    if (!s)
                        cs.sendMessage(ChatColor.RED + "Couldn't move " + ChatColor.GRAY + fi.getName() + ChatColor.RED + "!");
                }
                cs.sendMessage(ChatColor.BLUE + "Removing temporary folder...");
                try {
                    FileUtils.deleteDirectory(f.getParentFile());
                } catch (IOException ignored) {
                }
                cs.sendMessage(ChatColor.BLUE + "Downloaded plugin. Use " + ChatColor.GRAY + "/" + label + " load" + ChatColor.BLUE + " to enable it.");
                return true;
            } else if (subcmd.equalsIgnoreCase("updatecheckall")) {
                if (!plugin.isAuthorized(cs, "rcmds.pluginmanager.updatecheckall")) {
                    RUtils.dispNoPerms(cs);
                    return true;
                }
                final Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        for (Plugin p : pm.getPlugins()) {
                            String version = p.getDescription().getVersion();
                            if (version == null) continue;
                            String checked;
                            try {
                                checked = updateCheck(p.getName(), version);
                            } catch (Exception e) {
                                continue;
                            }
                            if (checked.contains(version)) continue;
                            cs.sendMessage(ChatColor.GRAY + p.getName() + ChatColor.BLUE + " may have an update. C: " + ChatColor.GRAY + version + ChatColor.BLUE + " N: " + ChatColor.GRAY + checked);
                        }
                    }
                };
                plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, r);
                cs.sendMessage(ChatColor.BLUE + "Finished checking for updates.");
                return true;
            } else if (subcmd.equalsIgnoreCase("updatecheck")) {
                if (!plugin.isAuthorized(cs, "rcmds.pluginmanager.updatecheck")) {
                    RUtils.dispNoPerms(cs);
                    return true;
                }
                if (args.length < 2) {
                    cs.sendMessage(ChatColor.RED + "Please provide the name of the plugin!");
                    return true;
                }
                Plugin p = pm.getPlugin(args[1]);
                if (p == null) {
                    cs.sendMessage(ChatColor.RED + "No such plugin!");
                    return true;
                }
                String tag = (args.length > 2) ? RoyalCommands.getFinalArg(args, 2) : p.getName();
                try {
                    tag = URLEncoder.encode(tag, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    cs.sendMessage(ChatColor.RED + "Tell the developer enc1.");
                    return true;
                }
                if (p == null) {
                    cs.sendMessage(ChatColor.RED + "No such plugin!");
                    return true;
                }
                if (p.getDescription() == null) {
                    cs.sendMessage(ChatColor.RED + "Plugin has no description!");
                    return true;
                }
                String version = p.getDescription().getVersion();
                if (version == null) {
                    cs.sendMessage(ChatColor.RED + "Plugin has not set a version!");
                    return true;
                }
                try {
                    String checked = updateCheck(tag, version);
                    cs.sendMessage(ChatColor.BLUE + "Current version is " + ChatColor.GRAY + version + ChatColor.BLUE + "; newest version is " + ChatColor.GRAY + checked + ChatColor.BLUE + ".");
                } catch (Exception e) {
                    cs.sendMessage(ChatColor.RED + "Could not check for update!");
                }
                return true;
            } else if (subcmd.equalsIgnoreCase("findtag")) {
                if (!plugin.isAuthorized(cs, "rcmds.pluginmanager.findtag")) {
                    RUtils.dispNoPerms(cs);
                    return true;
                }
                if (args.length < 2) {
                    cs.sendMessage(ChatColor.RED + "Please specify a search term!");
                    return true;
                }
                String search = RoyalCommands.getFinalArg(args, 1);
                try {
                    search = URLEncoder.encode(search, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    cs.sendMessage(ChatColor.RED + "Tell the developer enc1.");
                    return true;
                }
                URL u;
                try {
                    u = new URL("http://dev.bukkit.org/search/?scope=projects&search=" + search);
                } catch (MalformedURLException e) {
                    cs.sendMessage(ChatColor.RED + "Malformed search term!");
                    return true;
                }
                BufferedReader br;
                try {
                    br = new BufferedReader(new InputStreamReader(u.openStream()));
                } catch (IOException e) {
                    cs.sendMessage(ChatColor.RED + "Internal input/output error. Please try again.");
                    return true;
                }
                String inputLine;
                StringBuilder content = new StringBuilder();
                try {
                    while ((inputLine = br.readLine()) != null) content.append(inputLine);
                } catch (IOException e) {
                    cs.sendMessage(ChatColor.RED + "Internal input/output error. Please try again.");
                    return true;
                }
                cs.sendMessage(ChatColor.BLUE + "Project name" + ChatColor.GRAY + " - tag");
                for (int i = 0; i < 5; i++) {
                    String project = StringUtils.substringBetween(content.toString(), " row-joined-to-next\">", "</tr>");
                    String base = StringUtils.substringBetween(project, "<td class=\"col-search-entry\">", "</td>");
                    if (base == null) {
                        if (i == 0) cs.sendMessage(ChatColor.RED + "No results found.");
                        return true;
                    }
                    Pattern p = Pattern.compile("<h2><a href=\"/server-mods/([\\W\\w]+)/\">([\\w\\W]+)</a></h2>");
                    Matcher m = p.matcher(base);
                    if (m == null) {
                        if (i == 0) cs.sendMessage(ChatColor.RED + "No results found.");
                        return true;
                    }
                    m.find();
                    String name = m.group(2).replaceAll("</?\\w+>", "");
                    String tag = m.group(1);
                    int beglen = StringUtils.substringBefore(content.toString(), base).length();
                    content = new StringBuilder(content.substring(beglen + project.length()));
                    cs.sendMessage(ChatColor.BLUE + name + ChatColor.GRAY + " - " + tag);
                }
                return true;
            } else if (subcmd.equalsIgnoreCase("delete")) {
                if (!plugin.isAuthorized(cs, "rcmds.pluginmanager.delete")) {
                    RUtils.dispNoPerms(cs);
                    return true;
                }
                if (args.length < 2) {
                    cs.sendMessage(ChatColor.RED + "Please specify the filename to delete!");
                    return true;
                }
                String toDelete = args[1];
                if (!toDelete.endsWith(".jar")) {
                    cs.sendMessage(ChatColor.RED + "Please only specify jar files!");
                    return true;
                }
                if (toDelete.contains(File.separator)) {
                    cs.sendMessage(ChatColor.RED + "Please don't try to leave the plugins directory!");
                    return true;
                }
                File f = new File(plugin.getDataFolder().getParentFile() + File.separator + toDelete);
                if (!f.exists()) {
                    cs.sendMessage(ChatColor.RED + "No such file!");
                    return true;
                }
                boolean success = f.delete();
                if (!success)
                    cs.sendMessage(ChatColor.RED + "Could not delete " + ChatColor.GRAY + f.getName() + ChatColor.BLUE + ".");
                else
                    cs.sendMessage(ChatColor.BLUE + "Deleted " + ChatColor.GRAY + f.getName() + ChatColor.BLUE + ".");
                return true;
            } else {
                cs.sendMessage(ChatColor.RED + "Unknown subcommand!");
                cs.sendMessage(ChatColor.RED + "Try " + ChatColor.GRAY + "/" + label + " help");
                return true;
            }
        }

        return false;
    }
}
