package net.lomeli.boombot.core.addon.discovery;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import net.lomeli.boombot.api.Addon;
import net.lomeli.boombot.api.BoomAPI;
import net.lomeli.boombot.core.addon.exceptions.WrongBotVersionException;

public class AddonHelper {
    private static List<String> ignoreList;
    private static List<String> ignoreClassList;

    static {
        ignoreList = Lists.newArrayList();
        ignoreList.add("build/classes/main/net/lomeli/boombot/api");
        ignoreList.add("build/classes/main/net/lomeli/boombot/command");
        ignoreList.add("build/classes/main/net/lomeli/boombot/core");
        ignoreList.add("build/classes/main/net/lomeli/boombot/lib");
        ignoreList.add("build/classes/main/net/lomeli/boombot/BoomBot.class");
        ignoreList.add("net/lomeli/boombot/BoomBot.class");
        ignoreList.add("build/resources/main");
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
        ignoreList.add("BoomBot.jar");

        ignoreClassList = Lists.newArrayList();
        ignoreClassList.add("net/dv8tion");
        ignoreClassList.add("com/google/code/gson");
        ignoreClassList.add("com/google/gson");
        ignoreClassList.add("com/google/common");
        ignoreClassList.add("com/google/guava");
        ignoreClassList.add("com/google/thirdparty");
        ignoreClassList.add("commons-io");
        ignoreClassList.add("net/sourceforge/jaadec");
        ignoreClassList.add("jflac");
        ignoreClassList.add("com/mashape/unirest");
        ignoreClassList.add("org/apache/commons");
        ignoreClassList.add("net/java/dev/jna");
        ignoreClassList.add("org/tritonus");
        ignoreClassList.add("com/googlecode/soundlibs");
        ignoreClassList.add("org/json");
        ignoreClassList.add("com/neovisionaries");
        ignoreClassList.add("org/apache/httpcomponents");
        ignoreClassList.add("com/googlecode/soundlibs");
        ignoreClassList.add("org/apache/httpcomponents");
        ignoreClassList.add("commons-logging");
        ignoreClassList.add("commons-codec");
        ignoreClassList.add("org/apache/httpcomponents");
        ignoreClassList.add("com/iwebpp/crypto");
        ignoreClassList.add("tomp2p/opuswrapper");
        ignoreClassList.add("junit");
        ignoreClassList.add("gnu/trove");
        ignoreClassList.add("com/sun/jna");
        ignoreClassList.add("org/apache/http");
    }

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
            BoomAPI.logger.error("Could not find file %s", ex, file.getName());
        } catch (IOException ex) {
            BoomAPI.logger.error("Could not open file %s", ex, file.getName());
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

    public static List<Class> findAddonClass(List<String> clazzes, ClassLoader classLoader) throws ClassNotFoundException, WrongBotVersionException {
        List<Class> classList = Lists.newArrayList();
        for (String st : clazzes) {
            Class clazz = classLoader.loadClass(st);
            Addon annotation = (Addon) clazz.getAnnotation(Addon.class);
            if (annotation != null) {
                if (annotation.acceptedBoomBotVersion().equals(BoomAPI.BOOM_BOT_VERSION) || annotation.acceptedBoomBotVersion().equals("*"))
                    classList.add(clazz);
                else
                    throw new WrongBotVersionException(annotation.name(), annotation.acceptedBoomBotVersion(), BoomAPI.BOOM_BOT_VERSION);
            }
        }
        return classList;
    }

    public static boolean isAddonClass(Class clazz) throws WrongBotVersionException {
        if (clazz == null) return false;
        Addon addon = (Addon) clazz.getAnnotation(Addon.class);
        if (addon == null) return false;
        if (!Strings.isNullOrEmpty(addon.acceptedBoomBotVersion()) && (addon.acceptedBoomBotVersion().equals(BoomAPI.BOOM_BOT_VERSION) || addon.acceptedBoomBotVersion().equals("*")))
            return true;
        throw new WrongBotVersionException(addon.name(), addon.acceptedBoomBotVersion(), BoomAPI.BOOM_BOT_VERSION);
    }

    public static boolean ignoreClass(String className) {
        return ignoreClassList.stream().anyMatch(s -> className.startsWith(s));
    }

    public static boolean ignoreFile(String name) {
        return ignoreList.stream().anyMatch(s -> name.contains(s));
    }
}
