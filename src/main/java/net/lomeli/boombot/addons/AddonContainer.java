package net.lomeli.boombot.addons;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import net.lomeli.boombot.api.BoomAddon;

public class AddonContainer {
    private String id, name, version;
    private Object addonInstance;

    public AddonContainer(Class clazz) throws IllegalAccessException, InstantiationException {
        this.addonInstance = clazz.newInstance();
        BoomAddon addonInfo = (BoomAddon) clazz.getAnnotation(BoomAddon.class);
        this.id = addonInfo.addonID();
        this.name = addonInfo.name();
        this.version = addonInfo.version();
    }

    public void initAddon() {
        try {
            Field[] fields = addonInstance.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(BoomAddon.Instance.class)) {
                    field.set(addonInstance, addonInstance);
                    break;
                }
            }
            Method[] methods = addonInstance.getClass().getMethods();
            for (Method method : methods) {

                if (method.isAnnotationPresent(BoomAddon.Init.class))
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
