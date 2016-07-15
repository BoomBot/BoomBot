package net.lomeli.boombot.addons.discovery;

import com.google.common.collect.Lists;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.addons.AddonContainer;
import net.lomeli.boombot.addons.exceptions.WrongBoomBotVersion;
import net.lomeli.boombot.helper.AddonHelper;
import net.lomeli.boombot.logging.BoomLogger;

public class AddonCandidate {
    private File addonPath;
    private File addonFile;
    private AddonType type;

    public AddonCandidate(File path, File file, AddonType type) {
        this.addonPath = path;
        this.addonFile = file;
        this.type = type;
    }

    public void findAddons(AddonLoader addonLoader) {
        if (addonFile == null) return;
        try {
            BoomLogger.debug(addonFile.getAbsolutePath());
            if (type == AddonType.JAR) {
                JarFile jarFile = new JarFile(addonFile.getCanonicalPath());
                Enumeration<JarEntry> e = jarFile.entries();
                URL[] urls = {new URL("jar:file:" + addonFile.getCanonicalPath() + "!/")};
                ClassLoader loader = URLClassLoader.newInstance(urls);
                while (e.hasMoreElements()) {
                    JarEntry je = e.nextElement();
                    if (je.isDirectory() || !je.getName().endsWith(".class"))
                        continue;
                    String className = je.getName().substring(0, je.getName().length() - 6);
                    className = className.replace('/', '.');
                    BoomLogger.debug(className);
                    Class cl = loader.loadClass(className);
                    if (AddonHelper.isAddonClass(cl)) {
                        addonLoader.addContainer(new AddonContainer(cl));
                        break;
                    }
                }
            } else {
                URL url = addonFile.toURI().toURL();
                ClassLoader loader = new URLClassLoader(new URL[]{url});
                List<String> classNames = Lists.newArrayList();
                List<Class> classes = Lists.newArrayList();
                classNames.addAll(AddonHelper.getClassesInPath(addonFile, addonPath));
                classes.addAll(AddonHelper.findAddonClass(classNames, loader));
                for (Class cl : classes)
                    addonLoader.addContainer(new AddonContainer(cl));
            }
        } catch (IOException ex) {

        } catch (ClassNotFoundException ex) {
            BoomLogger.error("Failed to load addon %s", ex, addonFile);
        } catch (IllegalAccessException ex) {
            BoomLogger.error("Could not access addon class in %s", ex, addonFile.getName());
        } catch (InstantiationException ex) {
            BoomLogger.error("Initiate addon %s", ex, addonFile.getName());
        } catch (InvocationTargetException ex) {
            BoomLogger.error("Could not access addon info in %s", ex, addonFile.getName());
        } catch (WrongBoomBotVersion ex) {
            BoomLogger.error("Failed to load addon %s", ex, addonFile.getName());
            BoomBot.shutdownBoomBot();
        }
    }

    public enum AddonType {
        DIR, JAR
    }
}
