package net.lomeli.boombot.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Addon {
    /**
     * ID for the addon. MUST be all lowercase and have no spaces! Will be ignored otherwise!
     */
    String addonID();

    String name() default "";

    String version() default "";

    String acceptedBoomBotVersion() default "*";

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface Instance {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface Event {
    }
}
