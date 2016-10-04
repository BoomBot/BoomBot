package net.lomeli.boombot.lib;

import com.google.common.collect.Lists;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import net.lomeli.boombot.BoomBot;
import net.lomeli.boombot.api.events.Event;
import net.lomeli.boombot.api.events.IEventRegistry;

public class EventRegistry implements IEventRegistry {
    private final List<Object> eventHandlers;

    public EventRegistry() {
        this.eventHandlers = Lists.newArrayList();
    }

    @Override
    public void registerEventHandler(Object obj) {
        this.eventHandlers.add(obj);
    }

    @Override
    public boolean post(Event event) {
        for (Object obj : eventHandlers) {
            Method[] methods = obj.getClass().getMethods();
            for (Method method : methods) {
                Event.EventHandler annotation = method.getAnnotation(Event.EventHandler.class);
                if (annotation == null) continue;
                if (method.getParameterCount() == 1 && method.getParameterTypes()[0] == event.getClass()) {
                    try {
                        method.invoke(obj, event);
                        Event.Cancelable cancelable = event.getClass().getAnnotation(Event.Cancelable.class);
                        if (cancelable != null && event.isCanceled())
                            return true;
                    } catch (IllegalAccessException ex) {
                        BoomBot.logger.error("Could not access event handler %s in %s", ex, method.getName(), obj.getClass().getName());
                    } catch (InvocationTargetException ex) {
                        BoomBot.logger.error("", ex, method);
                    }
                }
            }
        }
        return false;
    }
}
