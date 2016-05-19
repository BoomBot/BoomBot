package net.lomeli.boombot.addons;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.lomeli.boombot.addons.discovery.AnnotationHelper;
import net.lomeli.boombot.api.BoomAddon;

public class AddonContainer {
    private String id, name, version;
    private Object addonInstance;

    public AddonContainer(Class clazz) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        this.addonInstance = clazz.newInstance();
        BoomAddon addonInfo = AnnotationHelper.getAnnotationFromClass(clazz, null, BoomAddon.class);
        if (addonInfo != null) {
            this.id = addonInfo.addonID();
            this.name = addonInfo.name();
            this.version = addonInfo.version();
        }
    }

    public void initAddon() {
        try {
            Field[] fields = addonInstance.getClass().getDeclaredFields();
            for (Field field : fields) {
                BoomAddon.Instance instance = AnnotationHelper.getAnnotationFromField(field, null, BoomAddon.Instance.class);
                if (instance != null) {
                    field.set(addonInstance, addonInstance);
                    break;
                }
            }
            Method[] methods = addonInstance.getClass().getMethods();
            for (Method method : methods) {
                BoomAddon.Init init = AnnotationHelper.getAnnotationFromMethod(method, null, BoomAddon.Init.class);
                if (init != null)
                    method.invoke(addonInstance);
            }
        } catch (Exception e) {

        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }
}
