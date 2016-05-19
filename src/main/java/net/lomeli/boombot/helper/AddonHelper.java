package net.lomeli.boombot.helper;

import com.google.common.collect.Lists;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import net.lomeli.boombot.addons.AddonClassLoader;
import net.lomeli.boombot.api.BoomAddon;

public class AddonHelper {
    static {
        ignoreList = Lists.newArrayList();
        ignoreList.add("build\\classes\\main");
        ignoreList.add("build\\resources\\main");
        ignoreList.add("charsets.jar");
        ignoreList.add("deploy.jar");
        ignoreList.add("access-bridge-64.jar");
        ignoreList.add("cldrdata.jar");
        ignoreList.add("dnsns.jar");
        ignoreList.add("jaccess.jar");
        ignoreList.add("jfxrt.jar");
        ignoreList.add("localedata.jar");
        ignoreList.add("nashorn.jar");
        ignoreList.add("sunec.jar");
        ignoreList.add("sunjce_provider.jar");
        ignoreList.add("sunmscapi.jar");
        ignoreList.add("sunpkcs11.jar");
        ignoreList.add("zipfs.jar");
        ignoreList.add("javaws.jar");
        ignoreList.add("jce.jar");
        ignoreList.add("jfr.jar");
        ignoreList.add("jfxswt.jar");
        ignoreList.add("jsse.jar");
        ignoreList.add("management-agent.jar");
        ignoreList.add("plugin.jar");
        ignoreList.add("resources.jar");
        ignoreList.add("rt.jar");
        ignoreList.add("gragent.jar");
        ignoreList.add("net.dv8tion");
        ignoreList.add("com.google.code.gson");
        ignoreList.add("com.google.guava");
        ignoreList.add("commons-io");
        ignoreList.add("net.sourceforge.jaadec");
        ignoreList.add("jflac");
        ignoreList.add("com.mashape.unirest");
        ignoreList.add("org.apache.commons");
        ignoreList.add("net.java.dev.jna");
        ignoreList.add("org.tritonus");
        ignoreList.add("com.googlecode.soundlibs");
        ignoreList.add("com.googlecode.soundlibs");
        ignoreList.add("org.json");
        ignoreList.add("com.neovisionaries");
        ignoreList.add("org.apache.httpcomponents");
        ignoreList.add("org.apache.httpcomponents");
        ignoreList.add("org.apache.httpcomponents");
        ignoreList.add("com.googlecode.soundlibs");
        ignoreList.add("org.apache.httpcomponents");
        ignoreList.add("commons-logging");
        ignoreList.add("commons-codec");
        ignoreList.add("org.apache.httpcomponents");
        ignoreList.add("junit");
    }

    private static List<String> ignoreList;

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

    public static List<String> getClassesInPath(File file, File base) {
        List<String> classNames = Lists.newArrayList();
        if (!ignoreFile(file.getAbsolutePath())) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                Arrays.sort(files);
                for (File entry : files) {
                    if (entry.isDirectory())
                        classNames.addAll(getClassesInPath(entry, base));
                    else {
                        if (entry.getName().endsWith(".class")) {
                            String dirtyName = entry.getAbsolutePath().substring(base.getAbsolutePath().length() + 1);
                            String className = dirtyName.replace('/', '.').replace('\\', '.'); // including ".class"
                            classNames.add(className.substring(0, className.length() - ".class".length()));
                        }
                    }
                }
            }
        }
        return classNames;
    }

    public static List<Class> findAddonClass(List<String> clazzes, AddonClassLoader classLoader) throws ClassNotFoundException {
        List<Class> classList = Lists.newArrayList();
        for (String st : clazzes) {
            Class clazz = classLoader.loadClass(st);
            Annotation[] annotations = clazz.getAnnotations();
            if (annotations != null && annotations.length > 0) {
                annotationsLoop:
                for (Annotation a : annotations) {
                    if (a.annotationType().toString().equals(BoomAddon.class.toString())) {
                        classList.add(clazz);
                        break annotationsLoop;
                    }
                }
            }
        }
        return classList;
    }

    public static boolean ignoreFile(String name) {
        for (String s : ignoreList) {
            if (name.contains(s))
                return true;
        }
        return false;
    }
}
