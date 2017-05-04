package net.lomeli.boombot.core.addon;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.lomeli.boombot.api.Addon;
import net.lomeli.boombot.api.BoomAPI;
import net.lomeli.boombot.api.events.bot.InitEvent;
import net.lomeli.boombot.api.events.bot.PostInitEvent;
import net.lomeli.boombot.api.events.bot.PreInitEvent;
import net.lomeli.boombot.core.addon.discovery.AddonType;

public class AddonContainer {
    private Class addonClass;
    private Addon addonInfo;
    private Object addonInstance;
    private File addonPath;
    private AddonType addonType;

    public AddonContainer(Class cl, File addonPath, AddonType addonType) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        this.addonClass = cl;
        this.addonPath = addonPath;
        this.addonInfo = (Addon) this.addonClass.getAnnotation(Addon.class);
        this.addonInstance = cl.newInstance();
        this.addonType = addonType;
    }

    public AddonContainer(Object addonInstance, AddonType addonType) {
        this.addonInfo = addonInstance.getClass().getAnnotation(Addon.class);
        this.addonInstance = addonInstance;
        this.addonType = addonType;
    }

    public void loadResources() {
        if (getAddonType().isJar())
            BoomAPI.langRegistry.loadLangFolder(addonInfo.addonID(), addonPath);
    }

    public void preInitAddon() throws IllegalAccessException, InvocationTargetException {
        Field[] fields = addonInstance.getClass().getDeclaredFields();
        for (Field f : fields) {
            if (f.getAnnotation(Addon.Instance.class) != null) {
                f.set(addonInstance, addonInstance);
                break;
            }
        }
        PreInitEvent event = new PreInitEvent(addonInfo);
        Method[] methods = addonInstance.getClass().getMethods();
        for (Method m : methods) {
            if (m.getAnnotation(Addon.Event.class) != null && m.getParameterCount() == 1 && m.getParameterTypes()[0] == event.getClass()) {
                m.invoke(addonInstance, event);
                break;
            }
        }
    }

    public void initAddon(InitEvent event) throws IllegalAccessException, InvocationTargetException {
        Method[] methods = addonInstance.getClass().getMethods();
        for (Method m : methods) {
            if (m.getAnnotation(Addon.Event.class) != null && m.getParameterCount() == 1 && m.getParameterTypes()[0] == event.getClass()) {
                m.invoke(addonInstance, event);
                break;
            }
        }
    }

    public void postInitAddon(PostInitEvent event) throws IllegalAccessException, InvocationTargetException {
        Method[] methods = addonInstance.getClass().getMethods();
        for (Method m : methods) {
            if (m.getAnnotation(Addon.Event.class) != null && m.getParameterCount() == 1 && m.getParameterTypes()[0] == event.getClass()) {
                m.invoke(addonInstance, event);
                break;
            }
        }
    }

    public Addon getAddonInfo() {
        return addonInfo;
    }

    public Object getAddonInstance() {
        return addonInstance;
    }

    public AddonType getAddonType() {
        return addonType;
    }
}
