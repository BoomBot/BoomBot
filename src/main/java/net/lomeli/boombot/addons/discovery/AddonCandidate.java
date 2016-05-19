package net.lomeli.boombot.addons.discovery;

import com.google.common.collect.Lists;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.List;

import net.lomeli.boombot.addons.AddonClassLoader;
import net.lomeli.boombot.addons.AddonContainer;
import net.lomeli.boombot.helper.AddonHelper;
import net.lomeli.boombot.helper.Logger;

public class AddonCandidate {
    private File addonPath;
    private File addonFile;
    private AddonType type;

    public AddonCandidate(File path, File file, AddonType type) {
        this.addonPath = path;
        this.addonFile = file;
        this.type = type;
    }

    public void findAddons(AddonLoader addonLoader, AddonClassLoader loader) {
        try {
            loader.addFile(addonFile);
            List<String> classNames = Lists.newArrayList();
            List<Class> classes = Lists.newArrayList();
            if (type == AddonType.DIR)
                classNames.addAll(AddonHelper.getClassesInPath(addonFile, addonPath));
            else
                classNames.addAll(AddonHelper.getClassesInFile(addonFile));
            classes.addAll(AddonHelper.findAddonClass(classNames, loader));
            for (Class cl : classes)
                addonLoader.addContainer(new AddonContainer(cl));
        } catch (ClassNotFoundException ex) {
            Logger.error("Failed to load addon %s", ex, addonFile);
        } catch (IllegalAccessException ex) {
            Logger.error("Could not access addon class in %s", ex, addonFile.getName());
        } catch (InstantiationException ex) {
            Logger.error("Initiate addon %s", ex, addonFile.getName());
        } catch (MalformedURLException ex) {
            Logger.error("Failed to load file %s", ex, addonFile.getName());
        } catch (InvocationTargetException ex) {
            Logger.error("Could not access addon info in %s", ex, addonFile.getName());
        }
    }

    public enum AddonType {
        DIR, JAR
    }
}
