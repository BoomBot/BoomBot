package net.lomeli.boombot.core.addon.discovery;

import com.google.common.collect.Lists;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import net.lomeli.boombot.api.BoomAPI;
import net.lomeli.boombot.core.addon.AddonContainer;
import net.lomeli.boombot.core.addon.exceptions.WrongBotVersionException;

public class AddonCandidate {
    private File addonPath;
    private AddonType addonType;

    public AddonCandidate(File path, AddonType addonType) {
        this.addonPath = path;
        this.addonType = addonType;
    }

    public List<AddonContainer> findAddons() {
        if (addonPath == null || !addonPath.exists()) return null;
        List<AddonContainer> addonContainers = Lists.newArrayList();
        try {
            if (addonType.isJar()) {
                JarFile jarFile = new JarFile(addonPath.getCanonicalPath());
                Enumeration<JarEntry> e = jarFile.entries();
                URL[] urls = {new URL("jar:file:" + addonPath.getCanonicalPath() + "!/")};
                URLClassLoader loader = URLClassLoader.newInstance(urls);
                while (e.hasMoreElements()) {
                    JarEntry je = e.nextElement();
                    if (je.isDirectory() || !je.getName().endsWith(".class")) continue;
                    String className = je.getName().substring(0, je.getName().length() - 6);
                    if (AddonHelper.ignoreClass(className)) continue;
                    Class cl = loader.loadClass(className.replace('/', '.'));
                    if (cl != null && AddonHelper.isAddonClass(cl)) {
                        addonContainers.add(new AddonContainer(cl, addonPath, addonType));
                        break;
                    }
                }
            } else {
                ClassLoader loader = new URLClassLoader(new URL[]{addonPath.toURI().toURL()});
                List<File> files = getAllClassFiles(addonPath);
                if (files.size() <= 0) return null;
                for (File file : files) {
                    String dirtyClassName = file.getCanonicalPath().substring(addonPath.getCanonicalPath().length());
                    String className = dirtyClassName.substring(1, dirtyClassName.length() - 6).replace('/', '.').replace('\\', '.');
                    Class cl = loader.loadClass(className);
                    if (cl != null && AddonHelper.isAddonClass(cl))
                        addonContainers.add(new AddonContainer(cl, addonPath, addonType));
                }
            }
        } catch (IOException ex) {
            BoomAPI.logger.error("Failed to load file %s", ex, addonPath);
        } catch (ClassNotFoundException ex) {
            BoomAPI.logger.error("Failed to load addon %s", ex, addonPath);
        } catch (IllegalAccessException ex) {
            BoomAPI.logger.error("Could not access addon class in %s", ex, addonPath.getName());
        } catch (InstantiationException ex) {
            BoomAPI.logger.error("Initiate addon %s", ex, addonPath.getName());
        } catch (InvocationTargetException ex) {
            BoomAPI.logger.error("Could not access addon info in %s", ex, addonPath.getName());
        } catch (WrongBotVersionException ex) {
            BoomAPI.logger.error("Failed to load addon %s", ex, addonPath.getName());
        }
        return addonContainers;
    }

    private List<File> getAllClassFiles(File head) {
        List<File> files = Lists.newArrayList();
        if (head != null && head.isDirectory()) {
            File[] children = head.listFiles();
            if (children != null && children.length > 0) {
                for (File f : children) {
                    if (f.isDirectory()) files.addAll(getAllClassFiles(f));
                    else if (f.isFile() && FilenameUtils.isExtension(f.getAbsolutePath(), "class") && !AddonHelper.ignoreFile(f.getAbsolutePath()))
                        files.add(f);
                }
            }
        }
        return files;
    }

}
