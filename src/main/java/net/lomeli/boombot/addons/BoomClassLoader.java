package net.lomeli.boombot.addons;

import com.google.common.collect.Lists;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

import net.lomeli.boombot.helper.JarHelper;
import net.lomeli.boombot.helper.Logger;

public class BoomClassLoader {
    private List<File> sourceFiles;
    private List<AddonContainer> addonContainers;

    public BoomClassLoader() {
        this.sourceFiles = Lists.newArrayList();
        this.addonContainers = Lists.newArrayList();
    }

    public void addFile(File file) throws FileNotFoundException {
        sourceFiles.add(file);
    }

    public boolean addonsFound() {
        return !sourceFiles.isEmpty();
    }

    public void loadMainClass() {
        for (File file : sourceFiles) {
            try {
                URL url = file.toURI().toURL();
                ClassLoader loader = new URLClassLoader(new URL[]{url});
                List<String> classNames = JarHelper.getClassesInFile(file);
                List<Class> classes = JarHelper.findAddonClass(classNames, loader);
                for (Class cl : classes)
                    addonContainers.add(new AddonContainer(cl));
                if (addonContainers.size() > 0) {
                    String addonList = "";
                    for (int i = 0; i < addonContainers.size(); i++) {
                        AddonContainer container = addonContainers.get(i);
                        addonList += String.format("%s {%s | %s}", container.getName(), container.getId(), container.getVersion());
                        if (i != addonContainers.size() - 1) addonList += ", ";
                    }
                    Logger.info("Loading the following addons: %s", addonList);
                }
                for (AddonContainer addonContainer : addonContainers) {
                    addonContainer.initAddon();
                }
            } catch (MalformedURLException ex) {
                Logger.error("Failed to load file %s", ex, file.getName());
            } catch (ClassNotFoundException ex) {
                Logger.error("Failed to load a class in %s", ex, file.getName());
            } catch (IllegalAccessException ex) {
                Logger.error("Could not access addon class in %s", ex, file.getName());
            } catch (InstantiationException ex) {
                Logger.error("Initiate addon %s", ex, file.getName());
            }
        }
    }
}
