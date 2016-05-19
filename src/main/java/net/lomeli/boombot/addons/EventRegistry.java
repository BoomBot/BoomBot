package net.lomeli.boombot.addons;

import com.google.common.collect.Lists;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import net.lomeli.boombot.api.event.Event;
import net.lomeli.boombot.helper.Logger;

public enum EventRegistry {
    INSTANCE();

    private final List<Object> eventHandlers;

    EventRegistry() {
        this.eventHandlers = Lists.newArrayList();
    }

    public void registerEventHandler(Object obj) {
        this.eventHandlers.add(obj);
    }

    public boolean post(Event event) {
        for (Object obj : eventHandlers) {
            Method[] methods = obj.getClass().getMethods();
            for (Method method : methods) {
                if (method.getParameterCount() == 1 && method.getParameterTypes()[0] == event.getClass()) {
                    try {
                        method.invoke(obj, event);
                        if (event.cancelable() && event.isCanceled())
                            return true;
                    } catch (IllegalAccessException ex) {
                        Logger.error("Could not access event handler %s in %s", ex, method.getName(), obj.getClass().getName());
                    } catch (InvocationTargetException ex) {
                        Logger.error("", ex, method);
                    }
                }
            }
        }
        return false;
    }
}
