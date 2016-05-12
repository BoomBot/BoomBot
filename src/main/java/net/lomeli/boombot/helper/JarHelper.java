package net.lomeli.boombot.helper;

import com.google.common.collect.Lists;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import net.lomeli.boombot.api.BoomAddon;

public class JarHelper {
    public static List<String> getClassesInFile(File file) {
        List<String> classNames = Lists.newArrayList();
        try {
            ZipInputStream zip = new ZipInputStream(new FileInputStream(file));
            for (ZipEntry entry = zip.getNextEntry(); entry != null; entry = zip.getNextEntry()) {
                if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
                    // This ZipEntry represents a class. Now, what class does it represent?
                    String className = entry.getName().replace('/', '.'); // including ".class"
                    classNames.add(className.substring(0, className.length() - ".class".length()));
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.error("Could not find file %s", ex, file.getName());
        } catch (IOException ex) {
            Logger.error("Could not open file %s", ex, file.getName());
        }
        return classNames;
    }

    public static List<Class> findAddonClass(List<String> clazzes, ClassLoader classLoader) throws ClassNotFoundException {
        List<Class> classList = Lists.newArrayList();
        for (String st : clazzes) {
            Class clazz = classLoader.loadClass(st);
            if (clazz.isAnnotationPresent(BoomAddon.class)) classList.add(clazz);
        }
        return classList;
    }
}
